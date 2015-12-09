package com.sage.projectwalk.InfoGraphs;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;


public class BatteryGraph extends Fragment{
    TextView countryOnePercent;
    TextView countryTwoPercent;
    TextView countryOneTotalEnergy;
    TextView countryTwoTotalEnergy;
    ImageView countryOneBattery;
    ImageView countryTwoBattery;
    Country countryOne;
    Country countryTwo;
    AlphaAnimation blinkAnimation;
    int currentYear;
    private int badColour = Color.parseColor("#FE0000");
    private int mediumColour = Color.parseColor("#0106FF");
    private int goodColour = Color.parseColor("#DEFF00");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.battery_graph_new,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countryOnePercent = (TextView) getView().findViewById(R.id.countryOnePercent);
        countryOneBattery  = (ImageView) getView().findViewById(R.id.countryOneBattery);
        countryOneTotalEnergy = (TextView) getView().findViewById(R.id.countryOneTotalEnergy);

        countryTwoPercent = (TextView) getView().findViewById(R.id.countryTwoPercent);
        countryTwoBattery = (ImageView) getView().findViewById(R.id.countryTwoBattery);
        countryTwoTotalEnergy = (TextView) getView().findViewById(R.id.countryTwoTotalEnergy);
//        batteryYearSeekBar = (SeekBar) getView().findViewById(R.id.batteryYearSeekBar);
//        batteryYearSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(allYears.size() > 0 && allYears.size() >= progress){
//                    currentYear.setText(allYears.get(progress) + "");
//                    refreshBatteryOne();
//                    refreshBatteryTwo();
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1500);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1500);
        fadeOut.setDuration(1500);

        blinkAnimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkAnimation.setDuration(300); // duration - half a second
        blinkAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkAnimation.setRepeatCount(1);
        blinkAnimation.setRepeatMode(Animation.REVERSE);

    }

    public void setCountryOne(Country country){
        countryOne = country;
    }

    public void setCountryTwo(Country country){
        countryTwo = country;
    }

    public void refresh(int year){
        currentYear = year;
        refreshBatteryOne();
        refreshBatteryTwo();
    }

    private void refreshBatteryOne(){
        if(countryOne != null){
            refreshBattery(countryOne,countryOnePercent,countryOneTotalEnergy);
        }
    }

    private void refreshBatteryTwo(){
        if(countryTwo != null){
            refreshBattery(countryTwo,countryTwoPercent,countryTwoTotalEnergy);
        }
    }


    private void refreshBattery(Country country,TextView countryPercent,TextView countryTotalEnergy){
        ///Get indicator objects
        Indicator consumptionIndicator = country.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator finalConsumptionIndicator = country.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION");
        try{
            int yearSelected = currentYear;
            int consumptionAmount = consumptionIndicator.getData(yearSelected).intValue();
            int finalAmount = finalConsumptionIndicator.getData(yearSelected).intValue();
            if(consumptionIndicator.getData(yearSelected) != null
                    && finalConsumptionIndicator.getData(yearSelected) != null){
                float p = (int) consumptionAmount * 100f / finalAmount;
                int percentage = (int) p;
                if(percentage >= 75){
                    countryPercent.setTextColor(goodColour);
                }else
                if(percentage >= 50){
                    countryPercent.setTextColor(mediumColour);
                }else{
                    countryPercent.setTextColor(badColour);
                }
                countryPercent.setText(percentage + "%");
                if(country.equals(countryOne)){
                    updateCountryImage(percentage,1);
                }else{
                    updateCountryImage(percentage,2);
                }
                countryTotalEnergy.setText(finalAmount + "");
            }else{
                Drawable missingIcon = getActivity().getDrawable(R.drawable.missingdata);
                if(country.equals(countryOne)){
                    countryOneBattery.setImageDrawable(missingIcon);
                }else{
                    countryTwoBattery.setImageDrawable(missingIcon);
                }
            }

        }catch (Exception e){
            Toast.makeText(getActivity(), "No data "+ country.getName(), Toast.LENGTH_SHORT);
        }
    }

    private void updateCountryImage(int percentage,int batteryNumber){
        percentage = roundUp(percentage);
        if(percentage <= 100 && percentage >= 0){
            try{
                //This generates the resource Id for that flag image
                int imageResource = getActivity().getResources().getIdentifier("drawable/battery"+percentage,null,getActivity().getPackageName());
                Drawable batteryImage = getActivity().getDrawable(imageResource);
                if(batteryNumber == 1){
                    countryOneBattery.setImageDrawable(batteryImage);
                    countryOneBattery.startAnimation(blinkAnimation);
                }else{
                    countryTwoBattery.setImageDrawable(batteryImage);
                    countryTwoBattery.startAnimation(blinkAnimation);
                }
            }catch (Exception e){
                Log.e("MYAPP","Error loading image");
            }

        }
    }

    public int roundUp(int n) {
        return (n + 4) / 5 * 5;
    }




}
