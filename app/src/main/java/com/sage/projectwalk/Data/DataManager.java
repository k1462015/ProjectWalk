package com.sage.projectwalk.Data;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.TextView;

import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.MainActivity;
import com.sage.projectwalk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataManager {
    private DataRetriever dataRetriever;
    private MainActivity context;
    ArrayList<String> indicators;

    public DataManager(MainActivity context){
        this.context = context;
        //All indicators to fetch data for
        indicators = new ArrayList<>();
        //For Fragment 1
        indicators.add("3.1_RE.CONSUMPTION");
        indicators.add("8.1.1_FINAL.ENERGY.CONSUMPTION");
        //For Fragment 2
        indicators.add("3.1.3_HYDRO.CONSUM");
        indicators.add("3.1.4_BIOFUELS.CONSUM");
        indicators.add("3.1.5_WIND.CONSUM");
        indicators.add("3.1.6_SOLAR.CONSUM");
        indicators.add("3.1.7_GEOTHERMAL.CONSUM");
        indicators.add("3.1.8_WASTE.CONSUM");
        indicators.add("3.1.9_BIOGAS.CONSUM");
        //For Fragment 3
        indicators.add("8.1.2_FINAL.ENERGY.INTENSITY");
    }
    /**
     * Fetched a country object
     * According to isoCode
     * @param isoCode - Country iso code
     * @param indicator - All indicators needed
     * @return Country object
     */
    public Country getCountryIndicator(String isoCode,String...indicators) throws IOException, JSONException {
        //First get countries base data
        Country country = getCountryBaseData(isoCode);
        if(country != null){
            //Then loop through every indicator and add to country object
            for (String indicator:indicators){
                JSONArray indicatorData = retrieveFile(isoCode + "_" + indicator + ".json");
                if(indicatorData != null){
                    Log.i("MYAPP","FOUND INDICATOR FOR "+isoCode+indicator);
                    JSONArray indicatorContent = indicatorData.getJSONArray(1);
                    //Create new indicator object
                    String id = indicatorContent.getJSONObject(0).getJSONObject("indicator").getString("id");
                    String name = indicatorContent.getJSONObject(0).getJSONObject("indicator").getString("value");
                    Indicator ind = new Indicator();
                    ind.setId(id);
                    ind.setName(name);
                    Log.i("MYAPP","CREATED indicator object "+indicator+" with id "+id+" and name "+name);
                    for (int i = 0;i < indicatorContent.length();i++){
                        JSONObject jsonObject = indicatorContent.getJSONObject(i);
                        Double value;
                        if(!jsonObject.isNull("value")){
                            value = Double.parseDouble(jsonObject.getString("value"));
                            Integer date;
                            try{
                                date = Integer.parseInt(jsonObject.getString("date"));
                                ind.addData(date, value);
//                                Log.i("MYAPP","Added "+jsonObject.getString("date")+" with value: "+value+" for indicator "+id);
                            }catch (Exception e){
                                Log.i("MYAPP","Date "+jsonObject.getString("date")+"not in correct format");
                            }
                        }
                    }
                    country.addIndicator(ind);
                }else{
                    Log.i("MYAPP","COUDLN'T GET "+isoCode+" "+indicator);
                }
            }
            try {
                FileInputStream fileInputStream = context.openFileInput(isoCode+".json");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            return null;
        }
        return country;
    }

    public Country getCountryBaseData(String iso2Code) throws IOException, JSONException {
        //First check if present in internal storage
        boolean inInteralStorage = false;
        JSONArray jsonArray = retrieveFile("Countries.json");
        //Get data part of array
        jsonArray = jsonArray.getJSONArray(1);
        for (int i = 0;i < jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("iso2Code").equals(iso2Code)){
                Country country = new Country();
                country.setIsoCode(iso2Code);
                country.setName(jsonObject.getString("name"));
                country.setCapitalCity(jsonObject.getString("capitalCity"));
                return country;
            }
        }
        return null;
    }

    public JSONArray retrieveFile(String fileName){
        //First attempt to read from internal
        JSONArray jsonArray = readFileInternal(fileName);
        if(jsonArray == null){
            //Since not in internal, load from asset
            jsonArray = readFileAsset(fileName);
            if(jsonArray == null){
                Log.i("MYAPP","Could not find "+fileName+" in neither assets folder or internal");
            }else{
                Log.i("MYAPP","Loading "+fileName+" from assets since not found in internal");
            }
        }else{
            Log.i("MYAPP","Loaded "+fileName+" from internal");
        }
        return jsonArray;
    }

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
            country.setIsoCode(countryJSON.getString("iso2Code"));
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
        dataRetriever.indicators = indicators;
        //Fetches countries.json and then all indicators
        dataRetriever.execute("http://api.worldbank.org/countries?format=json&per_page=300");
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

    public JSONArray readFileInternal(String fileName){
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            //Starts reading the file
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();

            //Reads contents of file, line by line
            String line;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            return new JSONArray(sb.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray readFileAsset(String fileName){
        //Load from assets folder
        AssetManager assetManager = context.getAssets();
        try{
            InputStream input = assetManager.open("data/"+fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            //Reads contents of file, line by line
            String line;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            return new JSONArray(sb.toString());
        }catch (Exception e){
            return null;
        }

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
