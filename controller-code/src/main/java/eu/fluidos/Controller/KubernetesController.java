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
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import io.kubernetes.client.util.Yaml;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import okhttp3.Request;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.kubernetes.client.util.Yaml;
import eu.fluidos.Module;
import eu.fluidos.jaxb.AuthorizationIntents;
import eu.fluidos.jaxb.CIDRSelector;
import eu.fluidos.jaxb.ConfigurationCondition;
import eu.fluidos.jaxb.ConfigurationRule;
import eu.fluidos.jaxb.ExternalData;
import eu.fluidos.jaxb.HSPL;
import eu.fluidos.jaxb.ITResourceOrchestrationType;
import eu.fluidos.jaxb.KeyValue;
import eu.fluidos.jaxb.KubernetesNetworkFilteringAction;
import eu.fluidos.jaxb.KubernetesNetworkFilteringCondition;
import eu.fluidos.jaxb.PodNamespaceSelector;
import eu.fluidos.jaxb.Priority;
import eu.fluidos.jaxb.ProtocolType;
import eu.fluidos.jaxb.RequestIntents;
import eu.fluidos.jaxb.ResourceSelector;
import eu.fluidos.traslator.Ruleinfo;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.ExecutionException;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;

import eu.fluidos.Crds.TunnelEndpoint;
import eu.fluidos.harmonization.HarmonizationController;
import eu.fluidos.harmonization.HarmonizationService;
import eu.fluidos.Namespace;
import eu.fluidos.Pod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.protobuf.BoolValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import ch.qos.logback.classic.joran.action.ConfigurationAction;
import ch.qos.logback.core.Context;
import eu.fluidos.harmonization.*;;

public class KubernetesController {

