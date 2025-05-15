package eu.fluidos;

import java.util.List;

public class Cluster {
	private List<Pod> pods;
	private List<Namespace> namespaces;
	
	public Cluster(List<Pod> pods, List<Namespace> namespaces) {
		super();
		this.pods = pods;
		this.namespaces = namespaces;
	}
	public List<Pod> getPods() {
		return pods;
	}
	public void setPods(List<Pod> pods) {
		this.pods = pods;
	}
	public List<Namespace> getNamespaces() {
		return namespaces;
	}
	public void setNamespaces(List<Namespace> namespaces) {
		this.namespaces = namespaces;
	}
	
	
}
