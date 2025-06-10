package eu.fluidos.harmonizationTests;

import eu.fluidos.Cluster;
import eu.fluidos.Namespace;
import eu.fluidos.Pod;
import eu.fluidos.harmonization.*;
import eu.fluidos.jaxb.*;
import org.junit.Before;
import org.junit.Test;
import eu.fluidos.harmonization.HarmonizationUtils;
import java.util.*;

import static org.junit.Assert.*;

public class HarmonizationServiceTest {

    private HarmonizationService harmonizationService;
    private Cluster dummyCluster;

    @Before
    public void setUp() {
        harmonizationService = new HarmonizationService();
        dummyCluster = TestDataUtils.createDummyCluster(); // Mock or build a minimal cluster
    }

    @Test
    public void testCompatibleIntents() {
        // Consumer and provider allow the same connection
        try{
            RequestIntents consumer = TestDataUtils.loadRequestIntentsFromXml("consumer_MSPL_test_scenario1.xml");
            AuthorizationIntents provider = TestDataUtils.loadAuthorizationIntentsFromXml("provider_MSPL_test_scenario3.xml");
            RequestIntents harmonized = harmonizationService.harmonize(dummyCluster, consumer, provider);
            assertNotNull(harmonized);
            assertTrue(harmonized.getConfigurationRule().size() > 0);
            assertTrue(harmonizationService.verify(dummyCluster, provider));
        } catch (Exception e) {
            fail("Failed to load XML files for testCompatibleIntents: " + e.getMessage());
        }
    }
/*
    //@Test
    public void testForbiddenConnection() {
        // Consumer requests something forbidden by provider
        RequestIntents consumer = TestDataUtils.buildRequestIntents("TCP", "22", "A", "B");
        AuthorizationIntents provider = TestDataUtils.buildAuthorizationIntentsWithForbidden("TCP", "22", "A", "B");
        RequestIntents harmonized = harmonizationService.harmonize(dummyCluster, consumer, provider);
        assertNull(harmonized); // Or check harmonized does not contain forbidden rule
    }

    //@Test
    public void testMandatoryConnectionNotRequested() {
        // Provider requires a mandatory connection not present in consumer
        RequestIntents consumer = TestDataUtils.buildRequestIntents("TCP", "80", "A", "B");
        AuthorizationIntents provider = TestDataUtils.buildAuthorizationIntentsWithMandatory("TCP", "443", "A", "B");
        RequestIntents harmonized = harmonizationService.harmonize(dummyCluster, consumer, provider);
        // Should add the mandatory rule or return null if not accepted
        assertNotNull(harmonized);
        // Check that mandatory rule is present if consumer accepts monitoring
    }

    //@Test
    public void testPartialOverlap() {
        // Overlapping port ranges or protocols
        RequestIntents consumer = TestDataUtils.buildRequestIntents("TCP", "80-100", "A", "B");
        AuthorizationIntents provider = TestDataUtils.buildAuthorizationIntentsWithForbidden("TCP", "90-110", "A", "B");
        RequestIntents harmonized = harmonizationService.harmonize(dummyCluster, consumer, provider);
        // Should only allow 80-89
        assertNotNull(harmonized);
        // Check that harmonized rules do not include forbidden ports
    }

    //@Test
    public void testWildcardProtocolAndPort() {
        // Wildcard protocol and port
        RequestIntents consumer = TestDataUtils.buildRequestIntents("ALL", "*", "A", "B");
        AuthorizationIntents provider = TestDataUtils.buildAuthorizationIntentsAllowAll();
        RequestIntents harmonized = harmonizationService.harmonize(dummyCluster, consumer, provider);
        assertNotNull(harmonized);
        // Should allow all
    }

    //@Test
    public void testEmptyIntents() {
        // Empty consumer/provider
        RequestIntents consumer = new RequestIntents();
        AuthorizationIntents provider = new AuthorizationIntents();
        RequestIntents harmonized = harmonizationService.harmonize(dummyCluster, consumer, provider);
        assertNotNull(harmonized);
        assertTrue(harmonized.getConfigurationRule().isEmpty());
    }
         */

    static class TestDataUtils {
        static RequestIntents buildRequestIntents(String protocol, String port, String src, String dst) {
            // Build and return a RequestIntents object with one rule
            // Fill in with your actual builder or constructor logic
            return new RequestIntents();
        }
        static AuthorizationIntents buildAuthorizationIntentsAllowAll() {
            // Build and return an AuthorizationIntents object that allows everything
            return new AuthorizationIntents();
        }
        static AuthorizationIntents buildAuthorizationIntentsWithForbidden(String protocol, String port, String src, String dst) {
            // Build and return an AuthorizationIntents object with a forbidden rule
            return new AuthorizationIntents();
        }
        static AuthorizationIntents buildAuthorizationIntentsWithMandatory(String protocol, String port, String src, String dst) {
            // Build and return an AuthorizationIntents object with a mandatory rule
            return new AuthorizationIntents();
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
        

        public static RequestIntents loadRequestIntentsFromXml(String xmlPath) throws Exception {
            ITResourceOrchestrationType consumerIntents = HarmonizationUtils.extractIntentsFromXMLFile(xmlPath);
            return HarmonizationUtils.extractRequestIntents(consumerIntents);
        }

        public static AuthorizationIntents loadAuthorizationIntentsFromXml(String xmlPath) throws Exception {
            ITResourceOrchestrationType providerIntents = HarmonizationUtils.extractIntentsFromXMLFile(xmlPath);
            return HarmonizationUtils.extractAuthorizationIntents(providerIntents);
        }
    }
}