    //private final String clusterToken; //Eliminare successivamente quando funziona il controller che si autentica automaticamente
    //private final String clusterApiServerUrl; //Eliminare successivamente quando funziona il controller che si autentica automaticamente
    private final ITResourceOrchestrationType intents;
    private static final Logger LOGGER = Logger.getLogger(KubernetesController.class.getName());
    private List<String> offloadedNamespace;
    private Map<String,List<String>> allowedIpList; //Questa mappa contiene Cluster ID e la lista degl' IP allowed per il cluster ID che ha fatto con me il peering, mi serve per creare le network policies per abilitare il traffico fra le risorse locali del cluster consumer e quelle offlodate
    //Aggiunto per il controller che si autentica automaticamente
    private List<RequestIntents> reqIntentsList = new ArrayList<>(); //Lista di requestIntent da installare
    private ApiClient client;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private HarmonizationController harmController;
    private V1NamespaceList providerNamespaceList;
    private V1NamespaceList consumerNamespaceList;
    private boolean firstCallToModuletimer = true;
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
        "calico-apiserver"
    ));
    public KubernetesController(ITResourceOrchestrationType intents) {
        this.intents=intents;
        try {
            String token = new String(Files.readAllBytes(Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/token"))); //Path per accedere al token assocaito al pod a cui ho associato il service account
            this.client = Config.fromToken("https://kubernetes.default.svc", token, false); //URL all' interno del namespace default per accedere all API server ed autenticarsi
            Configuration.setDefaultApiClient(this.client);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del client Kubernetes", e);
            throw new RuntimeException("Errore durante l'inizializzazione del client Kubernetes", e);
        }
        /*
        this.clusterApiServerUrl = "https://127.0.0.1:36781";
        this.clusterToken ="eyJhbGciOiJSUzI1NiIsImtpZCI6IllUTVk2aEM1OTlvS2pVcWxWR3RqVUFpWF9QQXY2bGhldlYzcXpMYkZ2LVkifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImNvc3R1bS1jb250cm9sbGVyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImNvc3R1bS1jb250cm9sbGVyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiZDBkYzRlOWMtY2RiNS00NjZiLWEwY2EtNjhjOTUxZDA5MGZjIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmRlZmF1bHQ6Y29zdHVtLWNvbnRyb2xsZXIifQ.WnxV9Ci7ymK8eQ1AUOoTHxd1ecTYga57bOf-ekCQDMt0xvy0ssDZt92h1q25tgoQdaKgK8cRIt4qAvnBmTCZj1IiUvWcowrEfoH7qHRRZSRTB_xAV-nQLPgIAFh4SkHMvkFUDgFaa6En2tK3GfFDO_kyIjCQ0sdxKOMhA9TMXX7GU8otM8_rzGF3dOIEVPYmOtPuSoaZK_gHSnMenfc2rNdswIb-vcA9Q3VzXGzgEGrKWpLGoIsEDeO8pA6YjAYrRiRTHl3TRe9ulAZaENN4csFG9qgy9JJSlLB_O5yZXxxvh85uIhISNLnuEgUc1_rENWUVrnM5iPW6s3IbsVwmVg";
        */
        this.offloadedNamespace=new ArrayList<>();
        this.allowedIpList = new HashMap<>();
    }

    public void start() throws Exception {
        
        try {
            LOGGER.info("Connesso al cluster: " + this.client.getBasePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore durante la connessione al cluster Kubernetes", e);
            throw e;
        }
        //ApiClient client = new ApiClient();
        /*
        try{
            AccessTokenAuthentication authentication = new AccessTokenAuthentication(clusterToken);
            this.client = ClientBuilder
                        .standard()
                        .setBasePath(clusterApiServerUrl)
                        .setAuthentication(authentication)
                        .setVerifyingSsl(false)
                        .build();
        }catch(Exception e){
            
        }*/
        CoreV1Api api = new CoreV1Api(client);
        Thread namespaceThread = new Thread(() -> {
            try {
                watchNamespaces(client,api);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error invoking Kubernetes' API: " + e.getMessage());
            }
        });

        Thread defaultDenyNetworkPolicyThread = new Thread(() -> {
            try {
                applyDefaultDenyNetworkPoliciies(client,api);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error invoking Kubernetes' API: " + e.getMessage());
            }
        });

        Thread podThread = new Thread(() -> {
            try {
                watchPods(client,api);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error invoking Kubernetes' API: " + e.getMessage());
            }
        });

        Thread tunnelEndpointThread = new Thread(() -> {
            try {
                watchTunnelEndpoint(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /*Thread flavorsThread = new Thread(() -> {
            try {
                watchFlavors(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

        Thread peeringCandidatesThread = new Thread(() -> {
            try {
                watchPeeringCandidates(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        peeringCandidatesThread.start();
        defaultDenyNetworkPolicyThread.start();
        //flavorsThread.start();
        namespaceThread.start();
        podThread.start();
        tunnelEndpointThread.start();
    }

        public void watchPods(ApiClient client,CoreV1Api api) throws Exception {
            // Watch per gli eventi dei pod
            Watch<V1Pod> podWatch = Watch.createWatch(
                client,
                api.listPodForAllNamespacesCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE, null),
                new TypeToken<Watch.Response<V1Pod>>() {}.getType()
            );

            for (Watch.Response<V1Pod> item : podWatch) {
                V1Pod pod = item.object;
                if (item.type.equals("ADDED") && this.offloadedNamespace.contains(pod.getMetadata().getNamespace())) {
                    System.out.println("New Pod created: " + pod.getMetadata().getName() + ", in Namespace: " + pod.getMetadata().getNamespace());
                    //Module module = new Module(this.intents, client);
                    CreateNetworkPolicies(client, pod.getMetadata().getNamespace());
                } else if (item.type.equals("DELETED")) {
                    System.out.println("Pod deleted: " + pod.getMetadata().getName() + ", from Namespace: " + pod.getMetadata().getNamespace());
                }
            }
        }



    public void applyDefaultDenyNetworkPoliciies(ApiClient client,CoreV1Api api) throws Exception {
        try{
            Watch<V1Namespace> namespaceWatch = Watch.createWatch(
                client,
                api.listNamespaceCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE, null),
                new TypeToken<Watch.Response<V1Namespace>>() {}.getType()
            );
            for (Watch.Response<V1Namespace> item : namespaceWatch) {
                V1Namespace namespace = item.object;

                if (item.type.equals("ADDED") && !namespacesToExclude.contains(namespace.getMetadata().getName()) && !namespace.getMetadata().getName().contains("liqo")) {
                    CreateDefaultDenyNetworkPolicies(client, namespace.getMetadata().getName());
                    createAllowKubeDNSNetworkPolicy(client,namespace.getMetadata().getName());
                    if (isNamespaceOffloaded(namespace)){
                        createNetworkPolicyForIPRange(client,namespace,namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id"));
                    }
                } else if (item.type.equals("DELETED") && isNamespaceOffloaded(namespace)) {
                    System.out.println("Offloaded Namespace deleted: " + namespace.getMetadata().getName());
                } else if (item.type.equals("MODIFIED") && isNamespaceToOffload(namespace)){
                    String key=allowedIpList.keySet().iterator().next();
                    createNetworkPolicyForIPRange(client,namespace,key);
                    System.out.println("Namespace: "+ namespace.getMetadata().getName()+" modified");
                }
            }
    }catch(ApiException e){
        System.err.println("Error invoking Kubenretes' API: " + e.getMessage());
        System.err.println("Error code: " + e.getCode());
        System.err.println("Error message: " + e.getResponseBody());
        e.printStackTrace();
    }
    }

    public void watchNamespaces(ApiClient client,CoreV1Api api) throws Exception {
        try{
            Watch<V1Namespace> namespaceWatch = Watch.createWatch(
                client,
                api.listNamespaceCall(null, null, null, null, null, null, null, null, null, Boolean.TRUE, null),
                new TypeToken<Watch.Response<V1Namespace>>() {}.getType()
            );
            for (Watch.Response<V1Namespace> item : namespaceWatch) {
                V1Namespace namespace = item.object;
                if (item.type.equals("ADDED") && isNamespaceOffloaded(namespace)) {
                    offloadedNamespace.add(namespace.getMetadata().getName());
                    System.out.println("New Offloaded Namespace: " + namespace.getMetadata().getName());
                    if (firstCallToModuletimer){
                        startModuleTimer(client);
                        firstCallToModuletimer = false;
                    }
                        
                    //CreateNetworkPolicies(client, namespace.getMetadata().getName());
                } else if (item.type.equals("DELETED") && isNamespaceOffloaded(namespace)) {
                    System.out.println("Offloaded Namespace deleted: " + namespace.getMetadata().getName());
                } else if (item.type.equals("MODIFIED") && !namespacesToExclude.contains(namespace.getMetadata().getName()) && !namespace.getMetadata().getName().contains("liqo")){
                    if (firstCallToModuletimer){
                            startModuleTimer(client);
                        firstCallToModuletimer = false;
                    }
                    }
                }
    }catch(ApiException e){
        System.err.println("Error invoking Kubenretes' API: " + e.getMessage());
        System.err.println("Error code: " + e.getCode());
        System.err.println("Error message: " + e.getResponseBody());
        e.printStackTrace();
    }
    }

 /*private void startModuleTimer(ApiClient client) throws java.util.concurrent.ExecutionException {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // Timer di 10 secondi
    ScheduledFuture<?> future = scheduler.schedule(() -> {
        // Azioni da eseguire dopo 10 secondi
        HarmonizationController harmController_new = new HarmonizationController();
        System.out.println("10 secondi passati, avvio modulo");
        try {
        listContract(client);
            List<RequestIntents> requestIntentsHarmonizedList = new ArrayList<>();
            for (RequestIntents reqIntent : reqIntentsList) {
                requestIntentsHarmonizedList.add(harmController_new.harmonize(createCluster(client,"provider"),reqIntent));
            }
            System.out.println("Chiamata al modulo");
            Module module = new Module(requestIntentsHarmonizedList, client);
            CoreV1Api api = new CoreV1Api(client);
            V1NamespaceList namespaceList = api.listNamespace(null,null,null,null,null,null,null,null,null,null);
            for (V1Namespace namespace: namespaceList.getItems()){
                CreateNetworkPolicies(client, namespace.getMetadata().getName());
            }
        } catch (Exception e) {
            System.out.println("Errore nella chimata del modulo");
            e.printStackTrace();
        }
    }, 20, TimeUnit.SECONDS);
    
    try {
        // Attendi il completamento del timer prima di procedere
        firstCallToModuletimer=true;
        future.get();  // Questo bloccherà il flusso fino a che il task non è completato
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
}*/

private void startModuleTimer(ApiClient client) {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // Timer di 20 secondi
    scheduler.schedule(() -> {
        HarmonizationController harmController_new = new HarmonizationController();
        //System.out.println("20 secondi passati, avvio modulo");
        try {
            AuthorizationIntents contrAuthorizationIntent = listContract(client);
            if (contrAuthorizationIntent != null) {
                if (reqIntentsList.size() >0 ){
                    List<RequestIntents> requestIntentsHarmonizedList = new ArrayList<>();
                    for (RequestIntents reqIntent : reqIntentsList) {
                            requestIntentsHarmonizedList.add(harmController_new.harmonize(createCluster(client, "provider"), reqIntent,contrAuthorizationIntent));
                    }
                    System.out.println("Start the harmonization");
                    Module module = new Module(requestIntentsHarmonizedList, client);
                    CoreV1Api api = new CoreV1Api(client);
                    V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, null, null);
                    for (V1Namespace namespace : namespaceList.getItems()) {
                        CreateNetworkPolicies(client, namespace.getMetadata().getName());
                    }
                } else {
                    System.out.println("reqIntent is empty");
                }
            }
        } catch (Exception e) {
            System.out.println("Error invoking the harmonization");
            e.printStackTrace();
        }
        firstCallToModuletimer=true;
    }, 5, TimeUnit.SECONDS); // -> era 20

    firstCallToModuletimer = true;


}


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
                    null
            ),
                new TypeToken<Watch.Response<TunnelEndpoint>>() {}.getType()
        );

        for (Watch.Response<TunnelEndpoint> item : watch) {
            if(item.type.equals("ADDED")){
                //System.out.println("Sono dentro il watch di tunnel endpoint");
                TunnelEndpoint tunnelEndpoint = item.object;
                if (tunnelEndpoint != null) {
                    TunnelEndpoint.Status status = tunnelEndpoint.getStatus();
                    if (status != null) {
                        TunnelEndpoint.Status.Connection connection = status.getConnection();
                        if (connection != null) {
                            this.allowedIpList.put(tunnelEndpoint.getMetadata().getLabels().get("clusterID"), new ArrayList<>(connection.getPeerConfiguration().getAllowedIPs()));
                            for (Map.Entry<String, List<String>> entry : this.allowedIpList.entrySet()) {
                                //System.out.println(entry);
                            }
                        }
                    }
                }
            }
        }
    }catch(ApiException e){
        System.err.println("Error invoking Kubenretes' API: " + e.getMessage());
        System.err.println("Error code: " + e.getCode());
        System.err.println("Error message: " + e.getResponseBody());
        e.printStackTrace();
    }
    }

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
                null                
            );
            Map<String, Object> responseMap = (Map<String, Object>) response;
            List<Map<String, Object>> items = (List<Map<String, Object>>) responseMap.get("items");
            
            for (Map<String, Object> item : items) {
                Map<String, Object> spec = (Map<String, Object>) item.get("spec");
                Map<String, Object> flavor = (Map<String, Object>) spec.get("flavor");
                Map<String, Object> flavorSpec = (flavor != null) ? (Map<String, Object>) flavor.get("spec") : null;
                String networkPropertyType = (flavorSpec != null && flavorSpec.containsKey("networkPropertyType")) ?
                        (String) flavorSpec.get("networkPropertyType") : null;

                String networkRequests = spec.containsKey("networkRequests") ? (String) spec.get("networkRequests") : null;
                String buyerClusterID = spec.containsKey("buyerClusterID") ? (String) spec.get("buyerClusterID") : null;

                if (networkRequests != null){
                    for (String namespace : offloadedNamespace){
                        try {
                            RequestIntents reqIntentToAdd = accessConfigMap(client,namespace,networkRequests);
                            if (reqIntentToAdd != null){
                                reqIntentsList.add(reqIntentToAdd);
                            }    
                        } catch (Exception e){

                        }

                    }
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(flavorSpec);
                JsonObject flavorSpecJson = gson.fromJson(jsonString, JsonObject.class);
                JsonObject flavorType = (flavorSpec != null) ? flavorSpecJson.getAsJsonObject("flavorType") : null;
                JsonObject typeData = (flavorType != null) ? flavorType.getAsJsonObject("typeData"):null;
                contrAuthorizationIntents = accessFlavour(typeData);
                //System.out.println("Stampa authIntent del provider presi dal contratto: ");
                //StampaAuthIntents(contractAuthIntents);
            }
            return contrAuthorizationIntents;
        } catch (ApiException e) {
            System.err.println("Error invoking Kubenretes' API: " + e.getMessage());
            System.err.println("Error code: " + e.getCode());
            System.err.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
        return contrAuthorizationIntents;
    }

public AuthorizationIntents insertAuthorizationIntents (JsonObject typeData){
    AuthorizationIntents authIntent = new AuthorizationIntents();
    List<ConfigurationRule> forbiddenConnectionList = authIntent.getForbiddenConnectionList();
    List<ConfigurationRule> mandatoryConnectionList = authIntent.getMandatoryConnectionList();
    JsonObject characteristics = typeData.getAsJsonObject("characteristics");
    KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
    Priority prio = new Priority();
    boolean isCNF = false;
    if (characteristics != null) {
        if(characteristics.has("action")){
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
            populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList,action,prio,isCNF);
            JsonArray mandatoryCommunications = characteristics.getAsJsonArray("mandatoryCommunications");
            populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList,action,prio,isCNF);
        } else if (characteristics.has("mandatoryCommunications")) {
            JsonArray mandatoryCommunications = characteristics.getAsJsonArray("mandatoryCommunications");
            populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList,action,prio,isCNF);
        } else if (characteristics.has("deniedCommunications")) {
            JsonArray deniedCommunications = characteristics.getAsJsonArray("deniedCommunications");
            populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList,action,prio,isCNF);
        }else {
            System.out.println("Mandatory Communications and Denied communications not found.");
        }
    } else {
        System.out.println("Characteristics not found.");
    }
    return authIntent;
}
    
