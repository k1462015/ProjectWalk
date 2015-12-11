package com.sage.projectwalk;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.BreakdownPieChart;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.SlideOutPanel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SlideOutPanel.CountryListListener{
    DataManager dataManager;
    SlideOutPanel menuFragment;
    TextView countryOneHolder;
    TextView countryTwoHolder;
    ImageView mainCountryOneImage;
    ImageView mainCountryTwoImage;
    BatteryGraph batteryGraph;
    RelativeLayout mainActivityRoot;
    BreakdownPieChart breakdownPieChart;
    SeekBar unifiedSeekBar;
    ArrayList<Integer> allYears;
    TextView unifiedYear;
    FactCards factCards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseUi();
    }

    public void initialiseUi(){
        dataManager = new DataManager(this);

        allYears = new ArrayList<>();
        unifiedYear = (TextView) findViewById(R.id.unifiedYear);
        unifiedSeekBar = (SeekBar) findViewById(R.id.unifiedSeekBar);
        unifiedSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.parseColor("#DEFF00"), PorterDuff.Mode.MULTIPLY));
        unifiedSeekBar.setOnSeekBarChangeListener(new UnifiedSeekBarListener());
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

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/chalk.ttf");
        Button syncButton = (Button) findViewById(R.id.syncButton);
        syncButton.setTypeface(tf);
        countryOneHolder.setTypeface(tf);
        countryTwoHolder.setTypeface(tf);
        unifiedYear.setTypeface(tf);

        //Gets required fragment stuff
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        //Adds all fragments to main activity
        batteryGraph = new BatteryGraph();
        factCards = new FactCards();
        menuFragment = new SlideOutPanel();
        breakdownPieChart = new BreakdownPieChart();

        //Adds all fragments to corresponding containers
        fragmentTransaction.add(R.id.batteryGraphContainer,batteryGraph);
        fragmentTransaction.add(R.id.factCardsContainer,factCards);
        fragmentTransaction.add(R.id.renewableSourcesContainer, breakdownPieChart);
        fragmentTransaction.add(R.id.out, menuFragment);
        fragmentTransaction.hide(menuFragment);
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.commit();
    }

    public void syncData(View view){
        dataManager.synchronizeData();
    }



    private class UnifiedSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(allYears.size() >= progress && allYears.size() != 0){
                refreshAllFragments(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public void refreshAllFragments(int progress){
        if(allYears.size() > 0){
            unifiedYear.setText(allYears.get(progress)+"");
            batteryGraph.refresh(allYears.get(progress));
            breakdownPieChart.refresh(allYears.get(progress));
        }else{
            unifiedYear.setText("Please select a different country for option 1");
            breakdownPieChart.refresh(99999);
            batteryGraph.refresh(99999);
        }
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
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1_RE.CONSUMPTION","8.1.1_FINAL.ENERGY.CONSUMPTION");
            batteryGraph.setCountryOne(country);
            extractYears(country, "3.1_RE.CONSUMPTION");
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1.3_HYDRO.CONSUM","3.1.4_BIOFUELS.CONSUM",
                    "3.1.5_WIND.CONSUM","3.1.6_SOLAR.CONSUM","3.1.7_GEOTHERMAL.CONSUM", "3.1.8_WASTE.CONSUM", "3.1.9_BIOGAS.CONSUM","3.1_RE.CONSUMPTION");
            breakdownPieChart.setCountryOne(country);
            refreshAllFragments(0);
            unifiedSeekBar.setProgress(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractYears(Country country,String indicatorName){
        ///Use one of the indicators to extract available years
        Indicator consumptionIndicator = country.getIndicators().get(indicatorName);
        //Get all Year values
        Set<Integer> dataYears = consumptionIndicator.getIndicatorData().keySet();
        allYears = new ArrayList<>();
        for (Integer year:dataYears){
            allYears.add(year);
        }
        Collections.sort(allYears);
        unifiedSeekBar.setMax(allYears.size() - 1);
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
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1_RE.CONSUMPTION","8.1.1_FINAL.ENERGY.CONSUMPTION");
            batteryGraph.setCountryTwo(country);
            country = dataManager.getCountryIndicator(country.getIsoCode(),"3.1.3_HYDRO.CONSUM","3.1.4_BIOFUELS.CONSUM",
                    "3.1.5_WIND.CONSUM","3.1.6_SOLAR.CONSUM","3.1.7_GEOTHERMAL.CONSUM", "3.1.8_WASTE.CONSUM", "3.1.9_BIOGAS.CONSUM","3.1_RE.CONSUMPTION");
            breakdownPieChart.setCountryTwo(country);
            refreshAllFragments(0);
            unifiedSeekBar.setProgress(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Drawable getFlagImage(String iso2Code){
        //This generates the resource Id for that flag image
        int imageResource = getResources().getIdentifier("drawable/" + iso2Code.toLowerCase() + "_img", null, getPackageName());
        return getDrawable(imageResource);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!menuFragment.isHidden()){
            openSlideFragment();
        }
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    public TextView getCountryOneHolder() {
        return countryOneHolder;
    }


    public SeekBar getUnifiedSeekBar() {
        return unifiedSeekBar;
    }

    public TextView getCountryTwoHolder() {
        return countryTwoHolder;
    }



}
