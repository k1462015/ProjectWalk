package com.sage.projectwalk.InfoGraphs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.util.ArrayList;

/**
 * Created by tahmidulislam on 27/11/2015.
 */
public class RenewableBreakdownContainer extends Fragment{
    //The two countried and all the inidicator info that is required for the data to be represented correctly
    Country countryA;
    Country countryB;
    Indicator TotalRenewableEnergyConsumed = new Indicator();
    Indicator indicator = null;


    //Easy access to the indicator strings for when fetching the data. Array for all the indicators and a stand alone string for the total renewable consumption
    String indicators[] = new String[]{"3.1.3_HYDRO.CONSUM","3.1.4_BIOFUELS.CONSUM","3.1.5_WIND.CONSUM","3.1.6_SOLAR.CONSUM","3.1.7_GEOTHERMAL.CONSUM", "3.1.8_WASTE.CONSUM", "3.1.9_BIOGAS.CONSUM"};
    String renewableTotalEnergy = "3.1_RE.CONSUMPTION";
    //the data will be changed according to the year of the spinner, needs a listener class to update stuff
    SeekBar seekBar = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.renewable_breakdown_graph,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO:set here the countries that are selected in the slide out
        countryA = new Country();
        countryA.setName("United States");
        countryA.setIsoCode("US");
        countryB = new Country();
        countryB.setName("Russia");
        countryB.setIsoCode("RU");
        //TODO:set all the inidicators to the right Data

        for(int i = 0; i< indicators.length; i++){
            indicator = new Indicator();
            indicator.setId(indicators[i]);
            indicator.addData(2012, 321321.0);

            countryA.addIndicator(indicator);
            countryB.addIndicator(indicator);
        }

        seekBar =  (SeekBar) getView().findViewById(R.id.YearOfData);
        seekBar.setMax(22);

        final HorizontalBarChart RenewableChart = (HorizontalBarChart) getView().findViewById(R.id.BreakDownChart);
        BarData barData = new BarData(getXAxisValues(),getDataSet());
        RenewableChart.setData(barData);
        RenewableChart.animateXY(2000, 2000);
        //final String[] description = {"1990"};
        RenewableChart.setDescription("");
        RenewableChart.invalidate();

        final TextView textView = (TextView) getView().findViewById(R.id.YearTextField);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                //to make sure that the selection is never below 1990, the earliest we have for these indicators
                //description[0] = "Year: " + (1990 + progress);
                textView.setText(Integer.toString(1990 + progress));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    //need it to be so that it gets the data from the indicators, at the moment it is hard coded
    private ArrayList<BarDataSet> getDataSet(){
        ArrayList<BarDataSet> dataSets = null;

        //get the total Renewable Energy before the for loop TODO get rid of the 5000000 and add in the // bits
        double totalREA = 5000000;//countryA.getIndicators().get(renewableTotalEnergy).getData(seekBar.getProgress());
        double totalREB = 5000000;//countryB.getIndicators().get(renewableTotalEnergy).getData(seekBar.getProgress());

        ArrayList<BarEntry> valuesCountryA = new ArrayList<>();
        ArrayList<BarEntry> valuesCountryB = new ArrayList<>();
        for(int i =0; i < indicators.length;i++){
           double valueA = countryA.getIndicators().get(indicators[i]).getData(2012);
           float a = (float)(valueA/totalREA) *100 ; //have to convert to float as that BarEntrys first param also calculating percentage here
           BarEntry A = new BarEntry(a,i);
           valuesCountryA.add(i,A);  //adding to index I so as to override each time SeekBar changes value

           Double valueB = countryB.getIndicators().get(indicators[i]).getData(2012);
           float b = (float)(valueB/totalREB)*100; ////have to convert to float as that BarEntrys first param also calculating percentage here
           BarEntry B = new BarEntry(b,i);
           valuesCountryB.add(i,B); //adding to index I so as to override each time SeekBar changes value
        }

        BarDataSet barDataSetA = new BarDataSet(valuesCountryA, countryA.getName());
        barDataSetA.setColor(Color.rgb(0,0,155));

        BarDataSet barDataSetB = new BarDataSet(valuesCountryB, countryB.getName());
        barDataSetB.setColor(Color.rgb(155,0,0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSetA);
        dataSets.add(barDataSetB);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Hydro Energy Consumption %");
        xAxis.add("Liquid Biofuel Consumption %");
        xAxis.add("Wind Energy Consumption %");
        xAxis.add("Solar Energy Consumption %");
        xAxis.add("Geothermal Energy Consumption %");
        xAxis.add("Waster Energy Consumption %");
        xAxis.add("Biogas Consumption %");
        return xAxis;
    }


}
