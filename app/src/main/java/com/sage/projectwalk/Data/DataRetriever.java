package com.sage.projectwalk.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tahmidul on 23/11/2015.
 */
public class DataRetriever extends AsyncTask<String,Integer,Void>{
    private AppCompatActivity context;
    ProgressDialog progressDialog;
    ArrayList<String> indicators;
    ArrayList<Country> countries;
    int counter;

    public DataRetriever(AppCompatActivity context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Get required views
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Synchronising data");
        progressDialog.setMessage("Connecting to World Data Bank...");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AlertDialog.Builder builder = new AlertDialog.Builder(progressDialog.getContext());
                    builder.setMessage("Are you sure you want to cancel the data fetching?")
                            .setTitle("Warning")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancel(true);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    progressDialog.show();
                                }
                            });
                    builder.create().show();
            }
        });
        progressDialog.show();

    }

    @Override
    protected Void doInBackground(String... urls) {
        try {
            //First process retrieving the country base data
            StringBuffer buffer = processURL(urls[0]);
            //Saves the json file to devices internal storage
            saveToFile(context, "Countries", buffer.toString());
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
                    saveToFile(context,isoCode+"_"+indicators.get(j),buffer.toString());
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
        try{
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
        }catch (InterruptedIOException e){
            Log.e("MYAPP","User may have cancelled data retieving operation");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(counter < countries.size()){
            //Updates progress message specifying which country is being downloaded
            progressDialog.setMessage("Fetching data for "+countries.get(counter+1).getName());
        }
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.hide();
    }

    /**
     *
     * @param context - Application Context
     * @param FILENAME - Name that you want to save file as
     * @param data - Actual data that is in JSON format
     */
    public void saveToFile(Context context,String FILENAME,String data){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(FILENAME + ".json", Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Log.i("MYAPP",FILENAME+" SUCCESFULLY SAVED");
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Log.i("MYAPP", "File NOT FOUND EXCEPTION");
        }catch (IOException e){
            e.printStackTrace();
            Log.i("MYAPP", "IOEXCEPTION");
        }
    }

}

