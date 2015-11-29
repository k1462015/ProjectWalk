package com.sage.projectwalk.Data;

import android.content.Context;
import android.util.Log;

import com.sage.projectwalk.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataManager {
    private DataRetriever dataRetriever;
    private MainActivity context;

    public DataManager(MainActivity context){
        this.context = context;
    }
    /**
     * Fetched a country object
     * According to isoCode
     * @param isoCode - Country iso code
     * @param indicator - All indicators needed
     * @return Country object
     */
//    public static Country getCountryIndicator(Context context,String isoCode,String...indicator){
//        Country country = new Country();
//        //First get countries base data
//        //Then loop through every indicator and add to country object
//        try {
//            FileInputStream fileInputStream = context.openFileInput(isoCode+".json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return country;
//    }

    /**
     * Fetches list of country objects from file
     * @return Arraylist containing country objects
     */
    public ArrayList<Country> getCountryList() throws IOException, JSONException {
        ArrayList<Country> countries = new ArrayList<>();
        //First get Countries.json file
        FileInputStream fileInputStream = context.openFileInput("Countries.json");
        String fileData = readFile(fileInputStream);
        JSONArray jsonArray = (new JSONArray(fileData)).getJSONArray(1);
        //Loop through array and create country object
        for(int i = 0;i < jsonArray.length();i++){
            JSONObject countryJSON = jsonArray.getJSONObject(i);
            Country country = new Country();
            country.setName(countryJSON.getString("name"));
            country.setCapitalCity(countryJSON.getString("capitalCity"));
            country.setLongitude(countryJSON.getString("longitude"));
            country.setLatitude(countryJSON.getString("latitude"));
            countries.add(country);
        }
        //Sort all by country name alphabetically
        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country c1, Country c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        for(Country country:countries){
            Log.i("MYAPP","Country: "+country.getName()+" Capital City: "+country.getCapitalCity()+" Longitude: "+country.getLongitude());
        }
        return countries;
    }

    /**
     * Goes to the world data site and fetches all data
     */
    public void synchronizeData(){
        dataRetriever = new DataRetriever(context);
        //First refresh the countries.json file
        dataRetriever.fetchCountryData();
        //Then for each indicator
        //Update it's isoCode_indicator.json file
    }

    public String readFile(FileInputStream fileInputStream) throws IOException {
        //Starts reading the file
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();

        //Reads contents of file, line by line
        String line;
        while((line = bufferedReader.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();

    }

    /**
     * Checks if there is any data on the current device
     * @return True: If there is data False: No data found
     */
    public boolean checkIfDataExists(){
        //Check if country data available
        try{
            FileInputStream fileInputStream = context.openFileInput("Countries.json");
            return true;
        }catch (FileNotFoundException e){
            return false;
        }
    }





}
