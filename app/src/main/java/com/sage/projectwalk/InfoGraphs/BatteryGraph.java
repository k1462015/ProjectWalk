package com.sage.projectwalk.InfoGraphs;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;


public class BatteryGraph extends Fragment{
    TextView countryOnePercent;
    TextView countryTwoPercent;
    TextView countryOneName;
    TextView countryTwoName;
    TextView currentYear;
    ImageView countryOneBattery;
    ImageView countryTwoBattery;
    SeekBar batteryYearSeekBar;
    ArrayList<Integer> allYears;
    Country countryOne;
    Country countryTwo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.battery_graph,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countryOneName = (TextView) getView().findViewById(R.id.countryOneName);
        countryOnePercent = (TextView) getView().findViewById(R.id.countryOnePercent);
        countryOneBattery  = (ImageView) getView().findViewById(R.id.countryOneBattery);

        countryTwoName = (TextView) getView().findViewById(R.id.countryTwoName);
        countryTwoPercent = (TextView) getView().findViewById(R.id.countryTwoPercent);
        countryTwoBattery = (ImageView) getView().findViewById(R.id.countryTwoBattery);

        batteryYearSeekBar = (SeekBar) getView().findViewById(R.id.batteryYearSeekBar);
        batteryYearSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(allYears.size() > 0 && allYears.size() >= progress){
                    currentYear.setText(allYears.get(progress) + "");
                    refreshBatteryOne();
                    refreshBatteryTwo();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        currentYear = (TextView) getView().findViewById(R.id.currentYear);

        allYears = new ArrayList<>();
        //User selects a country
        //This provides a set of years
        //Use these years to adjust scrollbar
        //Set the current year to the lowest year
        //Any increment in the scrollbar should go to the next available year
    }

    public void updateCountryOne(Country country){
        countryOne = country;
        ///Get indicator objects
        Indicator consumptionIndicator = countryOne.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator finalConsumptionIndicator = countryOne.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION");

        //Get all Year values
        Set<Integer> dataYears = consumptionIndicator.getIndicatorData().keySet();
        allYears = new ArrayList<>();
        for (Integer year:dataYears){
            allYears.add(year);
        }
        Collections.sort(allYears);

        batteryYearSeekBar.setProgress(0);
        batteryYearSeekBar.setMax(allYears.size() - 1);
        if(allYears.size() > 1){
            currentYear.setText(allYears.get(batteryYearSeekBar.getProgress()) + "");
            refreshBatteryOne();
            refreshBatteryTwo();
        }else{

        }

    }

    public void updateCountryTwo(Country country){
        countryTwo = country;
        refreshBatteryTwo();
        batteryYearSeekBar.setProgress(0);
        currentYear.setText(allYears.get(batteryYearSeekBar.getProgress()) + "");
    }

    private void refreshBatteryOne(){
        ///Get indicator objects
        Indicator consumptionIndicator = countryOne.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator finalConsumptionIndicator = countryOne.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION");

        try{
            int yearSelected = allYears.get(batteryYearSeekBar.getProgress());
            int consumptionAmount = consumptionIndicator.getData(yearSelected).intValue();
            int finalAmount = finalConsumptionIndicator.getData(yearSelected).intValue();
            Log.i("MYAPP","Consumption Ammount: "+consumptionAmount);
            Log.i("MYAPP","Fianl Amount: "+finalAmount);
            float p = (int) consumptionAmount * 100f / finalAmount;
            int percentage = (int) p;
            countryOnePercent.setText(percentage+"%");
            updateCountryImageOne(percentage);
            countryOneName.setText(countryOne.getName());
        }catch (Exception e){
            Toast.makeText(getActivity(), "No data for 2002 " + countryOne.getName(), Toast.LENGTH_SHORT);
        }
    }


    private void refreshBatteryTwo(){
        if(countryTwo != null){
            ///Get indicator objects
            Indicator consumptionIndicator = countryTwo.getIndicators().get("3.1_RE.CONSUMPTION");
            Indicator finalConsumptionIndicator = countryTwo.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION");

            try{
                int yearSelected = allYears.get(batteryYearSeekBar.getProgress());
                int consumptionAmount = consumptionIndicator.getData(yearSelected).intValue();
                int finalAmount = finalConsumptionIndicator.getData(yearSelected).intValue();
                Log.i("MYAPP","Consumption Ammount: "+consumptionAmount);
                Log.i("MYAPP","Fianl Amount: "+finalAmount);
                float p = (int) consumptionAmount * 100f / finalAmount;
                int percentage = (int) p;
                countryTwoPercent.setText(percentage+"%");
                updateCountryImageTwo(percentage);
                countryTwoName.setText(countryTwo.getName());
            }catch (Exception e){
                Toast.makeText(getActivity(), "No data for 2002 " + countryOne.getName(), Toast.LENGTH_SHORT);
            }
        }
    }

    private void updateCountryImageOne(int percentage){
        percentage = roundUp(percentage);
        if(percentage <= 100 && percentage >= 0){
            try{
                //This generates the resource Id for that flag image
                int imageResource = getActivity().getResources().getIdentifier("drawable/battery"+percentage,null,getActivity().getPackageName());
                Drawable batteryImage = getActivity().getResources().getDrawable(imageResource);
                countryOneBattery.setImageDrawable(batteryImage);
            }catch (Exception e){
                Log.e("MYAPP","Error loading image");
            }

        }
    }



    private void updateCountryImageTwo(int percentage){
        percentage = roundUp(percentage);

        if(percentage <= 100 && percentage >= 0){
            try{
                //This generates the resource Id for that flag image
                int imageResource = getActivity().getResources().getIdentifier("drawable/battery"+percentage,null,getActivity().getPackageName());
                Drawable batteryImage = getActivity().getResources().getDrawable(imageResource);
                countryTwoBattery.setImageDrawable(batteryImage);
            }catch (Exception e){
                Log.e("MYAPP","Error loading image");
            }

        }


    }

    public int roundUp(int n) {
        return (n + 4) / 5 * 5;
    }




}
