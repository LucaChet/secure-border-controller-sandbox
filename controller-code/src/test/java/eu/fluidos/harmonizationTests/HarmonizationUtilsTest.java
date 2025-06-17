package eu.fluidos.harmonizationTests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import eu.fluidos.jaxb.*;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import eu.fluidos.Pod;
import eu.fluidos.harmonization.HarmonizationUtils;


@RunWith(MockitoJUnitRunner.class)
public class HarmonizationUtilsTest {
    
    private CIDRSelector cidrSelector1;
    private CIDRSelector cidrSelector2;
    private PodNamespaceSelector podSelector1;
    private PodNamespaceSelector podSelector2;
    
    @Before
    public void setUp() {
        // Initialize CIDR selectors
        cidrSelector1 = new CIDRSelector();
        cidrSelector2 = new CIDRSelector();
        
        // Initialize Pod selectors
        podSelector1 = createPodNamespaceSelector("app", "web", "environment", "prod", true);
        podSelector2 = createPodNamespaceSelector("app", "api", "environment", "test", false);
    }
    
    private PodNamespaceSelector createPodNamespaceSelector(String podKey, String podValue, 
                                                           String nsKey, String nsValue, boolean isHostCluster) {
        PodNamespaceSelector selector = new PodNamespaceSelector();
        selector.setIsHostCluster(isHostCluster);

        // Create pod key-value pair
        KeyValue podKV = new KeyValue();
        podKV.setKey(podKey);
        podKV.setValue(podValue);
        selector.getPod().add(podKV);
        
        // Create namespace key-value pair
        KeyValue nsKV = new KeyValue();
        nsKV.setKey(nsKey);
        nsKV.setValue(nsValue);
        selector.getNamespace().add(nsKV);
        
        return selector;
    }
    
    // ===================== PORT RANGE TESTS =====================
    
    @Test
    public void testVerifyPortRange_OverlappingRanges() {
        assertTrue("Port ranges 80-100 and 90-120 should overlap", 
                   HarmonizationUtils.verifyPortRange("80-100", "90-120"));
    }
    
    @Test
    public void testVerifyPortRange_NonOverlappingRanges() {
        assertFalse("Port ranges 80-90 and 100-120 should not overlap", 
                    HarmonizationUtils.verifyPortRange("80-90", "100-120"));
    }
    
    @Test
    public void testVerifyPortRange_TouchingRanges() {
        assertTrue("Port ranges 80-90 and 90-100 should overlap (touching)", 
                   HarmonizationUtils.verifyPortRange("80-90", "90-100"));
    }
    
    @Test
    public void testVerifyPortRange_SinglePorts() {
        assertTrue("Single port 80 should overlap with itself", 
                   HarmonizationUtils.verifyPortRange("80", "80"));
        assertFalse("Single ports 80 and 90 should not overlap", 
                    HarmonizationUtils.verifyPortRange("80", "90"));
    }
    
    @Test
    public void testVerifyPortRange_SinglePortInRange() {
        assertTrue("Single port 85 should overlap with range 80-90", 
                   HarmonizationUtils.verifyPortRange("85", "80-90"));
        assertTrue("Range 80-90 should overlap with single port 85", 
                   HarmonizationUtils.verifyPortRange("80-90", "85"));
    }
    
    @Test
    public void testVerifyPortRange_WildcardHandling() {
        assertTrue("Wildcard * should overlap with any range", 
                   HarmonizationUtils.verifyPortRange("*", "80-90"));
        assertTrue("Any range should overlap with wildcard *", 
                   HarmonizationUtils.verifyPortRange("80-90", "*"));
        assertTrue("Two wildcards should overlap", 
                   HarmonizationUtils.verifyPortRange("*", "*"));
    }
    
    @Test
    public void testVerifyPortRange_ContainedRanges() {
        assertTrue("Range 85-95 should overlap with containing range 80-100", 
                   HarmonizationUtils.verifyPortRange("85-95", "80-100"));
        assertTrue("Range 80-100 should overlap with contained range 85-95", 
                   HarmonizationUtils.verifyPortRange("80-100", "85-95"));
    }
    
    // ===================== PROTOCOL TYPE TESTS =====================
    
    @Test
    public void testVerifyProtocolType_IdenticalProtocols() {
        assertTrue("TCP should overlap with TCP", 
                   HarmonizationUtils.verifyProtocolType("TCP", "TCP"));
        assertTrue("UDP should overlap with UDP", 
                   HarmonizationUtils.verifyProtocolType("UDP", "UDP"));
        assertTrue("SCTP should overlap with SCTP", 
                   HarmonizationUtils.verifyProtocolType("SCTP", "SCTP"));
    }
    
