package eu.fluidos.Controller;

import eu.fluidos.Cluster;
import eu.fluidos.Main;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1IPBlock;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1NetworkPolicy;
import io.kubernetes.client.openapi.models.V1NetworkPolicyEgressRule;
import io.kubernetes.client.openapi.models.V1NetworkPolicyIngressRule;
import io.kubernetes.client.openapi.models.V1NetworkPolicyPeer;
import io.kubernetes.client.openapi.models.V1NetworkPolicySpec;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import io.kubernetes.client.util.Yaml;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import eu.fluidos.Module;
import eu.fluidos.jaxb.AuthorizationIntents;
import eu.fluidos.jaxb.CIDRSelector;
import eu.fluidos.jaxb.ConfigurationRule;
import eu.fluidos.jaxb.ITResourceOrchestrationType;
import eu.fluidos.jaxb.KeyValue;
import eu.fluidos.jaxb.KubernetesNetworkFilteringAction;
import eu.fluidos.jaxb.KubernetesNetworkFilteringCondition;
import eu.fluidos.jaxb.PodNamespaceSelector;
import eu.fluidos.jaxb.Priority;
import eu.fluidos.jaxb.ProtocolType;
import eu.fluidos.jaxb.RequestIntents;
import eu.fluidos.jaxb.ResourceSelector;

import java.util.logging.Level;
import java.util.logging.Logger;


import eu.fluidos.Crds.TunnelEndpoint;
import eu.fluidos.harmonization.HarmonizationController;
import eu.fluidos.Namespace;
import eu.fluidos.Pod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParser;

public class KubernetesController {
    private static final Logger LOGGER = Logger.getLogger(KubernetesController.class.getName());
    private List<String> offloadedNamespace;
    private Map<String, List<String>> allowedIpList;
    private List<RequestIntents> reqIntentsList = new ArrayList<>();
    private ApiClient client;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HarmonizationController harmController;
    private V1NamespaceList providerNamespaceList;
    private V1NamespaceList consumerNamespaceList;
    private boolean firstCallToModuletimer = true;
    
    private static class SyncPatchedContract {
        public final AtomicBoolean contractAvailable = new AtomicBoolean(false);
        public final AtomicBoolean offloadedNamespaceAvailable = new AtomicBoolean(false);
        public String configMapName = "";
        public String targetNS = "";
        public SyncPatchedContract() {}

        public void setContractAvailable(boolean value, String configMapName) {
            this.contractAvailable.set(value);
            if (configMapName != null && !configMapName.isEmpty()) {
                this.configMapName = configMapName;
            }
        }

        public void setOffloadedNamespaceAvailable(boolean value) {
            this.contractAvailable.set(value);
        }

        public boolean compareSetOffloadedNamespaceAvailable(boolean expected, boolean desired, String targetNamespaceName) {
            targetNS = targetNamespaceName;
            return this.offloadedNamespaceAvailable.compareAndSet(expected, desired);
        }

        public String getConfigMapName() {
            return this.configMapName;
        }

        public boolean doubleConditionMet(){
            return (contractAvailable.get() && offloadedNamespaceAvailable.get());
        }

        private Object getContractAvailable() {
            return this.contractAvailable.get();   
        }
        
        private Object getOffloadedNamespaceAvailable() {
            return this.offloadedNamespaceAvailable.get();
        }
    }

    private final SyncPatchedContract syncPatchedContract = new SyncPatchedContract();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    
    List<String> namespacesToExclude = new ArrayList<>(Arrays.asList(
            "calico-apiserver",
            "calico-system",
            "kube-node-lease",
            "kube-public",
            "kube-system",
            "local-path-storage",
            "tigera-operator",
            "cert-manager",
            "fluidos",
            "calico-apiserver",
            "liqo"));

