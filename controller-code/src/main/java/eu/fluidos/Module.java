package eu.fluidos;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import eu.fluidos.jaxb.KeyValue;
import eu.fluidos.jaxb.RequestIntents;
import eu.fluidos.jaxb.ITResourceOrchestrationType;
import eu.fluidos.traslator.Translator;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.auth.ApiKeyAuth;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;
import io.fabric8.kubernetes.api.model.ConfigBuilder;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.authenticators.Authenticator;
import java.io.BufferedReader;
import java.io.FileReader;
import io.kubernetes.client.openapi.apis.AuthenticationV1Api;
import io.kubernetes.client.openapi.auth.*;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1Networking.NetworkPolicy;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.ApiException;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import eu.fluidos.LabelsKeyValue;
import eu.fluidos.Controller.*;

import java.io.File;

public class Module {
    private boolean isLocal;
    private ApiClient client;
    private Map<String, String> localNamespaces;
    private Map<String, String> remoteNamespaces;

    public Module(List<RequestIntents> reqIntentsListHarmonized, ApiClient client) throws Exception {
        this.isLocal = false;
        this.client = client;
        this.localNamespaces = new HashMap<>();
        this.remoteNamespaces = new HashMap<>();
        boolean validateSSL = false;
        CoreV1Api api = new CoreV1Api(client);

        try {
            V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, null,
                    null);
            List<String> namespaces = Epurate(namespaceList);
            Epurate1(namespaceList);
            System.out.println("List of local namespaces obtained from API server:");
            for (Map.Entry<String, String> entry : this.localNamespaces.entrySet()) { 
                System.out.println("Name: " + entry.getKey());
            }

            System.out.println("List of remote namespaces obtained from API server:");
            for (Map.Entry<String, String> entry : this.remoteNamespaces.entrySet()) {
                System.out.println("Name: " + entry.getKey() + " cluster ID: " + entry.getValue());
            }

            List<String> namePods = new ArrayList<>();
            List<LabelsKeyValue> labels = new ArrayList<>();
            Map<LabelsKeyValue, String> availablePodsMap = new HashMap();
            for (Map.Entry<String, String> entry : this.localNamespaces.entrySet()) {
                String namespaceName = entry.getKey();
                System.out.println("List of pods obtained from API server in local namespace " + namespaceName + ":");
                V1PodList podList = api.listNamespacedPod(namespaceName, null, null, null, null, null, null, null, null,
                        null, null);
                for (V1Pod pod : podList.getItems()) {
                    System.out.println(pod.getMetadata().getName());
                    namePods.add(pod.getMetadata().getName());
                    String key = pod.getMetadata().getLabels().keySet().iterator().next();
                    String value = pod.getMetadata().getLabels().values().iterator().next();
                    availablePodsMap.put(new LabelsKeyValue(key, value), namespaceName);
                }
            }

            for (Map.Entry<String, String> entry : this.remoteNamespaces.entrySet()) {
                String namespaceName = entry.getKey();
                System.out.println("List of pods obtained from API server in remote namespace " + namespaceName + ":");
                V1PodList podList = api.listNamespacedPod(namespaceName, null, null, null, null, null, null, null, null,
                        null, null);
                for (V1Pod pod : podList.getItems()) {
                    System.out.println(pod.getMetadata().getName());
                    namePods.add(pod.getMetadata().getName());
                    String key = pod.getMetadata().getLabels().keySet().iterator().next();
                    String value = pod.getMetadata().getLabels().values().iterator().next();
                    availablePodsMap.put(new LabelsKeyValue(key, value), namespaceName);
                }
            }
            Translator intent_traslation = new Translator(reqIntentsListHarmonized, this.localNamespaces,
                    this.remoteNamespaces, availablePodsMap);//, this.isLocal);
        } catch (ApiException e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.err.println("Error while invoking Kuernetes' API: " + e.getResponseBody());
        }

    }

    private List<String> Epurate(V1NamespaceList namespaceList) {
        List<String> namespacesToExclude = new ArrayList<>(Arrays.asList(
                "calico-apiserver",
                "calico-system",
                "kube-node-lease",
                "kube-public",
                "kube-system",
                "local-path-storage",
                "tigera-operator"));
        List<String> namespaces = new ArrayList<String>();
        for (V1Namespace namespace : namespaceList.getItems()) {
            if (!namespacesToExclude.contains(namespace.getMetadata().getName())) {
                namespaces.add(namespace.getMetadata().getName());
            }
        }
        return namespaces;
    }

    private void Epurate1(V1NamespaceList namespaceList) {
        List<String> namespacesToExclude = new ArrayList<>(Arrays.asList(
                "calico-apiserver",
                "calico-system",
                "kube-node-lease",
                "kube-public",
                "kube-system",
                "local-path-storage",
                "tigera-operator",
                "cert-manager",
                "fluidos"));

        List<String> namespaces = new ArrayList<String>();
        for (V1Namespace namespace : namespaceList.getItems()) {
            String liqo = "liqo";
            if (!namespacesToExclude.contains(namespace.getMetadata().getName())
                    && !namespace.getMetadata().getName().contains("liqo")) {
                if (namespace.getMetadata().getLabels().containsKey("liqo.io/remote-cluster-id")) {
                    this.remoteNamespaces.put(namespace.getMetadata().getName(),
                            namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id"));
                } else {
                    this.localNamespaces.put(namespace.getMetadata().getName(), "local");
                }
            }
        }
    }

    private void CreateNetworkPolicies(ApiClient client) {
        NetworkingV1Api api = new NetworkingV1Api(client);
        List<File> files = getFilesInFolder(
                "C:/Users/salva/Desktop/traslator/fluidos-security-orchestrator/fluidos-security-orchestrator/src/network_policies");
        for (File file : files) {
            try {
                String yamlContent = new String(Files.readAllBytes(file.toPath()));
                V1NetworkPolicy networkPolicy = Yaml.loadAs(yamlContent, V1NetworkPolicy.class);
                try {
                    api.createNamespacedNetworkPolicy(networkPolicy.getMetadata().getNamespace(), networkPolicy, null,
                            null, null);
                    System.out.println("NetworkPolicy: " + networkPolicy.getMetadata().getName() + " - applied");
                } catch (ApiException e) {
                    System.err.println("Error: " + e.getResponseBody());
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
}
