package eu.fluidos.cluster;

import java.util.HashMap;
import java.util.List;
import eu.fluidos.*;


public class ClusterService {
	private final ClusterData clusterData = new ClusterData();

	public HashMap<String, HashMap<String, List<Pod>>> initializeHashMaps(Cluster cluster) {
		return clusterData.initializeHashMaps(cluster);
	}

	public static Cluster getClusterData(){

		return new Cluster(null, null);
	}


}
