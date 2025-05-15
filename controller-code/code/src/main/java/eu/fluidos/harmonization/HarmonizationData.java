package eu.fluidos.harmonization;

import eu.fluidos.*;
import eu.fluidos.jaxb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class HarmonizationData {

	private static final Logger loggerInfo = LogManager.getLogger("harmonization");
	private final Scanner scan = new Scanner(System.in);
	public boolean verify(RequestIntents requestIntent,
                                 AuthorizationIntents authIntent,
                                 HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer,
                                 HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider) {

		//System.out.println(/*Main.ANSI_PURPLE*/ + "[VERIFY] " + /*Main.ANSI_RESET*/ + "Process started...");
		System.out.println("[VERIFY] Process started...");
		//printDash();
		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();

			/*System.out.println(Main.ANSI_BLUE +"[Request Intent]" + Main.ANSI_RESET);
			System.out.print(" (*) " + cr.getName() + " - ");
			System.out.print(Utils.kubernetesNetworkFilteringConditionToString(cond) + "\n");*/

			if(!verifyConfigurationRule(cr, authIntent.getForbiddenConnectionList(),
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider)){

				/* System.out.print(" (*) " + cr.getName() + " - ");
				System.out.print(Utils.kubernetesNetworkFilteringConditionToString(cond) + "\n");*/
				return false;
			}
		}
			return true;
	}

	/* Se almeno uno dei campi non ha sovrapposizione, non ci può essere sovrapposizione -> true */
	private boolean verifyConfigurationRule(ConfigurationRule cr, List<ConfigurationRule> connList, HashMap<String, HashMap<String, List<Pod>>> map_conn, HashMap<String, HashMap<String, List<Pod>>> map_connList){
		// Create a deep copy of the current ConfigurationRule to be modified and
		// eventually added in the resulting list.
		ConfigurationRule res = HarmonizationUtils.deepCopyConfigurationRule(cr);
		// Extract and cast the condition.
		KubernetesNetworkFilteringCondition resCond = (KubernetesNetworkFilteringCondition) res
				.getConfigurationCondition();

		// Loop over the forbiddenConnectionList.
		for (ConfigurationRule confRule : connList) {
			boolean overlap;
			boolean overlapDstPort;
			boolean overlapSrc;
			boolean overlapDst;

			KubernetesNetworkFilteringCondition tmp = (KubernetesNetworkFilteringCondition) confRule
					.getConfigurationCondition();

			/*System.out.println(Main.ANSI_BLUE +"[Authorization Intent]" +Main.ANSI_RESET);
			System.out.print(" (*) " + confRule.getName() + " - ");
			System.out.print(Utils.kubernetesNetworkFilteringConditionToString(tmp) + "\n"); */

			loggerInfo.debug("[VERIFY] - processing rule "
					+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + " vs. "
					+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(tmp));

			// Step-1.1: starts with the simplest case, that is protocol type. Detect if protocol types of res are overlapping with tmp.
			overlap = HarmonizationUtils.computeVerifyProtocolType(resCond.getProtocolType().value(),
					tmp.getProtocolType().value());

			// Step-1.2: check the ports. Detect if the port ranges of res are overlapping with tmp.
			overlapDstPort = HarmonizationUtils.computeVerifiedPortRange(resCond.getDestinationPort(), tmp.getDestinationPort());

			// Step-1.3: check the source and destination.s

			overlapSrc = HarmonizationUtils.computeOverlapResourceSelector(resCond.getSource(),
					tmp.getSource());


			overlapDst = HarmonizationUtils.computeOverlapResourceSelector(resCond.getDestination(),
					tmp.getDestination());


			if(overlap && overlapSrc && overlapDst && overlapDstPort) {
				//System.out.println("Overlap between these two intents: " );
				//printDash();
				//System.out.print(" (*) " + res.getName() + " - ");
                //System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + "\n");
				//System.out.print(" (*) " + confRule.getName() + " - ");
				//System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(tmp) + "\n");
				System.out.println("[VERIFY] - Found discordance between intents. ");
				return false;
			}
			/* else{
				System.out.println(Main.ANSI_GREEN +"-No overlap with this authorization intent-" +Main.ANSI_RESET);
			}*/
		}
		return true;
	}

	/**
	 * Discordances of Type-1 happens when the Requested Intents of the consumer are not all authorized by the provider.
	 * This function gets all the consumer.Requested connections and perform the set operation:
	 * 		(consumer.Requested) - (provider.AuthorizationIntents.deniedConnectionsList)
	 * i.e., remove from the requested connections those overlapping with the forbidden ones.
	 */

	public List<ConfigurationRule> solveTypeOneDiscordances(RequestIntents requestIntent,
                                                            AuthorizationIntents authIntent,
                                                            HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer,
                                                            HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider) {
		//printDash();
		//System.out.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + " Resolution of " + Main.ANSI_YELLOW
		//		+ "TYPE-1 DISCORDANCES" + Main.ANSI_RESET + /*
		// * " = when Requested intents (CONSUMER) are not authorized by the Authorization intents (PROVIDER)"
		//*/"...press ENTER to continue.");

		//printDash();
		//scan.nextLine();
															
		List<ConfigurationRule> harmonizedRules = new ArrayList<>();

		// External loop over the consumer's Request Intents.
		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			//System.out.println("Interno for tipo 1");
			/*System.out.println("[harmonization/harmonizeForbiddenConnectionIntent] - processing rule { [" + cr.getName()
			+ "]" + HarmonizationUtils.kubernetesNetworkFilteringConditionToString(
			(KubernetesNetworkFilteringCondition) cr.getConfigurationCondition())
			+ "}");*/
			loggerInfo.debug("[harmonization/harmonizeForbiddenConnectionIntent] - processing rule { [" + cr.getName()
					+ "]" + HarmonizationUtils.kubernetesNetworkFilteringConditionToString(
					(KubernetesNetworkFilteringCondition) cr.getConfigurationCondition())
					+ "}");

			harmonizedRules.addAll(harmonizeConfigurationRule(cr, authIntent.getForbiddenConnectionList(),
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider));
		}

		//printHarmonizedRules(harmonizedRules, "harmonized REQUEST intents", " after type-1 discordances resolution:");
		return harmonizedRules;

	}

	/**
	 * Discordances of Type-2 happens when the "mandatoryConnectionList" of the provider is not completely satisfied by the consumer.
	 * If this happens, additional rules are added to the Harmonized-Request set of the consumer.
	 */

	public List<ConfigurationRule> solverTypeTwoDiscordances(List<ConfigurationRule> harmonizedRequestConsumerRules,
                                                                    RequestIntents requestIntent, AuthorizationIntents authIntent,
                                                                    HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider,
                                                                    HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer) {
		List<ConfigurationRule> harmonizedRules = new ArrayList<>();
		//printDash();
		//System.out.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + " Resolution of " + Main.ANSI_YELLOW
		//		+ "TYPE-2 DISCORDANCES" + Main.ANSI_RESET
		//		+ /*
		// * " = when elements in the \"MandatoryConnectionsList\" (PROVIDER) are not
		// * completely satisfied by the set of Request Intents (CONSUMER)
		// */"...press ENTER to continue.");

		//printDash();
		//scan.nextLine();

		/* Monitoring */
		if (!requestIntent.isAcceptMonitoring())
			return null;
		else {
			harmonizedRules.addAll(harmonizedRequestConsumerRules);
			/*
			 * Like TYPE-1 but computing the difference between the mandatoryConnectionList
			 * of the provider and the harmonizedRequestIntents of the consumer. Then, the
			 * resulting harmonizedRequestIntents = harmonizedRequestIntents +
			 * (mandatoryConnectionList - harmonizedRequestIntents).
			 */
			for (ConfigurationRule cr_provider : authIntent.getMandatoryConnectionList()) {
				KubernetesNetworkFilteringCondition tmp1 = (KubernetesNetworkFilteringCondition) cr_provider
						.getConfigurationCondition();
				/*System.out.println(Main.ANSI_BLUE + cr_provider.getName()
						+ "]" + Utils.kubernetesNetworkFilteringConditionToString(
						(KubernetesNetworkFilteringCondition) cr_provider.getConfigurationCondition())
						+ "}"+ Main.ANSI_RESET);*/
				List<ConfigurationRule> tmp = harmonizeConfigurationRule(cr_provider, harmonizedRules,
						podsByNamespaceAndLabelsProvider, podsByNamespaceAndLabelsConsumer);
				for (ConfigurationRule cr : tmp)
					harmonizedRules.add(HarmonizationUtils.deepCopyConfigurationAndInvertVCluster(cr));
			}
			//printHarmonizedRules(harmonizedRules, "harmonized CONSUMER intents"," after type-2 discordances resolution:");
			return harmonizedRules;
		}
	}

	/**
	 * Discordances of Type-3 happens when the Requested intents of the consumer, already AUTHORIZED by the provider, do not have a corresponding rule in the hosting cluster.
	 * If this happens, additional rules are added to the harmonized-Request set of the provider in order to create the "hole".
	 */

	public List<ConfigurationRule> solverTypeThreeDiscordances(List<ConfigurationRule> harmonizedRequestConsumerRules,
                                                                      RequestIntents requestIntent, HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer,
                                                                      HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider) {
		List<ConfigurationRule> harmonizedRules = new ArrayList<>();
		//printDash();
		//System.out.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + " Resolution of " + Main.ANSI_YELLOW
		//		+ "TYPE-3 DISCORDANCES" + Main.ANSI_RESET + /*
		// * " = when elements in the Requested set of intents (CONSUMER), that have already been authorized, don't have a specular intent on the PROVIDER's Requested set (which is needed to open the \"hole\"
		// * in the protected border)
		// */"...press ENTER to continue.");
		//printDash();
		//scan.nextLine();

		harmonizedRules.addAll(requestIntent.getConfigurationRule());
		/*
		 * Loops over each elements of the harmonized Request intents of the Consumer
		 * and checks if the same connection is opened also in the Provider side. To do
		 * so, it computes the set difference between a single (consumer) Request and
		 * the current list of (provider) Request(s).
		 */
		for (ConfigurationRule cr_cons : harmonizedRequestConsumerRules) {
//			ConfigurationRule cr_inverted = Utils.deepCopyConfigurationRule(cr_cons);
			List<ConfigurationRule> tmp = harmonizeConfigurationRule(cr_cons, harmonizedRules,
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider);
			// If there are "harmonized" connections that are not already in the provider's
			// set, here they are reversed and added to it.
			for (ConfigurationRule cr : tmp) {
				ConfigurationRule crInverted = HarmonizationUtils.deepCopyConfigurationAndInvertVCluster(cr);
				harmonizedRules.add(crInverted);
			}
		}

		//printHarmonizedRules(harmonizedRules, "harmonized CONSUMER intents"," after type-3 discordances resolution:");

		return harmonizedRules;
	}

	private List<ConfigurationRule> harmonizeConfigurationRule(ConfigurationRule conn, List<ConfigurationRule> connList,
                                                                      HashMap<String, HashMap<String, List<Pod>>> map_conn,
                                                                      HashMap<String, HashMap<String, List<Pod>>> map_connList) {
		// Initialize the resulting list.
		List<ConfigurationRule> resList = new ArrayList<>();
		// Create a deep copy of the current ConfigurationRule to be modified and
		// eventually added in the resulting list.
		ConfigurationRule res = HarmonizationUtils.deepCopyConfigurationRule(conn);

		// Extract and cast the condition.
		KubernetesNetworkFilteringCondition resCond = (KubernetesNetworkFilteringCondition) res.getConfigurationCondition();
		KubernetesNetworkFilteringCondition tmp1 = (KubernetesNetworkFilteringCondition) conn
				.getConfigurationCondition();
		int flag = 0;
		boolean dirty = false;
		// Loop over the forbiddenConnectionList.
		

		for (ConfigurationRule confRule : connList) {
			//System.out.println("harmConfRule for");
			flag = 0;
			KubernetesNetworkFilteringCondition tmp = (KubernetesNetworkFilteringCondition) confRule
			.getConfigurationCondition();

			/*System.out.println("[harmonization/harmonizeForbiddenConnectionIntent] - processing rule "
			+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + " vs. "
			+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(tmp)
			);*/
			/*System.out.println("[harmonization/harmonizeForbiddenConnectionIntent] - processing rule "
			+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + " vs. "
			+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(tmp));*/


			/*loggerInfo.debug("[harmonization/harmonizeForbiddenConnectionIntent] - processing rule "
					+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + " vs. "
					+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(tmp));
*/


			// Step-1.1: starts with the simplest case, that is protocol type. Detect if
			// protocol types of res are overlapping with tmp.


			//System.out.println("-Step-1.1");

			String[] protocolList = HarmonizationUtils.computeHarmonizedProtocolType(resCond.getProtocolType().value(),
					tmp.getProtocolType().value());

			if (protocolList.length == 1 && protocolList[0].equals(resCond.getProtocolType().value())) {
				// No overlap with the current rule (tmp), continue and check next one.
				//System.out.println(" No overlap with the current rule (tmp), continue and check next one ");
				continue;
			}

			// Step-1.2: check the ports. Detect if the port ranges of res are overlapping
			// with tmp.

			//System.out.println("-Step-1.2");

			String[] destinationPortList = HarmonizationUtils
					.computeHarmonizedPortRange(resCond.getDestinationPort(), tmp.getDestinationPort()).split(";");


			if(destinationPortList[0].equals(resCond.getDestinationPort())){
				// No overlap with the current rule (tmp), continue and check next one.
				//System.out.println("No overlap with the current rule (tmp), continue and check next one");
				continue;
			}


			//Step-1.3: check the source and destination.s
			PodNamespaceSelector pns1 = (PodNamespaceSelector) resCond.getSource();
			PodNamespaceSelector pns2 = (PodNamespaceSelector) tmp.getSource();
			//System.out.println("-Step-1.3");

			List<ResourceSelector> source = HarmonizationUtils.computeHarmonizedResourceSelector(resCond.getSource(),
					tmp.getSource(), map_conn, map_connList);

			if (source == null) {
				// There was a comparison problem...likely trying to perform a CIDR/PodNamespace
				// comparison
				//System.out.println(" There was a comparison problem...likely trying to perform a CIDR/PodNamespace comparison ");
				continue;
			}

			if (!source.isEmpty()) {
				boolean result = HarmonizationUtils.compareResourceSelector(source.get(0), resCond.getSource());
				// No overlap with the current authorization rule (tmp), continue and check next
				// one.
				if(result) {
					//System.out.println(" No overlap with the current authorization rule (tmp), continue and check next one ");
					continue;
				}
			}

			List<ResourceSelector> destination = HarmonizationUtils.computeHarmonizedResourceSelector(resCond.getDestination(),
					tmp.getDestination(), map_conn, map_connList);

			if (destination == null) {
				// There was a comparison problem...likely trying to perform a CIDR/PodNamespace
				// comparison
				//System.out.println(" There was a comparison problem...likely trying to perform a CIDR/PodNamespace comparison ");
				continue;
			}

			if (!destination.isEmpty()
					&& HarmonizationUtils.compareResourceSelector(destination.get(0), resCond.getDestination())) {
				// No overlap with the current authorization rule (tmp), continue and check next
				// one.
				//System.out.println(" No overlap with the current authorization rule (tmp), continue and check next ");
				continue;
			}
			//if(!destination.isEmpty())
				//System.out.println("resourceSelector" + destination.get(0) + " " + resCond.getDestination());
			// Step-2: if this point is reached, both source, sourcePort, destination, and
			// destinationPort have an overlap (partial or complete) with the current
			// authorization rule.
			dirty = true;

			// Step-2.1: handle the overlap with the sourcePort and destinationPort fields.
			// Note that if there is a partial overlap, the port range could be broken into
			// two ranges (e.g., if overlap is in the middle of the interval).

			//System.out.println("-Step-2");
			// Repeat the process for the destinationPort range.
			if (destinationPortList[0].isEmpty()) {
				//System.out.println("destinationPortList[0] is empty");
				flag++;
			} else if (destinationPortList.length > 1) {
				//System.out.println("Destination - Partial overlap causing the port range to be broke into two ranges");
				ConfigurationRule res2 = addHarmonizedRules(res, resCond, destinationPortList[1], loggerInfo,
						"destinationPort", null);
				resList.addAll(harmonizeConfigurationRule(res2, connList, map_conn, map_connList));

				ConfigurationRule res3 = addHarmonizedRules(res, resCond, destinationPortList[0], loggerInfo,
						"destinationPort", null);
				resList.addAll(harmonizeConfigurationRule(res3, connList, map_conn, map_connList));

			} else {
				//System.out.println("Destination - Partial overlap, but no need to break the port range into two.");
				ConfigurationRule res3 = addHarmonizedRules(res, resCond, destinationPortList[0], loggerInfo,
						"destinationPort", null);
				KubernetesNetworkFilteringCondition k = (KubernetesNetworkFilteringCondition) res3
						.getConfigurationCondition();
				resList.addAll(harmonizeConfigurationRule(res3, connList, map_conn, map_connList));
			}

			// Step-2.2: handle the overlap with the protocol type field. Also in this case,
			// it could be that the overlap is partial and the result is a list of protocol
			// types (max size 2 WITH CURRENT VALUES).

			//System.out.println("-Step-2.2");
			if (protocolList.length == 0) {
				// If the protocol list is empty, it means that it is not possible to harmonize
				// the current intent (i.e., protocol type is included in the authorization
				// rule's one)... just update the flag for the moment.
				//System.out.println("protocolList is empty");
				flag++;
			} else if (protocolList.length > 1) {

				ConfigurationRule res1 = addHarmonizedRules(res, resCond, protocolList[1], loggerInfo,
						"transportProtocol", null);
				resList.addAll(harmonizeConfigurationRule(res1, connList, map_conn, map_connList));

				ConfigurationRule res2 = addHarmonizedRules(res, resCond, protocolList[0], loggerInfo,
						"transportProtocol", null);
				resList.addAll(harmonizeConfigurationRule(res2, connList, map_conn, map_connList));

			} else {
				ConfigurationRule res2 = addHarmonizedRules(res, resCond, protocolList[0], loggerInfo,
						"transportProtocol", null);
				resList.addAll(harmonizeConfigurationRule(res2, connList, map_conn, map_connList));
			}

			// Step-2.3: solve possible problems with the source and destination selectors.

			//System.out.println("-Step-2.3");

			if (source.isEmpty()) {
				// This means that it was not possible to harmonized current intent.
			} else {
				for (ResourceSelector rs : source) {
					ConfigurationRule res1 = addHarmonizedRules(res, resCond, "", loggerInfo, "sourceSelector", rs);
					resList.addAll(harmonizeConfigurationRule(res1, connList, map_conn, map_connList));
				}
			}

			if (destination.isEmpty()) {

			} else {
				for (ResourceSelector rs : destination) {
					ConfigurationRule res1 = addHarmonizedRules(res, resCond, "", loggerInfo, "destinationSelector",
							rs);
					resList.addAll(harmonizeConfigurationRule(res1, connList, map_conn, map_connList));
				}
			}

			// Step-3:
			// In this case, it either had complete overlap with all fields (i.e., the
			// connection is "fully denied") or partial overlap with all the field and the
			// recursive iterations with non-overlapping components have been issued.
			// In the end, whatever is the specific case, current request should be
			// discarded and stop comparing it versus other rules.

			//System.out.println("-Step-3");
			loggerInfo.debug(Main.ANSI_RED + "[harmonization/harmonizeForbiddenConnectionIntent] - complete or partial overlap found, current rule is removed {{}" + Main.ANSI_RESET + "}", HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));
			//System.out.println(Main.ANSI_RED + "[harmonization/harmonizeForbiddenConnectionIntent] - complete or partial overlap found, current rule is removed {{}" + Main.ANSI_RESET + "}" + HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));
			return resList;
		}

		// If all the rules into ForbiddenConnectionList have been processed, add the
		// current intent to the list and return it.

		loggerInfo.debug(Main.ANSI_GREEN + "[harmonization/harmonizeForbiddenConnectionIntent] - no overlap was found, current rule is added to HARMONIZED set {{}" + Main.ANSI_RESET + "}", HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));
		//System.out.println(Main.ANSI_GREEN + "[harmonization/harmonizeForbiddenConnectionIntent] - no overlap was found, current rule is added to HARMONIZED set {{}" + Main.ANSI_RESET + "}" + HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));

		resList.add(res);
		return resList;
	}

	private ConfigurationRule addHarmonizedRules(ConfigurationRule res, KubernetesNetworkFilteringCondition resCond,
                                                        String protocolList, Logger loggerInfo, String overlap, ResourceSelector rs) {
		ConfigurationRule res1 = HarmonizationUtils.deepCopyConfigurationRule(res);
		res1.setName(res.getName().split("-")[0] + "-HARMONIZED");
		KubernetesNetworkFilteringCondition resCond1 = (KubernetesNetworkFilteringCondition) res1
				.getConfigurationCondition();

		if (Objects.equals(overlap, "destinationPort"))
			resCond1.setDestinationPort(protocolList);
		else if (Objects.equals(overlap, "transportProtocol"))
			resCond1.setProtocolType(ProtocolType.fromValue(protocolList));
		else if (Objects.equals(overlap, "sourceSelector")) {
			resCond1.setSource(rs);
		}
		else if (Objects.equals(overlap, "destinationSelector"))
			resCond1.setDestination(rs);
		loggerInfo.debug("[harmonization/harmonizeForbiddenConnectionIntent] - found overlap with " + overlap + " {"
				+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + "} --> {"
				+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond1) + "}");
		/*System.out.println("[harmonization/harmonizeForbiddenConnectionIntent] - found overlap with " + overlap + " {"
				+ Utils.kubernetesNetworkFilteringConditionToString(resCond) + "} --> {"
				+ Utils.kubernetesNetworkFilteringConditionToString(resCond1) + "}");*/
		return res1;
	}

	public void printDash() {
		System.out.println(Main.ANSI_PURPLE + "-".repeat(100) + Main.ANSI_RESET);
	}

	public void printAuth() {
		System.out
				.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + "Local cluster defined the following "
						+ Main.ANSI_YELLOW + "Authorization Intents" + Main.ANSI_RESET + " (PROVIDER):");
	}

	public void printRequestIntents(RequestIntents requestIntent, String cluster) {
		if (requestIntent == null){
			System.out.println("Errore, request intent è null");
			return;
		}
		if (cluster == "consumer") {
			System.out.println("[+] Request intents (CONSUMER):");
			//System.out.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + "Received the following "
			//		+ Main.ANSI_YELLOW + "Request intents" + Main.ANSI_RESET + " (CONSUMER):");
			System.out.println("  (*) [AcceptMonitoring]: " + requestIntent.isAcceptMonitoring());
		} else if (cluster == "provider") {
			//System.out.println(
			//		Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + "Local cluster defined the following "
			//				+ Main.ANSI_YELLOW + "Request intents" + Main.ANSI_RESET + " (PROVIDER):");
			if(requestIntent.isAcceptMonitoring() != false)
				System.out.println("  (*) [RequestMonitoring]: " + requestIntent.isAcceptMonitoring());
		}

		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("  (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond));
			System.out.println(cond.getSource().isIsHostCluster() +" " + cond.getDestination().isIsHostCluster());
		}
	}

	public void printAuthorizationIntents(AuthorizationIntents authorizationIntent) {
		List<ConfigurationRule> forbiddenRule = null;
		List<ConfigurationRule> mandatoryRule = null;

		//System.out.print("   .-> " + /*Main.ANSI_YELLOW*/ + "ForbiddenConnectionList" + /*Main.ANSI_RESET*/ + ":\n");
		System.out.print("   .-> ForbiddenConnectionList:\n");
		forbiddenRule= authorizationIntent.getForbiddenConnectionList();

		for (ConfigurationRule cr : forbiddenRule) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("   | (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond));
			System.out.println(cond.getSource().isIsHostCluster() +" " + cond.getDestination().isIsHostCluster());
		}
		//System.out.print("   .-> " + /*Main.ANSI_YELLOW*/ + "MandatoryConnectionList" + /*Main.ANSI_RESET*/ + ":\n");
		System.out.print("   .-> MandatoryConnectionList:\n");
		mandatoryRule = authorizationIntent.getMandatoryConnectionList();

		for (ConfigurationRule cr : mandatoryRule) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("   | (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond) );
			System.out.println(cond.getSource().isIsHostCluster() +" " + cond.getDestination().isIsHostCluster());
		}
	}

	public void printHarmonizedRules(List<ConfigurationRule> harmonizedRules, String intents, String discordances) {
		System.out.println(Main.ANSI_PURPLE + "-".repeat(100) + Main.ANSI_RESET);
		System.out.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + "List of " + Main.ANSI_YELLOW
				+ intents + Main.ANSI_RESET + discordances);
		for (ConfigurationRule cr : harmonizedRules) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("   (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond) + "\n");
		}
		System.out.println(Main.ANSI_PURPLE + "-".repeat(100) + Main.ANSI_RESET);
	}

}
