package eu.fluidos;

import java.util.HashMap;

public class Pod {
	
	private HashMap<String, String> labels = new HashMap<>();
	private Namespace ns;

	public Pod() {
		super();
	}

	public HashMap<String, String> getLabels() {
		return labels;
	}

	public void setLabels(HashMap<String, String> labels) {
		this.labels = labels;
	}
	
	public void setSingleLabel(String key, String value) {
		this.labels.put(key, value);
	}
	
	public void setNamespace(Namespace ns) {
		this.ns = ns;
	}
	
	public Namespace getNamespace() {
		return ns;
	}
	

}
