package eu.fluidos.harmonization;

import eu.fluidos.Cluster;
import eu.fluidos.jaxb.AuthorizationIntents;
import eu.fluidos.jaxb.RequestIntents;

public class HarmonizationController {

	HarmonizationService harmonizationService = new HarmonizationService();

	public HarmonizationController() {
	}
	
	public  RequestIntents harmonize(Cluster cluster, RequestIntents requestIntents, AuthorizationIntents contracAuthorizationIntents) {
		return harmonizationService.harmonize(cluster, requestIntents,contracAuthorizationIntents);
	}

	public boolean verify(Cluster cluster, AuthorizationIntents authIntentsProvider) {
		return harmonizationService.verify(cluster, authIntentsProvider);
	}

}
