package eu.fluidos.orchestrator;

import java.io.IOException;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

//import java.io.ObjectInputFilter.Config;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import eu.fluidos.jaxb.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.fluidos.jaxb.ConfigurationRule;

// TODO: add the necessary imports for Kubernetes client and other dependencies
@SpringBootApplication
public class OrchestratorApplication {
    

    public OrchestratorApplication() {
        
    }
    public void start(String[] args) {
        
        
    }

}
