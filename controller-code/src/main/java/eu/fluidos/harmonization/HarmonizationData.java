package eu.fluidos.harmonization;

import eu.fluidos.*;
import eu.fluidos.jaxb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class HarmonizationData {

	private static final Logger loggerInfo = LogManager.getLogger("harmonization");

	/** LEGACY METHOD
	 * This LEGACY method verifies if the requested intents of the consumer are compatible
	 * with the authorization intents of the provider.
	 * It checks for overlaps between the requested configuration rules and the
	 * forbidden connection list.
	 *
	 * @param requestIntent                  The requested intents from the consumer.
	 * @param authIntent                     The authorization intents from the provider.
	 * @param podsByNamespaceAndLabelsConsumer The map of pods by namespace and labels for the consumer.
	 * @param podsByNamespaceAndLabelsProvider The map of pods by namespace and labels for the provider.
	 * @return true if request and authorization intents are compatible, false otherwise.
	 
	
	 public boolean verify(RequestIntents requestIntent,
			AuthorizationIntents authIntent,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider) {
		
		System.out.println("[VERIFY] Process started...");
		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			if (!verifyConfigurationRule(cr, authIntent.getForbiddenConnectionList(),
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider)) {
				return false;
			}
		}
		return true;
	}
	*/


	/**
	 * This method verifies if the requested intents of the consumer are compatible
	 * with the authorization intents of the provider.
	 * It checks for overlaps between the requested configuration rules and the
	 * forbidden connection list.
	 *
	 * @param requestIntent                  The requested intents from the consumer.
	 * @param authIntent                     The authorization intents from the provider.
	 * @return true if request and authorization intents are compatible, false otherwise.
	 */

	 public boolean verify(RequestIntents requestIntent, AuthorizationIntents authIntent) {
//		System.out.println("[VERIFY] Process started...");
		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			if (!verifyConfigurationRule(cr, authIntent.getForbiddenConnectionList())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method verifies if a single configuration rule overlaps with any of the
	 * connection rules in the provided list.
	 * It checks for overlaps in protocol type, port ranges, source, and destination
	 * selectors.
	 *
	 * @param configRule          The configuration rule to verify.
	 * @param connList    The list of connection rules to check against.
	 * @param map_conn    The map of pods by namespace and labels for the consumer.
	 * @param map_connList The map of pods by namespace and labels for the provider.
	 * @return true if there is no overlap, false otherwise.
	 */
	private boolean verifyConfigurationRule(ConfigurationRule configRule, List<ConfigurationRule> connList) {
				
		ConfigurationRule res = HarmonizationUtils.deepCopyConfigurationRule(configRule);
		KubernetesNetworkFilteringCondition resCond = (KubernetesNetworkFilteringCondition) res.getConfigurationCondition();
		for (ConfigurationRule confRule : connList) {
			boolean overlap;
			boolean overlapDstPort;
			boolean overlapSrc;
			boolean overlapDst;

			KubernetesNetworkFilteringCondition tmpCond = (KubernetesNetworkFilteringCondition) confRule
					.getConfigurationCondition();

			// loggerInfo.debug("[VERIFY] - processing rule "
			// 		+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + " vs. "
			// 		+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(tmpCond));

			// Step-1: starts with protocol type. Detect if protocol types of res are overlapping with tmpCond.
			overlap = HarmonizationUtils.verifyProtocolType(resCond.getProtocolType().value(),
					tmpCond.getProtocolType().value());

			// Step-2: check the ports. Detect if the port ranges of res are overlapping with tmpCond.
			overlapDstPort = HarmonizationUtils.verifyPortRange(resCond.getDestinationPort(),
					tmpCond.getDestinationPort());

			// Step-3: check the source and destination.
			overlapSrc = HarmonizationUtils.verifyResourceSelector(resCond.getSource(),
					tmpCond.getSource());
			overlapDst = HarmonizationUtils.verifyResourceSelector(resCond.getDestination(),
					tmpCond.getDestination());

			// Step-4: to have overlap, all the previous checks must be true.
			if (overlap && overlapSrc && overlapDst && overlapDstPort) {
				// System.out.println("[VERIFY] - Found discordance between intents. ");
				return false;
			}
		}
		// If all the rules into connList have been processed, it means that cr has no overlap with conn
		return true;
	}

	/**
	 * Discordances of Type-1 happens when the Requested Intents of the consumer are
	 * not all authorized by the provider.
	 * This function gets all the consumer.Requested connections and perform the set
	 * operation: (consumer.Requested) - (provider.AuthorizationIntents.deniedConnectionsList)
	 *
	 * @param requestIntent                  The requested intents from the consumer.
	 * @param authIntent                     The authorization intents from the provider.
	 * @param podsByNamespaceAndLabelsConsumer The map of pods by namespace and labels for the consumer.
	 * @param podsByNamespaceAndLabelsProvider The map of pods by namespace and labels for the provider.
	 * @return A list of harmonized configuration rules that represent the harmonized request intents of the consumer.
	 */
	public List<ConfigurationRule> solveTypeOneDiscordances(RequestIntents requestIntent,
			AuthorizationIntents authIntent,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider) {
		List<ConfigurationRule> harmonizedRules = new ArrayList<>();

		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			loggerInfo.debug("[harmonization/harmonizeForbiddenConnectionIntent] - processing rule { [" + cr.getName()
					+ "]" + HarmonizationUtils.kubernetesNetworkFilteringConditionToString(
							(KubernetesNetworkFilteringCondition) cr.getConfigurationCondition())
					+ "}");

			harmonizedRules.addAll(harmonizeConfigurationRule(cr, authIntent.getForbiddenConnectionList(),
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider, 0));
		}
		return harmonizedRules;
	}

	/**
	 * Discordances of Type-2 happens when the "mandatoryConnectionList" of the
	 * provider is not completely satisfied by the consumer.
	 * If this happens, additional rules are added to the Harmonized-Request set of
	 * the consumer.
	 * Similar to Type-1, but in this case the harmonization corresponds to the
	 * operation harmonizedRequestIntents + (mandatoryConnectionList - harmonizedRequestIntents). 
	 * 
	 * @param harmonizedRequestConsumerRules The harmonized request rules of the consumer (result of Type-1).
	 * @param requestIntent                  The requested intents from the consumer.
	 * @param authIntent                     The authorization intents from the provider.
	 * @param podsByNamespaceAndLabelsProvider The map of pods by namespace and labels for the provider.
	 * @param podsByNamespaceAndLabelsConsumer The map of pods by namespace and labels for the consumer.
	 * @return A list of harmonized configuration rules that represent the harmonized request intents of the consumer.
	 */
	public List<ConfigurationRule> solverTypeTwoDiscordances(List<ConfigurationRule> harmonizedRequestConsumerRules,
			RequestIntents requestIntent, AuthorizationIntents authIntent,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer) {
		List<ConfigurationRule> harmonizedRules = new ArrayList<>();

		if (!requestIntent.isAcceptMonitoring())
			return null;
		else {
			harmonizedRules.addAll(harmonizedRequestConsumerRules);
			for (ConfigurationRule cr_provider : authIntent.getMandatoryConnectionList()) {
				//KubernetesNetworkFilteringCondition tmp1 = (KubernetesNetworkFilteringCondition) cr_provider.getConfigurationCondition();
				List<ConfigurationRule> tmp = harmonizeConfigurationRule(cr_provider, harmonizedRules,
						podsByNamespaceAndLabelsProvider, podsByNamespaceAndLabelsConsumer, 0);
				for (ConfigurationRule cr : tmp)
					harmonizedRules.add(HarmonizationUtils.deepCopyConfigurationAndInvertVCluster(cr));
			}
			return harmonizedRules;
		}
	}

	/**
	 * Discordances of Type-3 happens when the Requested intents of the consumer,
	 * already AUTHORIZED by the provider, do not have a corresponding rule in the
	 * hosting cluster.
	 * If this happens, additional rules are added to the harmonized-Request set of
	 * the provider in order to create the "hole".
	 * 
	 */
	public List<ConfigurationRule> solverTypeThreeDiscordances(List<ConfigurationRule> harmonizedRequestConsumerRules,
			RequestIntents requestIntent, HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsConsumer,
			HashMap<String, HashMap<String, List<Pod>>> podsByNamespaceAndLabelsProvider) {
		List<ConfigurationRule> harmonizedRules = new ArrayList<>();

		harmonizedRules.addAll(requestIntent.getConfigurationRule());
		/*
		 * Loops over each elements of the harmonized Request intents of the Consumer
		 * and checks if the same connection is opened also in the Provider side. To do
		 * so, it computes the set difference between a single (consumer) Request and
		 * the current list of (provider) Request(s).
		 */
		for (ConfigurationRule cr_cons : harmonizedRequestConsumerRules) {
			List<ConfigurationRule> tmp = harmonizeConfigurationRule(cr_cons, harmonizedRules,
					podsByNamespaceAndLabelsConsumer, podsByNamespaceAndLabelsProvider, 0);
			for (ConfigurationRule cr : tmp) {
				ConfigurationRule crInverted = HarmonizationUtils.deepCopyConfigurationAndInvertVCluster(cr);
				harmonizedRules.add(crInverted);
			}
		}

		return harmonizedRules;
	}

	private List<ConfigurationRule> harmonizeConfigurationRule(ConfigurationRule conn, List<ConfigurationRule> connList,
			HashMap<String, HashMap<String, List<Pod>>> map_conn,
			HashMap<String, HashMap<String, List<Pod>>> map_connList,
			Integer level) {
		List<ConfigurationRule> resList = new ArrayList<>();
		ConfigurationRule res = HarmonizationUtils.deepCopyConfigurationRule(conn);
		KubernetesNetworkFilteringCondition resCond = (KubernetesNetworkFilteringCondition) res
				.getConfigurationCondition();
		int flag = 0;
		boolean dirty = false;
		for (ConfigurationRule confRule : connList) {
//			System.out.println("[harmonization] - processing rule { [" + confRule.getName()
//					+ "]} vs intent { [" + res.getName() + "-" + HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) +"] } - level: " + level);
			flag = 0;
			KubernetesNetworkFilteringCondition tmp = (KubernetesNetworkFilteringCondition) confRule
					.getConfigurationCondition();
			String[] protocolList = HarmonizationUtils.computeHarmonizedProtocolType(resCond.getProtocolType().value(),
					tmp.getProtocolType().value());

			if (protocolList.length == 1 && protocolList[0].equals(resCond.getProtocolType().value())) {
				continue;
			}

			// Step-1.2: check the ports. Detect if the port ranges of res are overlapping
			// with tmp.
			String[] destinationPortList = HarmonizationUtils
					.computeHarmonizedPortRange(resCond.getDestinationPort(), tmp.getDestinationPort()).split(";");

			if (destinationPortList[0].equals(resCond.getDestinationPort())) {
				// No overlap with the current rule (tmp), continue and check next one.
				// System.out.println("No overlap with the current rule (tmp), continue and
				// check next one");
				continue;
			}
			// Step-1.3: check the source and destination.s
			List<ResourceSelector> source = HarmonizationUtils.computeHarmonizedResourceSelector(resCond.getSource(),
					tmp.getSource(), map_conn, map_connList);

			if (source == null) {
				continue;
			}

			if (!source.isEmpty()) {
				boolean result = HarmonizationUtils.compareResourceSelector(source.get(0), resCond.getSource());
				if (result) {
					continue;
				}
			}

			List<ResourceSelector> destination = HarmonizationUtils.computeHarmonizedResourceSelector(
					resCond.getDestination(),
					tmp.getDestination(), map_conn, map_connList);

			if (destination == null) {
				continue;
			}

			if (!destination.isEmpty()
					&& HarmonizationUtils.compareResourceSelector(destination.get(0), resCond.getDestination())) {
				continue;
			}

			// Step-2: if this point is reached, both source, sourcePort, destination, and
			// destinationPort have an overlap (partial or complete) with the current
			// authorization rule.
			dirty = true;

			// Step-2.1: handle the overlap with the sourcePort and destinationPort fields.
			// Note that if there is a partial overlap, the port range could be broken into
			// two ranges (e.g., if overlap is in the middle of the interval).

			// Repeat the process for the destinationPort range.
			if (destinationPortList[0].isEmpty()) {
				flag++;
			} else if (destinationPortList.length > 1) {
				ConfigurationRule res2 = addHarmonizedRules(res, resCond, destinationPortList[1], loggerInfo,
						"destinationPort", null);
				resList.addAll(harmonizeConfigurationRule(res2, connList, map_conn, map_connList, level + 1));

				ConfigurationRule res3 = addHarmonizedRules(res, resCond, destinationPortList[0], loggerInfo,
						"destinationPort", null);
				resList.addAll(harmonizeConfigurationRule(res3, connList, map_conn, map_connList, level + 1));

			} else {
				ConfigurationRule res3 = addHarmonizedRules(res, resCond, destinationPortList[0], loggerInfo,
						"destinationPort", null);
				KubernetesNetworkFilteringCondition k = (KubernetesNetworkFilteringCondition) res3
						.getConfigurationCondition();
				resList.addAll(harmonizeConfigurationRule(res3, connList, map_conn, map_connList, level + 1));
			}

			// Step-2.2: handle the overlap with the protocol type field. Also in this case,
			// it could be that the overlap is partial and the result is a list of protocol
			// types (max size 2 WITH CURRENT VALUES).

			if (protocolList.length == 0) {
				// If the protocol list is empty, it means that it is not possible to harmonize
				// the current intent (i.e., protocol type is included in the authorization
				// rule's one)... just update the flag for the moment.
				flag++;
			} else if (protocolList.length > 1) {

				ConfigurationRule res1 = addHarmonizedRules(res, resCond, protocolList[1], loggerInfo,
						"transportProtocol", null);
				resList.addAll(harmonizeConfigurationRule(res1, connList, map_conn, map_connList, level + 1));

				ConfigurationRule res2 = addHarmonizedRules(res, resCond, protocolList[0], loggerInfo,
						"transportProtocol", null);
				resList.addAll(harmonizeConfigurationRule(res2, connList, map_conn, map_connList, level + 1));

			} else {
				ConfigurationRule res2 = addHarmonizedRules(res, resCond, protocolList[0], loggerInfo,
						"transportProtocol", null);
				resList.addAll(harmonizeConfigurationRule(res2, connList, map_conn, map_connList, level + 1));
			}
			// Step-2.3: solve possible problems with the source and destination selectors.
			if (source.isEmpty()) {
				// This means that it was not possible to harmonized current intent.
			} else {
				for (ResourceSelector rs : source) {
					ConfigurationRule res1 = addHarmonizedRules(res, resCond, "", loggerInfo, "sourceSelector", rs);
					resList.addAll(harmonizeConfigurationRule(res1, connList, map_conn, map_connList, level + 1));
				}
			}

			if (destination.isEmpty()) {

			} else {
				for (ResourceSelector rs : destination) {
					ConfigurationRule res1 = addHarmonizedRules(res, resCond, "", loggerInfo, "destinationSelector",
							rs);
					resList.addAll(harmonizeConfigurationRule(res1, connList, map_conn, map_connList, level + 1));
				}
			}

			// Step-3:
			// In this case, it either had complete overlap with all fields (i.e., the
			// connection is "fully denied") or partial overlap with all the field and the
			// recursive iterations with non-overlapping components have been issued.
			// In the end, whatever is the specific case, current request should be
			// discarded and stop comparing it versus other rules.
			loggerInfo.debug(Main.ANSI_RED
					+ "[harmonization/harmonizeForbiddenConnectionIntent] - complete or partial overlap found, current rule is removed {{}"
					+ Main.ANSI_RESET + "}", HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));
			return resList;
		}

		// If all the rules into ForbiddenConnectionList have been processed, add the
		// current intent to the list and return it.

		loggerInfo.debug(Main.ANSI_GREEN
				+ "[harmonization/harmonizeForbiddenConnectionIntent] - no overlap was found, current rule is added to HARMONIZED set {{}"
				+ Main.ANSI_RESET + "}", HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));
		// System.out.println(Main.ANSI_GREEN +
		// "[harmonization/harmonizeForbiddenConnectionIntent] - no overlap was found,
		// current rule is added to HARMONIZED set {{}" + Main.ANSI_RESET + "}" +
		// HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond));

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
		} else if (Objects.equals(overlap, "destinationSelector"))
			resCond1.setDestination(rs);
		loggerInfo.debug("[harmonization/harmonizeForbiddenConnectionIntent] - found overlap with " + overlap + " {"
				+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond) + "} --> {"
				+ HarmonizationUtils.kubernetesNetworkFilteringConditionToString(resCond1) + "}");
		return res1;
	}

	//TODO: think of moving all the following to a utility class

	public void printDash() {
		System.out.println(Main.ANSI_PURPLE + "-".repeat(100) + Main.ANSI_RESET);
	}

	public void printAuth() {
		System.out
				.println(Main.ANSI_PURPLE + "[DEMO_INFO]    " + Main.ANSI_RESET + "Local cluster defined the following "
						+ Main.ANSI_YELLOW + "Authorization Intents" + Main.ANSI_RESET + " (PROVIDER):");
	}

	public void printRequestIntents(RequestIntents requestIntent, String cluster) {
		if (requestIntent == null) {
			System.out.println("Errore, request intent è null");
			return;
		}
		if (cluster == "consumer") {
			System.out.println("[+] Request intents (CONSUMER):");
			System.out.println("  (*) [AcceptMonitoring]: " + requestIntent.isAcceptMonitoring());
		} else if (cluster == "provider") {
			if (requestIntent.isAcceptMonitoring() != false)
				System.out.println("  (*) [RequestMonitoring]: " + requestIntent.isAcceptMonitoring());
		}

		for (ConfigurationRule cr : requestIntent.getConfigurationRule()) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("  (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond));
			System.out.println(cond.getSource().isIsHostCluster() + " " + cond.getDestination().isIsHostCluster());
		}
	}

	public void printAuthorizationIntents(AuthorizationIntents authorizationIntent) {
		List<ConfigurationRule> forbiddenRule = null;
		List<ConfigurationRule> mandatoryRule = null;

		System.out.print("   .-> ForbiddenConnectionList:\n");
		forbiddenRule = authorizationIntent.getForbiddenConnectionList();

		for (ConfigurationRule cr : forbiddenRule) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("   | (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond));
			System.out.println(cond.getSource().isIsHostCluster() + " " + cond.getDestination().isIsHostCluster());
		}
		System.out.print("   .-> MandatoryConnectionList:\n");
		mandatoryRule = authorizationIntent.getMandatoryConnectionList();

		for (ConfigurationRule cr : mandatoryRule) {
			KubernetesNetworkFilteringCondition cond = (KubernetesNetworkFilteringCondition) cr
					.getConfigurationCondition();
			System.out.print("   | (*) " + cr.getName() + " - ");
			System.out.print(HarmonizationUtils.kubernetesNetworkFilteringConditionToString(cond));
			System.out.println(cond.getSource().isIsHostCluster() + " " + cond.getDestination().isIsHostCluster());
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