public Cluster createCluster (ApiClient client,String whoIs){
    CoreV1Api api = new CoreV1Api(client);
    Cluster myCluster = new Cluster(null,null);
    try {
    V1NamespaceList namespaceList = api.listNamespace(null,null,null,null,null,null,null,null,null,null);
    V1NamespaceList epuratedNamespaceList = new V1NamespaceList();
    Epurate1(namespaceList);
    if (whoIs.equals("provider")){
        epuratedNamespaceList=providerNamespaceList;
    }else if (whoIs.equals("consumer")){
        epuratedNamespaceList=consumerNamespaceList;
    }
    
    List <Namespace> NamespaceList = new ArrayList<>();
    List <Pod> PodList = new ArrayList<>();
    for (V1Namespace namespace : epuratedNamespaceList.getItems()){
        Namespace nm = new Namespace();
        HashMap<String, String> hashMapLabels = new HashMap<>(namespace.getMetadata().getLabels());
        //System.out.println("Chiamata per il namespace:"+namespace.getMetadata().getName());
        //nm.setLabels(hashMapLabels);
        //setto solo la prima label:
        HashMap<String, String> hashMapSingleLabels = new HashMap<>();
        Map.Entry<String, String> firstLabel = hashMapLabels.entrySet().iterator().next();
        hashMapSingleLabels.put(firstLabel.getKey(), firstLabel.getValue());
        nm.setLabels(hashMapSingleLabels);
        NamespaceList.add(nm);
        V1PodList podList = api.listNamespacedPod(namespace.getMetadata().getName(), null, null, null, null, null, null, null, null, null, null);
        for (V1Pod pod : podList.getItems()) {
            Pod pd = new Pod();
            HashMap<String, String> podHashMapLabels = new HashMap<>(pod.getMetadata().getLabels());
            ////setto solo la prima label:
            //pd.setLabels(podHashMapLabels);
            HashMap<String, String> hashMapSinglePodsLabels = new HashMap<>();
            Map.Entry<String, String> firstLabelPod = podHashMapLabels.entrySet().iterator().next();
            hashMapSinglePodsLabels.put(firstLabelPod.getKey(), firstLabelPod.getValue());
            pd.setLabels(hashMapSinglePodsLabels);
            pd.setNamespace(nm);
            PodList.add(pd);
            //System.out.println("Pod:" + pd.getLabels() + "Namespace" + pd.getNamespace().getLabels());
        }
    }

    myCluster.setNamespaces(NamespaceList);
    myCluster.setPods(PodList);
    //Lui dovrà fare l' Epurate dei namespace
    
} catch (ApiException e) {
    System.err.println("Error: ");
    e.printStackTrace();
    System.err.println("Error invoking Kubenretes' API: " + e.getResponseBody());
}
return myCluster;
}

