package com.sage.projectwalk;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.DummyFragment;
import com.sage.projectwalk.InfoGraphs.EnergyRatioGraph;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.RenewableBreakdownContainer;
import com.sage.projectwalk.InfoGraphs.SlideOutPanel;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SlideOutPanel.CountryListListener{
    DataManager dataManager;
    private Button showButton;
    SlideOutPanel menuFragment;
    TextView countryOneHolder;
    TextView countryTwoHolder;
    EnergyRatioGraph energyRatioGraph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = new DataManager(this);

        showButton = (Button)findViewById(R.id.Show);
        countryOneHolder = (TextView) findViewById(R.id.countryOneHolder);
        countryTwoHolder = (TextView) findViewById(R.id.countryTwoHolder);

        //Gets required fragment stuff
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        //Adds all fragments to main activity
        BatteryGraph batteryGraph = new BatteryGraph();
        energyRatioGraph = new EnergyRatioGraph();
        FactCards factCards = new FactCards();
        RenewableBreakdownContainer renewableBreakdownContainer = new RenewableBreakdownContainer();
        menuFragment = new SlideOutPanel();

        //Adds all fragments to corresponding containers
        fragmentTransaction.add(R.id.batteryGraphContainer,batteryGraph);
        fragmentTransaction.add(R.id.energyRatioContainer,energyRatioGraph);
        fragmentTransaction.add(R.id.factCardsContainer,factCards);
        fragmentTransaction.add(R.id.renewableSourcesContainer, renewableBreakdownContainer);
        fragmentTransaction.add(R.id.out, menuFragment);
        fragmentTransaction.hide(menuFragment);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.commit();

    }

    public void openSlideFragment(View v) {
        showButton.setVisibility(View.INVISIBLE);   //Hides show button
        RelativeLayout relativeLayoutOut = (RelativeLayout) findViewById(R.id.out);
        relativeLayoutOut.setBackgroundColor(Color.parseColor("#99000000"));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentTransaction.show(menuFragment);
        fragmentTransaction.commit();
    }

    public void hideShowButton(){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        showButton.startAnimation(fadeIn);
        showButton.setEnabled(true);
        showButton.setVisibility(View.VISIBLE);   //Hides show button
        RelativeLayout relativeLayoutOut = (RelativeLayout) findViewById(R.id.out);
        relativeLayoutOut.setBackgroundColor(Color.TRANSPARENT);
    }

    public void onCountryOption1Selected(Country country){
        countryOneHolder.setText(country.getName());
        try {
            country = dataManager.getCountryIndicator(country.getIsoCode(),"8.1.2_FINAL.ENERGY.INTENSITY");
            energyRatioGraph.updateCountryOne(country);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onCountryOption2Selected(Country country){
        countryTwoHolder.setText(country.getName());
        try {
            country = dataManager.getCountryIndicator(country.getIsoCode(),"8.1.2_FINAL.ENERGY.INTENSITY");
            energyRatioGraph.updateCountryTwo(country);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
