package eu.fluidos.orchestrator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.protobuf.Api;

import eu.fluidos.jaxb.ConfigurationRule;
import eu.fluidos.jaxb.KubernetesNetworkFilteringCondition;
import eu.fluidos.jaxb.RequestIntents;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;


@Service
public class KubernetesService {

    private final ApiClient kubernetesClient;

    public KubernetesService(ApiClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public void buildConfigMapFromIntents(RequestIntents intents, String namespace) {
        V1ConfigMap configMap = new V1ConfigMap().apiVersion("v1").kind("ConfigMap");
        V1ObjectMeta meta = new V1ObjectMeta();
        Map<String, String> data = new HashMap<>();
        List<KubernetesNetworkFilteringCondition> conditions = new ArrayList<>();
        String configMapName = "consumer-network-intent";
        System.out.println("Building ConfigMap from RequestIntents... ");
        meta.setName(configMapName);
        meta.setNamespace(namespace);
        configMap.setMetadata(meta);

        data.put("name", "example-intent");
        data.put("isCNF", "true");
        data.put("priority", "3000");
        data.put("action", "Allow");
        data.put("acceptMonitoring", String.valueOf(intents.isAcceptMonitoring()));
        
        
        for(ConfigurationRule cr: intents.getConfigurationRule()) {
            KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
            conditions.add(cond);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonIntents;
            jsonIntents = objectMapper.writeValueAsString(conditions);
            data.put("networkIntents", jsonIntents);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        configMap.setData(data);
        
        System.out.println("ConfigMap created with name: " + configMap.getMetadata().getName());
        System.out.println("ConfigMap data: " + data);
        CoreV1Api api = new CoreV1Api(this.kubernetesClient);
        try {
           api.createNamespacedConfigMap(namespace, configMap, null, null, null);
           System.out.println("ConfigMap " + configMapName + " created in namespace " + namespace);
        } catch (ApiException e) {
            System.out.println("Exception occurred when calling CoreV1Api#createNamespacedConfigMap: " + e.getMessage());
                System.out.println("Status code: " + e.getCode());
                System.out.println("Response body: " + e.getResponseBody());
                System.out.println("Response headers: " + e.getResponseHeaders());
                e.printStackTrace();
        }
          
    }
    
}