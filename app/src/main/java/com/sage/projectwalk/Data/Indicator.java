package com.sage.projectwalk.Data;

import java.util.HashMap;

/**
 * Created by Tahmidul on 28/11/2015.
 */
public class Indicator {
    private String id;
    private String name;
    private String description;
    private HashMap<Integer,Double> indicatorData;

    public Indicator(){
        indicatorData = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addData(Integer year,Double data){
        indicatorData.put(year,data);
    }

    public Double getData(Integer year){
        return indicatorData.get(year);
    }

    public HashMap<Integer, Double> getIndicatorData() {
        return indicatorData;
    }

    public void setIndicatorData(HashMap<Integer, Double> indicatorData) {
        this.indicatorData = indicatorData;
    }
}
