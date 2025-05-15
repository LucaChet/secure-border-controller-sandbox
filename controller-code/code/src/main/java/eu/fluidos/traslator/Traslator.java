package eu.fluidos.traslator;

import eu.fluidos.LabelsKeyValue;
import eu.fluidos.jaxb.*;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Yaml;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class Traslator {
    private List<RequestIntents> reqIntentsListHarmonized;
    private List<V1NetworkPolicy> networkPolicies;
    private Map<String,String> localNamespaces;
    private Map<String,String> remoteNamespaces;
    private Map <LabelsKeyValue,String> availablePodsMap;
    private int index;
    private boolean isLocal;

    public Traslator(List<RequestIntents> reqIntentsListHarmonized, Map<String,String> localNamespaces,Map<String,String> remoteNamespaces,Map <LabelsKeyValue,String> availablePodsMap,boolean isLocal ) {
        this.index=0;
        this.reqIntentsListHarmonized = reqIntentsListHarmonized;
        this.networkPolicies = new ArrayList<>();
        this.localNamespaces=localNamespaces;
        this.remoteNamespaces=remoteNamespaces;
        this.availablePodsMap = availablePodsMap;
        this.isLocal=isLocal;
        /*this.authIntents = intents.getITResource().stream()
            .filter(it -> it.getConfiguration().getClass().equals(AuthorizationIntents.class))
            .map(it -> (AuthorizationIntents) it.getConfiguration()).findFirst().orElse(null);
        this.intraVCluster = intents.getITResource().stream()
            .filter(it -> it.getConfiguration().getClass().equals(PrivateIntents.class))
            .map(it -> (PrivateIntents) it.getConfiguration()).findFirst().orElse(null);
        this.interVCluster = intents.getITResource().stream()
            .filter(it -> it.getConfiguration().getClass().equals(RequestIntents.class))
            .map(it -> (RequestIntents) it.getConfiguration()).findFirst().orElse(null);
            */
            
            if (this.reqIntentsListHarmonized != null){
                //System.out.println("reIntent is not null");
                for (RequestIntents reqIntent : reqIntentsListHarmonized){
                    for(ConfigurationRule cr: reqIntent.getConfigurationRule()) {
                        KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
                        Ruleinfo rule = retrieveInfo(cond);
                        
                        //Caso in cui non ho ne un indirizzo IP come source, ne come destinazione
                        if (rule.getCidrDestination().getAddressRange() == null && rule.getCidrSource().getAddressRange() == null){
                            //V1NetworkPolicy createdEgressNetworkPolicy = createEgressAllowNetworkPolicy(cr.getName(),rule);
                            //V1NetworkPolicy createdIngressNetworkPolicy = createIngressAllowNetworkPolicy(cr.getName(),rule);
                            List<V1NetworkPolicy> createdListIngressNetworkPolicy = createHeaderIngressAllowPolicyForNamespace(cr.getName(),rule);
                            List<V1NetworkPolicy> createdListEgressNetworkPolicy = createEgressAllowPolicyForNamespace(cr.getName(),rule);
                            for (V1NetworkPolicy createdNetworkPolicy : createdListEgressNetworkPolicy){
                                networkPolicies.add(createdNetworkPolicy);
                            }
                            
                            for (V1NetworkPolicy createdIngressNetworkPolicy : createdListIngressNetworkPolicy){
                                networkPolicies.add(createdIngressNetworkPolicy);
                            }
                        } else if (rule.getCidrDestination().getAddressRange() == null && rule.getCidrSource().getAddressRange() != null){
                            // V1NetworkPolicy createdIngressNetworkPolicy = createIngressAllowNetworkPolicy(cr.getName(),rule);
                            // networkPolicies.add(createdIngressNetworkPolicy);
                            List<V1NetworkPolicy> createdListIngressNetworkPolicy = createHeaderIngressAllowPolicyForNamespace(cr.getName(),rule);
                            for (V1NetworkPolicy createdIngressNetworkPolicy : createdListIngressNetworkPolicy){
                                networkPolicies.add(createdIngressNetworkPolicy);
                            }     
                        } else if (rule.getCidrDestination().getAddressRange() != null && rule.getCidrSource().getAddressRange() == null){
                            // V1NetworkPolicy createdEgressNetworkPolicy = createEgressAllowNetworkPolicy(cr.getName(),rule);
                            // networkPolicies.add(createdEgressNetworkPolicy);
                            List<V1NetworkPolicy> createdListEgressNetworkPolicy = createEgressAllowPolicyForNamespace(cr.getName(),rule);
                            for (V1NetworkPolicy createdNetworkPolicy : createdListEgressNetworkPolicy){
                                networkPolicies.add(createdNetworkPolicy);
                            }     
                        }
                        
                        
                    }
                }               
            }
            /*
            if (this.interVCluster != null){
                for(ConfigurationRule cr: this.interVCluster.getConfigurationRule()) {
                    KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
                    Ruleinfo rule = retrieveInfo(cond);
                    
                    //Caso in cui non ho ne un indirizzo IP come source, ne come destinazione
                    if (rule.getCidrDestination().getAddressRange() == null && rule.getCidrSource().getAddressRange() == null){
                        //V1NetworkPolicy createdEgressNetworkPolicy = createEgressAllowNetworkPolicy(cr.getName(),rule);
                        //V1NetworkPolicy createdIngressNetworkPolicy = createIngressAllowNetworkPolicy(cr.getName(),rule);
                        List<V1NetworkPolicy> createdListIngressNetworkPolicy = createHeaderIngressAllowPolicyForNamespace(cr.getName(),rule);
                        List<V1NetworkPolicy> createdListEgressNetworkPolicy = createEgressAllowPolicyForNamespace(cr.getName(),rule);
                        for (V1NetworkPolicy createdNetworkPolicy : createdListEgressNetworkPolicy){
                            networkPolicies.add(createdNetworkPolicy);
                        }
                        for (V1NetworkPolicy createdIngressNetworkPolicy : createdListIngressNetworkPolicy){
                            networkPolicies.add(createdIngressNetworkPolicy);
                        }
                    } else if (rule.getCidrDestination().getAddressRange() == null && rule.getCidrSource().getAddressRange() != null){
                        // V1NetworkPolicy createdIngressNetworkPolicy = createIngressAllowNetworkPolicy(cr.getName(),rule);
                        // networkPolicies.add(createdIngressNetworkPolicy);
                        List<V1NetworkPolicy> createdListIngressNetworkPolicy = createHeaderIngressAllowPolicyForNamespace(cr.getName(),rule);
                        for (V1NetworkPolicy createdIngressNetworkPolicy : createdListIngressNetworkPolicy){
                            networkPolicies.add(createdIngressNetworkPolicy);
                        }                        
                    } else if (rule.getCidrDestination().getAddressRange() != null && rule.getCidrSource().getAddressRange() == null){
                        // V1NetworkPolicy createdEgressNetworkPolicy = createEgressAllowNetworkPolicy(cr.getName(),rule);
                        // networkPolicies.add(createdEgressNetworkPolicy);
                        List<V1NetworkPolicy> createdListEgressNetworkPolicy = createEgressAllowPolicyForNamespace(cr.getName(),rule);
                        for (V1NetworkPolicy createdNetworkPolicy : createdListEgressNetworkPolicy){
                            networkPolicies.add(createdNetworkPolicy);
                        }                        
                    }
                    
                    
                }
            }
            
            if (this.authIntents != null) {
                for(ConfigurationRule cr: this.authIntents.getMandatoryConnectionList()) {
                    KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr.getConfigurationCondition();
                    Ruleinfo rule = retrieveInfo(cond);

                    //Caso in cui non ho ne un indirizzo IP come source, ne come destinazione
                    if (rule.getCidrDestination().getAddressRange() == null && rule.getCidrSource().getAddressRange() == null){
                        //V1NetworkPolicy createdEgressNetworkPolicy = createEgressAllowNetworkPolicy(cr.getName(),rule);
                        List<V1NetworkPolicy> createdListEgressNetworkPolicy = createEgressAllowPolicyForNamespace(cr.getName(),rule);
                        List<V1NetworkPolicy> createdListIngressNetworkPolicy = createHeaderIngressAllowPolicyForNamespace(cr.getName(),rule);
                        for (V1NetworkPolicy createdNetworkPolicy : createdListEgressNetworkPolicy){
                            networkPolicies.add(createdNetworkPolicy);
                        }
                        //V1NetworkPolicy createdIngressNetworkPolicy = createIngressAllowNetworkPolicy(cr.getName(),rule);
                        //networkPolicies.add(createdEgressNetworkPolicy);
                        //networkPolicies.add(createdIngressNetworkPolicy); 
                        for (V1NetworkPolicy createdIngressNetworkPolicy : createdListIngressNetworkPolicy){
                            networkPolicies.add(createdIngressNetworkPolicy);
                        }                        
                    } else if (rule.getCidrDestination().getAddressRange() == null && rule.getCidrSource().getAddressRange() != null){
                        //V1NetworkPolicy createdIngressNetworkPolicy = createIngressAllowNetworkPolicy(cr.getName(),rule);
                        //networkPolicies.add(createdIngressNetworkPolicy);
                        List<V1NetworkPolicy> createdListIngressNetworkPolicy = createHeaderIngressAllowPolicyForNamespace(cr.getName(),rule);
                        for (V1NetworkPolicy createdIngressNetworkPolicy : createdListIngressNetworkPolicy){
                            networkPolicies.add(createdIngressNetworkPolicy);
                        }
                    } else if (rule.getCidrDestination().getAddressRange() != null && rule.getCidrSource().getAddressRange() == null){
                        List<V1NetworkPolicy> createdListEgressNetworkPolicy = createEgressAllowPolicyForNamespace(cr.getName(),rule);
                        for (V1NetworkPolicy createdNetworkPolicy : createdListEgressNetworkPolicy){
                            networkPolicies.add(createdNetworkPolicy);
                        }
                    }
                }
            }
            */
            writeNetworkPoliciesToFile1(networkPolicies);
    }

    private Ruleinfo retrieveInfo (KubernetesNetworkFilteringCondition cond){
        List<KeyValue> sourcePodList = new ArrayList<>();
        String sourceNamespace = new String();
        CIDRSelector cidrSource = new CIDRSelector();
        List<KeyValue> destinationPodList = new ArrayList<>();
        List<KeyValue> destinationNamespaceList = new ArrayList<>();
        List<KeyValue> sourceNamespaceList = new ArrayList<>();
        CIDRSelector cidrDestination = new CIDRSelector();
		if(cond.getSource().getClass().equals(PodNamespaceSelector.class)){
			PodNamespaceSelector pns = (PodNamespaceSelector) cond.getSource();
            //sourcePod = pns.getPod().get(0).getValue(); //Gestire meglio nel caso in cui ci sono piu pod e namespace
            sourcePodList = pns.getPod();
            sourceNamespaceList=addRemoteTagNamespace (pns.getNamespace(),cond.getSource().isIsHostCluster());
            //sourceNamespace = pns.getNamespace().get(0).getValue();
		} else {
			cidrSource = (CIDRSelector) cond.getSource();
		}
		
		if(cond.getDestination().getClass().equals(PodNamespaceSelector.class)){
			PodNamespaceSelector pns = (PodNamespaceSelector) cond.getDestination();
            //destinationPod = pns.getPod().get(0).getValue();
            //destinationNamespace = pns.getNamespace().get(0).getValue();
            destinationPodList = pns.getPod();
            destinationNamespaceList = addRemoteTagNamespace (pns.getNamespace(),cond.getDestination().isIsHostCluster());
		} else {
			cidrDestination = (CIDRSelector) cond.getDestination();
		}

        String destPort = cond.getDestinationPort();
        String protocol = new String();
        switch (cond.getProtocolType()) {
            case TCP:
                protocol="TCP";
                break;
            case UDP:
                protocol="UDP";
                break;
            case SCTP:
                protocol="SCTP";
                break;
            case ALL:
                protocol="*";
                break;
        }
        return new Ruleinfo(sourcePodList, sourceNamespaceList, cidrSource, destinationPodList, destinationNamespaceList, cidrDestination,destPort,protocol,cond.getSource().isIsHostCluster(),cond.getDestination().isIsHostCluster());
    }
    
    private List<KeyValue> addRemoteTagNamespace (List<KeyValue> ruleNamespaces,boolean isHost){
        List<KeyValue> namespaceList = new ArrayList<>();
        for (KeyValue keyValue : ruleNamespaces){
            if (!isHost){
                for (Map.Entry<String, String> entry : this.remoteNamespaces.entrySet()){
                    String namespaceName = entry.getKey();
                    if (namespaceName.contains(keyValue.getValue())){
                        keyValue.setValue(namespaceName);;
                    }
                }
            }
        namespaceList.add(keyValue);
        }   
    return namespaceList;
    }
    
    private List<V1NetworkPolicy> createEgressAllowPolicyForNamespace(String name,Ruleinfo rule){

        List<V1NetworkPolicy> netPolicyList = new ArrayList<>();
        List <String> namespacesListToUse = new ArrayList<>();
        if (rule.isSourceHost()){
            namespacesListToUse.addAll(localNamespaces.keySet());
            //System.out.println("Source da usare locale");
        }else{
            namespacesListToUse.addAll(remoteNamespaces.keySet());
            //System.out.println("Source da usare remota");
        }
        List<KeyValue> sourcePods = rule.getSourcePod();
        for (KeyValue value : rule.getSourceNamespace()){
            //System.out.println(value.getValue());
            if(value.getValue().equals("*")){
                List<String> namespacesListToUseFinal=epurateAvailableNamespaces(namespacesListToUse,rule.getSourcePod());
                for(String namespaceName : namespacesListToUseFinal){ 
                    //terminare questa questione che non riesco a gestire
                    netPolicyList.addAll(createEgressAllowNetworkPolicyHeader(namespaceName,name,rule));
                }
            }else{
                netPolicyList.addAll(createEgressAllowNetworkPolicyHeader(value.getValue(),name,rule));
            }
        }
        return netPolicyList;
    }

    private List<String> epurateAvailableNamespaces (List<String> namespacesListToUse,List<KeyValue> Pods){
        List<String> namespacesListToUseFinal = new ArrayList<>();
        if (Pods.get(0).getKey().equals("*") && Pods.get(0).getValue().equals("*")){
            namespacesListToUseFinal.addAll(namespacesListToUse);
        }else if (Pods.get(0).getKey().equals("*")){
            for (KeyValue pods : Pods){
                for (Map.Entry<LabelsKeyValue, String> entry : availablePodsMap.entrySet()) {
                    LabelsKeyValue pod = entry.getKey();
                    String namespace = entry.getValue();
                    if(pod.getValue().equals(pods.getValue().replace("_", "-"))){
                        if (!namespacesListToUseFinal.contains(namespace)) {
                            namespacesListToUseFinal.add(namespace);
                        }
                    }
                }                                
            }
        } else if (Pods.get(0).getValue().equals("*")){
            for (KeyValue pods : Pods){
                for (Map.Entry<LabelsKeyValue, String> entry : availablePodsMap.entrySet()) {
                    LabelsKeyValue pod = entry.getKey();
                    String namespace = entry.getValue();
                    if(pod.getKey().equals(pods.getKey())){
                        if (!namespacesListToUseFinal.contains(namespace)) {
                            namespacesListToUseFinal.add(namespace);
                        }
                    }
                }                                
            }
        }else{
            for (KeyValue pods : Pods){
                for (Map.Entry<LabelsKeyValue, String> entry : availablePodsMap.entrySet()) {
                    LabelsKeyValue pod = entry.getKey();
                    String namespace = entry.getValue();
                    if(pod.getKey().equals(pods.getKey()) && pod.getValue().equals(pods.getValue().replace("_", "-"))){
                        if (!namespacesListToUseFinal.contains(namespace)) {
                            if (!namespacesListToUseFinal.contains(namespace)) {
                                namespacesListToUseFinal.add(namespace);
                            }
                        }
                    }
                }                                
            }
    }
    return namespacesListToUseFinal;
    }
    private List<V1NetworkPolicy> createEgressAllowNetworkPolicyHeader (String namespaceName,String name,Ruleinfo rule){

        //Network policy creation and setting of ApiVersion,Kind and metadata

        List<V1NetworkPolicy> networkPolicyList = new ArrayList<>();       
        //Creation of spec
        List <V1NetworkPolicySpec> specList = createEgressSpecList(rule.getLabelsSourcePod(),namespaceName);
        for (V1NetworkPolicySpec spec : specList){
            List <V1NetworkPolicyPeer> destinationPeerList = createEgressDestinationPeer(rule.getLabelsDestinationPod(),rule);
            networkPolicyList.addAll(createEgressNetworkPolicyList(destinationPeerList,spec,namespaceName,rule,name));
        }
        return networkPolicyList;
    }

    private List<V1NetworkPolicy> createEgressNetworkPolicyList (List <V1NetworkPolicyPeer> destinationPeerList,V1NetworkPolicySpec spec,String namespaceName,Ruleinfo rule,String name){
        List<V1NetworkPolicy> networkPolicyList = new ArrayList<>();  
        for (V1NetworkPolicyPeer destinationPeer : destinationPeerList){
            V1NetworkPolicy networkPolicy = new V1NetworkPolicy();
//                List <V1NetworkPolicyEgressRule> egressRuleList = createEgressRuleList (destinationPeer,rule);
            V1NetworkPolicySpec spec1 = new V1NetworkPolicySpec();
            spec1.setPodSelector(spec.getPodSelector());
            spec1.setPolicyTypes(spec.getPolicyTypes());
            V1NetworkPolicyEgressRule egressRule = new V1NetworkPolicyEgressRule();                    
            V1NetworkPolicyPort port = new V1NetworkPolicyPort();
            if (rule.getPort().contains("-")) {
                String[] portRange = rule.getPort().split("-");
                int startPort = Integer.parseInt(portRange[0]);
                int endPort = Integer.parseInt(portRange[1]);
                port.setPort(new IntOrString(startPort));
                port.setEndPort(endPort);
            } else {
                if (rule.getPort().equals("*")){
                    port.setPort(null);
                } else{
                    int portValue = Integer.parseInt(rule.getPort());
                    port.setPort(new IntOrString(portValue));
                }
            }
            if (rule.getProtocol().equals("*")){
                port.setProtocol(null);
            } else {
                port.setProtocol(rule.getProtocol());
            }
            if(port.getPort() != null || port.getProtocol() != null){
                egressRule.setPorts(Collections.singletonList(port)); 
            }

    
            egressRule.setTo(Collections.singletonList(destinationPeer));
            List<V1NetworkPolicyEgressRule> egressRules = new ArrayList<>(); 
            egressRules.add(egressRule);
            spec1.egress(egressRules);     
            networkPolicy.setApiVersion("networking.k8s.io/v1");
            networkPolicy.setKind("NetworkPolicy");
            V1ObjectMeta metadata = new V1ObjectMeta();
            //Qui aggiungere l' 
            networkPolicy.setSpec(spec1);  
            String hash_Name = hashName(name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(),networkPolicy,"Egress",namespaceName);
            metadata.setName(name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase()+namespaceName+hash_Name);
            index ++;
            metadata.namespace(namespaceName);
            networkPolicy.setMetadata(metadata);
            
            //System.out.print(networkPolicy.getSpec().getEgress().getFirst().getTo().getFirst().getNamespaceSelector().getMatchLabels().get("name"));
            networkPolicyList.add(networkPolicy);
        }
        // for (V1NetworkPolicy np: networkPolicyList){
        //     System.out.print(np.getSpec().getEgress().getFirst().getTo().getFirst().getNamespaceSelector().getMatchLabels().get("name"));
        // }
        //System.out.println(networkPolicyList.size());
        return networkPolicyList;
    }
    private List <V1NetworkPolicyEgressRule> createEgressRuleList (V1NetworkPolicyPeer destinationPeer,Ruleinfo rule) {

        List <V1NetworkPolicyEgressRule> egressRuleList = new ArrayList<>();
        Map<String, String> matchLabelsDestinationNamespace = rule.getLabelsDestinationNamespace();
        if (rule.getCidrDestination().getAddressRange() != null){
            V1NetworkPolicyEgressRule egressRule = new V1NetworkPolicyEgressRule();
            destinationPeer.setNamespaceSelector(null);
            egressRule.setTo(Collections.singletonList(destinationPeer));
            egressRuleList.add(egressRule);     
        }else{
            List<String> namespacesListToUse = new ArrayList<>();
            if (rule.isDestinationHost()){
                namespacesListToUse.addAll(localNamespaces.keySet());
                //System.out.println("Destination da usare locale");
            }else{
                namespacesListToUse.addAll(remoteNamespaces.keySet());
                //System.out.println("Destination da usare remota");
            }
            for (Map.Entry<String, String> entry : matchLabelsDestinationNamespace.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("*") && value.equals("*")){
                    for (String namespaceNameAvailable : namespacesListToUse){
                        V1NetworkPolicyEgressRule egressRule = new V1NetworkPolicyEgressRule();
                        V1NetworkPolicyPeer destinationPeer1 = new V1NetworkPolicyPeer();
                        V1LabelSelector namespace = new V1LabelSelector();
                        Map<String, String> map = new HashMap<>();
                        map.put("kubernetes.io/metadata.name", namespaceNameAvailable);
                        namespace.setMatchLabels(map);
                        destinationPeer.setNamespaceSelector(namespace);
                        egressRule.setTo(Collections.singletonList(destinationPeer));

                        //System.out.print(destinationPeer.getNamespaceSelector().getMatchLabels().get("name"));
                        //System.out.print(egressRule.getTo().getFirst().getNamespaceSelector().getMatchLabels().get("name"));
                        egressRuleList.add(egressRule);
                        //System.out.println(egressRule.getTo().getFirst().getNamespaceSelector().getMatchLabels().get("kubernetes.io/metadata.name"));  
                    }
                } else {
                    V1NetworkPolicyEgressRule egressRule = new V1NetworkPolicyEgressRule();
                    V1LabelSelector namespace = new V1LabelSelector();
                    namespace.setMatchLabels(matchLabelsDestinationNamespace);
                    destinationPeer.setNamespaceSelector(namespace); 
                    egressRule.setTo(Collections.singletonList(destinationPeer));
                    egressRuleList.add(egressRule);  
                    
            }
        }
        }
        return egressRuleList;
    }

    private List <V1NetworkPolicyPeer> addNamespaceToLabeDestinationPeer (Ruleinfo rule,V1LabelSelector destinationSelector){
        List <V1NetworkPolicyPeer> listDestinationPeer = new ArrayList<>();
        List<String> namespacesListToUse = new ArrayList<>();
        if (rule.isDestinationHost()){
            namespacesListToUse.addAll(localNamespaces.keySet());
        }else {
            namespacesListToUse.addAll(remoteNamespaces.keySet());
        }
        for (Map.Entry<String, String> entry : rule.getLabelsDestinationNamespace().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("*") && value.equals("*")){
                for (String namespaceNameAvailable : namespacesListToUse){
                    V1NetworkPolicyPeer destinationPeer = new V1NetworkPolicyPeer();
                    destinationPeer.setPodSelector(destinationSelector);
                    for (Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()) {
                        LabelsKeyValue key1 = entry1.getKey();
                        String namespaceAssociatedPod = entry1.getValue();
                        if(destinationSelector.getMatchLabels() != null && destinationSelector.getMatchLabels().equals(key1.getMap()) && namespaceNameAvailable.equals(namespaceAssociatedPod)){
                            V1LabelSelector namespace = new V1LabelSelector();
                            Map<String, String> map = new HashMap<>();
                            map.put("kubernetes.io/metadata.name", namespaceNameAvailable);
                            namespace.setMatchLabels(map);
                            destinationPeer.setNamespaceSelector(namespace);
                            listDestinationPeer.add(destinationPeer);
                        }
                    }
                    if (destinationSelector.getMatchLabels()==null){
                        V1LabelSelector namespace = new V1LabelSelector();
                        Map<String, String> map = new HashMap<>();
                        map.put("kubernetes.io/metadata.name", namespaceNameAvailable);
                        namespace.setMatchLabels(map);
                        destinationPeer.setNamespaceSelector(namespace);
                        listDestinationPeer.add(destinationPeer);
                    }
                }
            }else{
                V1NetworkPolicyPeer destinationPeer = new V1NetworkPolicyPeer();
                V1LabelSelector namespace = new V1LabelSelector();
                Map<String, String> map = new HashMap<>();
                if (destinationSelector.getMatchLabels() == null){
                    map.put("kubernetes.io/metadata.name", value);
                    destinationPeer.setPodSelector(destinationSelector);
                    namespace.setMatchLabels(map);
                    destinationPeer.setNamespaceSelector(namespace);
                    listDestinationPeer.add(destinationPeer);
                }
                else if(isInTheNamespaceCheck (destinationSelector,value)){
                    map.put("kubernetes.io/metadata.name", value);
                    destinationPeer.setPodSelector(destinationSelector);
                    namespace.setMatchLabels(map);
                    destinationPeer.setNamespaceSelector(namespace);
                    listDestinationPeer.add(destinationPeer);
                }
            }

        }
        return listDestinationPeer;
    }

    private boolean isInTheNamespaceCheck (V1LabelSelector destinationSelector,String value){
            for (Map.Entry<String, String> entry1 : destinationSelector.getMatchLabels().entrySet()) {
                String key1 = entry1.getKey();
                String value1 = entry1.getValue();
                for (Map.Entry<LabelsKeyValue, String> entry2 : availablePodsMap.entrySet()) {
                    LabelsKeyValue key2 = entry2.getKey();
                    String namespaceAssociatedPod = entry2.getValue();
                    if (key1.equals(key2.getKey()) && value1.equals(key2.getValue()) && value.equals(namespaceAssociatedPod)){
                        return true;
                    }
                }
            }
        return false;
    }
    private List <V1NetworkPolicySpec> createEgressSpecList (Map<String, String> matchLabelsSourcePod,String namespace){
        List <V1NetworkPolicySpec> specList = new ArrayList<>();
        for (Map.Entry<String, String> entry : matchLabelsSourcePod.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            List <V1LabelSelector> podSelectorList = new ArrayList<>();
            if(key.equals("*") && value.equals("*")){
                V1LabelSelector podSelector = null;
                podSelectorList.add(podSelector);
            }else if(value.equals("*")){
                for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                    LabelsKeyValue keyValue = entry1.getKey();
                    String namespaceValue = entry1.getValue();
                    if (namespace.equals(namespaceValue)){
                        if(key.equals(keyValue.getKey())){
                            V1LabelSelector podSelector = new V1LabelSelector();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            podSelector.setMatchLabels(labelsMap);
                            podSelectorList.add(podSelector);
                        }
                    }
                }
            }else if (key.equals("*")){
                for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                    LabelsKeyValue keyValue = entry1.getKey();
                    String namespaceValue = entry1.getValue();
                    if (namespace.equals(namespaceValue)){
                        if(value.equals(keyValue.getValue())){
                            V1LabelSelector podSelector = new V1LabelSelector();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            podSelector.setMatchLabels(labelsMap);
                            podSelectorList.add(podSelector);
                        }
                    }
                }
            } else {
                V1LabelSelector podSelector = new V1LabelSelector();
                Map<String, String> labelsMap = new HashMap<>();
                labelsMap.put(key,value);
                podSelector.setMatchLabels(labelsMap);
                podSelectorList.add(podSelector);               
            }

            for (V1LabelSelector podSelector : podSelectorList ){
                V1NetworkPolicySpec spec = new V1NetworkPolicySpec();
                spec.setPolicyTypes(Collections.singletonList("Egress")); //Setting of PolicyTyèe
                spec.podSelector(podSelector);
                specList.add(spec);
            }
        }
        return specList;
    }

    private List <V1NetworkPolicyPeer> createEgressDestinationPeer (Map<String, String> matchLabelsDestinationPod,Ruleinfo rule){
        List <V1NetworkPolicyPeer> listDestinationPeer = new ArrayList<>();
        if (rule.getCidrDestination().getAddressRange() != null){
            V1IPBlock ipBlock = new V1IPBlock();
            V1NetworkPolicyPeer destinationPeer = new V1NetworkPolicyPeer();
            ipBlock.setCidr(rule.getCidrDestination().getAddressRange());
            destinationPeer.setIpBlock(ipBlock);
            listDestinationPeer.add(destinationPeer);
        }else {
            for (Map.Entry<String, String> entry : matchLabelsDestinationPod.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("*") && value.equals("*")){
                    V1LabelSelector destinationSelector = new V1LabelSelector();
                    listDestinationPeer.addAll(addNamespaceToLabeDestinationPeer (rule,destinationSelector));
                }else if(value.equals("*")){
                    for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                        LabelsKeyValue keyValue = entry1.getKey();
                        String namespaceValue = entry1.getValue();
                        if(key.equals(keyValue.getKey())){
                            V1LabelSelector destinationSelector = new V1LabelSelector();
                            V1NetworkPolicyPeer destinationPeer = new V1NetworkPolicyPeer();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            destinationSelector.setMatchLabels(labelsMap);
                            listDestinationPeer.addAll(addNamespaceToLabeDestinationPeer (rule,destinationSelector));
                        }
                    }
                }else if (key.equals("*")){
                    for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                        LabelsKeyValue keyValue = entry1.getKey();
                        String namespaceValue = entry1.getValue();
                        if(value.equals(keyValue.getValue())){
                            V1LabelSelector destinationSelector = new V1LabelSelector();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            destinationSelector.setMatchLabels(labelsMap);
                            listDestinationPeer.addAll(addNamespaceToLabeDestinationPeer (rule,destinationSelector));
                        }
                    }
                }else {
                    V1LabelSelector destinationSelector = new V1LabelSelector();
                    V1NetworkPolicyPeer destinationPeer = new V1NetworkPolicyPeer();
                    Map<String, String> labelsMap = new HashMap<>();
                    labelsMap.put(key,value);
                    destinationSelector.setMatchLabels(labelsMap);
                    listDestinationPeer.addAll(addNamespaceToLabeDestinationPeer (rule,destinationSelector));            
                }
            }
    }
        return listDestinationPeer;
    }

    private List<V1NetworkPolicy> createHeaderIngressAllowPolicyForNamespace(String name,Ruleinfo rule){

        List<V1NetworkPolicy> netPolicyList = new ArrayList<>();
        List <String> namespacesListToUse = new ArrayList<>();
        if (rule.isDestinationHost()){
            namespacesListToUse.addAll(localNamespaces.keySet());
        }else{
            namespacesListToUse.addAll(remoteNamespaces.keySet());
        }
            for (KeyValue value : rule.getDestinationNamespace()){
                //System.out.println(value.getValue());
                if(value.getValue().equals("*")){
                    List<String> namespacesListToUseFinal=epurateAvailableNamespaces(namespacesListToUse,rule.getDestinationPod());
                    for(String namespace : namespacesListToUseFinal){ 
                        netPolicyList.addAll(createIngressAllowNetworkPolicyHeader(namespace,name,rule));
                    }
                }else{
                    netPolicyList.addAll(createIngressAllowNetworkPolicyHeader(value.getValue(),name,rule));
                }
            }

        return netPolicyList;
    }

    private List<V1NetworkPolicy> createIngressAllowNetworkPolicyHeader (String namespaceName,String name,Ruleinfo rule){

        //Network policy creation and setting of ApiVersion,Kind and metadata

        List<V1NetworkPolicy> networkPolicyList = new ArrayList<>();       
        //Creation of spec
        List <V1NetworkPolicySpec> specList = createIngressSpecList(rule.getLabelsDestinationPod(),namespaceName);
        for (V1NetworkPolicySpec spec : specList){
            List <V1NetworkPolicyPeer> sourcePeerList = createIngressSourcePeer(rule.getLabelsSourcePod(),rule);
            networkPolicyList.addAll(createIngressNetworkPolicyList(sourcePeerList,spec,namespaceName,rule,name));
        }
        return networkPolicyList;
    }

    private List <V1NetworkPolicySpec> createIngressSpecList (Map<String, String> matchLabelsDestinationPod,String namespace){
        List <V1NetworkPolicySpec> specList = new ArrayList<>();
        for (Map.Entry<String, String> entry : matchLabelsDestinationPod.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            List <V1LabelSelector> podSelectorList = new ArrayList<>();
            if(key.equals("*") && value.equals("*")){
                V1LabelSelector podSelector = null;
                podSelectorList.add(podSelector);
            }else if(value.equals("*")){
                for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                    LabelsKeyValue keyValue = entry1.getKey();
                    String namespaceValue = entry1.getValue();
                    if (namespace.equals(namespaceValue)){
                        if(key.equals(keyValue.getKey())){
                            V1LabelSelector podSelector = new V1LabelSelector();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            podSelector.setMatchLabels(labelsMap);
                            podSelectorList.add(podSelector);
                        }
                    }
                }
            }else if (key.equals("*")){
                for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                    LabelsKeyValue keyValue = entry1.getKey();
                    String namespaceValue = entry1.getValue();
                    if (namespace.equals(namespaceValue)){
                        if(value.equals(keyValue.getValue())){
                            V1LabelSelector podSelector = new V1LabelSelector();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            podSelector.setMatchLabels(labelsMap);
                            podSelectorList.add(podSelector);
                        }
                    }
                }
            } else {
                V1LabelSelector podSelector = new V1LabelSelector();
                Map<String, String> labelsMap = new HashMap<>();
                labelsMap.put(key,value);
                podSelector.setMatchLabels(labelsMap);
                podSelectorList.add(podSelector);               
            }

            for (V1LabelSelector podSelector : podSelectorList ){
                V1NetworkPolicySpec spec = new V1NetworkPolicySpec();
                spec.setPolicyTypes(Collections.singletonList("Ingress")); //Setting of PolicyTyèe
                spec.podSelector(podSelector);
                specList.add(spec);
            }
        }
        return specList;
    }
    
    private List <V1NetworkPolicyPeer> createIngressSourcePeer (Map<String, String> matchLabelsSourcePod,Ruleinfo rule){
        List <V1NetworkPolicyPeer> listSourcePeer = new ArrayList<>();
        if (rule.getCidrSource().getAddressRange() != null){
            V1IPBlock ipBlock = new V1IPBlock();
            V1NetworkPolicyPeer sourcePeer = new V1NetworkPolicyPeer();
            ipBlock.setCidr(rule.getCidrSource().getAddressRange());
            sourcePeer.setIpBlock(ipBlock);
            listSourcePeer.add(sourcePeer);
        }else {
            for (Map.Entry<String, String> entry : matchLabelsSourcePod.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("*") && value.equals("*")){
                    V1LabelSelector sourceSelector = new V1LabelSelector();
                    listSourcePeer.addAll(addNamespaceToLabeSourcePeer (rule,sourceSelector));
                }else if(value.equals("*")){
                    for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                        LabelsKeyValue keyValue = entry1.getKey();
                        String namespaceValue = entry1.getValue();
                        if(key.equals(keyValue.getKey())){
                            V1LabelSelector sourceSelector = new V1LabelSelector();
                            V1NetworkPolicyPeer sourcePeer = new V1NetworkPolicyPeer();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            sourceSelector.setMatchLabels(labelsMap);
                            listSourcePeer.addAll(addNamespaceToLabeSourcePeer (rule,sourceSelector));
                        }
                    }
                }else if (key.equals("*")){
                    for(Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()){
                        LabelsKeyValue keyValue = entry1.getKey();
                        String namespaceValue = entry1.getValue();
                        if(value.equals(keyValue.getValue())){
                            V1LabelSelector sourceSelector = new V1LabelSelector();
                            Map<String, String> labelsMap = new HashMap<>();
                            labelsMap.put(keyValue.getKey(), keyValue.getValue());
                            sourceSelector.setMatchLabels(labelsMap);
                            listSourcePeer.addAll(addNamespaceToLabeSourcePeer (rule,sourceSelector));
                        }
                    }
                }else {
                    V1LabelSelector sourceSelector = new V1LabelSelector();
                    V1NetworkPolicyPeer sourcePeer = new V1NetworkPolicyPeer();
                    Map<String, String> labelsMap = new HashMap<>();
                    labelsMap.put(key,value);
                    sourceSelector.setMatchLabels(labelsMap);
                    listSourcePeer.addAll(addNamespaceToLabeSourcePeer (rule,sourceSelector));            
                }
            }
    }
        return listSourcePeer;
    }

    private List <V1NetworkPolicyPeer> addNamespaceToLabeSourcePeer (Ruleinfo rule,V1LabelSelector sourceSelector){
        List <V1NetworkPolicyPeer> listSourcePeer = new ArrayList<>();
        List<String> namespacesListToUse = new ArrayList<>();
        if (rule.isDestinationHost()){
            namespacesListToUse.addAll(localNamespaces.keySet());
        }else {
            namespacesListToUse.addAll(remoteNamespaces.keySet());
        }
        for (Map.Entry<String, String> entry : rule.getLabelsSourceNamespace().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("*") && value.equals("*")){
                for (String namespaceNameAvailable : namespacesListToUse){
                    V1NetworkPolicyPeer sourcePeer = new V1NetworkPolicyPeer();
                    sourcePeer.setPodSelector(sourceSelector);
                    for (Map.Entry<LabelsKeyValue, String> entry1 : availablePodsMap.entrySet()) {
                        LabelsKeyValue key1 = entry1.getKey();
                        String namespaceAssociatedPod = entry1.getValue();
                        if(sourceSelector.getMatchLabels() != null && sourceSelector.getMatchLabels().equals(key1.getMap()) && namespaceNameAvailable.equals(namespaceAssociatedPod)){
                            V1LabelSelector namespace = new V1LabelSelector();
                            Map<String, String> map = new HashMap<>();
                            map.put("kubernetes.io/metadata.name", namespaceNameAvailable);
                            namespace.setMatchLabels(map);
                            sourcePeer.setNamespaceSelector(namespace);
                            listSourcePeer.add(sourcePeer);
                        }
                    }
                    if(sourceSelector.getMatchLabels() == null){ //Caso in cui ho tutti i pod e tutte le chaivi selezionate
                        V1LabelSelector namespace = new V1LabelSelector();
                        Map<String, String> map = new HashMap<>();
                        map.put("kubernetes.io/metadata.name", namespaceNameAvailable);
                        namespace.setMatchLabels(map);
                        sourcePeer.setNamespaceSelector(namespace);
                        listSourcePeer.add(sourcePeer);
                    }
                }
            }else{
                V1NetworkPolicyPeer sourcePeer = new V1NetworkPolicyPeer();
                V1LabelSelector namespace = new V1LabelSelector();
                Map<String, String> map = new HashMap<>();
                if (sourceSelector.getMatchLabels() == null){
                    map.put("kubernetes.io/metadata.name", value);
                    sourcePeer.setPodSelector(sourceSelector);
                    namespace.setMatchLabels(map);
                    sourcePeer.setNamespaceSelector(namespace);
                    listSourcePeer.add(sourcePeer);
                } else if(isInTheNamespaceCheck (sourceSelector,value)){
                    map.put("kubernetes.io/metadata.name", value);
                    sourcePeer.setPodSelector(sourceSelector);
                    namespace.setMatchLabels(map);
                    sourcePeer.setNamespaceSelector(namespace);
                    listSourcePeer.add(sourcePeer);
                }
            }

        }
        return listSourcePeer;
    }
    
    private List<V1NetworkPolicy> createIngressNetworkPolicyList (List <V1NetworkPolicyPeer> sourcePeerList,V1NetworkPolicySpec spec,String namespaceName,Ruleinfo rule,String name){
        List<V1NetworkPolicy> networkPolicyList = new ArrayList<>();  
        for (V1NetworkPolicyPeer sourcePeer : sourcePeerList){
            V1NetworkPolicy networkPolicy = new V1NetworkPolicy();
            V1NetworkPolicySpec spec1 = new V1NetworkPolicySpec();
            spec1.setPodSelector(spec.getPodSelector());
            spec1.setPolicyTypes(spec.getPolicyTypes());
            V1NetworkPolicyIngressRule ingressRule = new V1NetworkPolicyIngressRule();                    
            V1NetworkPolicyPort port = new V1NetworkPolicyPort();
            if (rule.getPort().contains("-")) {
                String[] portRange = rule.getPort().split("-");
                int startPort = Integer.parseInt(portRange[0]);
                int endPort = Integer.parseInt(portRange[1]);
                port.setPort(new IntOrString(startPort));
                port.setEndPort(endPort);
            } else {
                if (rule.getPort().equals("*")){
                    port.setPort(null);
                } else{
                    int portValue = Integer.parseInt(rule.getPort());
                    port.setPort(new IntOrString(portValue));
                }
            }
            if (rule.getProtocol().equals("*")){
                port.setProtocol(null);
            } else {
                port.setProtocol(rule.getProtocol());
            }
            if(port.getPort() != null || port.getProtocol() != null){
                ingressRule.setPorts(Collections.singletonList(port)); 
            }

            ingressRule.setFrom(Collections.singletonList(sourcePeer));
            List<V1NetworkPolicyIngressRule> IngressRules = new ArrayList<>(); 
            IngressRules.add(ingressRule);  
            spec1.ingress(IngressRules);
            networkPolicy.setApiVersion("networking.k8s.io/v1");
            networkPolicy.setKind("NetworkPolicy");
            V1ObjectMeta metadata = new V1ObjectMeta();
            networkPolicy.setSpec(spec1); 
            String hash_Name = hashName(name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(),networkPolicy,"Ingress",namespaceName);
            metadata.setName(name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase()+namespaceName+hash_Name);
            index++;
            metadata.namespace(namespaceName);
            networkPolicy.setMetadata(metadata);
             
            //System.out.print(networkPolicy.getSpec().getEgress().getFirst().getTo().getFirst().getNamespaceSelector().getMatchLabels().get("name"));
            networkPolicyList.add(networkPolicy);
        }
        // for (V1NetworkPolicy np: networkPolicyList){
        //     System.out.print(np.getSpec().getEgress().getFirst().getTo().getFirst().getNamespaceSelector().getMatchLabels().get("name"));
        // }
        //System.out.println(networkPolicyList.size());
        return networkPolicyList;
    }

    private V1NetworkPolicy createIngressAllowNetworkPolicy1 (String namespaceName,String name,Ruleinfo rule){

        //Network policy creation and setting of ApiVersion,Kind and metadata
        V1NetworkPolicy networkPolicy = new V1NetworkPolicy();
        networkPolicy.setApiVersion("networking.k8s.io/v1");
        networkPolicy.setKind("NetworkPolicy");
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase()+namespaceName);
        metadata.namespace(namespaceName);
        // if(rule.getDestinationNamespace().get(0).getValue().equals("*") || rule.getDestinationNamespace().isEmpty()){
        //     metadata.namespace(null);
        // }else{
        //     metadata.namespace(rule.getDestinationNamespace().get(0).getValue()); //Questo è da aggiornare in uno step successivo
        // }
        networkPolicy.setMetadata(metadata);
        
        //Creation of spec
        V1NetworkPolicySpec spec = new V1NetworkPolicySpec();
        spec.setPolicyTypes(Collections.singletonList("Ingress")); //Setting of PolicyTyèe
        V1LabelSelector podSelector = new V1LabelSelector();
        Map<String, String> matchLabelsDestinationPod = rule.getLabelsDestinationPod();
        //Setting of podSelector which is involved in the policy
        if (matchLabelsDestinationPod.containsValue("*")){
            spec.setPodSelector(null); // gestire il caso in cui devo gestire tutti i POD in quanto kubernetes non va se non specifico PodSelector
        }else {
            if(matchLabelsDestinationPod.isEmpty()){
                spec.setPodSelector(podSelector);
            }else{
                podSelector.setMatchLabels(matchLabelsDestinationPod);
                spec.setPodSelector(podSelector);               
            }
        }
        //Setting the egress rules and in particoular the destination port (protocol and number of the port)
        V1NetworkPolicyIngressRule ingressRule = new V1NetworkPolicyIngressRule();
        List<V1NetworkPolicyIngressRule> ingressRules = new ArrayList<>();
        V1NetworkPolicyPort port = new V1NetworkPolicyPort();
        
        if (rule.getPort().contains("-")) {
            String[] portRange = rule.getPort().split("-");
            int startPort = Integer.parseInt(portRange[0]);
            int endPort = Integer.parseInt(portRange[1]);
            port.setPort(new IntOrString(startPort));
            port.setEndPort(endPort);
        } else {;
            if (rule.getPort().equals("*")){
                port.setPort(null);
            } else{
                int portValue = Integer.parseInt(rule.getPort());
                port.setPort(new IntOrString(portValue));
            }
        }
        if (rule.getProtocol().equals("*")){
            port.setProtocol(null);
        } else {
            port.setProtocol(rule.getProtocol());
        }
        if(port.getPort() != null || port.getProtocol() != null){
            ingressRule.setPorts(Collections.singletonList(port)); 
        }

        //Setting of the destination, in particoular I'm setting the name of the destinationPod or the cidrIp address and eventually the destination namespace
        V1LabelSelector destinationSelector = new V1LabelSelector();
        V1NetworkPolicyPeer destinationPeer = new V1NetworkPolicyPeer();
        Map<String, String> matchLabelsSourcePod = rule.getLabelsSourcePod();
        if(matchLabelsSourcePod.containsValue("*")){
            destinationSelector.setMatchLabels(null);
            destinationPeer.setPodSelector(destinationSelector);            
        } else if (!matchLabelsSourcePod.containsValue("*") && !matchLabelsSourcePod.isEmpty()){
            destinationSelector.setMatchLabels(matchLabelsSourcePod);
            destinationPeer.setPodSelector(destinationSelector);            
        }

        if (rule.getCidrSource().getAddressRange() != null){
            V1IPBlock ipBlock = new V1IPBlock();
            ipBlock.setCidr(rule.getCidrSource().getAddressRange());
            destinationPeer.setIpBlock(ipBlock);
        }

        //Vedere meglio la questione del SourceNamespace
        Map<String, String> matchLabelsSourceNamespace = rule.getLabelsSourceNamespace();
        if(!matchLabelsSourceNamespace.containsValue("*") && !matchLabelsSourceNamespace.isEmpty()){
            V1LabelSelector namespace = new V1LabelSelector();
            namespace.setMatchLabels(matchLabelsSourceNamespace);
            destinationPeer.setNamespaceSelector(namespace);
        }else{
            if(rule.getCidrSource().getAddressRange() == null){
                V1LabelSelector namespace = new V1LabelSelector();
                destinationPeer.setNamespaceSelector(namespace);
            }
        }

        ingressRule.setFrom(Collections.singletonList(destinationPeer));
        ingressRules.add(ingressRule);
        spec.ingress(ingressRules);
        networkPolicy.setSpec(spec);
        return networkPolicy;
        
    }
    
    
    
    private void writeNetworkPoliciesToFile1(List<V1NetworkPolicy> networkPolicies) {
        try {
            for (V1NetworkPolicy networkPolicy : networkPolicies) {
                //String fileName = "src/network_policies/" + networkPolicy.getMetadata().getName() + " " + networkPolicy.getSpec().getPolicyTypes().get(0)+".yaml";
                String fileName = "/app/network_policies/" + networkPolicy.getMetadata().getName() + " " + networkPolicy.getSpec().getPolicyTypes().get(0)+".yaml";
                FileWriter fileWriter = new FileWriter(fileName);
                LinkedHashMap<String, Object> yamlData = new LinkedHashMap<>();
                yamlData.put("apiVersion", networkPolicy.getApiVersion());
                yamlData.put("kind", networkPolicy.getKind());
    
                LinkedHashMap<String, Object> metadata = new LinkedHashMap<>();
                metadata.put("name", networkPolicy.getMetadata().getName());
                if(networkPolicy.getMetadata().getNamespace() != null){
                    metadata.put("namespace", networkPolicy.getMetadata().getNamespace());
                }
                yamlData.put("metadata", metadata);
    
                LinkedHashMap<String, Object> spec = new LinkedHashMap<>();
                spec.put("policyTypes", networkPolicy.getSpec().getPolicyTypes());
                
                LinkedHashMap<String, Object> podSelector = new LinkedHashMap<>();
                LinkedHashMap<String, Object> matchLabels = new LinkedHashMap<>();
                if (networkPolicy.getSpec().getPodSelector() != null) {
                    matchLabels.putAll(networkPolicy.getSpec().getPodSelector().getMatchLabels());
                    podSelector.put("matchLabels", matchLabels);
                    spec.put("podSelector", podSelector);
                }else{
                    spec.put("podSelector",podSelector);
                }
    
                if (networkPolicy.getSpec().getEgress() != null && !networkPolicy.getSpec().getEgress().isEmpty()) {
                    spec.put("egress", networkPolicy.getSpec().getEgress());
                }
                if (networkPolicy.getSpec().getIngress() != null && !networkPolicy.getSpec().getIngress().isEmpty()) {
                    spec.put("ingress", networkPolicy.getSpec().getIngress());
                }
    
                yamlData.put("spec", spec);
    
                Yaml yaml = new Yaml();
                yaml.dump(yamlData, fileWriter);
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 private String hashName(String name , V1NetworkPolicy networkPolicy ,String Policy_type,String namespaceName) {
            StringBuilder string_to_hash = new StringBuilder(name);
            string_to_hash.append(Policy_type);
            string_to_hash.append(namespaceName);
            
            if (networkPolicy.getSpec().getEgress() != null) {
                for (V1NetworkPolicyEgressRule entry : networkPolicy.getSpec().getEgress()) {
                    if (entry.getPorts()!= null) {
                        for (V1NetworkPolicyPort port : entry.getPorts()) {
                            string_to_hash.append(port);
                        }
                    }
                    for (V1NetworkPolicyPeer peer : entry.getTo()) {
                        if (peer.getIpBlock() != null) {
                            string_to_hash.append(peer.getIpBlock().getCidr());
                        }
                        if (peer.getNamespaceSelector() != null && peer.getNamespaceSelector().getMatchLabels() != null) {
                            for (Map.Entry<String, String> namespaceEntry : peer.getNamespaceSelector().getMatchLabels().entrySet()) {
                                string_to_hash.append(namespaceEntry.getKey());
                                string_to_hash.append(namespaceEntry.getValue());
                            }
                        }
                        if (peer.getPodSelector() != null && peer.getPodSelector().getMatchLabels() != null) {
                            for (Map.Entry<String, String> podSelectorEntry : peer.getPodSelector().getMatchLabels().entrySet()) {
                                string_to_hash.append(podSelectorEntry.getKey());
                                string_to_hash.append(podSelectorEntry.getValue());
                            }
                        }
                    }
                }
            }
            
            if (networkPolicy.getSpec().getIngress() != null) {
                for (V1NetworkPolicyIngressRule entry : networkPolicy.getSpec().getIngress()) {
                    if (entry.getPorts()!= null) {
                        for (V1NetworkPolicyPort port : entry.getPorts()) {
                            string_to_hash.append(port);
                        }
                    }
                    for (V1NetworkPolicyPeer peer : entry.getFrom()) {
                        if (peer.getIpBlock() != null) {
                            string_to_hash.append(peer.getIpBlock().getCidr());
                        }
                        if (peer.getNamespaceSelector() != null && peer.getNamespaceSelector().getMatchLabels() != null) {
                            for (Map.Entry<String, String> namespaceEntry : peer.getNamespaceSelector().getMatchLabels().entrySet()) {
                                string_to_hash.append(namespaceEntry.getKey());
                                string_to_hash.append(namespaceEntry.getValue());
                            }
                        }
                        if (peer.getPodSelector() != null && peer.getPodSelector().getMatchLabels() != null) {
                            for (Map.Entry<String, String> podSelectorEntry : peer.getPodSelector().getMatchLabels().entrySet()) {
                                string_to_hash.append(podSelectorEntry.getKey());
                                string_to_hash.append(podSelectorEntry.getValue());
                            }
                        }
                    }
                }
            }
            
            if (networkPolicy.getSpec().getPodSelector() != null && networkPolicy.getSpec().getPodSelector().getMatchLabels() != null) {
                for (Map.Entry<String, String> entry : networkPolicy.getSpec().getPodSelector().getMatchLabels().entrySet()) {
                    string_to_hash.append(entry.getKey());
                    string_to_hash.append(entry.getValue());
                }
            }
            
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                // Convertire la stringa in un array di byte
                byte[] encodedhash = digest.digest(string_to_hash.toString().getBytes(StandardCharsets.UTF_8));
                // Convertire l'array di byte in una stringa esadecimale
                return bytesToHex(encodedhash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

    private String bytesToHex(byte[] hash) {
        // Utilizzare un Formatter per convertire i byte in una stringa esadecimale
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
