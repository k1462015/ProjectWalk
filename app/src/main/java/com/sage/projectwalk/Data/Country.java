package com.sage.projectwalk.Data;

import java.util.HashMap;

/**
 * Created by Tahmidul on 28/11/2015.
 */
public class Country {
    private String name;
    private String id;
    private String isoCode;
    private String capitalCity;
    private String longitude;
    private String latitude;
    private HashMap<String,Indicator> indicators;

    public Country(){
        indicators = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public HashMap<String, Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(HashMap<String, Indicator> indicators) {
        this.indicators = indicators;
    }
}
