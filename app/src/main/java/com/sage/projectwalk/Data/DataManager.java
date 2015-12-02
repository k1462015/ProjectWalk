package com.sage.projectwalk.Data;

import android.app.Activity;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sage.projectwalk.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataManager {
    private AppCompatActivity context;
    ArrayList<String> indicators;

    public DataManager(AppCompatActivity context){
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
        //For Population
        indicators.add("SP.POP.TOTL");
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
        }
        return country;
    }

    /**
     *
     * @param iso2Code - Country ISO CODE
     * @return Country object with basic information included
     * @throws IOException
     * @throws JSONException
     */
    public Country getCountryBaseData(String iso2Code) throws IOException, JSONException {
        JSONArray jsonArray = retrieveFile("Countries.json");
        //Get data part of array
        jsonArray = jsonArray.getJSONArray(1);
        for (int i = 0;i < jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("iso2Code").equals(iso2Code)){
                Country country = new Country();
                country.setName(jsonObject.getString("name"));
                country.setId(jsonObject.getString("id"));
                country.setIsoCode(iso2Code);
                country.setCapitalCity(jsonObject.getString("capitalCity"));
                country.setLongitude(jsonObject.getString("longitude"));
                country.setLatitude(jsonObject.getString("latitude"));
                //Get population data
                JSONArray populationData = null;
                //Extract population data
                try{
                    populationData = retrieveFile(iso2Code+"_SP.POP.TOTL.json");
                    JSONArray actualPopData= populationData.getJSONArray(1);
                    Indicator indicator = new Indicator();
                    indicator.setId("SP.POP.TOTL");
                    for (int j = 0;j < actualPopData.length();j++){
                        JSONObject jsonObject1 = actualPopData.getJSONObject(j);
                        try{
                            if(!jsonObject1.isNull("value") && !jsonObject1.isNull("date")){
                                Double date = new Double(jsonObject1.getString("value"));
                                int year = Integer.parseInt(jsonObject1.getString("date"));
                                indicator.addData(year,date);
                                Log.i("MYAPP","Added indicator "+date+" "+year+" "+iso2Code);
                            }
                        }catch (NumberFormatException e){
                            Log.i("MYAPP","PROBLEM ADDED POPULATION DATA "+iso2Code);
                        }
                    }
                    country.addIndicator(indicator);
                }catch (Exception e){
                    Log.e("MYAPP","JSON Exception for retrieving population data for "+iso2Code);
                }

                return country;
            }
        }
        return null;
    }

    /**
     * Fetches list of country objects from file
     * @return Arraylist containing country objects
     */
    public ArrayList<Country> getCountryList() throws IOException, JSONException {
        ArrayList<Country> countries = new ArrayList<>();
        JSONArray jsonArray = retrieveFile("Countries.json").getJSONArray(1);
        //Loop through array and create country object
        for(int i = 0;i < jsonArray.length();i++){
            JSONObject countryJSON = jsonArray.getJSONObject(i);
            Country country = new Country();
            country.setName(countryJSON.getString("name"));
            country.setIsoCode(countryJSON.getString("iso2Code"));
            country.setCapitalCity(countryJSON.getString("capitalCity"));
            country.setLongitude(countryJSON.getString("longitude"));
            country.setLatitude(countryJSON.getString("latitude"));
            Log.i("MYAPP", "COUNTRY LIST: " + country.toString());

            //Get population data
            JSONArray populationData = null;
            //Extract population data
            try {
                String iso2Code = countryJSON.getString("iso2Code");
                populationData = retrieveFile(iso2Code + "_SP.POP.TOTL.json");
                JSONArray actualPopData = populationData.getJSONArray(1);
                Indicator indicator = new Indicator();
                indicator.setId("SP.POP.TOTL");
                for (int j = 0; j < actualPopData.length(); j++) {
                    JSONObject jsonObject1 = actualPopData.getJSONObject(j);
                    try {
                        if (!jsonObject1.isNull("value") && !jsonObject1.isNull("date")) {
                            Double date = new Double(jsonObject1.getString("value"));
                            int year = Integer.parseInt(jsonObject1.getString("date"));
                            indicator.addData(year, date);
                            Log.i("MYAPP", "Added indicator " + date + " " + year + " " + iso2Code);
                        }
                    } catch (NumberFormatException e) {
                        Log.i("MYAPP", "PROBLEM ADDED POPULATION DATA " + iso2Code);
                    }
                }
                country.addIndicator(indicator);
            }catch (Exception e){

            }
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
        DataRetriever dataRetriever = new DataRetriever(context);
        dataRetriever.indicators = indicators;
        //Fetches countries.json and then all indicators
        dataRetriever.execute("http://api.worldbank.org/countries/AD;AE;AF;AG;AL;AM;AO;AR;AT;AU;AZ;BA;BB;BD;BE;BF;BG;BH;BI;BJ;BN;BO;BR;BS;BT;BW;BY;BZ;CA;CD;CF;CG;CH;CI;CL;CM;CN;CO;CR;CU;CV;CY;CZ;DE;DJ;DK;DM;DO;DZ;EC;EE;EG;ER;ES;ET;FI;FJ;FM;FR;GA;GB;GD;GE;GH;GM;GN;GQ;GR;GT;GW;GY;HN;HR;HT;HU;ID;IE;IL;IN;IQ;IR;IS;IT;JM;JO;JP;KE;KG;KH;KI;KM;KN;KP;KR;KW;KZ;LA;LB;LC;LI;LK;LR;LS;LT;LU;LV;LY;MA;MC;MD;ME;MG;MH;MK;ML;MM;MN;MR;MT;MU;MV;MW;MX;MY;MZ;NA;NE;NG;NI;NL;NO;NP;NZ;OM;PA;PE;PG;PH;PK;PL;PT;PW;PY;QA;RO;RS;RU;RW;SA;SB;SC;SD;SE;SG;SI;SK;SL;SM;SN;SO;SR;ST;SV;SY;SZ;TD;TG;TH;TJ;TL;TM;TN;TO;TR;TT;TV;TW;TZ;UA;UG;US;UY;UZ;VC;VE;VN;VU;WS;YE;ZA;ZM;ZW?format=json&per_page=500");
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
     * This retrieves the file from internal storage
     * The data in the internal storage would have been
     * retrieved from the internet/World Data bank
     * @param fileName - Name of file
     * @return JSONArray - Containing file data
     */
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

    /**
     * This retrieves the file from the assets folder
     * @param fileName - Name of File
     * @return JSONArray containing file data
     */
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


}
