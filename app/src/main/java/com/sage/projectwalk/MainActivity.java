package com.sage.projectwalk;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.Data.DataRetriever;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.EnergyRatioGraph;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.RenewableBreakdownContainer;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    DataManager dataManager;
    DataRetriever dataRetriever;
    TextView textViewer;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = new DataManager(this);

        //Get required views
        progressDialog = new ProgressDialog(this);

        //Checks if there is any data in device
        if(!dataManager.checkIfDataExists()){
            fetchData(null);
        }

        try {
            Country bangladesh = dataManager.getCountryIndicator("GB", "3.1.9_BIOGAS.CONSUM","3.1.8_WASTE.CONSUM","3.1.9_BIOGAS.CONSUM");
            if(bangladesh == null){
                Log.i("MYAPP","Couldn't find BD");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("MYAPP", "Couldn't retrieve file for countries");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("MYAPP", "JSON Exception");
        }
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

    public void getCountry(View view){
        EditText editText = (EditText) findViewById(R.id.countryOption);
        String input = editText.getText().toString();
        try {
            Country country = dataManager.getCountryIndicator(input,"3.1.9_BIOGAS.CONSUM");
            Log.i("MYAPP","Got Country data for "+country.getName());
            //THIS IS TEMPORARY //TODO:REMOVE THIS AFTER
            BatteryGraph batteryGraph = (BatteryGraph) getSupportFragmentManager().findFragmentById(R.id.batteryGraphContainer);
            TextView title = (TextView) batteryGraph.getView().findViewById(R.id.title);
            if(title != null && country != null){
                title.setText(country.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MYAPP","Couldn't find data for "+input);
        }
    }

    public void fetchData(View view){
        progressDialog.setTitle("Synchronizing Data");
        progressDialog.setMessage("Retrieving latest data from World Data Bank");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataManager.synchronizeData();
            }
        });
        thread.start();
    }

    public void getData(View view){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataManager.getCountryList();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void onButtonPressed(String response){
        Log.i("MYAPP",response);
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
