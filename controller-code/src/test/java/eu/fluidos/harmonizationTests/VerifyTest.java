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
    public void testScenario1_compatible_authorizations() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_demo.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_demo.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertTrue("Scenario 1: Request intents should be approved", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 1: " + e.getMessage());
        }
    }
    
    @Test
    public void testScenario2_incompatible_mandatory() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_demo.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_demo.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertFalse("Scenario 2: Monitoring access not allowed", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 2: " + e.getMessage());
        }
    }
    
    @Test
    public void testScenario3_incompatible_authorization() {
        AuthorizationIntents authorizationIntents;
        RequestIntents requestIntents;
        try {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/consumer_MSPL_demo.xml");
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile("./testfile/provider_MSPL_demo.xml");
            
            authorizationIntents = HarmonizationUtils.extractAuthorizationIntents(providerIntents);
            requestIntents = HarmonizationUtils.extractRequestIntents(consumerIntents);

            // Initialize consumer and provider pod maps
            boolean result = harmonizationData.verify(requestIntents, authorizationIntents,
                                                    consumerPodMap, providerPodMap);
            
            assertFalse("Scenario 3: Incompatible Authorizaiton intents", result);
            
        } catch (Exception e) {
            fail("Failed to load XML files for Scenario 3: " + e.getMessage());
        }
    }
}