private void Epurate1(V1NamespaceList namespaceList){
    providerNamespaceList=new V1NamespaceList();
    consumerNamespaceList = new V1NamespaceList();

    for (V1Namespace namespace : namespaceList.getItems()) {
        if (!namespacesToExclude.contains(namespace.getMetadata().getName()) && !namespace.getMetadata().getName().contains("liqo") && !namespace.getMetadata().getName().contains("remote-cluster")) {
            //Questa chiave è contenuta nei pod del cluster locale che vengono offloadati, mentre nel cluster host i pod offloadati hanno altre label, potrei usare un flag che dato in ingresso al modulo permette di settare se il cluster è locale o l' host in modo poi da discriminare queste cose
            /*
            if (this.isLocal){
            if (namespace.getMetadata().getLabels().containsKey("liqo.io/scheduling-enabled") && (namespace.getMetadata().getLabels().containsValue("true"))){
                namespaces.put(namespace.getMetadata().getName(),"remote");
            }else{
                namespaces.put(namespace.getMetadata().getName(),"local");
            }
            }else{
                */
            
            if (!namespace.getMetadata().equals(null) && !namespace.getMetadata().getLabels().containsKey("liqo.io/remote-cluster-id")){
                providerNamespaceList.addItemsItem(namespace);
                //System.out.println("Namespace provider aggiunto: "+ namespace.getMetadata().getName());
            }else{
                consumerNamespaceList.addItemsItem(namespace);
                //System.out.println("Namespace consumer aggiunto: "+ namespace.getMetadata().getName());
            }
            //namespaces.add(namespace.getMetadata().getName());
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
            System.out.println("ConfigMap found for namespace: "+namespace);
            //System.out.println(configMap);
            JsonArray jsonArray = JsonParser.parseString(networkIntent).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray){
                KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject source = jsonObject.getAsJsonObject("source");
                JsonObject destination = jsonObject.getAsJsonObject("destination");

                condition.setSource(parseResourceSelector(source.getAsJsonObject("resourceSelector")));
                condition.setDestination(parseResourceSelector(destination.getAsJsonObject("resourceSelector")));
                if (source.has("sourcePort") && !source.get("sourcePort").isJsonNull()){
                    condition.setSourcePort(source.get("sourcePort").getAsString());
                }
        
                if (jsonObject.has("destinationPort") && !jsonObject.get("destinationPort").isJsonNull()){
                    condition.setDestinationPort(jsonObject.get("destinationPort").getAsString());
                    //System.out.println("ce l'ha: "+jsonObject.get("destinationPort").getAsString());
                }
        
                if (jsonObject.has("protocolType") && !jsonObject.get("protocolType").isJsonNull()){
                    //System.out.println("Protocollo letto:"+ProtocolType.valueOf(jsonObject.get("protocolType").getAsString()));
                    condition.setProtocolType(ProtocolType.valueOf(jsonObject.get("protocolType").getAsString()));
                }
                ConfigurationRule rule = new ConfigurationRule();
                rule.setConfigurationCondition(condition);
                rule.setName(configMap.getData().get("name"));

                KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
                if (configMap.getData().get("action") != null){
                    action.setKubernetesNetworkFilteringActionType(configMap.getData().get("action"));
                    rule.setConfigurationRuleAction(action);
                }

                if(configMap.getData().get("isCNF") != null){
                    boolean isCNF=false;
                    if(configMap.getData().get("isCNF").equals(true)){
                        isCNF=true;
                    }
                    rule.setIsCNF(isCNF);
                }
                
                if(configMap.getData().get("acceptMonitoring") != null){
                    boolean acceptMonitoring=false;
                    if(configMap.getData().get("acceptMonitoring").equals("true")){
                        acceptMonitoring=true;
                    }
                    requestIntent.setAcceptMonitoring(acceptMonitoring);
                }

                if(configMap.getData().get("priority") != null){
                    Priority prio = new Priority();
                    BigInteger bigPrio = new BigInteger(configMap.getData().get("priority"));
                    prio.setValue(bigPrio);
                    rule.setExternalData(prio);
                }
                
                requestIntent.getConfigurationRule().add(rule);
            }
            //System.out.println("Stampa della ConfigMap:");
            return requestIntent;
            //StampaAuthIntents(authInt);
            //Adesso l' ho testato così, ossia ho aggiunto la lista delle configurationRule ad una lista preesistente di mandatoryconnection, tuttavia l' obbiettico è inviare la lsita delle configuration RUle all armonizzatore essendo solo quelle ad essere Request Intent
        } else {
            return null;
        }

    } catch (Exception e) {
        //System.out.println("No configMap found for namespace: "+namespace);
        return null;
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
            new TypeToken<Watch.Response<JsonObject>>() {}.getType()
        );

        AtomicBoolean firstTimeToCallVerifier = new AtomicBoolean(false);
        AtomicBoolean timerStarted = new AtomicBoolean(false);
        List <JsonObject> listTypeData = new ArrayList<>();
        String flavorName = new String();
        String peeringCandidateName = new String();
        List <AuthorizationIntents> listReturnedAuthorizationIntents = new ArrayList<>();
        List <String> listPeeringCandidateName = new ArrayList<>();
        for (Watch.Response<JsonObject> item : watch) {
            if (item.type.equals("ADDED")) {
                JsonObject peeringCandidate= item.object;
                peeringCandidateName = peeringCandidate.getAsJsonObject("metadata").get("name").getAsString();
                JsonObject spec = peeringCandidate.getAsJsonObject("spec");
                JsonObject flavor = spec.getAsJsonObject("flavor");
                JsonObject metadata = flavor.getAsJsonObject("metadata");
                flavorName = metadata.get("name").getAsString();
                System.out.println("Peering Candidate: "+peeringCandidateName);
                JsonObject spec1 = flavor.getAsJsonObject("spec");
                JsonObject flavorType = spec1.getAsJsonObject("flavorType");
                //String myFluidosID = retrieveFluidosId(client);
                //String providerID = spec1.get("providerID").getAsString();
                JsonObject typeData = flavorType.getAsJsonObject("typeData");
                AuthorizationIntents returnedAuthorizationIntents = accessFlavour(typeData);
                if (returnedAuthorizationIntents != null){
                    listReturnedAuthorizationIntents.add(returnedAuthorizationIntents);
                    listPeeringCandidateName.add(peeringCandidateName);
                }

                //printDash();
                //Faccio uso di un timer per ritardare il primo avvio, in quanto al primo avvio ho 4 flavour, in questo modo non chiamo più volte il verifier
                if (timerStarted.compareAndSet(false, true)) {
                    // Se il timer non è ancora stato avviato, lo avvia
                    scheduler.schedule(() -> {
                        firstTimeToCallVerifier.set(true);
                        //printDash();
                        if (!listReturnedAuthorizationIntents.isEmpty()){
                            System.out.println("Start the verifier");
                            callVerifier(listReturnedAuthorizationIntents,listPeeringCandidateName);
                        }
                    }, 5, TimeUnit.SECONDS); // -> era 20
                } 
                listTypeData.add(typeData);
                //Se dopo il primo avvio viene aggiunto un flavour, tale flavour viene comparato con tutti i flavour precedenti
                if(firstTimeToCallVerifier.get()){
                    //printDash();
                    if (!listReturnedAuthorizationIntents.isEmpty()){
                        System.out.println("Start the verifier");
                        callVerifier(listReturnedAuthorizationIntents,listPeeringCandidateName);
                    }
                    //System.out.println("[VERIFIER] valore riornato: "+callVerifier(listTypeData));
                }
                }
                }
    } catch (ApiException e) {
        System.err.println("Error invoking Kubenretes' API: " + e.getMessage());
        System.err.println("Error code: " + e.getCode());
        System.err.println("Error message: " + e.getResponseBody());
        e.printStackTrace();
    }
}
/*
 public void watchFlavors(ApiClient client) throws Exception {
    try {
        CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);

        Watch<JsonObject> watch = Watch.createWatch(
            client,
            customObjectsApi.listNamespacedCustomObjectCall(
                "nodecore.fluidos.eu",
                "v1alpha1",
                "fluidos",
                "flavors",
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
            new TypeToken<Watch.Response<JsonObject>>() {}.getType()
        );
        AtomicBoolean firstTimeToCallVerifier = new AtomicBoolean(false);
        AtomicBoolean timerStarted = new AtomicBoolean(false);
        //List <JsonObject> listTypeData = new ArrayList<>();
        List <AuthorizationIntents> listReturnedAuthorizationIntents = new ArrayList<>();
        String flavorName = new String();
        for (Watch.Response<JsonObject> item : watch) {
            if (item.type.equals("ADDED") || item.type.equals("MODIFIED")) {
                JsonObject flavor = item.object;
                JsonObject metadata = flavor.getAsJsonObject("metadata");
                flavorName = metadata.get("name").getAsString();
                JsonObject spec = flavor.getAsJsonObject("spec");
                JsonObject flavorType = spec.getAsJsonObject("flavorType");
                String myFluidosID = retrieveFluidosId(client);
                String providerID = spec.get("providerID").getAsString();
                JsonObject typeData = flavorType.getAsJsonObject("typeData");

                AuthorizationIntents returnedAuthIntents = accessFlavour(typeData);
                StampaAuthIntents(returnedAuthIntents);
                listReturnedAuthorizationIntents.add(returnedAuthIntents);

                printDash();
                //Faccio uso di un timer per ritardare il primo avvio, in quanto al primo avvio ho 4 flavour, in questo modo non chiamo più volte il verifier
                if (timerStarted.compareAndSet(false, true)) {
                    // Se il timer non è ancora stato avviato, lo avvia
                    scheduler.schedule(() -> {
                        firstTimeToCallVerifier.set(true);
                        //System.out.println("Avvio verifier");
                        printDash();
                        //System.out.println("[VERIFIER] valore riornato: "+callVerifier(listTypeData));
                    }, 20, TimeUnit.SECONDS);
                } 
                

                //Se dopo il primo avvio viene aggiunto un flavour, tale flavour viene comparato con tutti i flavour precedenti
                if(firstTimeToCallVerifier.get()){
                    //System.out.println("Avvio verifier");
                    printDash();
                        //System.out.println("[VERIFIER] valore riornato: "+callVerifier(listTypeData));
                    }
                }
                }
    } catch (ApiException e) {
        System.err.println("Errore durante la chiamata all'API Kubernetes per cercare i flavor: " + e.getMessage());
        System.err.println("Codice di errore: " + e.getCode());
        System.err.println("Corpo della risposta: " + e.getResponseBody());
        e.printStackTrace();
    }
}
*/

