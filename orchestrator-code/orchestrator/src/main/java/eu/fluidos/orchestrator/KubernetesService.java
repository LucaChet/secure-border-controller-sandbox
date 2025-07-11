package eu.fluidos.orchestrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.fluidos.jaxb.CIDRSelector;
import eu.fluidos.jaxb.ConfigurationRule;
import eu.fluidos.jaxb.KeyValue;
import eu.fluidos.jaxb.KubernetesNetworkFilteringCondition;
import eu.fluidos.jaxb.PodNamespaceSelector;
import eu.fluidos.jaxb.RequestIntents;
import eu.fluidos.jaxb.ResourceSelector;
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
        
        
        formatIntentsToJson(intents, data);

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
    void formatIntentsToJson(RequestIntents intents, Map<String, String> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ArrayNode formattedConditions = objectMapper.createArrayNode();

        for (ConfigurationRule cr : intents.getConfigurationRule()) {
            KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
            ResourceSelector src = cond.getSource();
            ResourceSelector dst = cond.getDestination();

            ObjectNode conditionNode = objectMapper.createObjectNode();

            
            ObjectNode sourceNode = objectMapper.createObjectNode();
            ObjectNode srcResourceSelector = objectMapper.createObjectNode();
            sourceNode.put("isHostCluster", src.isIsHostCluster());
            ObjectNode destinationNode = objectMapper.createObjectNode();
            ObjectNode dstResourceSelector = objectMapper.createObjectNode();
            destinationNode.put("isHostCluster", dst.isIsHostCluster());

            // Source
            if (src instanceof PodNamespaceSelector) {
                PodNamespaceSelector srcPodNamespaceSelector = (PodNamespaceSelector) src;
                srcResourceSelector.put("typeIdentifier", "PodNamespaceSelector");
                ObjectNode srcSelector = objectMapper.createObjectNode();   
                ObjectNode srcPodSelector = objectMapper.createObjectNode();
                ObjectNode srcNamespaceSelector = objectMapper.createObjectNode();

                for (KeyValue pod : srcPodNamespaceSelector.getPod()) {
                    srcPodSelector.put(pod.getKey(), pod.getValue());
                }
                for (KeyValue ns : srcPodNamespaceSelector.getNamespace()) {
                    srcNamespaceSelector.put(ns.getKey(), ns.getValue());
                }

                srcSelector.set("pod", srcPodSelector);
                srcSelector.set("namespace", srcNamespaceSelector);
                srcResourceSelector.set("selector", srcSelector);
                sourceNode.set("resourceSelector", srcResourceSelector);
                conditionNode.set("source", sourceNode);

            } else if (src instanceof CIDRSelector) {
                CIDRSelector cidrSrc = (CIDRSelector) src;
                srcResourceSelector.put("typeIdentifier", "CIDRSelector");
                srcResourceSelector.put("selector", cidrSrc.getAddressRange());
                
                sourceNode.set("resourceSelector", srcResourceSelector);
                conditionNode.set("source", sourceNode);
            } 
            // Destination
            if (dst instanceof PodNamespaceSelector) {
                PodNamespaceSelector dstPodNamespaceSelector = (PodNamespaceSelector) dst;
                dstResourceSelector.put("typeIdentifier", "PodNamespaceSelector");

                ObjectNode dstSelector = objectMapper.createObjectNode();
                ObjectNode dstPodSelector = objectMapper.createObjectNode();
                ObjectNode dstNamespaceSelector = objectMapper.createObjectNode();

                for (KeyValue pod : dstPodNamespaceSelector.getPod()) {
                    dstPodSelector.put(pod.getKey(), pod.getValue());
                }
                for (KeyValue ns : dstPodNamespaceSelector.getNamespace()) {
                    dstNamespaceSelector.put(ns.getKey(), ns.getValue());
                }

                dstSelector.set("pod", dstPodSelector);
                dstSelector.set("namespace", dstNamespaceSelector);
                dstResourceSelector.set("selector", dstSelector);
                destinationNode.set("resourceSelector", dstResourceSelector);
                conditionNode.set("destination", destinationNode);

                // Altri campi
                conditionNode.put("destinationPort", cond.getDestinationPort());
                conditionNode.put("protocolType", cond.getProtocolType().toString());
                formattedConditions.add(conditionNode);
            } else if(dst instanceof CIDRSelector) {
                CIDRSelector cidrDst = (CIDRSelector) dst;
                dstResourceSelector.put("typeIdentifier", "CIDRSelector");
                dstResourceSelector.put("selector", cidrDst.getAddressRange());
                
                destinationNode.set("resourceSelector", dstResourceSelector);
                conditionNode.set("source", destinationNode);
            }
        }

        try {
            String jsonIntents = objectMapper.writeValueAsString(formattedConditions);
            data.put("networkIntents", jsonIntents);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}