package eu.fluidos.orchestrator;

import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Watch;

@Service
public class ContractWatcherService {

    private final ApiClient kubernetesClient;

    public ContractWatcherService(ApiClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
        new Thread(this::startWatcher).start();
    }

    private void startWatcher() {
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(kubernetesClient);
            System.out.println("Starting contract watcher...");
            Watch<JsonObject> watch = Watch.createWatch(
                kubernetesClient,
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

                if ("ADDED".equals(item.type)) 
                    handleContractCreation(item.object);
            }
        }
        catch (ApiException e) {
            System.out.println("Error invoking Kubernetes API: " + e.getMessage());
            System.out.println("Error code: " + e.getCode());
            System.out.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Error starting contract watcher: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleContractCreation(JsonObject contract) {
        String contractName = contract.getAsJsonObject("metadata").get("name").getAsString();
        System.out.println("Contract creation detected: " + contract.getAsJsonObject("metadata").get("name").getAsString());
       
        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(kubernetesClient);

            String patchJson = """
            [
            {
                "op": "replace",
                "path": "/spec/networkRequests",
                "value": "consumer-network-intent"
            }
            ]
            """;

           V1Patch patch = new V1Patch(patchJson);                                                 
            customObjectsApi.patchNamespacedCustomObject(
                "reservation.fluidos.eu",
                "v1alpha1",
                "fluidos",
                "contracts",
                contractName,
                patch,
                null, null, null
            );
            System.out.println("Contract patched with consumer configMap name");
        } catch (ApiException e) {
            System.err.println("API error while patching contract: " + e.getMessage());
              System.out.println("Error code: " + e.getCode());
            System.out.println("Error message: " + e.getResponseBody());
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Generic error while patching contract: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
