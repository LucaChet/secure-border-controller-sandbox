package eu.fluidos.harmonization;

import eu.fluidos.Cluster;
import eu.fluidos.Pod;
import eu.fluidos.cluster.ClusterService;
import eu.fluidos.jaxb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class HarmonizationService{
	private final HarmonizationData harmonizationData = new HarmonizationData();
	private final ClusterService clusterService = new ClusterService();
    private final Logger loggerInfo = LogManager.getLogger("harmonizationManager");

	String consumer_mspl = "/app/testfile/consumer_MSPL_demo.xml";

	public RequestIntents harmonize(Cluster cluster, RequestIntents requestIntents, AuthorizationIntents authorizationIntents) {
		System.out.println("[HARMONIZATION] Process started...");

		AuthorizationIntents authIntentsProvider;
		RequestIntents requestIntentsConsumer;
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider;
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer = new HashMap<>();

		requestIntentsConsumer = requestIntents;
		authIntentsProvider = authorizationIntents;
		
		podsByNamespaceAndLabelsProvider = clusterService.initializeHashMaps(cluster);
		
 		System.out.println("[HARMONIZATION] Process the request intents:");
		harmonizationData.printRequestIntents(requestIntentsConsumer, "consumer");
		System.out.println("[HARMONIZATION] Process the authorization intents:");
		harmonizationData.printAuthorizationIntents(authIntentsProvider);

		if(authIntentsProvider != null) {
			if (authIntentsProvider.getMandatoryConnectionList().size() > 1 && !requestIntentsConsumer.isAcceptMonitoring()) {
				return null;
			}
		}else{
			System.out.println("authIntentsProvider is null");
			return null;
		}

		System.out.println("[HARMONIZATION] Solving discordances...");
		List<ConfigurationRule> harmonizedRequest_Consumer = harmonizationData.solveTypeOneDiscordances(requestIntentsConsumer, authIntentsProvider, podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider);
		harmonizedRequest_Consumer = harmonizationData.solverTypeTwoDiscordances(harmonizedRequest_Consumer, requestIntentsConsumer, authIntentsProvider, podsByNamespaceAndLabelsProvider, podsByNamespaceAndLabelsConsumer);
		RequestIntents requestIntent = new RequestIntents();
		requestIntent.getConfigurationRule().addAll(harmonizedRequest_Consumer);
        return requestIntent;
    }

	/* LEGACY METHOD
	public boolean verify(Cluster cluster, AuthorizationIntents authIntents) {
		AuthorizationIntents authIntentsProvider;
		RequestIntents requestIntentsConsumer;
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider = new HashMap<>();
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer;
	
		boolean verify;
		
		//TODO: remove hardcoded MSPL files
		ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile(consumer_mspl);
		requestIntentsConsumer = HarmonizationUtils.extractRequestIntents(consumerIntents);
		
		if(authIntents!=null)
			authIntentsProvider = authIntents;
		else {
			System.out.println("authIntents is null");
			return false;
		}


		//TODO: currently, the cluster is not used in the verification process. Need further checks.
		podsByNamespaceAndLabelsConsumer = clusterService.initializeHashMaps(cluster);

		System.out.println("[+] Checking authorization intents:");
		harmonizationData.printAuthorizationIntents(authIntentsProvider);
		if (requestIntentsConsumer != null){
			if (authIntentsProvider.getMandatoryConnectionList().size()>1 && !requestIntentsConsumer.isAcceptMonitoring()) {
				return false;
			}
			else
				verify = harmonizationData.verify(requestIntentsConsumer, authIntentsProvider,
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider);

			return verify;
		}else{
			System.out.println("RequestIntents is null");
			return false;
		}
	}
	*/

	public boolean verify(RequestIntents reqIntents, AuthorizationIntents authIntents) {
		boolean verifyResult;
		
		//TODO: remove hardcoded MSPL files
		
		if(authIntents==null) {
			System.out.println("authorization intents is null");
			return false;
		}

		if(reqIntents==null) {
			System.out.println("request intents is null");
			return false;
		}

		System.out.println("[+] Checking authorization intents:");
		harmonizationData.printAuthorizationIntents(authIntents); //Needed?
		
		if (authIntents.getMandatoryConnectionList().size()>1 && !reqIntents.isAcceptMonitoring()) {
			return false;
		}
		else
			verifyResult = harmonizationData.verify(reqIntents, authIntents);

		return verifyResult;
	}
}
