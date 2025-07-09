package eu.fluidos.harmonizationTests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import eu.fluidos.jaxb.*;
import eu.fluidos.Cluster;
import eu.fluidos.Namespace;
import eu.fluidos.Pod;
import eu.fluidos.harmonization.HarmonizationData_JNSM;
import eu.fluidos.harmonization.HarmonizationService;
import eu.fluidos.harmonization.HarmonizationUtils;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HarmonizeTest_JNSM {
    
    private HarmonizationData_JNSM harmonizationData = new HarmonizationData_JNSM();
    private HarmonizationService harmonizationService = new HarmonizationService();
    private List<List<Long>> harmonizationTimeList = new ArrayList<>();
        
    

    @Test
    public void harmonize_test_scalabiliy() {
    	
    	/*
    	  	[TEST] Average harmonization time: 111856.6 ns / 0.1118566 ms [ dimension: 100, percentage: 0]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 79492.5 ns / 0.0794925 ms [ dimension: 300, percentage: 0]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 118096.3 ns / 0.1180963 ms [ dimension: 500, percentage: 0]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 148119.9 ns / 0.1481199 ms [ dimension: 700, percentage: 0]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 201939.8 ns / 0.20193979999999997 ms [ dimension: 1000, percentage: 0]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 285063.2 ns / 0.2850632 ms [ dimension: 1500, percentage: 0]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 3.96450433E7 ns / 39.6450433 ms [ dimension: 100, percentage: 30]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 1.902210001E8 ns / 190.2210001 ms [ dimension: 300, percentage: 30]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 4.822466725E8 ns / 482.2466725 ms [ dimension: 500, percentage: 30]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 9.756602935E8 ns / 975.6602935 ms [ dimension: 700, percentage: 30]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 1.8667930794E9 ns / 1866.7930794000001 ms [ dimension: 1000, percentage: 30]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 4.0486194634E9 ns / 4048.6194634000003 ms [ dimension: 1500, percentage: 30]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 9.50558979E7 ns / 95.0558979 ms [ dimension: 100, percentage: 60]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 7.20517216E8 ns / 720.517216 ms [ dimension: 300, percentage: 60]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 2.0037330877E9 ns / 2003.7330877 ms [ dimension: 500, percentage: 60]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 3.8386071259E9 ns / 3838.6071259 ms [ dimension: 700, percentage: 60]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 7.7823039818E9 ns / 7782.3039818 ms [ dimension: 1000, percentage: 60]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 1.69384158129E10 ns / 16938.4158129 ms [ dimension: 1500, percentage: 60]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 2.570480445E8 ns / 257.0480445 ms [ dimension: 100, percentage: 100]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 1.9973671302E9 ns / 1997.3671302 ms [ dimension: 300, percentage: 100]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 5.4628032935E9 ns / 5462.8032935 ms [ dimension: 500, percentage: 100]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 1.05308617883E10 ns / 10530.861788299999 ms [ dimension: 700, percentage: 100]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 2.10607462442E10 ns / 21060.7462442 ms [ dimension: 1000, percentage: 100]
			[TEST] Total harmonization calls: 10
			[TEST] Average harmonization time: 4.73319919903E10 ns / 47331.991990300005 ms [ dimension: 1500, percentage: 100]
			[TEST] Total harmonization calls: 10
    	 */

        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/harmonize_JNSM/consumer.xml");
        fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
        java.io.File directory = new java.io.File("./testfile/verify_monitoring_JNSM");
        if (!directory.exists() || !directory.isDirectory()) {
        	fail("Directory ./testfile/verify_JNSM does not exist");
            return;
        }
        
        //define input for test
        int[] dimensions = {100, 300, 500, 700, 1000, 1500};
        int[] percentages = {0,30,60,100};
        int rounds = 10;
        
        
        for (int percentage: percentages) {
            for (int dimension: dimensions) {
            	for (int i=0; i<rounds; i++) {
	            	int tmpPerc = (100 - percentage);
	            	AuthorizationIntents authorizationIntents = harmonizationData.generateAuthorizationIntents(dimension, tmpPerc, 0, 0, 0, percentage);
	                
	                long startTime = System.nanoTime();
	                Cluster cluster = createDummyCluster();
	                harmonizationService.harmonize(cluster, fixedConsumerIntents, authorizationIntents);
	                long endTime = System.nanoTime();
	                        
	                long duration = endTime - startTime;
	                verifyTimes.add(duration);
            	}
                
            	double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST] Average harmonization time: " + (averageTime) + " ns / " + (averageTime / 1_000_000.0) + " ms [ dimension: " + dimension + ", percentage: " + percentage + "]");
                System.out.println("[TEST] Total harmonization calls: " + verifyTimes.size());
                verifyTimes.clear();
            }
        }
    }


    static Cluster createDummyCluster(){
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

        public static Pod createPod(String value, Namespace namespace) {
            Pod pod = new Pod();
            pod.setSingleLabel("app", value);
            pod.setNamespace(namespace);
            return pod;
        }
}