package eu.fluidos.traslator;
import eu.fluidos.jaxb.CIDRSelector;
import eu.fluidos.jaxb.KeyValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ruleinfo {
    private List<KeyValue> sourcePodList;
    //private String sourceNamespace;
    private CIDRSelector cidrSource;
    private List<KeyValue> destinationPodList;
    private List<KeyValue> destinationNamespaceList;
    private List<KeyValue> sourceNamespaceList;
    private CIDRSelector cidrDestination;
    private String port;
    private String protocol;
    private boolean isSourceHost;
    private boolean isDestinationHost;

    public Ruleinfo(List<KeyValue> sourcePodList, List<KeyValue> sourceNamespaceList, CIDRSelector cidrSource,
    List<KeyValue> destinationPodList, List<KeyValue> destinationNamespaceList, CIDRSelector cidrDestination,String port,String protocol,boolean isSourceHost,boolean isDestinationHost) {
        this.sourcePodList = sourcePodList;
        this.sourceNamespaceList = sourceNamespaceList;
        this.cidrSource = cidrSource;
        this.destinationPodList = destinationPodList;
        this.destinationNamespaceList = destinationNamespaceList;
        this.cidrDestination = cidrDestination;
        this.port = port;
        this.protocol=protocol;
        this.isSourceHost=isSourceHost;
        this.isDestinationHost=isDestinationHost;
    }

    public List<KeyValue> getSourcePod() {
        return sourcePodList;
    }

    public void setSourcePod(List<KeyValue> sourcePodList) {
        this.sourcePodList = sourcePodList;
    }

    public List<KeyValue> getSourceNamespace() {
        return sourceNamespaceList;
    }

    public void setSourceNamespace(List<KeyValue> sourceNamespaceList) {
        this.sourceNamespaceList = sourceNamespaceList;
    }

    public CIDRSelector getCidrSource() {
        return cidrSource;
    }

    public void setCidrSource(CIDRSelector cidrSource) {
        this.cidrSource = cidrSource;
    }

    public List<KeyValue> getDestinationPod() {
        return destinationPodList;
    }

    public void setDestinationPod(List<KeyValue> destinationPodList) {
        this.destinationPodList = destinationPodList;
    }

    public List<KeyValue> getDestinationNamespace() {
        return destinationNamespaceList;
    }

    public void setDestinationNamespace(List<KeyValue> destinationNamespaceList) {
        this.destinationNamespaceList = destinationNamespaceList;
    }

    public CIDRSelector getCidrDestination() {
        return cidrDestination;
    }

    public void setCidrDestination(CIDRSelector cidrDestination) {
        this.cidrDestination = cidrDestination;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String,String> getLabelsSourcePod (){
        Map<String, String> labelsSourcePod = new HashMap<>();
        for (KeyValue keyValue : this.sourcePodList){
            labelsSourcePod.put(keyValue.getKey(),keyValue.getValue().replaceAll("_", "-"));
        }
        return labelsSourcePod;
    }

    public Map<String,String> getLabelsDestinationPod (){
        Map<String, String> labelsDestinationPod = new HashMap<>();
        for (KeyValue keyValue : this.destinationPodList){
            labelsDestinationPod.put(keyValue.getKey(),keyValue.getValue().replaceAll("_", "-"));
        }
        return labelsDestinationPod;
    }

    public Map<String,String> getLabelsDestinationNamespace(){
        Map<String, String> labelsDestinationNamespace = new HashMap<>();
        for (KeyValue keyValue : this.destinationNamespaceList){
            labelsDestinationNamespace.put(keyValue.getKey(),keyValue.getValue());
        }
        return labelsDestinationNamespace;
    }

    public Map<String,String> getLabelsSourceNamespace(){
        Map<String, String> labelsSourceNamespace = new HashMap<>();
        for (KeyValue keyValue : this.sourceNamespaceList){
            labelsSourceNamespace.put(keyValue.getKey(),keyValue.getValue());
        }
        return labelsSourceNamespace;
    }
    public void setIsSourceHost(boolean isSourceHost) {
        this.isSourceHost = isSourceHost;
    }

    public boolean isSourceHost() {
        return isSourceHost;
    }

    public void setIsDestinationHost(boolean isDestinationHost) {
        this.isDestinationHost = isDestinationHost;
    }

    public boolean isDestinationHost() {
        return isDestinationHost;
    }
}