    @Test
    public void testVerifyProtocolType_DifferentProtocols() {
        assertFalse("TCP should not overlap with UDP", 
                    HarmonizationUtils.verifyProtocolType("TCP", "UDP"));
        assertFalse("UDP should not overlap with SCTP", 
                    HarmonizationUtils.verifyProtocolType("UDP", "SCTP"));
    }
    
    @Test
    public void testVerifyProtocolType_ALLProtocol() {
        assertTrue("ALL should overlap with TCP", 
                   HarmonizationUtils.verifyProtocolType("ALL", "TCP"));
        assertTrue("TCP should overlap with ALL", 
                   HarmonizationUtils.verifyProtocolType("TCP", "ALL"));
        assertTrue("ALL should overlap with ALL", 
                   HarmonizationUtils.verifyProtocolType("ALL", "ALL"));
    }
    
    // ===================== HARMONIZED PORT RANGE TESTS =====================
    
    @Test
    public void testComputeHarmonizedPortRange_SinglePortMatch() {
        assertEquals("Same single ports should result in empty string", 
                     "", HarmonizationUtils.computeHarmonizedPortRange("80", "80"));
    }
    
    @Test
    public void testComputeHarmonizedPortRange_SinglePortDifferent() {
        assertEquals("Different single ports should return original", 
                     "80", HarmonizationUtils.computeHarmonizedPortRange("80", "90"));
    }
    
    @Test
    public void testComputeHarmonizedPortRange_SinglePortInRange() {
        assertEquals("Single port contained in range should return empty", 
                     "", HarmonizationUtils.computeHarmonizedPortRange("85", "80-90"));
    }
    
    @Test
    public void testComputeHarmonizedPortRange_SinglePortOutsideRange() {
        assertEquals("Single port outside range should return original", 
                     "75", HarmonizationUtils.computeHarmonizedPortRange("75", "80-90"));
    }
    
    @Test
    public void testComputeHarmonizedPortRange_WildcardHandling() {
        assertEquals("Wildcard should be converted to full range", 
                     "", HarmonizationUtils.computeHarmonizedPortRange("*", "*"));
    }
    
    @Test
    public void testComputeHarmonizedPortRange_ComplexScenarios() {
        // Test range minus single port - should return modified range
        String result = HarmonizationUtils.computeHarmonizedPortRange("80-90", "85");
        assertNotNull("Range minus single port should not be null", result);
        
        // Test non-overlapping ranges - should return original
        assertEquals("Non-overlapping ranges should return original", 
                     "70-80", HarmonizationUtils.computeHarmonizedPortRange("70-80", "90-100"));
    }
    
    // ===================== HARMONIZED PROTOCOL TYPE TESTS =====================
    
    @Test
    public void testComputeHarmonizedProtocolType_IdenticalProtocols() {
        String[] result = HarmonizationUtils.computeHarmonizedProtocolType("TCP", "TCP");
        assertEquals("Identical protocols should result in empty array", 0, result.length);
    }
    
    @Test
    public void testComputeHarmonizedProtocolType_ALLMinus() {
        String[] result = HarmonizationUtils.computeHarmonizedProtocolType("TCP", "ALL");
        assertEquals("Any protocol minus ALL should result in empty array", 0, result.length);
    }
    
    @Test
    public void testComputeHarmonizedProtocolType_DifferentProtocols() {
        String[] result = HarmonizationUtils.computeHarmonizedProtocolType("TCP", "UDP");
        assertEquals("Different protocols should return original", 1, result.length);
        assertEquals("Should return first protocol", "TCP", result[0]);
    }
    
    @Test
    public void testComputeHarmonizedProtocolType_ALLMinusSpecific() {
        String[] result = HarmonizationUtils.computeHarmonizedProtocolType("ALL", "TCP");
		System.out.println("Result: " + result.length);
		for (String protocol : result) {
			System.out.println("Protocol: " + protocol);
		}
		assertEquals("ALL minus specific should return two protocols", 2, result.length);
		// Should contain UDP and SCTP (order may vary)
		List<String> protocols = Arrays.asList(result);
		assertTrue("Should contain UDP", protocols.contains("UDP"));
		assertTrue("Should contain SCTP", protocols.contains("SCTP"));
    }
    
    // ===================== KUBERNETES CONDITION TO STRING TESTS =====================
    
    @Test
    public void testKubernetesNetworkFilteringConditionToString_PodSelectors() {
        KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();
        
        PodNamespaceSelector source = createPodNamespaceSelector("app", "web", "env", "prod", true);
        PodNamespaceSelector destination = createPodNamespaceSelector("app", "api", "env", "test", false);
        
        condition.setSource(source);
        condition.setDestination(destination);
        condition.setDestinationPort("80-90");
		ProtocolType protocolType = ProtocolType.TCP;
        condition.setProtocolType(protocolType);
        
        String result = HarmonizationUtils.kubernetesNetworkFilteringConditionToString(condition);
        
        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain port information", result.contains("DstPort: [80-90]"));
        assertTrue("Result should contain protocol information", result.contains("ProtocolType: [TCP]"));
    }
    