AuthorizationIntents accessFlavour (JsonObject typeData){
    AuthorizationIntents authorizationIntents = new AuthorizationIntents();
    List<ConfigurationRule> forbiddenConnectionList = authorizationIntents.getForbiddenConnectionList();
    List<ConfigurationRule> mandatoryConnectionList = authorizationIntents.getMandatoryConnectionList();
    if(typeData != null){
        if (!typeData.has("properties") || typeData.getAsJsonObject("properties")==null || typeData.getAsJsonObject("properties").isEmpty()  ){
            return null;
        }
        JsonObject properties = typeData.getAsJsonObject("properties");
        JsonObject networkAuthorizations = properties.getAsJsonObject("networkAuthorizations");
        KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
        Priority prio = new Priority();
        boolean isCNF = false;
        if (networkAuthorizations != null) {
            //NOTA: Bisogna accertarsi che flavour abbia questi campi in PROPERTIES e non in characteristics
            if(properties.has("action")){
                action.setKubernetesNetworkFilteringActionType(properties.get("action").getAsString());
            }

            if (properties.has("isCNF")) {
                isCNF = properties.get("isCNF").getAsBoolean();
            }

            if (properties.has("externalData")) {
                JsonObject externalDataJson = properties.getAsJsonObject("externalData");
                prio.setValue(externalDataJson.getAsJsonObject("priority").getAsBigInteger());
            }
            if (networkAuthorizations.has("deniedCommunications") && networkAuthorizations.has("mandatoryCommunications")) {
                if (networkAuthorizations.get("deniedCommunications") != null && networkAuthorizations.get("mandatoryCommunications")!=null){
                    JsonArray deniedCommunications = networkAuthorizations.getAsJsonArray("deniedCommunications");
                    populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList,action,prio,isCNF);
                    JsonArray mandatoryCommunications = networkAuthorizations.getAsJsonArray("mandatoryCommunications");
                    populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList,action,prio,isCNF);   
                }
            } else if (networkAuthorizations.has("mandatoryCommunications")) {
                if ( networkAuthorizations.get("mandatoryCommunications")!=null){
                    JsonArray mandatoryCommunications = networkAuthorizations.getAsJsonArray("mandatoryCommunications");
                    populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList,action,prio,isCNF);
                }
            } else if (networkAuthorizations.has("deniedCommunications")) {
                if ( networkAuthorizations.get("deniedCommunications")!=null){
                    JsonArray deniedCommunications = networkAuthorizations.getAsJsonArray("deniedCommunications");
                    populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList,action,prio,isCNF);
                }
            }else {
                System.out.println("Mandatory Communications and Denied communications not found.");
            }
        } else {
            System.out.println("Characteristics not found.");
        }
        if (authorizationIntents != null && authorizationIntents.getForbiddenConnectionList().size() > 0 && authorizationIntents.getMandatoryConnectionList().size() > 0){
            return authorizationIntents;
        }

    
}
    return null;    
}

