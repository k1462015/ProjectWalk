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
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.util.Set;


public class BatteryGraph extends Fragment{
    TextView countryOnePercent;
    TextView countryTwoPercent;
    ImageView countryOneBattery;
    ImageView countryTwoBattery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.battery_graph,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        countryOnePercent = (TextView) getView().findViewById(R.id.countryOnePercent);
        countryOneBattery  = (ImageView) getView().findViewById(R.id.countryOneBattery);

        countryTwoPercent = (TextView) getView().findViewById(R.id.countryTwoPercent);
        countryTwoBattery = (ImageView) getView().findViewById(R.id.countryTwoBattery);



    }

    public void updateCountryOne(Country country){
        ///Example for 2002
        Indicator consumptionIndicator = country.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator finalConsumptionIndicator = country.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION");

        try{
            int consumptionAmount = consumptionIndicator.getData(2002).intValue();
            int finalAmount = finalConsumptionIndicator.getData(2002).intValue();
            Log.i("MYAPP","Consumption Ammount: "+consumptionAmount);
            Log.i("MYAPP","Fianl Amount: "+finalAmount);
            float p = (int) consumptionAmount * 100f / finalAmount;
            int percentage = (int) p;
            countryOnePercent.setText(percentage+"%");
            updateCountryImageOne(percentage);
        }catch (Exception e){
            Toast.makeText(getActivity(), "No data for 2002 " + country.getName(), Toast.LENGTH_SHORT);
        }
    }

    public void updateCountryTwo(Country country){
        ///Example for 2002
        Indicator consumptionIndicator = country.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator finalConsumptionIndicator = country.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION");
        try{
            int consumptionAmount = consumptionIndicator.getData(2002).intValue();
            int finalAmount = finalConsumptionIndicator.getData(2002).intValue();
            Log.i("MYAPP","Consumption Ammount: "+consumptionAmount);
            Log.i("MYAPP","Fianl Amount: "+finalAmount);
            float p = (int) consumptionAmount * 100f / finalAmount;
            int percentage = (int) p;

            countryTwoPercent.setText(percentage+"%");
            updateCountryImageTwo(percentage);
        }catch (Exception e){
            Toast.makeText(getActivity(), "No data for 2002 " + country.getName(), Toast.LENGTH_SHORT);
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
