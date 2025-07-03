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
                System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST ACCEPT MONITORING] Average verify time: " + (averageTime) + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST ACCEPT MONITORING] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING PROTOCOL] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING PROTOCOL] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING PORT] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING PORT] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING SRC (CIDR)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING SRC (CIDR)] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING SRC (PODNS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING SRC (PODNS)] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING DST (CIDR)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING DST (CIDR)] Total verify calls: " + verifyTimes.size());
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
                
                long startTime = System.nanoTime();
                boolean result = harmonizationService.verify(fixedConsumerIntents, authorizationIntents);
                long endTime = System.nanoTime();
                
                long duration = endTime - startTime;
                verifyTimes.add(duration);
                verifyTimesTotal.add(duration);
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING DST (PODNS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING DST (PODNS)] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");
                
                assertTrue("File " + providerFile.getName() + " compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST NON-OVERLAPPING DIFFERENT SELECTORS] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST NON-OVERLAPPING DST DIFFERENT SELECTORS] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST OVERLAPPING (CIDR SELECTORS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST OVERLAPPING (CIDR SELECTORS)] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST OVERLAPPING (PODNS SELECTORS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST OVERLAPPING (PODNS SELECTORS)] Total verify calls: " + verifyTimes.size());
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
                System.out.println("Verify call took: " + (duration) + " ns");

                assertFalse("File " + providerFile.getName() + " NOT compatible with consumer intents", result);
            }
            
            // Calculate and print average
            if (!verifyTimes.isEmpty()) {
                double averageTime = verifyTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
                System.out.println("[TEST OVERLAPPING (MIX SELECTORS)] Average verify time: " + averageTime + " ns / " + (averageTime / 1_000_000.0) + " ms");
                System.out.println("[TEST OVERLAPPING (MIX SELECTORS)] Total verify calls: " + verifyTimes.size());
            }
            
        } catch (Exception e) {
            fail("Failed to process JNSM verification files: " + e.getMessage());
        }
    }
}