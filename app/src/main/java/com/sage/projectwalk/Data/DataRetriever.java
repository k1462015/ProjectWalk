package com.sage.projectwalk.Data;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tahmidul on 23/11/2015.
 */
public class DataRetriever extends AsyncTask<String,Integer,ArrayList<String>>{
    private ArrayList<String> fetchedData;
    private TextView dataViewer;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        //This will hold all json strings retrieved from the urls
        ArrayList<String> jsonStrings = new ArrayList<>();
        try {
            //Will loop through every url link give
            for (int i = 0;i < urls.length;i++){
                StringBuffer buffer = new StringBuffer();
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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
                jsonStrings.add(buffer.toString());
            }
            return jsonStrings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<String> jsonStrings) {
        super.onPostExecute(jsonStrings);
        fetchedData = jsonStrings;
        for (String data:fetchedData){
            dataViewer.setText(dataViewer.getText()+" "+data);
        }
    }

    /**
     *
     * @param dataViewer - The view that you want the data to show in
     * @param urls - All links that data should be fetched from
     */
    public void fetchData(TextView dataViewer,String... urls){
        this.execute(urls);
        this.dataViewer = dataViewer;

    }
}

