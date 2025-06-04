package eu.fluidos.harmonization;

import eu.fluidos.Cluster;
import eu.fluidos.Namespace;
import eu.fluidos.Pod;
import eu.fluidos.cluster.ClusterService;
import eu.fluidos.jaxb.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HarmonizationService{
	private final HarmonizationData harmonizationData = new HarmonizationData();
	private final ClusterService clusterService = new ClusterService();
    private final Logger loggerInfo = LogManager.getLogger("harmonizationManager");
	String arg_1 = "/app/testfile/provider_MSPL_demo.xml";
	String arg_2 = "/app/testfile/consumer_MSPL_demo.xml";

	public static Cluster createProviderCluster() {
        List<Pod> podsProvider = new ArrayList<>();
        Namespace nsP1 = new Namespace();
        nsP1.setSingleLabel("name", "default");
        Namespace nsP2 = new Namespace();
        nsP2.setSingleLabel("name", "monitoring");
		Pod pP1 = createPod("database", nsP1);
        podsProvider.add(pP1);
		Pod pP2 = createPod("product_catalogue", nsP1);
        podsProvider.add(pP2);
        Pod pP3 = createPod("resource_monitor", nsP2);
        podsProvider.add(pP3);
        return new Cluster(podsProvider, null);
    }

	public static Pod createPod(String value, Namespace namespace) {
        Pod pod = new Pod();
        pod.setSingleLabel("app", value);
        pod.setNamespace(namespace);
        return pod;
    }

	public RequestIntents harmonize(Cluster cluster, RequestIntents requestIntents, AuthorizationIntents contracAuthorizationIntents) {
		System.out.println("[HARMONIZATION] Process started...");
        ITResourceOrchestrationType intents_1 = null;
		AuthorizationIntents authIntentsProvider;
		RequestIntents requestIntentsConsumer;
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider;
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer = new HashMap<>();

        try {
            JAXBContext jc = JAXBContext.newInstance("eu.fluidos.jaxb");
            Unmarshaller u = jc.createUnmarshaller();
            Object tmp_1 = u.unmarshal(new FileInputStream(arg_1));
            intents_1 = (ITResourceOrchestrationType) ((JAXBElement<?>) tmp_1).getValue();
        } catch (Exception e) {
            System.out.println(e);
        }

        ITResourceOrchestrationType providerIntents = intents_1;
		requestIntentsConsumer = requestIntents;

		podsByNamespaceAndLabelsProvider = clusterService.initializeHashMaps(cluster);

		
        loggerInfo.debug(
                "[harmonization] - parse the received ITResourceOrchestration types to extract the CONSUMER/PROVIDER intent sets.");
        authIntentsProvider = contracAuthorizationIntents;
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
		List<ConfigurationRule> harmonizedRequest_Consumer = harmonizationData.solveTypeOneDiscordances(requestIntentsConsumer, authIntentsProvider,
				podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider);
		harmonizedRequest_Consumer = harmonizationData.solverTypeTwoDiscordances(harmonizedRequest_Consumer, requestIntentsConsumer, authIntentsProvider, podsByNamespaceAndLabelsProvider, podsByNamespaceAndLabelsConsumer);
		RequestIntents requestIntent = new RequestIntents();
		requestIntent.getConfigurationRule().addAll(harmonizedRequest_Consumer);
        return requestIntent;
    }

	public boolean verify(Cluster cluster, AuthorizationIntents authIntents) {
		AuthorizationIntents authIntentsProvider;
		RequestIntents requestIntentsConsumer;
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider = new HashMap<>();
		HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer;
		ITResourceOrchestrationType intents_1 = null;
		String arg_1 = "/app/testfile/consumer_MSPL_demo.xml";
		boolean verify;
		try {
			JAXBContext jc = JAXBContext.newInstance("eu.fluidos.jaxb");
			Unmarshaller u = jc.createUnmarshaller();
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema sc = sf.newSchema(new File("/app/xsd/mspl.xsd"));
			u.setSchema(sc);
			Object tmp_1 = u.unmarshal(new FileInputStream(arg_1));
			intents_1 = (ITResourceOrchestrationType) ((JAXBElement<?>) tmp_1).getValue();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		ITResourceOrchestrationType consumerIntents = intents_1;
		requestIntentsConsumer = extractRequestIntents(consumerIntents);
		if(authIntents!=null)
			authIntentsProvider = authIntents;
		else {
			System.out.println("authIntents is null");
			return false;
		}
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

	private AuthorizationIntents extractAuthorizationIntents(ITResourceOrchestrationType intent) {
		return intent.getITResource().stream()
				.filter(it -> it.getConfiguration().getClass().equals(AuthorizationIntents.class))
				.map(it -> (AuthorizationIntents) it.getConfiguration()).findFirst().orElse(null);
	}

	private PrivateIntents extractPrivateIntents(ITResourceOrchestrationType intent) {
		return intent.getITResource().stream()
				.filter(it -> it.getConfiguration().getClass().equals(PrivateIntents.class))
				.map(it -> (PrivateIntents) it.getConfiguration()).findFirst().orElse(null);
	}

	private RequestIntents extractRequestIntents(ITResourceOrchestrationType intent) {
		return intent.getITResource().stream()
				.filter(it -> it.getConfiguration().getClass().equals(RequestIntents.class))
				.map(it -> (RequestIntents) it.getConfiguration()).findFirst().orElse(null);
	}

}