private void callVerifier (List<AuthorizationIntents> listReturnedAuthorizationIntents, List<String> names){
    int i = 0;
    for (AuthorizationIntents authorizationIntents : listReturnedAuthorizationIntents){
        if (authorizationIntents != null && authorizationIntents.getForbiddenConnectionList().size() > 0 && authorizationIntents.getMandatoryConnectionList().size() > 0){
            HarmonizationController harmonizationController = new HarmonizationController();
            System.out.println("[+] Processing PeeringCandidate: "+ names.get(i));
            Boolean value = harmonizationController.verify(createCluster(client,"consumer"),authorizationIntents);
            if (value){
                System.out.println("[+] Result for PeeringCandidate "+ names.get(i) +" from Verifier is: "+ Main.ANSI_GREEN + value + Main.ANSI_RESET);
                return;
            }
            System.out.println("[+] Result for PeeringCandidate "+ names.get(i) +" from Verifier is: "+ Main.ANSI_RED + value + Main.ANSI_RESET);
            
            //printDash();
            System.out.println(" ");
        }
        i++;
    }

}
/*
private boolean callVerifier(List<JsonObject> listTypeData){
    Boolean value = false;
    for (JsonObject typeData : listTypeData){
        AuthorizationIntents authorizationIntents = new AuthorizationIntents();
        List<ConfigurationRule> forbiddenConnectionList = authorizationIntents.getForbiddenConnectionList();
        List<ConfigurationRule> mandatoryConnectionList = authorizationIntents.getMandatoryConnectionList();
        if (typeData != null) {
            JsonObject characteristics = typeData.getAsJsonObject("characteristics");
            KubernetesNetworkFilteringAction action = new KubernetesNetworkFilteringAction();
            Priority prio = new Priority();
            boolean isCNF = false;
            if (characteristics != null) {
                if(characteristics.has("action")){
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
                    populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList,action,prio,isCNF);
                    JsonArray mandatoryCommunications = characteristics.getAsJsonArray("mandatoryCommunications");
                    populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList,action,prio,isCNF);
                } else if (characteristics.has("mandatoryCommunications")) {
                    JsonArray mandatoryCommunications = characteristics.getAsJsonArray("mandatoryCommunications");
                    populateAuthorizationIntents(mandatoryCommunications, mandatoryConnectionList,action,prio,isCNF);
                } else if (characteristics.has("deniedCommunications")) {
                    JsonArray deniedCommunications = characteristics.getAsJsonArray("deniedCommunications");
                    populateAuthorizationIntents(deniedCommunications, forbiddenConnectionList,action,prio,isCNF);
                }else {
                    System.out.println("Mandatory Communications and Denied communications not found.");
                }
            } else {
                System.out.println("Characteristics not found.");
            }
            if (authorizationIntents != null && authorizationIntents.getForbiddenConnectionList().size() > 0 && authorizationIntents.getMandatoryConnectionList().size() > 0){
                HarmonizationController harmonizationController = new HarmonizationController();
                value = harmonizationController.verify(createCluster(client,"consumer"),authorizationIntents);
                //System.out.println("[VERIFIER] Valore dalla chiamata del : " + value +" per il flavour");
                //printDash();
                //System.out.println(" ");
                forbiddenConnectionList.clear();
                mandatoryConnectionList.clear();
                if (value){
                    return value;
                }
            }
        } else {
            System.out.println("TypeData not found.");
        }
    }
    return value;
}*/

