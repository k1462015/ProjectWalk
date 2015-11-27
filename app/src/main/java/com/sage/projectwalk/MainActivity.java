package com.sage.projectwalk;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sage.projectwalk.Data.DataRetriever;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.EnergyRatioGraph;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.RenewableBreakdownContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    DataRetriever dataRetriever;
    TextView textViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gets required fragment stuff
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        //Adds all fragments to main activity
        BatteryGraph batteryGraph = new BatteryGraph();
        EnergyRatioGraph energyRatioGraph = new EnergyRatioGraph();
        FactCards factCards = new FactCards();
        RenewableBreakdownContainer renewableBreakdownContainer = new RenewableBreakdownContainer();

        //Adds all fragments to corresponding containers
        fragmentTransaction.add(R.id.batteryGraphContainer,batteryGraph);
        fragmentTransaction.add(R.id.energyRatioContainer,energyRatioGraph);
        fragmentTransaction.add(R.id.factCardsContainer,factCards);
        fragmentTransaction.add(R.id.renewableSourcesContainer,renewableBreakdownContainer);
        fragmentTransaction.commit();

    }

    public void onButtonPressed(String response){
        Log.i("MYAPP",response);
    }

    /**
     * Looks inside the devices internal directory
     * ANd loads all json files
     */
    public void loadDataFromStorage(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(dataRetriever.getStatus() != AsyncTask.Status.FINISHED){
                    Log.i("MYAPP","WAITING FOR DATA RETRIEVAL");
                }
                Log.i("MYAPP","ATTEMPTING TO READ DATA");
                try {
                    //This grabs all available files saved in internal storage
                    File[] allFiles = getFilesDir().listFiles();
                    //This loads the country json files
                    FileInputStream fip = openFileInput(allFiles[0].getName());
                    if(fip != null){
                        Log.i("MYAPP","LOADED "+allFiles[0].getName());
                    }else{
                        Log.i("MYAPP","FILE NOT FOUND");
                    }
                    //Starts reading the file
                    InputStreamReader inputStreamReader = new InputStreamReader(fip);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder sb = new StringBuilder();

                    //Reads contents of file, line by line
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }
                    final StringBuilder completedFile = sb;
                    JSONArray jsonArray = new JSONArray(completedFile.toString());
                    JSONArray allCountryData = jsonArray.getJSONArray(1);
                    String allCountries = "";
                    for (int i = 0; i < allCountryData.length();i++){
                        JSONObject country = allCountryData.getJSONObject(i);
                        String countryName = country.getString("name");
                        allCountries += countryName+"\n";
                    }
                    final String textViewCountries = allCountries;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewer.setText(textViewCountries);
                        }
                    });
                    fip.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.i("MYAPP","FILE NOT FOUND EXCEPTION");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("MYAPP", "IO EXCEPTION");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
