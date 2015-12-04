package com.sage.projectwalk.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Tahmidul on 28/11/2015.
 */
public class Indicator {
    private String id;
    private String name;
    private String description;
    private HashMap<Integer,BigDecimal> indicatorData;

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

    public void addData(Integer year,BigDecimal data){
        indicatorData.put(year,data);
    }

    public BigDecimal getData(Integer year){
        return indicatorData.get(year);
    }

    public HashMap<Integer, BigDecimal> getIndicatorData() {
        return indicatorData;
    }

    public void setIndicatorData(HashMap<Integer, BigDecimal> indicatorData) {
        this.indicatorData = indicatorData;
    }

    public String toString(){
        Set<Integer> allYears = indicatorData.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer year:allYears){
            stringBuilder.append("Year: "+year+" Value: "+indicatorData.get(year)+"|");
        }
        return stringBuilder.toString();
    }
}
