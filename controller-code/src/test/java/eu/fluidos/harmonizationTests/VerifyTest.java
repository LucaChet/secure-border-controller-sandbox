package eu.fluidos.harmonizationTests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import eu.fluidos.jaxb.*;

import java.util.HashMap;
import java.util.List;

import eu.fluidos.Pod;
import eu.fluidos.harmonization.HarmonizationData;
import eu.fluidos.harmonization.HarmonizationUtils;

@RunWith(MockitoJUnitRunner.class)
public class VerifyTest {
    
    private HarmonizationData harmonizationData = new HarmonizationData();
    private HashMap<String, HashMap<String, List<Pod>>> consumerPodMap = new HashMap<>();
    private HashMap<String, HashMap<String, List<Pod>>> providerPodMap = new HashMap<>();
    
    
    // ===================== XML FILE-BASED TEST SCENARIOS =====================
    
    @Test
    public void testScenario1_incompatible_authorizations1() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_test_scenario1.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_test_scenario1.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertFalse("Flavor 1: Request intents should be incompatible with first flavor", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 1: " + e.getMessage());
        }
    }
    
    @Test
    public void testScenario2_incompatible_authorizations2() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_test_scenario1.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_test_scenario2.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertFalse("Flavor 2: Request intents should be incompatible with second flavor", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 2: " + e.getMessage());
        }
    }
    
    @Test
    public void testScenario3_compatible_authorization() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_test_scenario1.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_test_scenario3.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertTrue("Flavor 3: This is the compatible flavor and should be selected in verification", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 3: " + e.getMessage());
        }
    }

     @Test
    public void testScenario4_incompatible_authorization() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_test_scenario1.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_test_scenario4.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertTrue("Flavor 3: This is the compatible flavor and should be selected in verification", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 3: " + e.getMessage());
        }
    }
}