private String retrieveFluidosId (ApiClient client) throws ApiException{

    CoreV1Api api = new CoreV1Api(client);
    V1ConfigMap configMap = api.readNamespacedConfigMap("fluidos-network-manager-identity", "fluidos", null);
    String nodeID = configMap.getData().get("nodeID");
    //System.out.println("NodeID: " + nodeID);

    return nodeID;
}
private void populateAuthorizationIntents(JsonArray communications, List<ConfigurationRule> connectionList,KubernetesNetworkFilteringAction action,Priority prio,boolean isCNF) {
    for (JsonElement comm : communications) {
        JsonObject communication = comm.getAsJsonObject();
        

        KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();
        
        JsonObject source = communication.getAsJsonObject("source");
        JsonObject destination = communication.getAsJsonObject("destination");

        condition.setSource(parseResourceSelector(source.getAsJsonObject("resourceSelector")));
        condition.setDestination(parseResourceSelector(destination.getAsJsonObject("resourceSelector")));

        if (communication.has("sourcePort") && !communication.get("sourcePort").isJsonNull()){
            condition.setSourcePort(communication.get("sourcePort").getAsString());
        }

        if (communication.has("destinationPort") && !communication.get("destinationPort").isJsonNull()){
            condition.setDestinationPort(communication.get("destinationPort").getAsString());
        }

        if (communication.has("protocolType") && !communication.get("protocolType").isJsonNull()){
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
        //System.out.println("Valore isHostCluster: " + resourceSelector.get("isHotCluster").getAsBoolean());
        selector.setIsHostCluster(resourceSelector.get("isHotCluster").getAsBoolean());
    }

    return selector;
}
    //Stampa di prova, da commentare
    void StampaAuthIntents(AuthorizationIntents authorizationIntents) {
        System.out.println(" ");
        //printDash();
        if (authorizationIntents != null) {
            System.out.println("Mandatory communications: ");
            List<ConfigurationRule> mandatoryConnectionList = authorizationIntents.getMandatoryConnectionList();
            if (mandatoryConnectionList != null) {
                for (ConfigurationRule cr : mandatoryConnectionList) {
                        KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
                        System.out.println("Source:");
                        if(cond.getSource().getClass().equals(PodNamespaceSelector.class)){
                            PodNamespaceSelector pns = (PodNamespaceSelector) cond.getSource();
                            for (KeyValue namespace: pns.getNamespace()){
                                System.out.println("namespace");
                                System.out.println("key:"+namespace.getKey()+" "+"value: "+namespace.getValue());
                            }
                            for (KeyValue pod: pns.getPod()){
                                System.out.println("pod");
                                System.out.println("key:"+pod.getKey()+" "+"value: "+pod.getValue());
                            }
                        } else {
                            CIDRSelector CIDRAddress = (CIDRSelector) cond.getSource();
                            System.out.println("cidrDestination: "+CIDRAddress.getAddressRange());

                        }
                        System.out.println("Destination:");
                        if(cond.getDestination().getClass().equals(PodNamespaceSelector.class)){
                            PodNamespaceSelector pns = (PodNamespaceSelector) cond.getDestination();
                            for (KeyValue namespace: pns.getNamespace()){
                                System.out.println("namespace");
                                System.out.println("key:"+namespace.getKey()+" "+"value: "+namespace.getValue());
                            }
                            for (KeyValue pod: pns.getPod()){
                                System.out.println("pod");
                                System.out.println("key:"+pod.getKey()+" "+"value: "+pod.getValue());
                            }
                        } else {
                            CIDRSelector CIDRAddress = (CIDRSelector) cond.getDestination();
                            System.out.println("cidrDestination: "+CIDRAddress.getAddressRange());

                        }
                        String destPort = cond.getDestinationPort();
                        System.out.println("DestinationPort: "+destPort);
                        String proto = cond.getProtocolType().getClass().toString();
                        System.out.println("ProtocolType: "+proto);
                        
                }
                System.out.println(" ");
            }
            System.out.println("Denied communications: ");
            List<ConfigurationRule> deniedConnectionList = authorizationIntents.getForbiddenConnectionList();
            if (deniedConnectionList != null) {
                for (ConfigurationRule cr : deniedConnectionList) {
                        KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
                        System.out.println("Source:");
                        if(cond.getSource().getClass().equals(PodNamespaceSelector.class)){
                            PodNamespaceSelector pns = (PodNamespaceSelector) cond.getSource();
                            for (KeyValue namespace: pns.getNamespace()){
                                System.out.println("namespace");
                                System.out.println("key:"+namespace.getKey()+" "+"value: "+namespace.getValue());
                            }
                            for (KeyValue pod: pns.getPod()){
                                System.out.println("pod");
                                System.out.println("key:"+pod.getKey()+" "+"value: "+pod.getValue());
                            }
                        } else {
                            CIDRSelector CIDRAddress = (CIDRSelector) cond.getSource();
                            System.out.println("cidrDestination: "+CIDRAddress.getAddressRange());

                        }
                        System.out.println("Destination:");
                        if(cond.getDestination().getClass().equals(PodNamespaceSelector.class)){
                            PodNamespaceSelector pns = (PodNamespaceSelector) cond.getDestination();
                            for (KeyValue namespace: pns.getNamespace()){
                                System.out.println("namespace");
                                System.out.println("key:"+namespace.getKey()+" "+"value: "+namespace.getValue());
                            }
                            for (KeyValue pod: pns.getPod()){
                                System.out.println("pod");
                                System.out.println("key:"+pod.getKey()+" "+"value: "+pod.getValue());
                            }
                        } else {
                            CIDRSelector CIDRAddress = (CIDRSelector) cond.getDestination();
                            System.out.println("cidrDestination: "+CIDRAddress.getAddressRange());

                        }
                        String destPort = cond.getDestinationPort();
                        System.out.println("DestinationPort: "+destPort);
                        
                }
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    /*
    private boolean isNamespaceOffloaded(V1Namespace namespace) {
        if (namespace.getMetadata().getAnnotations() != null) {
            String remoteClusterId = namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id");
            if (remoteClusterId != null && !remoteClusterId.isEmpty()) {
                createAllowKubeDNSNetworkPolicy(client,namespace.getMetadata().getName());
                try{
                    createNetworkPolicyForIPRange(client,namespace,namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id"));
                }catch(ApiException e){
                    e.printStackTrace();
                    System.err.println("Errore: "+e.getResponseBody());
                }
                return true;
            }
        }
        return false;
    }
    */

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

    private void CreateNetworkPolicies (ApiClient client,String Namespace){
        NetworkingV1Api api = new NetworkingV1Api(client);
        List<File> files = getFilesInFolder("/app/network_policies/");
        for (File file : files) {
            try {
            String yamlContent = new String(Files.readAllBytes(file.toPath()));
            V1NetworkPolicy networkPolicy = Yaml.loadAs(yamlContent, V1NetworkPolicy.class);
                try {
                if (networkPolicy.getMetadata().getNamespace().equals(Namespace)){
                    api.createNamespacedNetworkPolicy(networkPolicy.getMetadata().getNamespace(), networkPolicy, null, null, null);
                    System.out.println("NetworkPolicy: "+networkPolicy.getMetadata().getName()+" applied to namespace "+ Namespace);
                }
                } catch (ApiException e){
                    //System.err.println("Errore: "+e.getResponseBody());
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

public void createNetworkPolicyForIPRange(ApiClient client, V1Namespace namespace,String clusterId) throws ApiException {
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
                    .policyTypes(List.of("Ingress", "Egress"))  // Specifica sia Ingress che Egress
                    .podSelector(new V1LabelSelector())  // Seleziona tutti i pod
                    .ingress(buildIngressRules(this.allowedIpList.get(clusterId)))  // Regole Ingress
                    .egress(buildEgressRules(this.allowedIpList.get(clusterId))));  // Regole Egress
    
    // Applica la NetworkPolicy combinata
    
    

    System.out.println ("Network Policies to allow traffic between offloaded pods and local ones applied to namespace: "+offloadedNamespace);
    networkingApi.createNamespacedNetworkPolicy(offloadedNamespace, combinedPolicy, null, null, null);
}
    private List<V1NetworkPolicyEgressRule> buildEgressRules(List<String> ipAddress) {        
        if(ipAddress == null){
            return Collections.emptyList();
        }
        return ipAddress.stream()
                .map(ip -> new V1NetworkPolicyEgressRule()
                        .to(List.of(new V1NetworkPolicyPeer()
                                .ipBlock(new V1IPBlock().cidr(ip)))))
                .toList();
    }

    private List<V1NetworkPolicyIngressRule> buildIngressRules(List<String> ipAddress) {
        if(ipAddress == null){
            return Collections.emptyList();
        }
        return ipAddress.stream()
                .map(ip -> new V1NetworkPolicyIngressRule()
                        .from(List.of(new V1NetworkPolicyPeer()
                                .ipBlock(new V1IPBlock().cidr(ip)))))
                .toList();
    }


    public void createAllowKubeDNSNetworkPolicy(ApiClient client,String namespace) {
        try {
            V1NetworkPolicy networkPolicy = new V1NetworkPolicy();

            networkPolicy.setApiVersion("networking.k8s.io/v1");
            networkPolicy.setKind("NetworkPolicy");
            networkPolicy.setMetadata(new V1ObjectMeta().name("default-allow-kubedns-"+namespace).namespace(namespace));

            networkPolicy.setSpec(new V1NetworkPolicySpec()
                    .podSelector(new V1LabelSelector())
                    .policyTypes(Collections.singletonList("Egress"))
                    .egress(Collections.singletonList(
                            new V1NetworkPolicyEgressRule()
                                    .to(Collections.singletonList(
                                            new V1NetworkPolicyPeer()
                                                    .namespaceSelector(
                                                            new V1LabelSelector()
                                                                    .matchLabels(Collections.singletonMap("kubernetes.io/metadata.name", "kube-system"))
                                                    )
                                                    .podSelector(
                                                            new V1LabelSelector()
                                                                    .matchLabels(Collections.singletonMap("k8s-app", "kube-dns"))
                                                    )
                                    ))
                    ))
            );
            NetworkingV1Api api = new NetworkingV1Api(client);
            api.createNamespacedNetworkPolicy(namespace, networkPolicy, null, null, null);
            System.out.println("Network Policy successfully created to allow traffic to kube-dns in namespace " + namespace);
        } catch (ApiException e) {
            System.err.println("Error while creating Network Policy: " + e.getResponseBody());
            e.printStackTrace();
        }
    }

    public void printDash() {
        System.out.println(Main.ANSI_PURPLE + "-".repeat(100) + Main.ANSI_RESET);
    }
}

/* Questa è la versione del controllore da implementare per rendere automatica e generale l' autenticazione del controllore.
 public class KubernetesController {
    private static final Logger LOGGER = Logger.getLogger(KubernetesController.class.getName());
    private ApiClient client;

    public KubernetesController() {

        try {
            String token = new String(Files.readAllBytes(Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/token"))); //Path per accedere al token assocaito al pod a cui ho associato il service account
            this.client = Config.fromToken("https://kubernetes.default.svc", token, false); //URL all' interno del namespace default per accedere all API server ed autenticarsi
            Configuration.setDefaultApiClient(this.client);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inizializzazione del client Kubernetes", e);
            throw new RuntimeException("Errore durante l'inizializzazione del client Kubernetes", e);
        }
    }

    public void start() throws Exception {
        try {
            LOGGER.info("Connesso al cluster: " + this.client.getBasePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore durante la connessione al cluster Kubernetes", e);
            throw e;
        }
    }
}
 */
