package eu.fluidos;

import java.util.HashMap;
import java.util.Map;

public class LabelsKeyValue {
    private String key;
    private String value;

    public LabelsKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Map<String,String> getMap() {
        Map<String,String> map = new HashMap<>();
        map.put(this.key,this.value);
        return map;
    }
}
