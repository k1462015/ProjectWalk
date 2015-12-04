package com.sage.projectwalk.InfoGraphs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.util.Set;


public class BatteryGraph extends Fragment{
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
        //Initialises two example countries
        //Once you have completed the implemention behind this fragment
        //I will create a link between the activity and this fragment
        //So the fragment changes data according to selected country
        //DO NOT CHANGE
        //<<<<<<<<<<EXAMPLE>>>>>>>>>>>>>>>>//
//        countryOne = new Country();
//        countryOne.setName("United States");
//        countryOne.setIsoCode("US");
//        countryTwo = new Country();
//        countryTwo.setName("Russia");
//        countryTwo.setIsoCode("RU");

//        Indicator indicator = new Indicator();
//        indicator.setName("3.1.3_HYDRO.CONSUM");
//        indicator.addData(2015, 34395.0);
//        indicator.addData(2014, 2434.2);
//        indicator.addData(2013, 323123.2);
//        indicator.addData(2012, 321321.0);

//        countryOne.addIndicator(indicator);
//        countryTwo.addIndicator(indicator);
        //<<<<<<<<<<EXAMPLE>>>>>>>>>>>>>>>>//
        //This method should be called to create the graph
        //createGraph();
    }

    /**
     * This method should use the 2 country objects
     * in the field variables, extract data from them
     * And then create 2 graphs (one for each country)
     */
    public void createGraph(){
        //Example of how to extract data
        //<<<<EXAMPLE>>>
        ///Let's say I want data for the indicator 3.1.3_HYDRO.CONSUM of countryOne
        ///of the year 2014
//        Double value = countryOne.getIndicators().get("3.1.3_HYDRO.CONSUM").getData(2014);

        //Let's say I want all the possible years of data the indicator 3.1.3_HYDRO.CONSUM has
//        Set<Integer> allYear = countryOne.getIndicators().get("3.1.3_HYDRO.CONSUM").getIndicatorData().keySet();
        //And then to extract the years, use enhanced for loop
//        for (Integer year:allYear){
//            //Log.i("MYAPP",year);
//        }
        //<<<<EXAMPLE>>>

    }
}
