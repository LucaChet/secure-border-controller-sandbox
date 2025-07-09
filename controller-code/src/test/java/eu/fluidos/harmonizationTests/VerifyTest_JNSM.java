package eu.fluidos.harmonizationTests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import eu.fluidos.jaxb.*;

import eu.fluidos.harmonization.HarmonizationService;
import eu.fluidos.harmonization.HarmonizationUtils;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class VerifyTest_JNSM {
    
    private HarmonizationService harmonizationService = new HarmonizationService();
    private List<Long> verifyTimesTotal = new ArrayList<>();
    /*
	[TEST NON-OVERLAPPING SRC (CIDR)] Average verify time: 47826.15 ns / 0.047826150000000005 ms
	[TEST NON-OVERLAPPING SRC (CIDR)] Total verify calls: 20 >>> [110675, 28077, 62212, 56421, 31842, 31972, 30863, 41469, 31317, 30824, 50014, 31755, 26758, 62294, 56285, 59734, 71800, 32501, 54739, 54971]
	[TEST OVERLAPPING (CIDR SELECTORS)] Average verify time: 41489.45 ns / 0.04148945 ms
	[TEST OVERLAPPING (CIDR SELECTORS)] Total verify calls: 20 >>> [34670, 31645, 47170, 43548, 31567, 37076, 31607, 34082, 84079, 81895, 64287, 64347, 32579, 30348, 29809, 36878, 33115, 25132, 28101, 27854]
	[TEST NON-OVERLAPPING DIFFERENT SELECTORS] Average verify time: 20074.1 ns / 0.020074099999999998 ms
	[TEST NON-OVERLAPPING DST DIFFERENT SELECTORS] Total verify calls: 20 >>> [21441, 19086, 58492, 43767, 18811, 18719, 14237, 12762, 14140, 12010, 10799, 11250, 12365, 18486, 17237, 16100, 22101, 14960, 19155, 25564]
	[TEST NON-OVERLAPPING DST (CIDR)] Average verify time: 21016.4 ns / 0.0210164 ms
	[TEST NON-OVERLAPPING DST (CIDR)] Total verify calls: 20 >>> [22520, 19726, 20081, 23728, 21367, 18041, 18597, 26236, 24416, 19395, 24718, 19754, 20737, 21733, 19135, 21758, 21541, 20649, 18080, 18116]
	[TEST OVERLAPPING (PODNS SELECTORS)] Average verify time: 20239.21052631579 ns / 0.02023921052631579 ms
	[TEST OVERLAPPING (PODNS SELECTORS)] Total verify calls: 19 >>> [23841, 23086, 23401, 20878, 19200, 26983, 21678, 15513, 19142, 19689, 14757, 18620, 21019, 20651, 21873, 21416, 18049, 15870, 18879]
	[TEST NON-OVERLAPPING SRC (PODNS)] Average verify time: 19307.3 ns / 0.0193073 ms
	[TEST NON-OVERLAPPING SRC (PODNS)] Total verify calls: 20 >>> [19919, 19317, 18794, 22677, 20200, 18929, 18519, 19091, 19249, 18829, 21899, 22721, 20996, 19484, 18816, 18053, 18780, 16551, 17348, 15974]
	[TEST NON-OVERLAPPING DST (PODNS)] Average verify time: 25715.1 ns / 0.025715099999999998 ms
	[TEST NON-OVERLAPPING DST (PODNS)] Total verify calls: 20 >>> [21184, 26207, 21668, 22013, 22640, 27586, 22268, 22723, 50076, 33586, 24850, 24154, 24501, 24437, 23942, 23060, 22652, 26417, 24689, 25649]
	[TEST NON-OVERLAPPING PORT] Average verify time: 22231.25 ns / 0.02223125 ms
	[TEST NON-OVERLAPPING PORT] Total verify calls: 20 >>> [24407, 24182, 22280, 21060, 24401, 23210, 21070, 23856, 22874, 21602, 22788, 23033, 23158, 22111, 21909, 19611, 20002, 18360, 26823, 17888]
	[TEST ACCEPT MONITORING] Average verify time: 2306.6 ns / 0.0023066 ms
	[TEST ACCEPT MONITORING] Total verify calls: 20 >>> [7446, 2027, 2185, 2383, 2167, 2039, 2204, 2084, 2158, 1961, 1939, 1893, 1917, 2099, 1897, 1955, 1858, 2109, 1854, 1957]
	[TEST NON-OVERLAPPING PROTOCOL] Average verify time: 21889.15 ns / 0.021889150000000003 ms
	[TEST NON-OVERLAPPING PROTOCOL] Total verify calls: 20 >>> [23088, 19149, 21456, 22457, 22368, 19945, 23971, 18760, 21228, 23079, 21538, 30593, 22836, 23916, 23637, 18958, 21946, 18880, 17448, 22530]
	[TEST OVERLAPPING (MIX SELECTORS)] Average verify time: 30408.85 ns / 0.030408849999999998 ms
	[TEST OVERLAPPING (MIX SELECTORS)] Total verify calls: 20 >>> [28513, 24943, 25550, 23931, 29530, 27042, 26716, 25314, 26583, 24872, 26693, 25565, 66239, 74576, 22395, 32248, 37791, 18514, 21833, 19329]
     */
    
    // ===================== TEST FOR INCOMPATIBLE "ACCEPT MONITORING" =====================

    @Test
    public void verify_false_accept_monitoring() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_monitoring_JNSM/consumer_accept_false.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_monitoring_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_accept_false.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST ACCEPT MONITORING] Average verify time: " + (averageTime) + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST ACCEPT MONITORING] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING PROTOCOL =====================

    @Test
    public void verify_true_protocol() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_protocol_JNSM/consumer_protocol_TCP.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_protocol_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_protocol_TCP.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING PROTOCOL] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING PROTOCOL] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING PORT =====================

    @Test
    public void verify_true_port() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_port_JNSM/consumer_port_80_85.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_port_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_port_80_85.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING PORT] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING PORT] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING SOURCE CIDR =====================
    
    @Test
    public void verify_true_src_CIDR() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_src_cidr_JNSM/consumer_src_cidr_8888_24.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_src_cidr_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_src_cidr_8888_24.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING SRC (CIDR)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING SRC (CIDR)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING SOURCE PODNS =====================
    
    @Test
    public void verify_true_src_podns() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_src_podns_JNSM/consumer_src_podns.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_src_podns_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_src_podns.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING SRC (PODNS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING SRC (PODNS)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING DESTINATION CIDR =====================
    @Test
    public void verify_true_dst_CIDR() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_dst_cidr_JNSM/consumer_dst_cidr_8888_30.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_dst_cidr_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_dst_cidr_8888_30.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING DST (CIDR)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING DST (CIDR)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING DESTINATION PODNS =====================
    @Test
    public void verify_true_dst_podns() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_dst_podns_JNSM/consumer_dst_podns.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_dst_podns_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_dst_podns.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
//                System.out.println("Checking file: "+providerFile.getName());
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING DST (PODNS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING DST (PODNS)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR NON-OVERLAPPING, DIFFERENT SELECTORS =====================
    
    @Test
    public void verify_true_different_selectors() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_different_selector_JNSM/consumer_podns.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_different_selector_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_podns.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING DIFFERENT SELECTORS] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING DST DIFFERENT SELECTORS] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR OVERLAPPING, CIDR SELECTORS =====================
    @Test
    public void verify_false_cidr() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_overlap_cidr_JNSM/consumer_cidr.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_overlap_cidr_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_cidr.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST OVERLAPPING (CIDR SELECTORS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST OVERLAPPING (CIDR SELECTORS)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR OVERLAPPING, PODNS SELECTORS =====================
    @Test
    public void verify_false_podns() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_overlap_podns_JNSM/consumer_podns.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_overlap_podns_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_podns.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST OVERLAPPING (PODNS SELECTORS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST OVERLAPPING (PODNS SELECTORS)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }

    // ===================== TEST FOR OVERLAPPING, DIFFERENT SELECTORS =====================
    @Test
    public void verify_false_mix() {
        RequestIntents fixedConsumerIntents;
        List<Long> verifyTimes = new ArrayList<>();
        
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/verify_overlap_mix_JNSM/consumer_mix.xml");
            fixedConsumerIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);
            
            java.io.File directory = new java.io.File("./testfile/verify_overlap_mix_JNSM");
            if (!directory.exists() || !directory.isDirectory()) {
                fail("Directory ./testfile/verify_JNSM does not exist");
                return;
            }
            
            java.io.File[] providerFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (providerFiles == null || providerFiles.length == 0) {
                fail("No XML files found in ./testfile/verify_JNSM directory");
                return;
            }
            
            for (java.io.File providerFile : providerFiles) {
                if (providerFile.getName().equals("consumer_mix.xml")) {
                    continue;
                }

                ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(providerFile.getAbsolutePath());
                AuthorizationIntents authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                //System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST OVERLAPPING (MIX SELECTORS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST OVERLAPPING (MIX SELECTORS)] Total verify calls: " + verifyTimes.size() + " >>> " + verifyTimes);
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }
}