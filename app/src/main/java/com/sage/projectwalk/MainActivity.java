package com.sage.projectwalk;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.DummyFragment;
import com.sage.projectwalk.InfoGraphs.EnergyRatioGraph;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.OnSwipeTouchListener;
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
    ImageView mainCountryOneImage;
    ImageView mainCountryTwoImage;
    EnergyRatioGraph energyRatioGraph;
    BatteryGraph batteryGraph;
    RelativeLayout mainActivityRoot;
    RenewableBreakdownContainer renewableBreakdownContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = new DataManager(this);

        mainActivityRoot = (RelativeLayout) findViewById(R.id.mainActivityRoot);
        mainActivityRoot.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                openSlideFragment();
            }
        });

        countryOneHolder = (TextView) findViewById(R.id.countryOneHolder);
        countryTwoHolder = (TextView) findViewById(R.id.countryTwoHolder);
        mainCountryOneImage = (ImageView) findViewById(R.id.mainCountryOneImage);
        mainCountryTwoImage = (ImageView) findViewById(R.id.mainCountryTwoImage);

        //Gets required fragment stuff
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        //Adds all fragments to main activity
        batteryGraph = new BatteryGraph();
        energyRatioGraph = new EnergyRatioGraph();
        FactCards factCards = new FactCards();
        renewableBreakdownContainer = new RenewableBreakdownContainer();
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

    public void openSlideFragment() {
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
        RelativeLayout relativeLayoutOut = (RelativeLayout) findViewById(R.id.out);
        relativeLayoutOut.setBackgroundColor(Color.TRANSPARENT);
    }

    public void onCountryOption1Selected(Country country){
        countryOneHolder.setText(country.getName() + "\n" + country.getCapitalCity());
        final String isoCode = country.getIsoCode();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Drawable flagImage = getFlagImage(isoCode);
                if(flagImage != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainCountryOneImage.setImageDrawable(flagImage);
                        }
                    });
                }
            }
        }).start();
        try {
            country = dataManager.getCountryIndicator(country.getIsoCode(),"8.1.2_FINAL.ENERGY.INTENSITY");
            energyRatioGraph.updateCountryOne(country);
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1_RE.CONSUMPTION","8.1.1_FINAL.ENERGY.CONSUMPTION");
            batteryGraph.updateCountryOne(country);
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1.3_HYDRO.CONSUM","3.1.4_BIOFUELS.CONSUM",
                    "3.1.5_WIND.CONSUM","3.1.6_SOLAR.CONSUM","3.1.7_GEOTHERMAL.CONSUM", "3.1.8_WASTE.CONSUM", "3.1.9_BIOGAS.CONSUM","3.1_RE.CONSUMPTION");
            renewableBreakdownContainer.updateCountryOne(country);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onCountryOption2Selected(Country country){
        countryTwoHolder.setText(country.getName() + "\n" + country.getCapitalCity());
        final String isoCode = country.getIsoCode();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Drawable flagImage = getFlagImage(isoCode);
                if(flagImage != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainCountryTwoImage.setImageDrawable(flagImage);
                        }
                    });
                }
            }
        }).start();
        try {
            country = dataManager.getCountryIndicator(country.getIsoCode(),"8.1.2_FINAL.ENERGY.INTENSITY");
            energyRatioGraph.updateCountryTwo(country);
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1_RE.CONSUMPTION","8.1.1_FINAL.ENERGY.CONSUMPTION");
            batteryGraph.updateCountryTwo(country);
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1.3_HYDRO.CONSUM","3.1.4_BIOFUELS.CONSUM",
                    "3.1.5_WIND.CONSUM","3.1.6_SOLAR.CONSUM","3.1.7_GEOTHERMAL.CONSUM", "3.1.8_WASTE.CONSUM", "3.1.9_BIOGAS.CONSUM","3.1_RE.CONSUMPTION");
            renewableBreakdownContainer.updateCountryTwo(country);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Drawable getFlagImage(String iso2Code){
        //This generates the resource Id for that flag image
        int imageResource = getResources().getIdentifier("drawable/"+iso2Code.toLowerCase()+"_img",null,getPackageName());
        return getDrawable(imageResource);
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
