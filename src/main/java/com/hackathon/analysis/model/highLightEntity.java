package com.hackathon.analysis.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class highLightEntity implements Serializable{

    private Map<String, List<String>> highlight;

    public Map<String, List<String>> getHighlight() {
        return highlight;
    }

    public void setHighlight(Map<String, List<String>> highlight) {
        this.highlight = highlight;
    }
}
