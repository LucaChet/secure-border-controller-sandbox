package eu.fluidos;

import java.util.HashMap;

public class Namespace {
	
	private HashMap<String, String> labels = new HashMap<>();

	public Namespace() {
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
}
