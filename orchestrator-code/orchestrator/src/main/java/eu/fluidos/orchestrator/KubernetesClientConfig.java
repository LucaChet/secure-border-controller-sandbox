package eu.fluidos.orchestrator;

import java.io.IOException;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import eu.fluidos.jaxb.*;
//import org.springframework.context.annotation.Configuration;

@org.springframework.context.annotation.Configuration
public class KubernetesClientConfig {

    private ApiClient client;

    @Bean
    public ApiClient KubernetesClient() {
        try {
            String token = new String(Files.readAllBytes(Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/token"))); 
            this.client = Config.fromToken("https://kubernetes.default.svc", token, false);                                       
            Configuration.setDefaultApiClient(this.client);
            return this.client;
        } catch (IOException e) {
            System.out.println("Error reading Kubernetes token: " + e.getMessage());
            e.printStackTrace();
                
        }
        try {
            return Config.defaultClient();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}