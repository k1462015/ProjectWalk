package com.sage.projectwalk.Data;

import android.content.Context;
import android.os.AsyncTask;
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
    private Context context;
    private DataStorer ds;

    public DataRetriever(){
        ds = new DataStorer();
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        //This will hold all json strings retrieved from the urls
        ArrayList<String> jsonStrings = new ArrayList<>();
        try {
            //Will loop through every url link give
            for (int i = 0;i < urls.length;i++){
                StringBuffer buffer = new StringBuffer();
                URL url = new URL(urls[i]);
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
                //Saves the json file to devices internal storage
                ds.saveToFile(context, "COUNTRY_DATA_" + i, buffer.toString());
            }
            return jsonStrings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param context - The context of the application
     * @param urls - All urls with JSON data
     */
    public void fetchData(Context context,String... urls){
        this.context = context;
        this.execute(urls);
    }
}