    public KubernetesController() {
        try {
            String token = new String(
                    Files.readAllBytes(Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/token"))); // Path per
                                                                                                           // accedere
                                                                                                           // al token
                                                                                                           // assocaito
                                                                                                           // al pod a
                                                                                                           // cui ho
                                                                                                           // associato
                                                                                                           // il service
                                                                                                           // account
            this.client = Config.fromToken("https://kubernetes.default.svc", token, false); // URL all' interno del
                                                                                            // namespace default per
                                                                                            // accedere all API server
                                                                                            // ed autenticarsi
            Configuration.setDefaultApiClient(this.client);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del client Kubernetes", e);
            throw new RuntimeException("Errore durante l'inizializzazione del client Kubernetes", e);
        }
        this.offloadedNamespace = new ArrayList<>();
        this.allowedIpList = new HashMap<>();
    }

    public void start() throws Exception {

        try {
            LOGGER.info("Connesso al cluster: " + this.client.getBasePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore durante la connessione al cluster Kubernetes", e);
            throw e;
        }

        CoreV1Api api = new CoreV1Api(client);
        Thread namespaceThread = new Thread(() -> {
            try {
                watchNamespaces(client, api);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error invoking Kubernetes' API in namespace thread: " + e.getMessage());
            }
        });

        /* Unused thread: applyDefaultNetworkPolicies has been integrated into watchNameSpaces
        Thread defaultDenyNetworkPolicyThread = new Thread(() -> {
            try {
                applyDefaultDenyNetworkPolicies(client, api);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error invoking Kubernetes' API: " + e.getMessage());
            }
        });*/

        Thread podThread = new Thread(() -> {
            try {
                watchPods(client, api);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error invoking Kubernetes' API: " + e.getMessage());
            }
        });

        // Useless watcher? Test if it's safe to remove
        /*Thread tunnelEndpointThread = new Thread(() -> {
            try {
                watchTunnelEndpoint(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        */

        Thread peeringCandidatesThread = new Thread(() -> {
            try {
                watchPeeringCandidates(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread contractThread = new Thread(() -> {
            try {
                watchContract(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        peeringCandidatesThread.start();
        //defaultDenyNetworkPolicyThread.start();
        namespaceThread.start();
        podThread.start();
        //tunnelEndpointThread.start();
        contractThread.start();
    }

    public void watchPods(ApiClient client, CoreV1Api api) throws Exception {
        Watch<V1Pod> podWatch = Watch.createWatch(
                client,
                api.listPodForAllNamespacesCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE,
                        null),
                new TypeToken<Watch.Response<V1Pod>>() {
                }.getType());

        for (Watch.Response<V1Pod> item : podWatch) {
            V1Pod pod = item.object;
            if (item.type.equals("ADDED") && this.offloadedNamespace.contains(pod.getMetadata().getNamespace())) {
                System.out.println("New Pod created: " + pod.getMetadata().getName() + ", in Namespace: "
                        + pod.getMetadata().getNamespace());
                CreateNetworkPolicies(client, pod.getMetadata().getNamespace());
            } else if (item.type.equals("DELETED")) {
                System.out.println("Pod deleted: " + pod.getMetadata().getName() + ", from Namespace: "
                        + pod.getMetadata().getNamespace());
            }
        }
    }

    /* Useless since its logic has been integrated in the namespace watcher, which now adds also the netpols according to the matched conditions
    public void applyDefaultDenyNetworkPolicies(ApiClient client, CoreV1Api api) throws Exception {
        try {
            Watch<V1Namespace> namespaceWatch = Watch.createWatch(
                    client,
                    api.listNamespaceCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE, null),
                    new TypeToken<Watch.Response<V1Namespace>>() {
                    }.getType());
            for (Watch.Response<V1Namespace> item : namespaceWatch) {
                V1Namespace namespace = item.object;

                if (item.type.equals("ADDED") && !namespacesToExclude.contains(namespace.getMetadata().getName())) {
                    CreateDefaultDenyNetworkPolicies(client, namespace.getMetadata().getName());
                    createAllowKubeDNSNetworkPolicy(client, namespace.getMetadata().getName());
                    if (isNamespaceOffloaded(namespace)) {
                        createNetworkPolicyForIPRange(client, namespace, namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id"));
                    }
                } else if (item.type.equals("DELETED") && isNamespaceOffloaded(namespace)) {
                    System.out.println("Offloaded Namespace deleted: " + namespace.getMetadata().getName());
                } else if (item.type.equals("MODIFIED") && isNamespaceToOffload(namespace)) {
                    String key = allowedIpList.keySet().iterator().next();
                    createNetworkPolicyForIPRange(client, namespace, key);
                    System.out.println("Namespace: " + namespace.getMetadata().getName() + " modified");
                }
            }
        } catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
    */

    public void watchNamespaces(ApiClient client, CoreV1Api api) throws Exception {
        try {
            Watch<V1Namespace> namespaceWatch = Watch.createWatch(
                    client,
                    api.listNamespaceCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE, null),
                    new TypeToken<Watch.Response<V1Namespace>>() {
                    }.getType());
            for (Watch.Response<V1Namespace> item : namespaceWatch) {
                V1Namespace namespace = item.object;
                if (item.type.equals("ADDED")){
                    if(isNamespaceOffloaded(namespace)) {
                        offloadedNamespace.add(namespace.getMetadata().getName());
                        createNetworkPolicyForIPRange(client, namespace, namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id"));
                        System.out.println("New Offloaded Namespace: " + namespace.getMetadata().getName());
                        if (firstCallToModuletimer) {
                            startModuleTimer(client);
                            firstCallToModuletimer = false;
                        }
                        // first time this happens, trigger the offloading of the configMap containing the request intents of the consumer
                        syncPatchedContract.compareSetOffloadedNamespaceAvailable(false, true, namespace.getMetadata().getName());
                        System.out.println("Condition on NS offloaded met");
                        checkDoubleCondition();
                    } 
                    if (!namespacesToExclude.contains(namespace.getMetadata().getName())  && !namespace.getMetadata().getName().contains("liqo")){
                        CreateDefaultDenyNetworkPolicies(client, namespace.getMetadata().getName());
                        createAllowKubeDNSNetworkPolicy(client, namespace.getMetadata().getName());
                    }
                } else if (item.type.equals("DELETED") && isNamespaceOffloaded(namespace)) {
                    offloadedNamespace.remove(namespace.getMetadata().getName());
                    System.out.println("Offloaded Namespace deleted: " + namespace.getMetadata().getName());
                } else if (item.type.equals("MODIFIED")) {
                    if (isNamespaceOffloaded(namespace)) {
                        if (firstCallToModuletimer) {
                            startModuleTimer(client);
                            firstCallToModuletimer = false;
                        }
                    } else if (isNamespaceToOffload(namespace)) {
                        // first time this happens, trigger the offloading of the configMap containing the request intents of the consumer
                        syncPatchedContract.compareSetOffloadedNamespaceAvailable(false, true, namespace.getMetadata().getName());
                        System.out.println("Condition on NS offloaded met - NS to offload: " + namespace.getMetadata().getName());
                        checkDoubleCondition();
                        String key = allowedIpList.keySet().iterator().next();
                        createNetworkPolicyForIPRange(client, namespace, key);
                    }
                }
            }
        } catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API for watchNamespaces function: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }

    /* Backup function of watchNamespaces, before integrating the applyDefaultNetworkPolicies() logic in it, as above
    
    public void watchNamespaces(ApiClient client, CoreV1Api api) throws Exception {
        try {
            Watch<V1Namespace> namespaceWatch = Watch.createWatch(
                    client,
                    api.listNamespaceCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE, null),
                    new TypeToken<Watch.Response<V1Namespace>>() {
                    }.getType());
            for (Watch.Response<V1Namespace> item : namespaceWatch) {
                V1Namespace namespace = item.object;
                if (item.type.equals("ADDED") && isNamespaceOffloaded(namespace)) {
                    offloadedNamespace.add(namespace.getMetadata().getName());
                    System.out.println("New Offloaded Namespace: " + namespace.getMetadata().getName());
                    if (firstCallToModuletimer) {
                        startModuleTimer(client);
                        firstCallToModuletimer = false;
                    }

                } else if (item.type.equals("DELETED") && isNamespaceOffloaded(namespace)) {
                    offloadedNamespace.remove(namespace.getMetadata().getName());
                    System.out.println("Offloaded Namespace deleted: " + namespace.getMetadata().getName());
                } else if (item.type.equals("MODIFIED") && isNamespaceOffloaded(namespace)) {
                    if (firstCallToModuletimer) {
                        startModuleTimer(client);
                        firstCallToModuletimer = false;
                    }
                }
            }
        } catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
     */

    private void startModuleTimer(ApiClient client) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            HarmonizationController harmonizationController = new HarmonizationController();
            try {
                AuthorizationIntents contractAuthIntents = listContract(client);

                // should we support for a contract with empty authorization intents?
                if (contractAuthIntents != null) {
                    if (reqIntentsList.size() > 0) {
                        List<RequestIntents> harmonizedIntents = new ArrayList<>();
                        System.out.println("Start the harmonization");
                        for (RequestIntents reqIntent : reqIntentsList) {
                            Cluster cluster = createCluster(client, "provider");
                            harmonizedIntents
                                    .add(harmonizationController.harmonize(cluster, reqIntent, contractAuthIntents));
                        }

                        Module module = new Module(harmonizedIntents, client);
                        CoreV1Api api = new CoreV1Api(client);

                        // apply network policies to all namespaces
                        V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null,
                                null, null, null);
                        for (V1Namespace namespace : namespaceList.getItems()) {
                            CreateNetworkPolicies(client, namespace.getMetadata().getName());
                        }
                    } else {
                        System.out.println("reqIntent is empty");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error invoking the harmonization");
                e.printStackTrace();
            }
            firstCallToModuletimer = true;
        }, 5, TimeUnit.SECONDS);
        firstCallToModuletimer = true;
    }

    //might be useless -> no tunnelendpoint resurce in cluster
    /* 
    public void watchTunnelEndpoint(ApiClient client) throws Exception {
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            Watch<TunnelEndpoint> watch = Watch.createWatch(
                    client,
                    customObjectsApi.listClusterCustomObjectCall(
                            "net.liqo.io",
                            "v1alpha1",
                            "tunnelendpoints",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true,
                            null),
                    new TypeToken<Watch.Response<TunnelEndpoint>>() {
                    }.getType());

            for (Watch.Response<TunnelEndpoint> item : watch) {
                if (item.type.equals("ADDED")) {
                    TunnelEndpoint tunnelEndpoint = item.object;
                    if (tunnelEndpoint != null) {
                        TunnelEndpoint.Status status = tunnelEndpoint.getStatus();
                        if (status != null) {
                            TunnelEndpoint.Status.Connection connection = status.getConnection();
                            if (connection != null) {
                                this.allowedIpList.put(tunnelEndpoint.getMetadata().getLabels().get("clusterID"),
                                        new ArrayList<>(connection.getPeerConfiguration().getAllowedIPs()));
                                for (Map.Entry<String, List<String>> entry : this.allowedIpList.entrySet()) {
                                    System.out.println(entry);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
    */
    
    public AuthorizationIntents listContract(ApiClient client) throws Exception {
        AuthorizationIntents contrAuthorizationIntents = new AuthorizationIntents();
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            Object response = customObjectsApi.listNamespacedCustomObject(
                    "reservation.fluidos.eu",
                    "v1alpha1",
                    "fluidos",
                    "contracts",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            Map<String, Object> responseMap = (Map<String, Object>) response;
            List<Map<String, Object>> items = (List<Map<String, Object>>) responseMap.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> spec = (Map<String, Object>) item.get("spec");
                Map<String, Object> flavor = (Map<String, Object>) spec.get("flavor");
                Map<String, Object> flavorSpec = (flavor != null) ? (Map<String, Object>) flavor.get("spec") : null;
                String networkPropertyType = (flavorSpec != null && flavorSpec.containsKey("networkPropertyType"))
                        ? (String) flavorSpec.get("networkPropertyType")
                        : null;

                String networkRequests = spec.containsKey("networkRequests") ? (String) spec.get("networkRequests") : null;
                String buyerClusterID = spec.containsKey("buyerClusterID") ? (String) spec.get("buyerClusterID") : null;

                if (networkRequests != null) {
                    for (String namespace : offloadedNamespace) {
                        try {
                            RequestIntents reqIntentToAdd = accessConfigMap(client, namespace, networkRequests);
                            if (reqIntentToAdd != null) {
                                reqIntentsList.add(reqIntentToAdd);
                            }
                        } catch (Exception e) {

                        }

                    }
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(flavorSpec);
                JsonObject flavorSpecJson = gson.fromJson(jsonString, JsonObject.class);
                JsonObject flavorType = (flavorSpec != null) ? flavorSpecJson.getAsJsonObject("flavorType") : null;
                JsonObject typeData = (flavorType != null) ? flavorType.getAsJsonObject("typeData") : null;
                contrAuthorizationIntents = accessFlavour(typeData);
            }
            return contrAuthorizationIntents;
        } catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
        return contrAuthorizationIntents;
    }

    public AuthorizationIntents insertAuthorizationIntents(JsonObject typeData) {
        AuthorizationIntents authIntent = new AuthorizationIntents();
        List<ConfigurationRule> forbiddenConnectionList = authIntent.getForbiddenConnectionList();
        List<ConfigurationRule> mandatoryConnectionList = authIntent.getMandatoryConnectionList();
        JsonObject characteristics = typeData.getAsJsonObject("characteristics");
        KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
        Priority prio = new Priority();
        boolean isCNF = false;
        if (characteristics != null) {
            if (characteristics.has("action")) {
                action.setKubernetesNetworkFilteringActionType(characteristics.get("action").getAsString());
            }

            if (characteristics.has("isCNF")) {
                isCNF = characteristics.get("isCNF").getAsBoolean();
            }

            if (characteristics.has("externalData")) {
                JsonObject externalDataJson = characteristics.getAsJsonObject("externalData");
                prio.setValue(externalDataJson.getAsJsonObject("priority").getAsBigInteger());
            }
            if (characteristics.has("deniedCommunications") && characteristics.has("mandatoryCommunications")) {
                JsonArray deniedCommunications = characteristics.getAsJsonArray("deniedCommunications");
                populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList, action, prio, isCNF);
                JsonArray mandatoryCommunications = characteristics.getAsJsonArray("mandatoryCommunications");
                populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList, action, prio, isCNF);
            } else if (characteristics.has("mandatoryCommunications")) {
                JsonArray mandatoryCommunications = characteristics.getAsJsonArray("mandatoryCommunications");
                populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList, action, prio, isCNF);
            } else if (characteristics.has("deniedCommunications")) {
                JsonArray deniedCommunications = characteristics.getAsJsonArray("deniedCommunications");
                populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList, action, prio, isCNF);
            } else {
                System.out.println("Mandatory Communications and Denied communications not found.");
            }
        } else {
            System.out.println("Characteristics not found.");
        }
        return authIntent;
    }

    public Cluster createCluster(ApiClient client, String whoIs) {
        CoreV1Api api = new CoreV1Api(client);
        Cluster myCluster = new Cluster(null, null);
        try {
            V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, null,
                    null);
            V1NamespaceList epuratedNamespaceList = new V1NamespaceList();
            Epurate1(namespaceList);
            if (whoIs.equals("provider")) {
                epuratedNamespaceList = providerNamespaceList;
            } else if (whoIs.equals("consumer")) {
                epuratedNamespaceList = consumerNamespaceList;
            }

            List<Namespace> NamespaceList = new ArrayList<>();
            List<Pod> PodList = new ArrayList<>();
            for (V1Namespace namespace : epuratedNamespaceList.getItems()) {
                Namespace nm = new Namespace();
                HashMap<String, String> hashMapLabels = new HashMap<>(namespace.getMetadata().getLabels());
                HashMap<String, String> hashMapSingleLabels = new HashMap<>();
                Map.Entry<String, String> firstLabel = hashMapLabels.entrySet().iterator().next();
                hashMapSingleLabels.put(firstLabel.getKey(), firstLabel.getValue());
                nm.setLabels(hashMapSingleLabels);
                NamespaceList.add(nm);
                V1PodList podList = api.listNamespacedPod(namespace.getMetadata().getName(), null, null, null, null,
                        null, null, null, null, null, null);
                for (V1Pod pod : podList.getItems()) {
                    Pod pd = new Pod();
                    HashMap<String, String> podHashMapLabels = new HashMap<>(pod.getMetadata().getLabels());
                    HashMap<String, String> hashMapSinglePodsLabels = new HashMap<>();
                    Map.Entry<String, String> firstLabelPod = podHashMapLabels.entrySet().iterator().next();
                    hashMapSinglePodsLabels.put(firstLabelPod.getKey(), firstLabelPod.getValue());
                    pd.setLabels(hashMapSinglePodsLabels);
                    pd.setNamespace(nm);
                    PodList.add(pd);
                }
            }

            myCluster.setNamespaces(NamespaceList);
            myCluster.setPods(PodList);

        } catch (ApiException e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.err.println("Error invoking Kubernetes API: " + e.getResponseBody());
        }
        return myCluster;
    }

    private void Epurate1(V1NamespaceList namespaceList) {
        providerNamespaceList = new V1NamespaceList();
        consumerNamespaceList = new V1NamespaceList();

        for (V1Namespace namespace : namespaceList.getItems()) {
            if (!namespacesToExclude.contains(namespace.getMetadata().getName()) && !namespace.getMetadata().getName().contains("liqo")
                    && !namespace.getMetadata().getName().contains("remote-cluster")) {
                if (!namespace.getMetadata().equals(null)
                        && !namespace.getMetadata().getLabels().containsKey("liqo.io/remote-cluster-id")) {
                    providerNamespaceList.addItemsItem(namespace);
                } else {
                    consumerNamespaceList.addItemsItem(namespace);
                }
            }
        }
    }

    public RequestIntents accessConfigMap(ApiClient client, String namespace, String configMapName) throws Exception {
        RequestIntents requestIntent = new RequestIntents();
        try {
            CoreV1Api api = new CoreV1Api(client);
            V1ConfigMap configMap = api.readNamespacedConfigMap(configMapName, namespace, null);

            if (configMap != null) {
                String networkIntent = configMap.getData().get("networkIntents");
                System.out.println("ConfigMap found for namespace: " + namespace);
                JsonArray jsonArray = JsonParser.parseString(networkIntent).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonObject source = jsonObject.getAsJsonObject("source");
                    JsonObject destination = jsonObject.getAsJsonObject("destination");

                    condition.setSource(parseResourceSelector(source.getAsJsonObject("resourceSelector")));
                    condition.setDestination(parseResourceSelector(destination.getAsJsonObject("resourceSelector")));
                    if (source.has("sourcePort") && !source.get("sourcePort").isJsonNull()) {
                        condition.setSourcePort(source.get("sourcePort").getAsString());
                    }

                    if (jsonObject.has("destinationPort") && !jsonObject.get("destinationPort").isJsonNull()) {
                        condition.setDestinationPort(jsonObject.get("destinationPort").getAsString());
                    }

                    if (jsonObject.has("protocolType") && !jsonObject.get("protocolType").isJsonNull()) {
                        condition.setProtocolType(ProtocolType.valueOf(jsonObject.get("protocolType").getAsString()));
                    }
                    ConfigurationRule rule = new ConfigurationRule();
                    rule.setConfigurationCondition(condition);
                    rule.setName(configMap.getData().get("name"));

                    KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
                    if (configMap.getData().get("action") != null) {
                        action.setKubernetesNetworkFilteringActionType(configMap.getData().get("action"));
                        rule.setConfigurationRuleAction(action);
                    }

                    if (configMap.getData().get("isCNF") != null) {
                        boolean isCNF = false;
                        if (configMap.getData().get("isCNF").equals(true)) {
                            isCNF = true;
                        }
                        rule.setIsCNF(isCNF);
                    }

                    if (configMap.getData().get("acceptMonitoring") != null) {
                        boolean acceptMonitoring = false;
                        if (configMap.getData().get("acceptMonitoring").equals("true")) {
                            acceptMonitoring = true;
                        }
                        requestIntent.setAcceptMonitoring(acceptMonitoring);
                    }

                    if (configMap.getData().get("priority") != null) {
                        Priority prio = new Priority();
                        BigInteger bigPrio = new BigInteger(configMap.getData().get("priority"));
                        prio.setValue(bigPrio);
                        rule.setExternalData(prio);
                    }

                    requestIntent.getConfigurationRule().add(rule);
                }
                return requestIntent;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }

    }

    public void watchContract(ApiClient client) throws Exception {
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            Watch<JsonObject> watch = Watch.createWatch(
                client,
                customObjectsApi.listNamespacedCustomObjectCall(
                    "reservation.fluidos.eu",
                    "v1alpha1",
                    "fluidos",
                    "contracts",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null),
                new TypeToken<Watch.Response<JsonObject>>() {
                }.getType());

            for (Watch.Response<JsonObject> item : watch) {
                System.out.println("Event received by contract watcher: " + item.type);
                if (item.type.equals("MODIFIED")) {
                    JsonObject contract = item.object;
                    //TODO: add configmap name based on contract field 
                    syncPatchedContract.setContractAvailable(true, ""); //contract available! -> might be convenient to better check the condition
                    System.out.println("Contract modified: " + contract.getAsJsonObject("metadata").get("name").getAsString()); 
                    checkDoubleCondition(); //trigger the condition check on the offloaded NS also, if met then trigger the creation of the configMap in the offloaded NS
                }
            }
        }
        catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }

    private void checkDoubleCondition(){
        System.out.println("Checking double condition, contract has been modified (" + syncPatchedContract.getContractAvailable().toString() + "), namespace ("+ syncPatchedContract.getOffloadedNamespaceAvailable()+")");
        if(syncPatchedContract.doubleConditionMet()){
            String configMapName = syncPatchedContract.getConfigMapName();
            System.out.println("Double condition met -> calling the offloadCM function");
            offloadIntentsCM(configMapName, syncPatchedContract.targetNS); //double condition met -> offload a configMap to inform the provider about the consumer's intents
        }
    }

    private void offloadIntentsCM(String configMapToCreateName, String targetNamespaceName){
        try {
            // strategy: replicate "consumer-network-intent" from "fluidos" to the first available offloaded namespace (added by the NS watcher)
            String sourceNamespace = "fluidos";
            String configMapName = "consumer-network-intent"; 
             CoreV1Api api = new CoreV1Api(client);

            // Read the ConfigMap from the local namespace
            V1ConfigMap sourceConfigMap = api.readNamespacedConfigMap(configMapName, sourceNamespace, null);
            System.out.println("Source configmap" + sourceConfigMap == null ? " not found" : " found");
            
            if (sourceConfigMap != null && !offloadedNamespace.isEmpty()) {

            V1ConfigMap targetConfigMap = new V1ConfigMap();
            V1ObjectMeta meta = new V1ObjectMeta();
            meta.setName(configMapName);
            meta.setNamespace(targetNamespaceName);
            targetConfigMap.setMetadata(meta);
            targetConfigMap.setData(sourceConfigMap.getData());

            try {
                api.createNamespacedConfigMap(targetNamespaceName, targetConfigMap, null, null, null);
                System.out.println("ConfigMap " + configMapName + " replicated to namespace " + targetNamespaceName);
            } catch (ApiException e) {
                if (e.getCode() == 409) { // Already exists, so replace it
                    api.replaceNamespacedConfigMap(configMapName, targetNamespaceName, targetConfigMap, null, null, null);
                    System.out.println("ConfigMap " + configMapName + " replaced in namespace " + targetNamespaceName);
                } else {
                    System.err.println("Error replicating ConfigMap to namespace " + targetNamespaceName + ": " + e.getResponseBody());
                }
            }
            } else {
                System.err.println("Source ConfigMap " + configMapName + " not found in namespace " + sourceNamespace + " or no offloaded namespace available.");
            }
        } catch (Exception e) {
            System.err.println("Exception during ConfigMap replication: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void watchPeeringCandidates(ApiClient client) throws Exception {
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            Watch<JsonObject> watch = Watch.createWatch(
                    client,
                    customObjectsApi.listNamespacedCustomObjectCall(
                            "advertisement.fluidos.eu",
                            "v1alpha1",
                            "fluidos",
                            "peeringcandidates",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            true,
                            null),
                    new TypeToken<Watch.Response<JsonObject>>() {
                    }.getType());

            AtomicBoolean firstTimeToCallVerifier = new AtomicBoolean(false);
            AtomicBoolean timerStarted = new AtomicBoolean(false);
            List<JsonObject> listTypeData = new ArrayList<>();
            String peeringCandidateName = new String();
            List<AuthorizationIntents> listReturnedAuthorizationIntents = new ArrayList<>();
            List<String> listPeeringCandidateName = new ArrayList<>();
            for (Watch.Response<JsonObject> item : watch) {
                if (item.type.equals("ADDED")) {
                    JsonObject peeringCandidate = item.object;
                    peeringCandidateName = peeringCandidate.getAsJsonObject("metadata").get("name").getAsString();
                    JsonObject spec = peeringCandidate.getAsJsonObject("spec");
                    JsonObject flavor = spec.getAsJsonObject("flavor");
                    System.out.println("Peering Candidate: " + peeringCandidateName);
                    JsonObject spec1 = flavor.getAsJsonObject("spec");
                    JsonObject flavorType = spec1.getAsJsonObject("flavorType");
                    JsonObject typeData = flavorType.getAsJsonObject("typeData");
                    AuthorizationIntents returnedAuthorizationIntents = accessFlavour(typeData);
                    if (returnedAuthorizationIntents != null) {
                        listReturnedAuthorizationIntents.add(returnedAuthorizationIntents);
                        listPeeringCandidateName.add(peeringCandidateName);
                    }
                    else{
                        System.out.println("Authorization Intents from peering candidate " + peeringCandidateName + " resulted to be null");
                    }

                    if (timerStarted.compareAndSet(false, true)) {
                        scheduler.schedule(() -> {
                            timerStarted.set(false);
                            //firstTimeToCallVerifier.set(true);
                            if (!listReturnedAuthorizationIntents.isEmpty()) {
                                System.out.println("Start the verifier");
                                callVerifier(listReturnedAuthorizationIntents, listPeeringCandidateName);
                            }
                        }, 5, TimeUnit.SECONDS);
                    }

                    /*
                    listTypeData.add(typeData);
                    if (firstTimeToCallVerifier.get()) {
                        if (!listReturnedAuthorizationIntents.isEmpty()) {
                            System.out.println("Start the verifier");
                            callVerifier(listReturnedAuthorizationIntents, listPeeringCandidateName);
                        }
                    }
                         */
                }
            }
        } catch (ApiException e) {
            System.err.println("Error invoking Kubernetes API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
    }

    AuthorizationIntents accessFlavour(JsonObject typeData) {
        AuthorizationIntents authorizationIntents = new AuthorizationIntents();
        List<ConfigurationRule> forbiddenConnectionList = authorizationIntents.getForbiddenConnectionList();
        List<ConfigurationRule> mandatoryConnectionList = authorizationIntents.getMandatoryConnectionList();
        if (typeData != null) {
            if (!typeData.has("properties") || typeData.getAsJsonObject("properties") == null
                    || typeData.getAsJsonObject("properties").isEmpty()) {
                return null;
            }
            JsonObject properties = typeData.getAsJsonObject("properties");
            JsonObject networkAuthorizations = properties.getAsJsonObject("networkAuthorizations");
            KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
            Priority prio = new Priority();
            boolean isCNF = false;
            if (networkAuthorizations != null) {
                if (properties.has("action")) {
                    action.setKubernetesNetworkFilteringActionType(properties.get("action").getAsString());
                }

                if (properties.has("isCNF")) {
                    isCNF = properties.get("isCNF").getAsBoolean();
                }

                if (properties.has("externalData")) {
                    JsonObject externalDataJson = properties.getAsJsonObject("externalData");
                    prio.setValue(externalDataJson.getAsJsonObject("priority").getAsBigInteger());
                }
                if (networkAuthorizations.has("deniedCommunications")
                        && networkAuthorizations.has("mandatoryCommunications")) {
                    if (networkAuthorizations.get("deniedCommunications") != null
                            && networkAuthorizations.get("mandatoryCommunications") != null) {
                        JsonArray deniedCommunications = networkAuthorizations.getAsJsonArray("deniedCommunications");
                        populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList, action, prio,
                                isCNF);
                        JsonArray mandatoryCommunications = networkAuthorizations
                                .getAsJsonArray("mandatoryCommunications");
                        populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList, action, prio,
                                isCNF);
                    }
                } else if (networkAuthorizations.has("mandatoryCommunications")) {
                    if (networkAuthorizations.get("mandatoryCommunications") != null) {
                        JsonArray mandatoryCommunications = networkAuthorizations
                                .getAsJsonArray("mandatoryCommunications");
                        populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList, action, prio,
                                isCNF);
                    }
                } else if (networkAuthorizations.has("deniedCommunications")) {
                    if (networkAuthorizations.get("deniedCommunications") != null) {
                        JsonArray deniedCommunications = networkAuthorizations.getAsJsonArray("deniedCommunications");
                        populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList, action, prio,
                                isCNF);
                    }
                } else {
                    System.out.println("Mandatory Communications and Denied communications not found.");
                }
            } else {
                System.out.println("Characteristics not found.");
            }
            if (authorizationIntents != null && authorizationIntents.getForbiddenConnectionList().size() > 0
                    && authorizationIntents.getMandatoryConnectionList().size() > 0) {
                return authorizationIntents;
            }
        }
        return null;
    }

    private void callVerifier(List<AuthorizationIntents> authIntentsFromFlavors, List<String> PeeringCandidateNames) {
        int i = 0;
        for (AuthorizationIntents authorizationIntents : authIntentsFromFlavors) {
            if (authorizationIntents != null && authorizationIntents.getForbiddenConnectionList().size() > 0
                    && authorizationIntents.getMandatoryConnectionList().size() > 0) {
                HarmonizationController harmonizationController = new HarmonizationController();
                System.out.println("[+] Received PeeringCandidate: " + PeeringCandidateNames.get(i));
                //TODO: extract request intents from standard ConfigMap (created by UMU's meta orchestrator) and pass it to verify directly
                //Boolean value = harmonizationController.verify(createCluster(client, "consumer"), authorizationIntents); -> LEGACY CALL
                RequestIntents requestIntents;
                try {
                    System.out.println("Waiting for \"consumer-network-intent\" configMap to become available before starting verification process...");
                    while ((requestIntents = accessConfigMap(client, "fluidos", "consumer-network-intent")) == null){ //Name and Namespace of ConfigMap to be defined
                        Thread.sleep(1);
                    }
                
                    Boolean value = harmonizationController.verify(requestIntents, authorizationIntents);
                    if (value) {
                        System.out.println("[+] Result for PeeringCandidate " + PeeringCandidateNames.get(i) + " from Verifier is: "
                                + Main.ANSI_GREEN + value + Main.ANSI_RESET);
                        return;
                    }
                    System.out.println("[+] Result for PeeringCandidate " + PeeringCandidateNames.get(i) + " from Verifier is: "
                            + Main.ANSI_RED + value + Main.ANSI_RESET);
                    System.out.println(" ");
                } catch (Exception e) {
                    System.out.println("Exception occured while waiting for configMap containing consumer's Request Intents (UMU) to become available");
                    e.printStackTrace();
                }
            }
            i++;
        }
    }

    private String retrieveFluidosId(ApiClient client) throws ApiException {

        CoreV1Api api = new CoreV1Api(client);
        V1ConfigMap configMap = api.readNamespacedConfigMap("fluidos-network-manager-identity", "fluidos", null);
        String nodeID = configMap.getData().get("nodeID");
        return nodeID;
    }

    private void populateAuthorizationIntents(JsonArray communications, List<ConfigurationRule> connectionList,
            KubernetesNetworkFilteringAction action, Priority prio, boolean isCNF) {
        for (JsonElement comm : communications) {
            JsonObject communication = comm.getAsJsonObject();

            KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();

            JsonObject source = communication.getAsJsonObject("source");
            JsonObject destination = communication.getAsJsonObject("destination");

            condition.setSource(parseResourceSelector(source.getAsJsonObject("resourceSelector")));
            condition.setDestination(parseResourceSelector(destination.getAsJsonObject("resourceSelector")));

            if (communication.has("sourcePort") && !communication.get("sourcePort").isJsonNull()) {
                condition.setSourcePort(communication.get("sourcePort").getAsString());
            }

            if (communication.has("destinationPort") && !communication.get("destinationPort").isJsonNull()) {
                condition.setDestinationPort(communication.get("destinationPort").getAsString());
            }

            if (communication.has("protocolType") && !communication.get("protocolType").isJsonNull()) {
                condition.setProtocolType(ProtocolType.valueOf(communication.get("protocolType").getAsString()));
            }

            ConfigurationRule rule = new ConfigurationRule();
            rule.setConfigurationCondition(condition);
            rule.setName(communication.get("name").getAsString());

            rule.setConfigurationRuleAction(action);
            rule.setExternalData(prio);
            rule.setIsCNF(isCNF);
            connectionList.add(rule);
        }

    }

    private ResourceSelector parseResourceSelector(JsonObject resourceSelector) {
        ResourceSelector selector = null;
        String typeIdentifier = resourceSelector.get("typeIdentifier").getAsString();

        if (typeIdentifier.equals("CIDRSelector")) {
            CIDRSelector cidrSelector = new CIDRSelector();
            cidrSelector.setAddressRange(resourceSelector.getAsJsonPrimitive("selector").getAsString());
            selector = cidrSelector;

        } else if (typeIdentifier.equals("PodNamespaceSelector")) {
            PodNamespaceSelector podNamespaceSelector = new PodNamespaceSelector();

            JsonObject selectorObject = resourceSelector.getAsJsonObject("selector");

            if (selectorObject.has("namespace")) {
                JsonObject namespaces = selectorObject.getAsJsonObject("namespace");
                List<KeyValue> namespaceList = new ArrayList<>();
                for (String key : namespaces.keySet()) {
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(key);
                    keyValue.setValue(namespaces.get(key).getAsString());
                    namespaceList.add(keyValue);
                }
                podNamespaceSelector.getNamespace().addAll(namespaceList);
            }

            if (selectorObject.has("pod")) {
                JsonObject pods = selectorObject.getAsJsonObject("pod");
                List<KeyValue> podList = new ArrayList<>();
                for (String key : pods.keySet()) {
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(key);
                    keyValue.setValue(pods.get(key).getAsString());
                    podList.add(keyValue);
                }
                podNamespaceSelector.getPod().addAll(podList);
            }

            selector = podNamespaceSelector;
        }

        if (resourceSelector.has("isHotCluster")) {
            selector.setIsHostCluster(resourceSelector.get("isHotCluster").getAsBoolean());
        }

        return selector;
    }

    void PrintAuthIntents(AuthorizationIntents authorizationIntents) {
        System.out.println(" ");
        if (authorizationIntents != null) {
            System.out.println("Mandatory communications: ");
            List<ConfigurationRule> mandatoryConnectionList = authorizationIntents.getMandatoryConnectionList();
            if (mandatoryConnectionList != null) {
                for (ConfigurationRule cr : mandatoryConnectionList) {
                    KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
                            .getConfigurationCondition();
                    System.out.println("Source:");
                    if (cond.getSource().getClass().equals(PodNamespaceSelector.class)) {
                        PodNamespaceSelector pns = (PodNamespaceSelector) cond.getSource();
                        for (KeyValue namespace : pns.getNamespace()) {
                            System.out.println("namespace");
                            System.out.println("key:" + namespace.getKey() + " " + "value: " + namespace.getValue());
                        }
                        for (KeyValue pod : pns.getPod()) {
                            System.out.println("pod");
                            System.out.println("key:" + pod.getKey() + " " + "value: " + pod.getValue());
                        }
                    } else {
                        CIDRSelector CIDRAddress = (CIDRSelector) cond.getSource();
                        System.out.println("cidrDestination: " + CIDRAddress.getAddressRange());

                    }
                    System.out.println("Destination:");
                    if (cond.getDestination().getClass().equals(PodNamespaceSelector.class)) {
                        PodNamespaceSelector pns = (PodNamespaceSelector) cond.getDestination();
                        for (KeyValue namespace : pns.getNamespace()) {
                            System.out.println("namespace");
                            System.out.println("key:" + namespace.getKey() + " " + "value: " + namespace.getValue());
                        }
                        for (KeyValue pod : pns.getPod()) {
                            System.out.println("pod");
                            System.out.println("key:" + pod.getKey() + " " + "value: " + pod.getValue());
                        }
                    } else {
                        CIDRSelector CIDRAddress = (CIDRSelector) cond.getDestination();
                        System.out.println("cidrDestination: " + CIDRAddress.getAddressRange());

                    }
                    String destPort = cond.getDestinationPort();
                    System.out.println("DestinationPort: " + destPort);
                    String proto = cond.getProtocolType().getClass().toString();
                    System.out.println("ProtocolType: " + proto);

                }
                System.out.println(" ");
            }
            System.out.println("Denied communications: ");
            List<ConfigurationRule> deniedConnectionList = authorizationIntents.getForbiddenConnectionList();
            if (deniedConnectionList != null) {
                for (ConfigurationRule cr : deniedConnectionList) {
                    KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
                            .getConfigurationCondition();
                    System.out.println("Source:");
                    if (cond.getSource().getClass().equals(PodNamespaceSelector.class)) {
                        PodNamespaceSelector pns = (PodNamespaceSelector) cond.getSource();
                        for (KeyValue namespace : pns.getNamespace()) {
                            System.out.println("namespace");
                            System.out.println("key:" + namespace.getKey() + " " + "value: " + namespace.getValue());
                        }
                        for (KeyValue pod : pns.getPod()) {
                            System.out.println("pod");
                            System.out.println("key:" + pod.getKey() + " " + "value: " + pod.getValue());
                        }
                    } else {
                        CIDRSelector CIDRAddress = (CIDRSelector) cond.getSource();
                        System.out.println("cidrDestination: " + CIDRAddress.getAddressRange());

                    }
                    System.out.println("Destination:");
                    if (cond.getDestination().getClass().equals(PodNamespaceSelector.class)) {
                        PodNamespaceSelector pns = (PodNamespaceSelector) cond.getDestination();
                        for (KeyValue namespace : pns.getNamespace()) {
                            System.out.println("namespace");
                            System.out.println("key:" + namespace.getKey() + " " + "value: " + namespace.getValue());
                        }
                        for (KeyValue pod : pns.getPod()) {
                            System.out.println("pod");
                            System.out.println("key:" + pod.getKey() + " " + "value: " + pod.getValue());
                        }
                    } else {
                        CIDRSelector CIDRAddress = (CIDRSelector) cond.getDestination();
                        System.out.println("cidrDestination: " + CIDRAddress.getAddressRange());

                    }
                    String destPort = cond.getDestinationPort();
                    System.out.println("DestinationPort: " + destPort);

                }
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    private boolean isNamespaceOffloaded(V1Namespace namespace) {
        if (namespace.getMetadata().getAnnotations() != null) {
            String remoteClusterId = namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id");
            if (remoteClusterId != null && !remoteClusterId.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isNamespaceToOffload(V1Namespace namespace) {
        if (namespace.getMetadata().getLabels() != null) {
            String flag = namespace.getMetadata().getLabels().get("liqo.io/scheduling-enabled");
            if (flag != null && flag.equals("true")) {
                return true;
            }
        }
        return false;
    }

    private void CreateNetworkPolicies(ApiClient client, String Namespace) {
        NetworkingV1Api api = new NetworkingV1Api(client);
        List<File> files = getFilesInFolder("/app/network_policies/");
        for (File file : files) {
            try {
                String yamlContent = new String(Files.readAllBytes(file.toPath()));
                V1NetworkPolicy networkPolicy = Yaml.loadAs(yamlContent, V1NetworkPolicy.class);
                try {
                    if (networkPolicy.getMetadata().getNamespace().equals(Namespace)) {
                        api.createNamespacedNetworkPolicy(networkPolicy.getMetadata().getNamespace(), networkPolicy,
                                null, null, null);
                        System.out.println("NetworkPolicy: " + networkPolicy.getMetadata().getName()
                                + " applied to namespace " + Namespace);
                    }
                } catch (ApiException e) {
                }
            } catch (IOException e) {
                System.err.println("Error");
            }
        }
    }

    public List<File> getFilesInFolder(String folderPath) {
        List<File> files = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] fileList = folder.listFiles();
            for (File file : fileList) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }

        return files;
    }

    private void CreateDefaultDenyNetworkPolicies(ApiClient client, String Namespace) {
        NetworkingV1Api api = new NetworkingV1Api(client);

        V1NetworkPolicy egressPolicy = new V1NetworkPolicy();
        V1NetworkPolicySpec egressPolicySpec = new V1NetworkPolicySpec();
        egressPolicySpec.setPodSelector(new V1LabelSelector());
        egressPolicySpec.setPolicyTypes(Arrays.asList("Egress"));
        egressPolicySpec.setEgress(Collections.emptyList());
        egressPolicy.setMetadata(new V1ObjectMeta().name(Namespace + "-deny-egress").namespace(Namespace));
        egressPolicy.setSpec(egressPolicySpec);

        V1NetworkPolicy ingressPolicy = new V1NetworkPolicy();
        V1NetworkPolicySpec ingressPolicySpec = new V1NetworkPolicySpec();
        ingressPolicySpec.setPodSelector(new V1LabelSelector());
        ingressPolicySpec.setPolicyTypes(Arrays.asList("Ingress"));
        ingressPolicySpec.setIngress(Collections.emptyList());
        ingressPolicy.setMetadata(new V1ObjectMeta().name(Namespace + "-deny-ingress").namespace(Namespace));
        ingressPolicy.setSpec(ingressPolicySpec);

        try {
            api.createNamespacedNetworkPolicy(Namespace, egressPolicy, null, null, null);
            api.createNamespacedNetworkPolicy(Namespace, ingressPolicy, null, null, null);
            System.out.println("Network Policies created for namespace " + Namespace);
        } catch (ApiException e) {
            System.err.println("Error while generating network policies: " + e.getResponseBody());
        }
    }

    public void createNetworkPolicyForIPRange(ApiClient client, V1Namespace namespace, String clusterId)
            throws ApiException {
        Configuration.setDefaultApiClient(client);
        String offloadedNamespace = namespace.getMetadata().getName();

        NetworkingV1Api networkingApi = new NetworkingV1Api(client);
        V1NetworkPolicy combinedPolicy = new V1NetworkPolicy()
                .apiVersion("networking.k8s.io/v1")
                .kind("NetworkPolicy")
                .metadata(new V1ObjectMeta()
                        .name("allow-comunication-with-offloaded-pods-" + offloadedNamespace)
                        .namespace(offloadedNamespace))
                .spec(new V1NetworkPolicySpec()
                        .policyTypes(List.of("Ingress", "Egress"))
                        .podSelector(new V1LabelSelector())
                        .ingress(buildIngressRules(this.allowedIpList.get(clusterId)))
                        .egress(buildEgressRules(this.allowedIpList.get(clusterId))));

        System.out.println(
                "Network Policies to allow traffic between offloaded pods and local ones applied to namespace: "
                        + offloadedNamespace);
        networkingApi.createNamespacedNetworkPolicy(offloadedNamespace, combinedPolicy, null, null, null);
    }

    private List<V1NetworkPolicyEgressRule> buildEgressRules(List<String> ipAddress) {
        if (ipAddress == null) {
            return Collections.emptyList();
        }
        return ipAddress.stream()
                .map(ip -> new V1NetworkPolicyEgressRule()
                        .to(List.of(new V1NetworkPolicyPeer()
                                .ipBlock(new V1IPBlock().cidr(ip)))))
                .toList();
    }

    private List<V1NetworkPolicyIngressRule> buildIngressRules(List<String> ipAddress) {
        if (ipAddress == null) {
            return Collections.emptyList();
        }
        return ipAddress.stream()
                .map(ip -> new V1NetworkPolicyIngressRule()
                        .from(List.of(new V1NetworkPolicyPeer()
                                .ipBlock(new V1IPBlock().cidr(ip)))))
                .toList();
    }

    public void createAllowKubeDNSNetworkPolicy(ApiClient client, String namespace) {
        try {
            V1NetworkPolicy networkPolicy = new V1NetworkPolicy();

            networkPolicy.setApiVersion("networking.k8s.io/v1");
            networkPolicy.setKind("NetworkPolicy");
            networkPolicy
                    .setMetadata(new V1ObjectMeta().name("default-allow-kubedns-" + namespace).namespace(namespace));

            networkPolicy.setSpec(new V1NetworkPolicySpec()
                    .podSelector(new V1LabelSelector())
                    .policyTypes(Collections.singletonList("Egress"))
                    .egress(Collections.singletonList(
                            new V1NetworkPolicyEgressRule()
                                    .to(Collections.singletonList(
                                            new V1NetworkPolicyPeer()
                                                    .namespaceSelector(
                                                            new V1LabelSelector()
                                                                    .matchLabels(Collections.singletonMap(
                                                                            "kubernetes.io/metadata.name",
                                                                            "kube-system")))
                                                    .podSelector(
                                                            new V1LabelSelector()
                                                                    .matchLabels(Collections.singletonMap("k8s-app",
                                                                            "kube-dns"))))))));
            NetworkingV1Api api = new NetworkingV1Api(client);
            api.createNamespacedNetworkPolicy(namespace, networkPolicy, null, null, null);
            System.out.println(
                    "Network Policy successfully created to allow traffic to kube-dns in namespace " + namespace);
        } catch (ApiException e) {
            System.err.println("Error while creating Network Policy: " + e.getResponseBody());
            e.printStackTrace();
        }
    }

    public void printDash() {
        System.out.println(Main.ANSI_PURPLE + "-".repeat(100) + Main.ANSI_RESET);
    }
}