    @Test
    public void testKubernetesNetworkFilteringConditionToString_CIDRSelectors() {
        KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();
        
        CIDRSelector source = new CIDRSelector();
        source.setAddressRange("192.168.1.0/24");
        CIDRSelector destination = new CIDRSelector();
        destination.setAddressRange("10.0.0.0/8");
        
        condition.setSource(source);
        condition.setDestination(destination);
        condition.setDestinationPort("443");
		ProtocolType protocolType = ProtocolType.TCP;
        condition.setProtocolType(protocolType);
        
        String result = HarmonizationUtils.kubernetesNetworkFilteringConditionToString(condition);
        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain port information", result.contains("DstPort: [443]"));
        assertTrue("Result should contain protocol information", result.contains("ProtocolType: [TCP]"));
    }
    
    @Test
    public void testKubernetesNetworkFilteringConditionToString_MixedSelectors() {
        KubernetesNetworkFilteringCondition condition = new KubernetesNetworkFilteringCondition();
        
        PodNamespaceSelector source = createPodNamespaceSelector("app", "web", "env", "prod", true);
        CIDRSelector destination = new CIDRSelector();
        destination.setAddressRange("0.0.0.0/0");
        
        condition.setSource(source);
        condition.setDestination(destination);
        condition.setDestinationPort("*");
		ProtocolType protocolType = ProtocolType.ALL;
        condition.setProtocolType(protocolType);
        
        String result = HarmonizationUtils.kubernetesNetworkFilteringConditionToString(condition);
        
        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain wildcard port", result.contains("DstPort: [*]"));
        assertTrue("Result should contain ALL protocol", result.contains("ProtocolType: [ALL]"));
    }
    
    // ===================== HARMONIZED RESOURCE SELECTOR TESTS =====================
    
    @Test
    public void testComputeHarmonizedResourceSelector_WithMaps() {
        // Create test maps
        HashMap<String, HashMap<String, List<Pod>>> map1 = new HashMap<>();
        HashMap<String, HashMap<String, List<Pod>>> map2 = new HashMap<>();
        
        // Create test selectors
        PodNamespaceSelector selector1 = createPodNamespaceSelector("app", "web", "env", "prod", true);
        PodNamespaceSelector selector2 = createPodNamespaceSelector("app", "api", "env", "test", false);
        
        List<ResourceSelector> result = HarmonizationUtils.computeHarmonizedResourceSelector(
            selector1, selector2, map1, map2);
        
        assertNotNull("Result should not be null", result);
        // Note: The actual implementation logic would determine the expected behavior
    }
    
    @Test
    public void testComputeHarmonizedResourceSelector_CIDRSelectors() {
        HashMap<String, HashMap<String, List<Pod>>> map1 = new HashMap<>();
        HashMap<String, HashMap<String, List<Pod>>> map2 = new HashMap<>();
        
        CIDRSelector selector1 = new CIDRSelector();
        selector1.setAddressRange("192.168.1.0/24");
        CIDRSelector selector2 = new CIDRSelector();
        selector2.setAddressRange("192.168.2.0/24");
        
        List<ResourceSelector> result = HarmonizationUtils.computeHarmonizedResourceSelector(
            selector1, selector2, map1, map2);
        
        assertNotNull("Result should not be null", result);
    }
    
    @Test
    public void testComputeHarmonizedResourceSelector_NullMaps() {
        PodNamespaceSelector selector1 = createPodNamespaceSelector("app", "web", "env", "prod", true);
        PodNamespaceSelector selector2 = createPodNamespaceSelector("app", "api", "env", "test", false);
        
        try {
            List<ResourceSelector> result = HarmonizationUtils.computeHarmonizedResourceSelector(
                selector1, selector2, null, null);
            // Behavior depends on implementation - may return empty list or throw exception
            assertNotNull("Result should handle null maps gracefully", result);
        } catch (Exception e) {
            // Expected behavior for null input
        }
    }

    // ===================== EXISTING RESOURCE SELECTOR TESTS =====================
    
