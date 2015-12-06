package com.sage.projectwalk.InfoGraphs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by tahmidulislam on 27/11/2015.
 */
public class RenewableBreakdownContainer extends Fragment{
    ArrayList<Integer> dataYears;
    HorizontalBarChart RenewableChart;
    Country countryOne;
    Country countryTwo;
    TextView YearTextField;
    SeekBar seekBar = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.renewable_breakdown_graph,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataYears = new ArrayList<>();
        YearTextField = (TextView) getView().findViewById(R.id.YearTextField);
        seekBar = (SeekBar) getView().findViewById(R.id.YearOfData);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshBarChart();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RenewableChart = (HorizontalBarChart) getView().findViewById(R.id.BreakDownChart);
    }

    public void updateCountryOne(Country country){
        countryOne = country;
        //Extract years from the data
        Indicator hydroIndicator = country.getIndicators().get("3.1.3_HYDRO.CONSUM");

        //Get all Year values
        Set<Integer> years = hydroIndicator.getIndicatorData().keySet();
        dataYears = new ArrayList<>();
        for (Integer year:years){
            dataYears.add(year);
        }
        Collections.sort(dataYears);
        seekBar.setProgress(0);
        seekBar.setMax(dataYears.size() - 1);

        refreshBarChart();
    }

    public void updateCountryTwo(Country country){
        countryTwo = country;
        refreshBarChart();
    }

    public void refreshBarChart(){
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> countryOneData = getCountryOneData();
        if(countryOneData != null){
            BarDataSet countryOneBarDataSet = new BarDataSet(getCountryOneData(), countryOne.getName());
            countryOneBarDataSet.setColor(Color.parseColor("#636161"));
            dataSets.add(countryOneBarDataSet);
        }
        ArrayList<BarEntry> countryTwoData = getCountryTwoData();
        if(countryTwoData != null){
            BarDataSet countryTwoBarDataSet = new BarDataSet(getCountryTwoData(), countryTwo.getName());
            countryTwoBarDataSet.setColor(Color.rgb(0, 155, 0));
            dataSets.add(countryTwoBarDataSet);
        }
        if(dataSets.size() > 0){
            BarData barData = new BarData(getXAxisValues(),dataSets);
            RenewableChart.setData(barData);
            RenewableChart.animateXY(2000, 2000);
        }

    }

    public ArrayList<BarEntry> getCountryOneData(){
        if(countryOne == null){
            return null;
        }
        int year = 2000;
        if(dataYears.size() >= seekBar.getProgress()){
            year = dataYears.get(seekBar.getProgress());
            YearTextField.setText(year+"");
            Toast.makeText(getActivity(),"Showing data for "+year,Toast.LENGTH_SHORT).show();
        }else{
            return null;
        }
        //Get data from year
        Indicator totalConsump = countryOne.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator bioFuelIndicator = countryOne.getIndicators().get("3.1.4_BIOFUELS.CONSUM");
        Indicator hydroIndicator = countryOne.getIndicators().get("3.1.3_HYDRO.CONSUM");
        Indicator windIndicator = countryOne.getIndicators().get("3.1.5_WIND.CONSUM");
        Indicator solarIndicator = countryOne.getIndicators().get("3.1.6_SOLAR.CONSUM");
        Indicator geoIndicator = countryOne.getIndicators().get("3.1.7_GEOTHERMAL.CONSUM");
        Indicator wasteIndicator = countryOne.getIndicators().get("3.1.8_WASTE.CONSUM");
        Indicator bioGasIndicator = countryOne.getIndicators().get("3.1.9_BIOGAS.CONSUM");

        try{
            BigDecimal totalC = totalConsump.getData(year);
            float hydro = (hydroIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float bioFuel = (bioFuelIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float wind = (windIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float solar = (solarIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float geo = (geoIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float waste = (wasteIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float bio = (bioGasIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            ArrayList<BarEntry> countryOneBarEntry = new ArrayList<>();
            countryOneBarEntry.add(new BarEntry(hydro,0));
            countryOneBarEntry.add(new BarEntry(bioFuel,1));
            countryOneBarEntry.add(new BarEntry(wind,2));
            countryOneBarEntry.add(new BarEntry(solar,3));
            countryOneBarEntry.add(new BarEntry(geo,4));
            countryOneBarEntry.add(new BarEntry(waste,5));
            countryOneBarEntry.add(new BarEntry(bio,6));
            Log.i("MYAPP", "Data for country one: Total C: " + totalC +" "+totalConsump.getData(year)+ " Hydro: " + hydro +" "+hydroIndicator.getData(year)+ " BioFuel: " + bioFuel + " Wind: " + wind + " Solar: " + solar);
            return countryOneBarEntry;
        }catch (Exception e){
            return null;
        }
    }

    public ArrayList<BarEntry> getCountryTwoData(){
        if(countryTwo == null){
            return null;
        }
        int year = 2000;
        if(dataYears.size() >= seekBar.getProgress()){
            year = dataYears.get(seekBar.getProgress());
            YearTextField.setText(year+"");
            Toast.makeText(getActivity(),"Showing data for "+year,Toast.LENGTH_SHORT).show();
        }else{
            return null;
        }
        //Get data from year
        Indicator totalConsump = countryTwo.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator bioFuelIndicator = countryTwo.getIndicators().get("3.1.4_BIOFUELS.CONSUM");
        Indicator hydroIndicator = countryTwo.getIndicators().get("3.1.3_HYDRO.CONSUM");
        Indicator windIndicator = countryTwo.getIndicators().get("3.1.5_WIND.CONSUM");
        Indicator solarIndicator = countryTwo.getIndicators().get("3.1.6_SOLAR.CONSUM");
        Indicator geoIndicator = countryTwo.getIndicators().get("3.1.7_GEOTHERMAL.CONSUM");
        Indicator wasteIndicator = countryTwo.getIndicators().get("3.1.8_WASTE.CONSUM");
        Indicator bioGasIndicator = countryTwo.getIndicators().get("3.1.9_BIOGAS.CONSUM");

        try{
            BigDecimal totalC = totalConsump.getData(year);
            float hydro = (hydroIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float bioFuel = (bioFuelIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float wind = (windIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float solar = (solarIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float geo = (geoIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float waste = (wasteIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float bio = (bioGasIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            ArrayList<BarEntry> countryTwoBarEntry = new ArrayList<>();
            countryTwoBarEntry.add(new BarEntry(hydro,0));
            countryTwoBarEntry.add(new BarEntry(bioFuel,1));
            countryTwoBarEntry.add(new BarEntry(wind,2));
            countryTwoBarEntry.add(new BarEntry(solar,3));
            countryTwoBarEntry.add(new BarEntry(geo,4));
            countryTwoBarEntry.add(new BarEntry(waste,5));
            countryTwoBarEntry.add(new BarEntry(bio,6));
            Log.i("MYAPP", "Data for country two: Total C: " + totalC +" "+totalConsump.getData(year)+ " Hydro: " + hydro +" "+hydroIndicator.getData(year)+ " BioFuel: " + bioFuel + " Wind: " + wind + " Solar: " + solar);
            return countryTwoBarEntry;
        }catch (Exception e){
            return null;
        }
    }

    public int divide(int first,int second){
        float p = (int) first * 100f / second;
        return (int) p;
    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Hydro Energy Consumption %");
        xAxis.add("Liquid Biofuel Consumption %");
        xAxis.add("Wind Energy Consumption %");
        xAxis.add("Solar Energy Consumption %");
        xAxis.add("Geothermal Energy Consumption %");
        xAxis.add("Waste Energy Consumption %");
        xAxis.add("Biogas Consumption %");
        return xAxis;
    }


}
