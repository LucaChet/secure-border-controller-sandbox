package eu.fluidos.harmonizationTests;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import eu.fluidos.Cluster;
import eu.fluidos.LabelsKeyValue;
import eu.fluidos.Namespace;
import eu.fluidos.Pod;
import eu.fluidos.harmonization.HarmonizationData_JNSM;
import eu.fluidos.harmonization.HarmonizationService;
import eu.fluidos.harmonization.HarmonizationUtils;
import eu.fluidos.jaxb.ITResourceOrchestrationType;
import eu.fluidos.jaxb.RequestIntents;
import eu.fluidos.traslator.Translator;

@RunWith(MockitoJUnitRunner.class)
public class TranslationTest_JNSM {
    
    private HarmonizationData_JNSM harmonizationData = new HarmonizationData_JNSM();
    private HarmonizationService harmonizationService = new HarmonizationService();
    
    /*
	[TEST CIDR] Average time: 46140.1 ns / 0.046140099999999996 ms
	[TEST CIDR] Total calls: 40 >>> [736781, 736781, 9640, 9640, 9277, 9277, 15624, 15624, 9663, 9663, 7034, 7034, 9419, 9419, 7647, 7647, 7284, 7284, 7460, 7460, 7015, 7015, 7954, 7954, 6785, 6785, 7241, 7241, 8295, 8295, 8139, 8139, 8933, 8933, 24976, 24976, 15338, 15338, 8297, 8297]
	[TEST MIXED] Average time: 647769.7 ns / 0.6477697 ms
	[TEST MIXED] Total calls: 40 >>> [3716745, 3716745, 677779, 677779, 900533, 900533, 1302591, 1302591, 838892, 838892, 402439, 402439, 481540, 481540, 491115, 491115, 527101, 527101, 416248, 416248, 365592, 365592, 477159, 477159, 403430, 403430, 288577, 288577, 266238, 266238, 298821, 298821, 301148, 301148, 256888, 256888, 260120, 260120, 282438, 282438]
	[TEST PODNS] Average time: 117038.5 ns / 0.1170385 ms
	[TEST PODNS] Total calls: 40 >>> [681378, 681378, 98903, 98903, 204428, 204428, 61559, 61559, 125545, 125545, 78016, 78016, 94644, 94644, 57359, 57359, 94694, 94694, 79268, 79268, 64333, 64333, 60816, 60816, 101770, 101770, 99656, 99656, 64293, 64293, 47207, 47207, 84801, 84801, 83588, 83588, 84984, 84984, 73528, 73528]
     */
    @Test
    public void translation_test_mix() {
    	
        List<Long> translationTimes = new ArrayList<>();
        Cluster clusterConsumer = createDummyClusterConsumer();
		Cluster clusterProvider = createDummyClusterProvider();
		Map<String, String> consumerLabels = new HashMap<>();
        Map<String, String> providerLabels = new HashMap<>();
        Map <LabelsKeyValue,String> availablePodsMap = new HashMap<>();
		

		for (Namespace ns : clusterConsumer.getNamespaces()) {
			consumerLabels.putAll(ns.getLabels());
		}
		for (Namespace ns : clusterProvider.getNamespaces()) {
			providerLabels.putAll(ns.getLabels());
		}

        for (Pod pod : clusterConsumer.getPods()) {
            for(String key : pod.getLabels().keySet()) {
                LabelsKeyValue keyValue = new LabelsKeyValue(key, pod.getLabels().get(key));
                availablePodsMap.put(keyValue, pod.getNamespace().getLabels().keySet().iterator().next());
            }
        }

        for (Pod pod : clusterProvider.getPods()) {
            for(String key : pod.getLabels().keySet()) {
                LabelsKeyValue keyValue = new LabelsKeyValue(key, pod.getLabels().get(key));
                availablePodsMap.put(keyValue, pod.getNamespace().getLabels().keySet().iterator().next());
            }
        }
		
		
        try {
            java.io.File directory = new java.io.File("./testfile/translation_mix_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/translation_mix_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/translation_mix_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                ITResourceOrchestrationType intents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
				List<RequestIntents> requestIntents = new ArrayList<>();
                requestIntents.add(HarmonizationUtils.extractRequestIntents(intents));
                
                long startTime = System.nanoTime();
                new Translator(requestIntents,consumerLabels,providerLabels,availablePodsMap);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                translationTimes.add(duration);
                translationTimes.add(duration);
            }
            
            // Calculate and print average
            if (!translationTimes.isEmpty()) {
                double averageTime = translationTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST MIXED] Average time: " + (averageTime) + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST MIXED] Total calls: " + translationTimes.size() + " >>> " + translationTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
        
    }

        @Test
    public void translation_test_cidr() {
    	
        List<Long> translationTimes = new ArrayList<>();
        Cluster clusterConsumer = createDummyClusterConsumer();
		Cluster clusterProvider = createDummyClusterProvider();
		Map<String, String> consumerLabels = new HashMap<>();
        Map<String, String> providerLabels = new HashMap<>();
        Map <LabelsKeyValue,String> availablePodsMap = new HashMap<>();
		

		for (Namespace ns : clusterConsumer.getNamespaces()) {
			consumerLabels.putAll(ns.getLabels());
		}
		for (Namespace ns : clusterProvider.getNamespaces()) {
			providerLabels.putAll(ns.getLabels());
		}

        for (Pod pod : clusterConsumer.getPods()) {
            for(String key : pod.getLabels().keySet()) {
                LabelsKeyValue keyValue = new LabelsKeyValue(key, pod.getLabels().get(key));
                availablePodsMap.put(keyValue, pod.getNamespace().getLabels().keySet().iterator().next());
            }
        }

        for (Pod pod : clusterProvider.getPods()) {
            for(String key : pod.getLabels().keySet()) {
                LabelsKeyValue keyValue = new LabelsKeyValue(key, pod.getLabels().get(key));
                availablePodsMap.put(keyValue, pod.getNamespace().getLabels().keySet().iterator().next());
            }
        }
		
		
        try {
            java.io.File directory = new java.io.File("./testfile/translation_cidr_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/translation_cidr_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/translation_cidr_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                ITResourceOrchestrationType intents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
				List<RequestIntents> requestIntents = new ArrayList<>();
                requestIntents.add(HarmonizationUtils.extractRequestIntents(intents));
                
                long startTime = System.nanoTime();
                new Translator(requestIntents,consumerLabels,providerLabels,availablePodsMap);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                translationTimes.add(duration);
                translationTimes.add(duration);
            }
            
            // Calculate and print average
            if (!translationTimes.isEmpty()) {
                double averageTime = translationTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST CIDR] Average time: " + (averageTime) + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST CIDR] Total calls: " + translationTimes.size() + " >>> " + translationTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
        
    }
        @Test
    public void translation_test_podns() {
    	
        List<Long> translationTimes = new ArrayList<>();
        Cluster clusterConsumer = createDummyClusterConsumer();
		Cluster clusterProvider = createDummyClusterProvider();
		Map<String, String> consumerLabels = new HashMap<>();
        Map<String, String> providerLabels = new HashMap<>();
        Map <LabelsKeyValue,String> availablePodsMap = new HashMap<>();
		

		for (Namespace ns : clusterConsumer.getNamespaces()) {
			consumerLabels.putAll(ns.getLabels());
		}
		for (Namespace ns : clusterProvider.getNamespaces()) {
			providerLabels.putAll(ns.getLabels());
		}

        for (Pod pod : clusterConsumer.getPods()) {
            for(String key : pod.getLabels().keySet()) {
                LabelsKeyValue keyValue = new LabelsKeyValue(key, pod.getLabels().get(key));
                availablePodsMap.put(keyValue, pod.getNamespace().getLabels().keySet().iterator().next());
            }
        }

        for (Pod pod : clusterProvider.getPods()) {
            for(String key : pod.getLabels().keySet()) {
                LabelsKeyValue keyValue = new LabelsKeyValue(key, pod.getLabels().get(key));
                availablePodsMap.put(keyValue, pod.getNamespace().getLabels().keySet().iterator().next());
            }
        }
		
		
        try {
            java.io.File directory = new java.io.File("./testfile/translation_podns_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/translation_podns_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/translation_podns_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                ITResourceOrchestrationType intents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
				List<RequestIntents> requestIntents = new ArrayList<>();
                requestIntents.add(HarmonizationUtils.extractRequestIntents(intents));
                
                long startTime = System.nanoTime();
                new Translator(requestIntents,consumerLabels,providerLabels,availablePodsMap);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                translationTimes.add(duration);
                translationTimes.add(duration);
            }
            
            // Calculate and print average
            if (!translationTimes.isEmpty()) {
                double averageTime = translationTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST PODNS] Average time: " + (averageTime) + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST PODNS] Total calls: " + translationTimes.size() + " >>> " + translationTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
        
    }

    static Cluster createDummyClusterConsumer(){
        List<Pod> pods = new ArrayList<>();
        List<Namespace> namespaces = new ArrayList<>();
        Namespace nsP1 = new Namespace();
        nsP1.setSingleLabel("name", "default");
        Namespace nsP2 = new Namespace();
        nsP2.setSingleLabel("name", "monitoring");
        Pod pP1 = createPod("database", nsP1);
        pods.add(pP1);
        Pod pP2 = createPod("product_catalogue", nsP1);
        pods.add(pP2);
        Pod pP3 = createPod("resource_monitor", nsP2);
        pods.add(pP3);
        namespaces.add(nsP1);
        namespaces.add(nsP2);
    	return new Cluster(pods, namespaces);
    }
	
	static Cluster createDummyClusterProvider(){
        List<Pod> pods = new ArrayList<>();
        List<Namespace> namespaces = new ArrayList<>();
        Namespace nsP1 = new Namespace();
        nsP1.setSingleLabel("name", "development");
        Namespace nsP2 = new Namespace();
        nsP2.setSingleLabel("name", "production");
        Pod pP1 = createPod("frontend", nsP1);
        pods.add(pP1);
        Pod pP2 = createPod("backend", nsP1);
        pods.add(pP2);
        Pod pP3 = createPod("testing", nsP2);
        pods.add(pP3);
        namespaces.add(nsP1);
        namespaces.add(nsP2);
    	return new Cluster(pods, namespaces);
    }

	public static Pod createPod(String value, Namespace namespace) {
    	Pod pod = new Pod();
        pod.setSingleLabel("app", value);
        pod.setNamespace(namespace);
        return pod;
    }
}