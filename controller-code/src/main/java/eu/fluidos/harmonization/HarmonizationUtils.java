package eu.fluidos.harmonization;

import eu.fluidos.*;
import eu.fluidos.jaxb.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HarmonizationUtils {
	/**
	 * Function to compute the difference between two port ranges.
	 * @param portRangeX is the first port range.
	 * @param portRangeY is the second port range.
	 * @return the resulting of first port range MINUS second port range (could be multiple ranges separated by a semicolon)
	 */
	public static String computeHarmonizedPortRange(String portRangeX, String portRangeY) {
		if(portRangeX.equals("*"))
			portRangeX = "0-65535";
		if (portRangeY.equals("*"))
			portRangeY = "0-65535";

		String[] x = portRangeX.split("-");
		String[] y = portRangeY.split("-");
		Integer x_0 = Integer.parseInt(x[0]);
		Integer x_1 = x_0;
		if (x.length == 2)
			x_1 = Integer.parseInt(x[1]);
		Integer y_0 = Integer.parseInt(y[0]);
		Integer y_1 = y_0;
		if (y.length == 2)
			y_1 = Integer.parseInt(y[1]);

		if(x.length == 1 && y.length == 1){
			// Both are single ports
			if(x[0].equals(y[0])){
				return "";
			}
		} else if(x.length == 1 && y.length == 2){
			// x is a single port and y is a range
			if(x_0 >= y_0 && x_0 <= y_1){
				return "";
			}
		} else if(x.length == 2 && y.length == 1){
			// x is a range and y is a single port
			if(x_0 <= y_0 && x_1 >= y_0){
				return ((x_0 != y_0)? x_0 + "-" + (y_0 - 1) : x_0) + ";" + ((y_0 != x_1)?(y_0 + 1) + "-" + x_1:y_0);
			}
		} else if(x.length == 2 && y.length == 2){
			// Both are ranges
			if(x_0 <= y_1 && x_1 >= y_0) {
				if(x_0 >= y_0 && x_1 <= y_1){
					return "";
				} else if(x_0 <= y_0 && x_1 <= y_1){
					return (x_0 != (y_0-1))? x_0 + "-" + (y_0 - 1): String.valueOf(x_0);
				} else if(x_0 >= y_0 && x_1 >= y_1){
					return ((y_1+1) != x_1)?(y_1 + 1) + "-" + x_1: String.valueOf(x_1);
				} else if(x_0 <= y_0 && x_1 >= y_1){
					return ((x_0 != (y_0-1))?x_0 + "-" + (y_0 - 1): x_0) + ";" + (((y_1+1) != x_1)?(y_1 + 1) + "-" + x_1: x_1);
				}
			}
		}
		return portRangeX;
	}

	/**
	 * Function to compute the difference between two port ranges.
	 * @param portRangeX is the first port range.
	 * @param portRangeY is the second port range.
	 * @return the resulting of first port range MINUS second port range (true (overlap) / false (no overlap))
	 */
	public static boolean computeVerifiedPortRange(String portRangeX, String portRangeY) {
		portRangeX = portRangeX.equals("*") ? "0-65535" : portRangeX;
		portRangeY = portRangeY.equals("*") ? "0-65535" : portRangeY;

		String[] x = portRangeX.split("-");
		String[] y = portRangeY.split("-");

		int x0 = Integer.parseInt(x[0]);
		int x1 = x.length == 2 ? Integer.parseInt(x[1]) : x0;

		int y0 = Integer.parseInt(y[0]);
		int y1 = y.length == 2 ? Integer.parseInt(y[1]) : y0;

		/* Return true if portRangeX are overlapping with portRangeY */
		return (x0 <= y1 && x1 >= y0);
	}

	/**
	 * Function to compute the (set) difference between two Protocol types.
	 * @param value is the first set of protocols.
	 * @param value2 is the second set of protocols.
	 * @return the resulting of first set MINUS second set.
	 */
	public static String[] computeHarmonizedProtocolType(String value, String value2) {
		if(value.equals(value2)) {
			// two identical sets.
			return new String[] {};
		} else if(value2.equals("ALL")) {
			// inner set minus the whole domain.
			return new String[] {};
		} else if(value.equals("ALL")) {
			// inner set is whole domain, return it minus the second set.
			if(value2.equals("TCP")){
				return new String[]{"UDP", "SCTP"};
			} else if(value2.equals("UDP")){
				return new String[]{"TCP","SCTP"};
			} else if(value2.equals("SCTP")){
				return new String[]{"TCP", "UDP"};
			}
		}
		// If none of the previous, then they are two disjoint sets.
		return new String[] {value};
	}

	/**
	 * Function to compute the (set) difference between two Protocol types.
	 * @param value is the first set of protocols.
	 * @param value2 is the second set of protocols.
	 * @return true if value is overlapping with value2.
	 */
	public static boolean computeVerifyProtocolType(String value, String value2) {
		return value.equals(value2) || value.equals("ALL") || value2.equals("ALL") || value.equals("*") || value2.equals("*");
	}

	/**
	 * Function convert a KubernetesNetworkFilteringCondition into a string.
	 * @param cond is the condition to be printed.
	 * @return the string representation
	 */
	public static String kubernetesNetworkFilteringConditionToString(KubernetesNetworkFilteringCondition cond) {
		String res = "";
		if(cond.getSource().getClass().equals(PodNamespaceSelector.class)){
			PodNamespaceSelector pns = (PodNamespaceSelector) cond.getSource();
			res = res + "Src: [" + pns.getPod().stream()
					.map(it -> it.getKey() + ":" + it.getValue())
					.reduce("", (a,b) -> a + " " + b) + " - " + pns.getNamespace().get(0).getKey() + ":" + pns.getNamespace().get(0).getValue() + " ], ";
		} else {
			CIDRSelector cidr = (CIDRSelector) cond.getSource();
			res = res + "Src: [" + cidr.getAddressRange() + "], ";
		}
		if(cond.getDestination().getClass().equals(PodNamespaceSelector.class)){
			PodNamespaceSelector pns = (PodNamespaceSelector) cond.getDestination();
			res = res + "Dst: [" + pns.getPod().stream()
					.map(it -> it.getKey() + ":" + it.getValue())
					.reduce("", (a,b) -> a + " " + b) + " - " + pns.getNamespace().get(0).getKey() + ":" + pns.getNamespace().get(0).getValue() + " ], ";
		} else {
			CIDRSelector cidr = (CIDRSelector) cond.getDestination();
			res = res + "Dst: [" + cidr.getAddressRange() + "], ";
		}
		res = res + "DstPort: [" + cond.getDestinationPort() + "], ";
		res = res + "ProtocolType: [" + cond.getProtocolType() + "]";
		return res;
	}

	/**
	 * Function to compute the difference between two different resourceSelectors.
	 * @param sel_1 is the first resourceSelector.
	 * @param sel_2 is the second resourceSelector.
	 * @param map_1 is the hashmap of pods grouped by namespace and labels for the first selector's cluster.
	 * @param map_2  is the hashmap of pods grouped by namespace and labels for the second selector's cluster.
	 * @return the list of harmonized resourceSelector; i.e. the set difference between them, so the selector for the resources that are selected by sel_1 and NOT by sel_2)
	 */
	public static List<ResourceSelector> computeHarmonizedResourceSelector(ResourceSelector sel_1, ResourceSelector sel_2, HashMap<String, HashMap<String, List<Pod>>> map_1, HashMap<String, HashMap<String, List<Pod>>> map_2) {
		Boolean isCIDR_1 = false, isCIDR_2 = false;
		List<ResourceSelector> res = new ArrayList<ResourceSelector>();
		if(sel_1.getClass().equals(PodNamespaceSelector.class)){
			isCIDR_1 = false;
		} else {
			isCIDR_1 = true;
		}
		if(sel_2.getClass().equals(PodNamespaceSelector.class)){
			isCIDR_2 = false;
		} else {
			isCIDR_2 = true;
		}

		// If both are CIDRSelectors, then we can compute the difference in this way.
		if(isCIDR_1 && isCIDR_2){
			CIDRSelector cidr1 = (CIDRSelector) sel_1;
			CIDRSelector cidr2 = (CIDRSelector) sel_2;
			String resCIDRHarmonization = cidrDifference(cidr1.getAddressRange(), cidr2.getAddressRange());
			if(resCIDRHarmonization == null) {
				return res;
			}
			// Convert the resulting CIDR strings into CIDRSelectors.
			for(String s: resCIDRHarmonization.split(";")){
				CIDRSelector tmp = new CIDRSelector();
				tmp.setAddressRange(s);
				res.add(tmp);
			}
			return res;
		} else if(!isCIDR_1 && !isCIDR_2){
			// If both are PodNamespaceSelectors, then we can compute the difference in this way.
			PodNamespaceSelector pns1 = (PodNamespaceSelector) sel_1;
			PodNamespaceSelector pns2 = (PodNamespaceSelector) sel_2;

			// First, need to check if the two selectors refer to the same cluster.
			if(pns1.isIsHostCluster() && !pns2.isIsHostCluster()) {
				// sel_1 considers the host cluster, sel_2 considers the local one. Use map_2.
				res.addAll(computeHarmonizedPodNamespaceSelector(pns1, pns2, map_2));
			} else if(!pns1.isIsHostCluster() && pns2.isIsHostCluster()){
				// sel_1 considers the local cluster, sel_2 considers the remote one. Use map_1.
				res.addAll(computeHarmonizedPodNamespaceSelector(pns1, pns2, map_1));
			} else {
				// There is not compatibility between the two selectors... they cover different clusters.
				return null;
			}
		} else {
			// If one is a CIDRSelector and the other is a PodNamespaceSelector, then we can not compute the difference (for the moment...)
			return null;
		}

		return res;
	}

	/**
	 * Function to compute the difference between two different PodNamespaceSelector.
	 * @param pns1 is the first PodNamespaceSelector.
	 * @param pns2 is the second PodNamespaceSelector.
	 * @param clusterMap is the cluster where the selectors are applied.
	 * @return the list of harmonized PodNamespaceSelectors (selecting the resources that are selected by pns1 and NOT by pns2)
	 */
	private static List<PodNamespaceSelector> computeHarmonizedPodNamespaceSelector(PodNamespaceSelector pns1, PodNamespaceSelector pns2, HashMap<String, HashMap<String, List<Pod>>> clusterMap) {
		ArrayList<Pod> pns1SelectedPods = new ArrayList<Pod>();
		ArrayList<Pod> pns2SelectedPods = new ArrayList<Pod>();

		//Case-1: pns2 selects all pods and namespaces, so pns1 - pns2 = 0
		if(pns2.getNamespace().get(0).getKey().equals("*") && pns2.getPod().get(0).getValue().equals("*")){
			return new ArrayList<PodNamespaceSelector>();
		}

		//Case-2: possibility of having none or partial overlap between pns1 and pns2. Need to move to Pods.

		pns1SelectedPods = selectPods(pns1, clusterMap);

		// Repeat everything for pns2...

		pns2SelectedPods = selectPods(pns2, clusterMap);

		//Change the list.

		List<PodNamespaceSelector> resSelectors = new ArrayList<>();

		pns1SelectedPods.removeAll(pns2SelectedPods);

		//Now we have the list of pods. We need to convert it into a list of PodNamespaceSelectors.
		resSelectors = convertPods(pns1, pns1SelectedPods, clusterMap);

		return resSelectors;
	}

	private static ArrayList<Pod> selectPods(PodNamespaceSelector pns, HashMap<String, HashMap<String, List<Pod>>> clusterMap){

		ArrayList<Pod> pnsSelectedPods = new ArrayList<Pod>();

		if(pns.getNamespace().get(0).getKey().equals("*") && pns.getNamespace().get(0).getValue().equals("*") &&
				pns                                                                   .getPod().get(0).getKey().equals("*") && pns.getPod().get(0).getValue().equals("*")){
			// Special case-1: select all cluster's pods
			// Select all pods in the cluster
			pnsSelectedPods.addAll(clusterMap.values().stream().flatMap(it -> it.values().stream()).flatMap(it -> it.stream()).collect(Collectors.toList()));
		} else if(pns.getPod().get(0).getKey().equals("*") && pns.getPod().get(0).getValue().equals("*")){
			// Special case-2: select all pods in the namespace
			// Select all pods in the namespace
			for(KeyValue ns : pns.getNamespace()) {
				if(clusterMap.containsKey(ns.getKey() + ":" + ns.getValue())) {
					for(List<Pod> pods : clusterMap.get(ns.getKey() + ":" + ns.getValue()).values()) {
						pnsSelectedPods.addAll(pods);
					}
				}
			}
		} else if(pns.getNamespace().get(0).getKey().equals("*") && pns.getNamespace().get(0).getValue().equals("*")){
			// Special case-3: select all namespaces with a specific pod label
			// Select all namespaces with a specific pod label
			for(KeyValue pod : pns.getPod()) {
				for(HashMap<String, List<Pod>> pods : clusterMap.values()) {
					if(pods.containsKey(pod.getKey() + ":" + pod.getValue())) {
						pnsSelectedPods.addAll(pods.get(pod.getKey() + ":" + pod.getValue()));
					}
				}
			}
		} else {
			// Normal selection: select specific pods in specific namespaces
			for(KeyValue ns : pns.getNamespace()) {
				// Search namespace and pod in the clusterMap...
				if(clusterMap.containsKey(ns.getKey() + ":" + ns.getValue())) {
					for(KeyValue pod : pns.getPod()) {
						if(clusterMap.get(ns.getKey() + ":" + ns.getValue()).containsKey(pod.getKey() + ":" + pod.getValue())) {
							pnsSelectedPods.addAll(clusterMap.get(ns.getKey() + ":" + ns.getValue()).get(pod.getKey() + ":" + pod.getValue()));
						}
					}
				}
			}
		}
		return pnsSelectedPods;
	}
	/*Convert list of pods to list of Selectors using the HashMap*/
	private static List<PodNamespaceSelector> convertPods(PodNamespaceSelector pns1, ArrayList<Pod> pns1SelectedPods, HashMap<String, HashMap<String, List<Pod>>> clusterMap ){
		List<PodNamespaceSelector> resSelectors = new ArrayList<>();
		for(Pod p: pns1SelectedPods) {
			// Take the namespace from current pod and extract the associated labels in the form "key:label"...
			for(String s :p.getNamespace().getLabels().keySet().stream().map(k -> k + ":" + p.getNamespace().getLabels().get(k)).collect(Collectors.toList())) {
				// ...then iterate also over all the pod's labels
				for(String si: p.getLabels().keySet().stream().map(k -> k + ":" + p.getLabels().get(k)).collect(Collectors.toList())) {
					// ...and check if the combination is present in the HashMap
					if(clusterMap.containsKey(s) && clusterMap.get(s).containsKey(si)) {
						// Check if all elements selected by this combination are in the list of selected pods..
						Boolean flag = true;
						for(Pod pi: clusterMap.get(s).get(si)) {
							if(!pns1SelectedPods.contains(pi)) {
								// If not, then break the loop and go to the next combination
								flag = false;
								break;
							}
						}
						// If the previous check is successful, then add the PodNamespaceSelector to the list of results
						if (flag) {
							PodNamespaceSelector pns = new PodNamespaceSelector();
							KeyValue kv_1 = new KeyValue();
							kv_1.setKey(si.split(":")[0]);
							kv_1.setValue(si.split(":")[1]);
							pns.getPod().add(kv_1);
							KeyValue kv_2 = new KeyValue();
							kv_2.setKey(s.split(":")[0]);
							kv_2.setValue(s.split(":")[1]);
							pns.getNamespace().add(kv_2);
							pns.setIsHostCluster(pns1.isIsHostCluster());
							resSelectors.add(pns);
						}
					}
				}
			}
		}
		return resSelectors;
	}

	/**
	 * Function to compute the difference between two different resourceSelectors.
	 * @param sel_1 is the first resourceSelector.
	 * @param sel_2 is the second resourceSelector.
	 * @return true if there is an overlap
	 *
	 */
	public static boolean computeOverlapResourceSelector(ResourceSelector sel_1, ResourceSelector sel_2) {
		Boolean isCIDR_1 = sel_1 instanceof CIDRSelector;
		Boolean isCIDR_2 = sel_2 instanceof CIDRSelector;

		if (isCIDR_1 && isCIDR_2) {
			CIDRSelector cidr1 = (CIDRSelector) sel_1;
			CIDRSelector cidr2 = (CIDRSelector) sel_2;
			String resCIDRHarmonization = cidrDifference(cidr1.getAddressRange(), cidr2.getAddressRange());
			return resCIDRHarmonization == null;
		} else if (!isCIDR_1 && !isCIDR_2) {
			PodNamespaceSelector pns1 = (PodNamespaceSelector) sel_1;
			PodNamespaceSelector pns2 = (PodNamespaceSelector) sel_2;

			if (pns1.isIsHostCluster() == pns2.isIsHostCluster()) {
				return false;
			}
			String pns1PodKey = pns1.getPod().get(0).getKey();
			String pns1PodValue = pns1.getPod().get(0).getValue();
			String pns2PodKey = pns2.getPod().get(0).getKey();
			String pns2PodValue = pns2.getPod().get(0).getValue();
			String pns1NamespaceKey = pns1.getNamespace().get(0).getKey();
			String pns1NamespaceValue = pns1.getNamespace().get(0).getValue();
			String pns2NamespaceKey = pns2.getNamespace().get(0).getKey();
			String pns2NamespaceValue = pns2.getNamespace().get(0).getValue();

			if(areEqual(pns1NamespaceKey, pns1NamespaceValue, pns2NamespaceKey, pns2NamespaceValue)){
				return areEqual(pns1PodKey, pns1PodValue, pns2PodKey, pns2PodValue);
			}
			else
				return false;
		} else {
			return false;
		}
	}

	public static boolean areEqual(String key1, String value1, String key2, String value2) {

		boolean keysEqual = key1.equals(key2) || "*".equals(key1) || "*".equals(key2);

		boolean valuesEqual = value1.equals(value2) || "*".equals(value1) || "*".equals(value2);

		return keysEqual && valuesEqual;
	}

	public static boolean compareOverlapResourceSelector(ResourceSelector rs_1, ResourceSelector rs_2) {
		boolean found;

		if (rs_1 instanceof PodNamespaceSelector && rs_2 instanceof PodNamespaceSelector) {
			PodNamespaceSelector pns_1 = (PodNamespaceSelector) rs_1;
			PodNamespaceSelector pns_2 = (PodNamespaceSelector) rs_2;


			if (pns_1.getNamespace().size() == 1 && pns_1.getNamespace().get(0).getKey().equals("*") && pns_1.getNamespace().get(0).getValue().equals("*") &&
					pns_1.getPod().size() == 1 && pns_1.getPod().get(0).getKey().equals("*") && pns_1.getPod().get(0).getValue().equals("*")|| pns_2.getNamespace().size() == 1 && pns_2.getNamespace().get(0).getKey().equals("*") && pns_2.getNamespace().get(0).getValue().equals("*") &&
					pns_2.getPod().size() == 1 && pns_2.getPod().get(0).getKey().equals("*") && pns_2.getPod().get(0).getValue().equals("*")) {
				return true;
			}

			if (pns_1.getPod().size() != pns_2.getPod().size() || pns_1.getNamespace().size() != pns_2.getNamespace().size()) {
				return false;
			}

			for (KeyValue kv_1 : pns_1.getPod()) {
				found = false;
				for (KeyValue kv_2 : pns_2.getPod()) {
					if (kv_1.getKey().equals(kv_2.getKey()) && kv_1.getValue().equals(kv_2.getValue())) {
						found = true;
						break;
					}
				}
				if (!found) {
					return false;
				}
			}

			for (KeyValue kv_1 : pns_1.getNamespace()) {
				found = false;
				for (KeyValue kv_2 : pns_2.getNamespace()) {
					if (kv_1.getKey().equals(kv_2.getKey()) && kv_1.getValue().equals(kv_2.getValue())) {
						found = true;
						break;
					}
				}
				if (!found) {
					return false;
				}
			}

			return true;
		} else if (rs_1 instanceof CIDRSelector && rs_2 instanceof CIDRSelector) {
			CIDRSelector cidr_1 = (CIDRSelector) rs_1;
			CIDRSelector cidr_2 = (CIDRSelector) rs_2;
			return cidr_1.getAddressRange().equals(cidr_2.getAddressRange());
		} else {
			return false;
		}
	}

	public static boolean checkWildcard(ResourceSelector rs){
		boolean isCIDR_1 = !rs.getClass().equals(PodNamespaceSelector.class);
		if(isCIDR_1)
			return false;
		else {
			PodNamespaceSelector pns = (PodNamespaceSelector) rs;
			return pns.getPod().get(0).getKey().equals("*") && pns.getPod().get(0).getValue().equals("*");
		}
	}
	public static List<PodNamespaceSelector> computePodNamespaceSelector(ResourceSelector rs,  HashMap<String, HashMap<String, List<Pod>>> clusterMap){
		PodNamespaceSelector pns1 = (PodNamespaceSelector) rs;
		ArrayList<Pod> pnsSelectedPods = new ArrayList<Pod>();
		if(!pns1.getNamespace().get(0).getValue().equals("*")) {
			for (KeyValue ns : pns1.getNamespace()) {
				if (clusterMap.containsKey(ns.getKey() + ":" + ns.getValue())) {
					for (List<Pod> pods : clusterMap.get(ns.getKey() + ":" + ns.getValue()).values()) {
						pnsSelectedPods.addAll(pods);
					}
				}
			}
		}
		else
			pnsSelectedPods.addAll(clusterMap.values().stream().flatMap(it -> it.values().stream()).flatMap(it -> it.stream()).collect(Collectors.toList()));
		List<PodNamespaceSelector> resSelectors = new ArrayList<PodNamespaceSelector>();
		for(Pod p: pnsSelectedPods) {
			// Take the namespace from current pod and extract the associated labels in the form "key:label"...
			for(String s :p.getNamespace().getLabels().keySet().stream().map(k -> k + ":" + p.getNamespace().getLabels().get(k)).collect(Collectors.toList())) {
				// ...then iterate also over all the pod's labels
				for(String si: p.getLabels().keySet().stream().map(k -> k + ":" + p.getLabels().get(k)).collect(Collectors.toList())) {
					// ...and check if the combination is present in the HashMap
					if(clusterMap.containsKey(s) && clusterMap.get(s).containsKey(si)) {
						// Check if all elements selected by this combination are in the list of selected pods..
						Boolean flag = true;
						for(Pod pi: clusterMap.get(s).get(si)) {
							if(!pnsSelectedPods.contains(pi)) {
								// If not, then break the loop and go to the next combination
								flag = false;
								break;
							}
						}
						// If the previous check is successful, then add the PodNamespaceSelector to the list of results
						if (flag) {
							PodNamespaceSelector pns = new PodNamespaceSelector();
							KeyValue kv_1 = new KeyValue();
							kv_1.setKey(si.split(":")[0]);
							kv_1.setValue(si.split(":")[1]);
							pns.getPod().add(kv_1);
							KeyValue kv_2 = new KeyValue();
							kv_2.setKey(s.split(":")[0]);
							kv_2.setValue(s.split(":")[1]);
							pns.getNamespace().add(kv_2);
							pns.setIsHostCluster(pns.isIsHostCluster());
							resSelectors.add(pns);
						}
					}
				}
			}
		}
		return resSelectors;
	}
	/**
	 * Function to convert a CIDR string into an array of two integers: the first is the IP address in numeric format, the second is the length of the network prefix.
	 * @param cidr is the CIDR string to be converted.
	 * @return an array of two integers: the first is the IP address in numeric format, the second is the length of the network prefix.
	 *
	 */
	private static int[] cidrToIp(String cidr) {
		// Split the string into two parts, IP address and prefix.
		if(cidr.equals("*"))
			cidr = "0.0.0.0/0";
		String[] parts = cidr.split("/");
		String ip = parts[0];
		int prefix = Integer.parseInt(parts[1]);
		// Now convert the IP address into a numeric format.
		int ipNum = 0;
		String[] octets = ip.split("\\.");
		for (int i = 0; i < 4; i++) {
			ipNum = ipNum << 8 | Integer.parseInt(octets[i]);
		}
		return new int[] {ipNum, prefix};
	}

	/**
	 * Function to compute the difference between two CIDR ranges.
	 * @param cidr1 is the first CIDR range.
	 * @param cidr2 is the second CIDR range.
	 * @return is the resulting of first CIDR range MINUS second CIDR range (could be multiple ranges separated by a semicolon). If the difference is empty, then the function returns null.
	 */
	private static String cidrDifference(String cidr1, String cidr2) {
		String resString = "";

		// Converts the CIDR strings into two arrays of two integers.
		int[] ip1 = cidrToIp(cidr1);
		int[] ip2 = cidrToIp(cidr2);

		// Compute the subnet mask using right shift.
		int mask1 = (ip1[1]==32)? 0 : -1 >>> ip1[1];
		int mask2 = (ip2[1]==32)? 0 : -1 >>> ip2[1];


		// Compute the network addresses using AND between IP addresses and subnet mask.
		int net1 = ip1[0] & ~mask1;
		int net2 = ip2[0] & ~mask2;

		// Compute broadcast addresses (i.e., largest IP address) for both ranges using OR between network addresses and subnet masks negated.
		int bc1 = ip1[0] | mask1;
		int bc2 = ip2[0] | mask2;

		// If range 1 is completely included in range 2, then the difference is empty.
		if(Integer.compareUnsigned(net1, bc2) <= 0 && Integer.compareUnsigned(net2, bc1) >= 0) {
			//if (net1 <= bc2 && net2 >= bc1) {
			return null;
		}

		// Range 1 is partially (or completely) overlapping with range 2.
		if(Integer.compareUnsigned(net1, bc2) <= 0 && Integer.compareUnsigned(bc1, net2) >= 0) {
			//if(net1 <= bc2 && bc1 >= net2) {
			if(Integer.compareUnsigned(net1, net2) >= 0 && Integer.compareUnsigned(bc1, bc2) <= 0) {
				//if(net1 >= net2 && bc1 <= bc2){
				// range 1 is completely included in range 2...
				return null;
			} else if(Integer.compareUnsigned(net1, net2) <= 0 && Integer.compareUnsigned(bc1, bc2) <= 0) {
				//} else if(net1 <= net2 && bc1 <= bc2){
				// range 1 starts before the range 2 and ends inside the range 2...
				int diff = net2 - net1;
				resString += computeTotalSubnetting(net1, net1 + diff);
				return resString;
			} else if(Integer.compareUnsigned(net1,net2) >= 0 && Integer.compareUnsigned(bc1, bc2) >= 0) {
				//} else if(net1 >= net2 && bc1 >= bc2){
				// range 1 starts inside range 2 and ends after range 2...
				int diff = bc1 - bc2;
				resString += computeTotalSubnetting(bc2 + 1, bc2 + diff);
				return resString;
			} else if(Integer.compareUnsigned(net1, net2) <= 0 && Integer.compareUnsigned(bc1, bc2) >= 0) {
				//} else if(net1 <= net2 && bc1 >= bc2){
				// range 2 is completely included in range 1...
				int diff_1 = net2 - net1;
				int diff_2 = bc1 - bc2;
				resString += computeTotalSubnetting(net1, net1 + diff_1);
				resString += computeTotalSubnetting(bc2 + 1, bc2 + 1 + diff_2);
				return resString;
			}
		}

		// If none of the previous, the two ranges do not overlap!
		return cidr1;
	}

	/**
	 * Function to compute the subnetting of an IP address range.
	 * @param startIP is the first IP address of the range.
	 * @param endIP is the last IP address of the range.
	 * @return one or multiple subnet addresses (in the form of CIDR strings) that cover the whole range - ENDS INCLUDED. They are separated by ";".
	 */
	private static String computeTotalSubnetting(int startIP, int endIP) {
		// Compute the total number of IPs in the range.
		int diff = endIP - startIP + 1;
		// Compute the largest power of 2 that is smaller than the number of IPs.
		int prefix = 1;
		while(Integer.compareUnsigned(prefix, diff) <= 0 //(prefix <= diff)
				&& prefix != 0) {
			prefix *= 2;
		}
		if(prefix != 0) {
			prefix /= 2;
		} else {
			// Handle edge case: overflow of the prefix value
			prefix = 1 << 31;
		}
		// Then the prexif is...
		prefix = 32 - Integer.numberOfTrailingZeros(prefix);
		// Find valid start address (i.e., the first address that is a valid network address for the computed prefix).
		int net;
		if((startIP & (-1 << (32 - prefix))) != startIP){
			// If the startIP is not a valid network address, then find the next valid network address.
			net = (startIP & (-1 << (32 - prefix))) + (1 << (32 - prefix));
		} else {
			// Compute the network address using the AND operator between startIP and the mask.
			net = startIP & (-1 << (32 - prefix));
		}
		// Convert the network address into a string.
		String[] octect = new String[4];
		for(int i = 0; i < 4; i++){
			octect[i] = String.valueOf((net >> (24 - 8 * i)) & 255);
		}
		String netString = octect[0] + "." + octect[1] + "." + octect[2] + "." + octect[3] + "/" + prefix + ";";
		// Now check if there is still some subnetting to do in the higher range...
		int bc = net | ~(-1 << (32 - prefix));
		if(Integer.compareUnsigned(bc, endIP) < 0) { //((bc+1) < endIP)
			netString += computeTotalSubnetting(bc + 1, endIP);
		}
		// ... but also on the lower range (possible if the startIP was changed because not a valid network address)
		if(net != startIP){
			netString += computeTotalSubnetting(startIP, net-1);
		}
		return netString;
	}

	/**
	 * Function to check strict equality between two different resourceSelectors.
	 * @param rs_1 is the first resourceSelector.
	 * @param rs_2 is the second resourceSelector.
	 * @return true if rs_1 and rs_2 are strictly equal, false otherwise.
	 */
	public static boolean compareResourceSelector(ResourceSelector rs_1, ResourceSelector rs_2) {
		boolean found;

		if(rs_1.getClass().equals(PodNamespaceSelector.class) && rs_2.getClass().equals(PodNamespaceSelector.class)){
			PodNamespaceSelector pns_1 = (PodNamespaceSelector) rs_1;
			PodNamespaceSelector pns_2 = (PodNamespaceSelector) rs_2;

			if(pns_1.getPod().size() != pns_2.getPod().size() || pns_1.getNamespace().size() != pns_2.getNamespace().size()){
				return false;
			}

			for(KeyValue kv_1: pns_1.getPod()){
				found = false;
				for(KeyValue kv_2: pns_2.getPod()){
					//if((kv_1.getKey().equals(kv_2.getKey() )|| kv_2.getKey().equals("*")) && (kv_1.getValue().equals(kv_2.getValue()) || kv_2.getValue().equals("*"))){
					if(kv_1.getKey().equals(kv_2.getKey() ) && kv_1.getValue().equals(kv_2.getValue())){
						found = true;
						break;
					}
				}
				if(!found){
					return false;
				}
			}

			for(KeyValue kv_1: pns_1.getNamespace()){
				found = false;
				for(KeyValue kv_2: pns_2.getNamespace()){
					if(kv_1.getKey().equals(kv_2.getKey()) && kv_1.getValue().equals(kv_2.getValue())){
						//if((kv_1.getKey().equals(kv_2.getKey()) || kv_1.getKey().equals("*"))  && (kv_1.getValue().equals(kv_2.getValue()) || kv_1.getValue().equals("*"))){
						found = true;
						break;
					}
				}
				if(!found){
					return false;
				}
			}

			return true;
		} else if(rs_1.getClass().equals(CIDRSelector.class) && rs_2.getClass().equals(CIDRSelector.class)){
			CIDRSelector cidr_1 = (CIDRSelector) rs_1;
			CIDRSelector cidr_2 = (CIDRSelector) rs_2;
			return cidr_1.getAddressRange().equals(cidr_2.getAddressRange());
		} else {
			return false;
		}
	}
	/**
	 * Function to create a deep copy of a ConfigurationRule.
	 * @param it the ConfigurationRule to be copied.
	 * @return the deep copy of the ConfigurationRule.
	 */
	public static ConfigurationRule deepCopyConfigurationRule(ConfigurationRule it) {
		ConfigurationRule res = new ConfigurationRule();
		res.setName(it.getName());
		res.setIsCNF(it.isIsCNF());
		res.getHSPL().addAll(it.getHSPL());
		res.setExternalData(it.getExternalData());
		res.setConfigurationRuleAction(it.getConfigurationRuleAction());
		res.setConfigurationCondition(deepCopyKubernetesNetworkFilterConfigurationCondition((KubernetesNetworkFilteringCondition) it.getConfigurationCondition()));
		return res;
	}

	/**
	 * Function to create a deep copy of a KubernetesNetworkFilteringCondition.
	 * @param it the KubernetesNetworkFilteringCondition to be copied.
	 * @return the deep copy of the KubernetesNetworkFilteringCondition.
	 */
	private static KubernetesNetworkFilteringCondition deepCopyKubernetesNetworkFilterConfigurationCondition(KubernetesNetworkFilteringCondition it) {
		KubernetesNetworkFilteringCondition k = new KubernetesNetworkFilteringCondition();
		k.setIsCNF(it.isIsCNF());
		k.setProtocolType(it.getProtocolType());
		k.setDestination(it.getDestination());
		k.setSource(it.getSource());
		k.setDestinationPort(it.getDestinationPort());
		k.setSourcePort(it.getSourcePort());
		return k;
	}


	/**
	 * Function to deepCopy a ConfigurationRule and inverting the source and destination "isHostCluster" flags.
	 * @param cr is the ConfigurationRule.
	 * @return the resulting ConfigurationRule.
	 */
	public static ConfigurationRule deepCopyConfigurationAndInvertVCluster(ConfigurationRule cr) {
		ConfigurationRule cr_inverse = deepCopyConfigurationRule(cr);

		KubernetesNetworkFilteringCondition cr_inverse_cond = (KubernetesNetworkFilteringCondition) cr_inverse.getConfigurationCondition();
		// Invert the source and destination "isHostCluster" flags.

		cr_inverse_cond.getSource().setIsHostCluster(!cr_inverse_cond.getSource().isIsHostCluster());
		cr_inverse_cond.getDestination().setIsHostCluster(!cr_inverse_cond.getDestination().isIsHostCluster());

		return cr_inverse;
	}
}