    // CIDR Selector Tests
    @Test
    public void testVerifyResourceSelector_CIDROverlapping() {
        cidrSelector1.setAddressRange("192.168.1.0/24");
        cidrSelector2.setAddressRange("192.168.1.128/25");
        
        assertTrue("CIDR ranges should overlap", 
                   HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
    
    @Test
    public void testVerifyResourceSelector_CIDRNonOverlapping() {
        cidrSelector1.setAddressRange("192.168.1.0/24");
        cidrSelector2.setAddressRange("192.168.2.0/24");
        
        assertFalse("CIDR ranges should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
    
    @Test
    public void testVerifyResourceSelector_CIDRIdentical() {
        cidrSelector1.setAddressRange("192.168.1.0/24");
        cidrSelector2.setAddressRange("192.168.1.0/24");
        
        assertTrue("Identical CIDR ranges should overlap", 
                   HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
    
    @Test
    public void testVerifyResourceSelector_CIDRFullRange() {
        cidrSelector1.setAddressRange("0.0.0.0/0");
        cidrSelector2.setAddressRange("10.0.0.0/8");
        
        assertTrue("Full range CIDR should overlap with any range", 
                   HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
    
    // PodNamespaceSelector Tests - Different Clusters
    @Test
    public void testVerifyResourceSelector_PodSelectorsDifferentClusters() {
        PodNamespaceSelector hostSelector = createPodNamespaceSelector("app", "web", "env", "prod", true);
        PodNamespaceSelector localSelector = createPodNamespaceSelector("app", "web", "env", "prod", false);
        
        assertTrue("Selectors with opposite isHostCluster should overlap", 
                    HarmonizationUtils.verifyResourceSelector(hostSelector, localSelector));
    }
    
    @Test
    public void testVerifyResourceSelector_PodSelectorsSameCluster() {
        PodNamespaceSelector selector1 = createPodNamespaceSelector("app", "web", "env", "prod", true);
        PodNamespaceSelector selector2 = createPodNamespaceSelector("app", "web", "env", "prod", true);
        
        assertFalse("Selectors with same isHostCluster should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(selector1, selector2));
    }
    
    @Test
    public void testVerifyResourceSelector_PodSelectorsWithWildcards() {
        PodNamespaceSelector wildcardSelector = createPodNamespaceSelector("*", "*", "*", "*", true);
        PodNamespaceSelector specificSelector = createPodNamespaceSelector("app", "web", "env", "prod", false);
        
        assertTrue("Wildcard selector should match with specific selector", 
                   HarmonizationUtils.verifyResourceSelector(wildcardSelector, specificSelector));
    }
    
    @Test
    public void testVerifyResourceSelector_PodSelectorsPartialWildcard() {
        PodNamespaceSelector partialWildcard = createPodNamespaceSelector("*", "web", "env", "prod", true);
        PodNamespaceSelector specific = createPodNamespaceSelector("app", "web", "env", "prod", false);
        
        assertTrue("Partial wildcard should match when non-wildcard parts match", 
                   HarmonizationUtils.verifyResourceSelector(partialWildcard, specific));
    }
    
    @Test
    public void testVerifyResourceSelector_PodSelectorsNoMatch() {
        PodNamespaceSelector selector1 = createPodNamespaceSelector("app", "web", "env", "prod", true);
        PodNamespaceSelector selector2 = createPodNamespaceSelector("service", "api", "tier", "backend", false);
        
        assertFalse("Non-matching selectors should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(selector1, selector2));
    }
    
    // Mixed Type Tests
    @Test
    public void testVerifyResourceSelector_MixedTypes_CIDRAndPod() {
        cidrSelector1.setAddressRange("192.168.1.0/24");
        
        assertFalse("CIDR and Pod selectors should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(cidrSelector1, podSelector1));
    }
    
    @Test
    public void testVerifyResourceSelector_MixedTypes_PodAndCIDR() {
        cidrSelector1.setAddressRange("10.0.0.0/8");
        
        assertFalse("Pod and CIDR selectors should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(podSelector1, cidrSelector1));
    }
    
    // Edge Cases
    @Test
    public void testVerifyResourceSelector_CIDREdgeCases() {
        // Test /32 subnet (single IP)
        cidrSelector1.setAddressRange("192.168.1.1/32");
        cidrSelector2.setAddressRange("192.168.1.1/32");
        
        assertTrue("Single IP CIDR should overlap with itself", 
                   HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
    
    @Test
    public void testVerifyResourceSelector_CIDRSingleIPNoOverlap() {
        cidrSelector1.setAddressRange("192.168.1.1/32");
        cidrSelector2.setAddressRange("192.168.1.2/32");
        
        assertFalse("Different single IP CIDRs should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
    
    @Test
    public void testVerifyResourceSelector_CIDRBoundaryOverlap() {
        cidrSelector1.setAddressRange("192.168.1.0/25");    // 192.168.1.0 - 192.168.1.127
        cidrSelector2.setAddressRange("192.168.1.128/25");  // 192.168.1.128 - 192.168.1.255
        
        assertFalse("Adjacent CIDR ranges should not overlap", 
                    HarmonizationUtils.verifyResourceSelector(cidrSelector1, cidrSelector2));
    }
}