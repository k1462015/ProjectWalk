package com.sage.projectwalk.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.sage.projectwalk.MainActivity;
import com.sage.projectwalk.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tahmidul on 23/11/2015.
 */
public class DataRetriever extends AsyncTask<String,Integer,Void>{
    private MainActivity context;
    private String fileName;
    private DataStorer ds;
    ProgressDialog progressDialog;

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
            //Will loop through every url link give
            StringBuffer buffer = new StringBuffer();
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            publishProgress(10);
            connection.connect();
            BufferedReader in;
            in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
            publishProgress(20);
            String inputLine = in.readLine();
            while(inputLine != null){
                buffer.append(inputLine);
                inputLine = in.readLine();
            }
            in.close();
            publishProgress(30);
            connection.disconnect();
            //Saves the json file to devices internal storage
            ds.saveToFile(context, fileName, buffer.toString());
            publishProgress(100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
        Log.i("MYAPP","UPDATING PROGRESS TO "+values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.hide();
    }

    //    /**
//     *
//     * @param context - The context of the application
//     * @param urls - All urls with JSON data
//     */
//    public void fetchData(Context context,String... urls){
//        this.context = context;
//        this.execute(urls);
//    }

    public void fetchCountryData(){
        fileName = "Countries";
        execute("http://api.worldbank.org/countries?format=json&per_page=300");
    }


}

