package com.sage.projectwalk.InfoGraphs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by tahmidulislam on 07/12/2015.
 */
public class BreakdownPieChart extends Fragment{
    PieChart pieChartOne;
    PieChart pieChartTwo;
    Country countryOne;
    Country countryTwo;
    ArrayList<Integer> dataYears;
    SeekBar breakdownSeekBar;
    TextView yearTextField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.breakdown_pie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pieChartOne = (PieChart) getView().findViewById(R.id.pieChartOne);
        pieChartTwo = (PieChart) getView().findViewById(R.id.pieChartTwo);

        dataYears = new ArrayList<>();

        yearTextField = (TextView) getView().findViewById(R.id.yearTextField);

        breakdownSeekBar = (SeekBar) getView().findViewById(R.id.breakdownSeekBar);
        breakdownSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yearTextField.setText(dataYears.get(progress)+"");
                if(countryOne != null){
                    refreshCountryOne();
                }
                if(countryTwo != null){
                    refreshCountryTwo();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pieChartOne.setUsePercentValues(true);
        pieChartTwo.setUsePercentValues(true);
        pieChartOne.setDescription("Energy Breakdown");
        pieChartTwo.setDescription("Energy Breakdown");
        pieChartOne.setRotationAngle(0);
        pieChartOne.setRotationEnabled(true);
        pieChartTwo.setRotationAngle(0);
        pieChartTwo.setRotationEnabled(true);


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
        breakdownSeekBar.setProgress(0);
        breakdownSeekBar.setMax(dataYears.size() - 1);

        refreshCountryOne();
    }

    public void updateCountryTwo(Country country){
        countryTwo = country;
        refreshCountryTwo();
    }

    private void refreshCountryOne(){
        if(countryOne == null){
            return;
        }
        int year = 2000;
        if(dataYears.size() >= breakdownSeekBar.getProgress()){
            year = dataYears.get(breakdownSeekBar.getProgress());
            yearTextField.setText(year+"");
            refreshCountry(countryOne,pieChartOne,year);
            Toast.makeText(getActivity(), "Showing data for " + year, Toast.LENGTH_SHORT).show();
        }else{
            return;
        }

    }

    private void refreshCountryTwo(){
        if(countryTwo == null){
            return;
        }
        int year = 2000;
        if(dataYears.size() >= breakdownSeekBar.getProgress()){
            year = dataYears.get(breakdownSeekBar.getProgress());
            yearTextField.setText(year+"");
            refreshCountry(countryTwo,pieChartTwo,year);
            Toast.makeText(getActivity(), "Showing data for " + year, Toast.LENGTH_SHORT).show();
        }else{
            return;
        }

    }

    private void refreshCountry(Country country,PieChart pieChart,int year){
        //Get data from year
        Indicator totalConsump = country.getIndicators().get("3.1_RE.CONSUMPTION");
        Indicator bioFuelIndicator = country.getIndicators().get("3.1.4_BIOFUELS.CONSUM");
        Indicator hydroIndicator = country.getIndicators().get("3.1.3_HYDRO.CONSUM");
        Indicator windIndicator = country.getIndicators().get("3.1.5_WIND.CONSUM");
        Indicator solarIndicator = country.getIndicators().get("3.1.6_SOLAR.CONSUM");
        Indicator geoIndicator = country.getIndicators().get("3.1.7_GEOTHERMAL.CONSUM");
        Indicator wasteIndicator = country.getIndicators().get("3.1.8_WASTE.CONSUM");
        Indicator bioGasIndicator = country.getIndicators().get("3.1.9_BIOGAS.CONSUM");

        try{
            BigDecimal totalC = totalConsump.getData(year);
            float hydro = (hydroIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float bioFuel = (bioFuelIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float wind = (windIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float solar = (solarIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float geo = (geoIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float waste = (wasteIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            float bio = (bioGasIndicator.getData(year)).divide(totalC, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
            ArrayList<Entry> countryOneEntry = new ArrayList<>();
            float other = 100 - (hydro+bioFuel+wind+solar+geo+waste+bio);
            countryOneEntry.add(new Entry(hydro, 0));
            countryOneEntry.add(new Entry(bioFuel, 1));
            countryOneEntry.add(new Entry(wind, 2));
            countryOneEntry.add(new Entry(solar, 3));
            countryOneEntry.add(new Entry(geo, 4));
            countryOneEntry.add(new Entry(waste, 5));
            countryOneEntry.add(new Entry(bio, 6));
            countryOneEntry.add(new Entry(other,7));

            // create pie data set
            PieDataSet dataSet = new PieDataSet(countryOneEntry, "Market Share");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);

            dataSet.setColors(getColors());

            // instantiate pie data object now
            ArrayList<String> xAxis = getXAxisValues();
            xAxis.add("Other");
            PieData data = new PieData(xAxis, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.GRAY);

            pieChart.setData(data);
            pieChart.setDrawSliceText(false);

            // undo all highlights
            pieChart.highlightValues(null);

            // update pie chart
            pieChart.invalidate();

            Log.i("MYAPP", "Data for country "+country.getName()+": Total C: " + totalC + " " + totalConsump.getData(year) + " Hydro: " + hydro + " " + hydroIndicator.getData(year) + " BioFuel: " + bioFuel + " Wind: " + wind + " Solar: " + solar);
        }catch (Exception e){
        }
    }

    private void addData() {
//        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
//
//        for (int i = 0; i < yData.length; i++)
//            yVals1.add(new Entry(yData[i], i));
//
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        for (int i = 0; i < xData.length; i++)
//            xVals.add(xData[i]);
//
//        // create pie data set
//        PieDataSet dataSet = new PieDataSet(yVals1, "Market Share");
//        dataSet.setSliceSpace(3);
//        dataSet.setSelectionShift(5);
//
//        // add many colors
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//        dataSet.setColors(colors);
//
//        // instantiate pie data object now
//        PieData data = new PieData(xVals, dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.GRAY);
//
//        pieChartOne.setData(data);
//
//        // undo all highlights
//        pieChartOne.highlightValues(null);
//
//        // update pie chart
//        pieChartOne.invalidate();
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

    public ArrayList<Integer> getColors(){
        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        return colors;
    }
}
