package eu.fluidos;
/*import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.mockito.Mockito;

import eu.fluidos.cluster.ClusterService;
import eu.fluidos.harmonization.HarmonizationData;
import eu.fluidos.harmonization.HarmonizationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class VerifyTest {
    private HarmonizationService harmonizationService;
    private ClusterService mockClusterService;
    private HarmonizationData mockHarmonizationData;

    @BeforeEach
    public void setUp(){
        harmonizationService = new HarmonizationService();

        mockClusterService = mock(ClusterService.class);
        mockHarmonizationData = mock(HarmonizationData.class);
        
    }

    @Test
    public void testTranslate() {

    }
}
*/

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import eu.fluidos.cluster.ClusterService;
import eu.fluidos.harmonization.HarmonizationData;
import eu.fluidos.harmonization.HarmonizationService;
import eu.fluidos.jaxb.AuthorizationIntents;
import eu.fluidos.jaxb.ConfigurationAction;
import eu.fluidos.jaxb.ConfigurationRule;
import eu.fluidos.jaxb.KubernetesNetworkFilteringAction;
import eu.fluidos.jaxb.KubernetesNetworkFilteringCondition;
import eu.fluidos.jaxb.PodNamespaceSelector;
import eu.fluidos.jaxb.Priority;
import eu.fluidos.jaxb.ProtocolType;
import eu.fluidos.jaxb.RequestIntents;
import eu.fluidos.jaxb.ResourceSelector;

public class VerifyTest {

    @Test
    public void testVerifyWithCompatibleCandidate() {
    AuthorizationIntents authIntents = new AuthorizationIntents();

    // Mandatory: monitoring -> *:43 TCP
    KubernetesNetworkFilteringCondition cond1 = new KubernetesNetworkFilteringCondition();
    KubernetesNetworkFilteringAction action1 = new KubernetesNetworkFilteringAction();
    ResourceSelector rSelector1 = new PodNamespaceSelector();
    ConfigurationRule rule1  = new ConfigurationRule();
   
    rSelector1.setIsHostCluster(false);
    //rSelector1.
    //action1.setKubernetesNetworkFilteringActionType("ALLOW");
    //cond1.setIsCNF(false);
    //cond1.setProtocolType(ProtocolType.TCP);
    //cond1.setSource(null);
    //rule1.setConfigurationCondition(cond1);
    //rule1.setConfigurationRuleAction(action1);
    

    }



    @Test
    public void dummyTest() {
        assertTrue(true);
    }
}
