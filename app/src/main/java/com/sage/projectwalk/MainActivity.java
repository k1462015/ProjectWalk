package com.sage.projectwalk;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.DummyFragment;
import com.sage.projectwalk.InfoGraphs.EnergyRatioGraph;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.RenewableBreakdownContainer;
import com.sage.projectwalk.InfoGraphs.SlideOutPanel;

public class MainActivity extends AppCompatActivity {
    DataManager dataManager;
    public ProgressDialog progressDialog;
    private Button openCloseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = new DataManager(this);

        //Get required views
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        //Example of how to retrieve a country object
//        try {
//            Country bangladesh = dataManager.getCountryIndicator("GB", "3.1.9_BIOGAS.CONSUM","3.1.8_WASTE.CONSUM","3.1.9_BIOGAS.CONSUM");
//        } catch (Exception e) {
//            Log.i("MYAPP", "Couldn't retrieve file for countries");
//        }

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
        openCloseButton = (Button)findViewById(R.id.button2);

    }

    public void openSlideFragment(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SlideOutPanel menuFragment = new SlideOutPanel();
//        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, 0);
        fragmentTransaction.add(R.id.out, menuFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        openCloseButton.setVisibility(View.INVISIBLE);

    }

    public void closeSlideFragment(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DummyFragment menuFragment = new DummyFragment();
//        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, 0);
        fragmentTransaction.replace(R.id.out, menuFragment);
        fragmentTransaction.commit();
        openCloseButton.setVisibility(View.VISIBLE);

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
        // automatically handle clicks on the Home/Up openCloseButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
