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
import eu.fluidos.traslator.Traslator;
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
    private Map<String,String> localNamespaces;
    private Map<String,String> remoteNamespaces; //mappa con namespace remoto e cluster-id

    // public Module(ITResourceOrchestrationType intentsToTraslate) throws Exception {

    //     this.intents = intentsToTraslate;
    //     String kubeConfigPath = "C:/Users/salva/Desktop/config";
    //     ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
    //     Configuration.setDefaultApiClient(client);
    //     CoreV1Api api = new CoreV1Api();
    //     try {
    //         V1PodList podList= api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null,null);
    //         V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, null, null);
    //         List<String> namespaces = Epurate(namespaceList);
    //         for (String n : namespaces){
    //             System.out.println(n);
    //         }
    //         Traslator intent_traslation = new Traslator(intentsToTraslate,namespaces);
    //     } catch (Exception e) {
    //         System.err.println("Exception when calling CoreV1Api#listPodForAllNamespaces");
    //         e.printStackTrace();
    //     }
        
    // }

    public Module(List<RequestIntents> reqIntentsListHarmonized,ApiClient client) throws Exception {
        this.isLocal=false;
        this.client=client;
        this.localNamespaces = new HashMap<>();
        this.remoteNamespaces = new HashMap<>();
        //String url = "https://127.0.0.1:45917";
        //String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IkE1bkRvOFlSanZpUGtSRzZGYWV0cWRnMzBkLXFrY0t5WXppWXo3VVFPUWcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InRyYXNsYXRvci1zZXJ2aWNlLWFjY291bnQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoidHJhc2xhdG9yLXNlcnZpY2UtYWNjb3VudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjBjYTFhMjhmLWQ3OWEtNDQ5NS1iZTE1LTc5Y2M4YmIzNGNkMiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OnRyYXNsYXRvci1zZXJ2aWNlLWFjY291bnQifQ.xnbVyB4bA7q613T8SkdjGsG36edYk8MT6tK5QFD9cv8R38YRAKi4ullslCR-esQb2sZ5oGbij3ObsH1CNUjUaKkzbGZUqTCpmONI24hLlnY9KN4Q6kelfcwhmwl7wBzPJkDrNi9N9w9wua94jzeKLDLJeqGUmXobZqikv0iXunRVH0BlzDfJ1uX5A3v3j2caLG6bqybEmnCTIavEJPSTzsBYmO6DKUhDTgMc3dXXhB51Qy0BgbpNTRzV11jOjpvaSY1RzyqWa8qsvRwMVDiqMaTuK5crCU2BbD7JWzaDv58Uyg9WKSrO5W-sybq1tDdg_DGlfApodTginYCj396I9A";      
        //String url_rome="https://127.0.0.1:36465";
        //String token_rome="eyJhbGciOiJSUzI1NiIsImtpZCI6IllzRWpjWmZodjJzVUhfdUk0YzNYZW9mdjdOV1Z4YkRBUzhlTjRjdzh4SXMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InRyYXNsYXRvci1zZXJ2aWNlLWFjY291bnQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoidHJhc2xhdG9yLXNlcnZpY2UtYWNjb3VudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjFlMGExOWQ4LWIwMDYtNDhkMi1iZWRiLTBmNzk1YjQyMjRhOCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OnRyYXNsYXRvci1zZXJ2aWNlLWFjY291bnQifQ.ZLnlANGuP5aEAur0Ax4PuC5Rwh68VzQLlsMcRp0pc80KSU5G_nSZQs2ktWK3MOrOH3lgl737oALkW88oyBlxu1ttLhjNWv9946_8yFtDZb4CPJwhvRWbLK8q9sndB9TC3U_0HIPj8FbQFMxu3V-S9a5cyKOAQizYFNmEis_ap157WMhEBfOZk7qQ2EA9qLMWHp12fhXVoIlljZWmNukYdOiZFmS_hvnOF7sYG5fDHOi561oOs1wjYwLgb89PDieLhuXEYBsnPTYddFUVzBDNWeRG1N93_ju5qRQtd6BNh0-pSgIuy0ysgKUMP7dQNmc3GstEOXzfpuf62KBxOor3dQ";
        //Devo testare una network_policy che da un pod nel cluster virtuale offloadato vadi verso internet
        //String url_milan="https://127.0.0.1:34459";
        //String token_milan="eyJhbGciOiJSUzI1NiIsImtpZCI6ImlXNHJCSm5sckt3YnRCOTRtd0dFRmpPTy1DT0NEMGZYa1hXUklvUEYzeUkifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InRyYXNsYXRvci1zZXJ2aWNlLWFjY291bnQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoidHJhc2xhdG9yLXNlcnZpY2UtYWNjb3VudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjQxNzg1Njg5LTQ1MTMtNGYwNS1hMzk0LTUzMmJhY2MyOWM4MCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDpkZWZhdWx0OnRyYXNsYXRvci1zZXJ2aWNlLWFjY291bnQifQ.aXaCcyLf6ZPKAosXGvqKVzls7rLb_qPJfxch5LMKwJ0cCDORUBi03spesiZT6NN5IqS34xfjCXdCgszuvjWcGxsKYib0d4XTA3HQmoAsH6vBX2AN4xSmDwuRdCo7r-OX0VuPJzkfFMCNyK-LOVeLxyNdxa9gJt9N9mmLBMdRoZ0GYu_q38hPbmKMfXz4CNESvifMLwzlnU_smm0ZUSEp6gSb5f-14RsVaJxQQF5zR6jb2-Q_S-JwvOcFfUijOvILyOki_alMEkA1QS5xFuveMJ7Syo3eaXR210MgKee8sgsbfIL9WAf2__Tu1UezcqU9zBcMkFd9l5MDxRWrarWsDg";
        // Qui mi sono connesso al cluster locale Rome che sta offlodando alcuni namespace nel cluster remoto Milan. Grazie alle label che ottengo dalla lista dei namespace, sono capace di capire se quel namespaceè offlodato nel cluster remoto o meno
        boolean validateSSL = false;
        //KubernetesController controller = new KubernetesController();
        /*
        ApiClient client = new ApiClient();
        if (this.isLocal){
            AccessTokenAuthentication authentication = new AccessTokenAuthentication(token_rome);
            client = ClientBuilder
                        .standard()
                        .setBasePath(url_rome)
                        .setAuthentication(authentication)
                        .setVerifyingSsl(validateSSL)
                        .build();
        } else {
            AccessTokenAuthentication authentication = new AccessTokenAuthentication(token_milan);
            client = ClientBuilder
                        .standard()
                        .setBasePath(url_milan)
                        .setAuthentication(authentication)
                        .setVerifyingSsl(validateSSL)
                        .build();          
        }
        */
        CoreV1Api api = new CoreV1Api(client);
        
        try {
            V1NamespaceList namespaceList = api.listNamespace(null,null,null,null,null,null,null,null,null,null);
            /*
            for (V1Namespace item : namespaceList.getItems()) {
                System.out.println(item.getMetadata().getName());
                System.out.println(item.getMetadata().getLabels());
                if (item.getMetadata().getLabels().containsKey("liqo.io/scheduling-enabled") && (item.getMetadata().getLabels().containsValue("true"))){
                    System.out.println("Si lo contniene");
                }
            }*/
            List<String> namespaces = Epurate(namespaceList);
            Epurate1(namespaceList);


            System.out.println("List of local namespaces obtained from API server:");
            for (Map.Entry<String, String> entry : this.localNamespaces.entrySet()) { //nella key metto il nome del namespace
                System.out.println("Name: "+entry.getKey());
            }
            //System.out.println("");

            System.out.println("List of remote namespaces obtained from API server:");
            for (Map.Entry<String, String> entry : this.remoteNamespaces.entrySet()) {
                System.out.println("Name: "+entry.getKey()+" cluster ID: "+entry.getValue());
            }
            //System.out.println("");
            
            List<String> namePods = new ArrayList<>();
            List<LabelsKeyValue> labels = new ArrayList<>();
            Map <LabelsKeyValue,String> availablePodsMap = new HashMap();
            for (Map.Entry<String, String> entry : this.localNamespaces.entrySet()){
                String namespaceName = entry.getKey();
                System.out.println("List of pods obtained from API server in local namespace "+namespaceName+":");
                V1PodList podList = api.listNamespacedPod(namespaceName, null, null, null, null, null, null, null, null, null, null);
                for (V1Pod pod : podList.getItems()) {
                    System.out.println(pod.getMetadata().getName());
                    namePods.add(pod.getMetadata().getName());
                    String key = pod.getMetadata().getLabels().keySet().iterator().next();
                    String value = pod.getMetadata().getLabels().values().iterator().next();
                    availablePodsMap.put(new LabelsKeyValue(key,value), namespaceName);
                    //labels.add(new LabelsKeyValue(key,value));
                }
                //System.out.println("");
            }
            //System.out.println("");

            for (Map.Entry<String, String> entry : this.remoteNamespaces.entrySet()){
                String namespaceName = entry.getKey();
                System.out.println("List of pods obtained from API server in remote namespace "+namespaceName+":");
                V1PodList podList = api.listNamespacedPod(namespaceName, null, null, null, null, null, null, null, null, null, null);
                for (V1Pod pod : podList.getItems()) {
                    System.out.println(pod.getMetadata().getName());
                    namePods.add(pod.getMetadata().getName());
                    String key = pod.getMetadata().getLabels().keySet().iterator().next();
                    String value = pod.getMetadata().getLabels().values().iterator().next();
                    availablePodsMap.put(new LabelsKeyValue(key,value), namespaceName);
                    //labels.add(new LabelsKeyValue(key,value));
                }
                //System.out.println("");
            }
            //System.out.println("");
            Traslator intent_traslation = new Traslator(reqIntentsListHarmonized,this.localNamespaces,this.remoteNamespaces,availablePodsMap,this.isLocal);
            //CreateNetworkPolicies(client);
            

                
        } catch (ApiException e) {
            System.err.println("Error: ");
            e.printStackTrace();
            System.err.println("Error while invoking Kuernetes' API: " + e.getResponseBody());
        }
    
    }


    private List<String> Epurate(V1NamespaceList namespaceList){
        List<String> namespacesToExclude = new ArrayList<>(Arrays.asList(
            "calico-apiserver",
            "calico-system",
            "kube-node-lease",
            "kube-public",
            "kube-system",
            "local-path-storage",
            "tigera-operator"
        ));
        List<String> namespaces = new ArrayList<String>(); 
        for (V1Namespace namespace : namespaceList.getItems()) {
            if (!namespacesToExclude.contains(namespace.getMetadata().getName())) {
                namespaces.add(namespace.getMetadata().getName());
            }
        }
        
        return namespaces;
    }

    private void Epurate1(V1NamespaceList namespaceList){
        List<String> namespacesToExclude = new ArrayList<>(Arrays.asList(
            "calico-apiserver",
            "calico-system",
            "kube-node-lease",
            "kube-public",
            "kube-system",
            "local-path-storage",
            "tigera-operator",
            "cert-manager",
            "fluidos"
        ));

        List<String> namespaces = new ArrayList<String>();
        for (V1Namespace namespace : namespaceList.getItems()) {
            String liqo = "liqo";
            if (!namespacesToExclude.contains(namespace.getMetadata().getName()) && !namespace.getMetadata().getName().contains("liqo")) {
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
                if (namespace.getMetadata().getLabels().containsKey("liqo.io/remote-cluster-id")){
                    //namespaces.put(namespace.getMetadata().getName(),"remote");
                    this.remoteNamespaces.put(namespace.getMetadata().getName(),namespace.getMetadata().getLabels().get("liqo.io/remote-cluster-id"));
                }else{
                    //namespaces.put(namespace.getMetadata().getName(),"local");
                    this.localNamespaces.put(namespace.getMetadata().getName(),"local");
                }                    
                //namespaces.add(namespace.getMetadata().getName());
    }
    }
}

    private void CreateNetworkPolicies (ApiClient client){
        NetworkingV1Api api = new NetworkingV1Api(client);
        List<File> files = getFilesInFolder("C:/Users/salva/Desktop/traslator/fluidos-security-orchestrator/fluidos-security-orchestrator/src/network_policies");
        for (File file : files) {
            try {
            String yamlContent = new String(Files.readAllBytes(file.toPath()));
            V1NetworkPolicy networkPolicy = Yaml.loadAs(yamlContent, V1NetworkPolicy.class);
                try {
                api.createNamespacedNetworkPolicy(networkPolicy.getMetadata().getNamespace(), networkPolicy, null, null, null);
                System.out.println("NetworkPolicy: "+networkPolicy.getMetadata().getName()+" - applied");
                } catch (ApiException e){
                    System.err.println("Error: "+e.getResponseBody());
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
