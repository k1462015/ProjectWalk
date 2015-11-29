package com.sage.projectwalk.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.MainActivity;
import com.sage.projectwalk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tahmidul on 23/11/2015.
 */
public class DataRetriever extends AsyncTask<String,Integer,Void>{
    private MainActivity context;
    private DataStorer ds;
    ProgressDialog progressDialog;
    ArrayList<String> indicators;
    ArrayList<Country> countries;
    int counter;
    public DataRetriever(MainActivity context){
        this.context = context;
        ds = new DataStorer();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = context.progressDialog;
    }

    @Override
    protected Void doInBackground(String... urls) {
        try {
            //First process retrieving the country base data
            StringBuffer buffer = processURL(urls[0]);
            //Saves the json file to devices internal storage
            ds.saveToFile(context, "Countries", buffer.toString());
            countries = new ArrayList<>();
            JSONArray jsonArray = (new JSONArray(buffer.toString())).getJSONArray(1);
            for (int i = 0;i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Country country = new Country();
                country.setName(jsonObject.getString("name"));
                country.setIsoCode(jsonObject.getString("iso2Code"));
                countries.add(country);
            }
            Log.i("MYAPP","Total Countries found: "+countries.size());
            //Now loop through every indicator and download data
            String baseURL = "http://api.worldbank.org/countries/CCODE/indicators/ICODE?format=json&per_page=10000";
            for (int i = 0;i < countries.size();i++){
                counter = i;
                Country country = countries.get(i);
                String isoCode = country.getIsoCode();
                String URL = baseURL.replace("CCODE", isoCode);
                for (int j = 0;j < indicators.size();j++){
                    String indicatorURL = URL.replace("ICODE",indicators.get(j));
                    Log.i("MYAPP",indicatorURL);
                    buffer = processURL(indicatorURL);
                    //Save the json file to internal storage
                    ds.saveToFile(context,isoCode+"_"+indicators.get(j),buffer.toString());
                }
                double currentProgress = i;
                double toDo = countries.size();
                Double status = (currentProgress / toDo) * 100;
                int progress = status.intValue();
                publishProgress(progress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StringBuffer processURL(String websiteURL) throws IOException {
        URL url = new URL(websiteURL);
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setDoInput(true);
        connection.connect();
        BufferedReader in;
        in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
        String inputLine = in.readLine();
        while(inputLine != null){
            buffer.append(inputLine);
            inputLine = in.readLine();
        }
        in.close();
        connection.disconnect();
        return buffer;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(counter < countries.size()){
            progressDialog.setMessage("Fetching data for "+countries.get(counter+1).getName());
        }
        progressDialog.setProgress(values[0]);
        Log.i("MYAPP","UPDATING PROGRESS TO "+values[0]+"%");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.hide();
    }

}

