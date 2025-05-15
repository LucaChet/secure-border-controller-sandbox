package eu.fluidos.cluster;

import java.io.IOException;
import java.util.List;
import eu.fluidos.*;

public class ClusterController {
	private final ClusterService clusterService = new ClusterService();

	public void initializeHashMap(Cluster cluster) {
		clusterService.initializeHashMaps(cluster);
	}

	public Cluster getClusterData() throws IOException {
		return ClusterService.getClusterData();
	}
}
