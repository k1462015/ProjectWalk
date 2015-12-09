package com.sage.projectwalk.InfoGraphs;

import android.graphics.Color;
import android.graphics.Typeface;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
public class BreakdownPieChart extends Fragment implements OnChartValueSelectedListener{
    PieChart pieChartOne;
    PieChart pieChartTwo;
    Country countryOne;
    Country countryTwo;
    ArrayList<Integer> dataYears;
    SeekBar breakdownSeekBar;
    ImageView breakdownMissingOne;
    ImageView breakdownMissingTwo;
    ArrayList<String> xAxis = new ArrayList<>();
    Typeface mTf;
    TextView breakdownYear;
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

        xAxis.add("Hydro Energy");
        xAxis.add("Liquid Biofuel");
        xAxis.add("Wind Energy");
        xAxis.add("Solar Energy");
        xAxis.add("Geothermal Energy");
        xAxis.add("Waste Energy");
        xAxis.add("Biogas Energy");
        xAxis.add("Other");

        dataYears = new ArrayList<>();
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "android_7.ttf");
        breakdownYear = (TextView) getView().findViewById(R.id.breakdownYear);
        breakdownMissingOne = (ImageView) getView().findViewById(R.id.breakdownMissingOne);
        breakdownMissingTwo = (ImageView) getView().findViewById(R.id.breakdownMissingTwo);

        breakdownSeekBar = (SeekBar) getView().findViewById(R.id.breakdownSeekBar);
        breakdownSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (countryOne != null) {
                    refreshCountryOne();
                } else {
                    Toast.makeText(getActivity(), "Country 1 is not selected", Toast.LENGTH_SHORT).show();
                }
                if (countryTwo != null) {
                    refreshCountryTwo();
                    Toast.makeText(getActivity(), "Country 2 is not selected", Toast.LENGTH_SHORT).show();
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
        pieChartOne.setDescription("");
        pieChartOne.setRotationAngle(0);
        pieChartOne.setRotationEnabled(true);
        pieChartOne.setHighlightPerTapEnabled(true);
        pieChartOne.setTouchEnabled(true);
        pieChartOne.setOnChartValueSelectedListener(this);
        pieChartOne.setElevation(20);

        pieChartTwo.setUsePercentValues(true);
        pieChartTwo.setDescription("");
        pieChartTwo.setRotationAngle(0);
        pieChartTwo.setRotationEnabled(true);
        pieChartTwo.setHighlightPerTapEnabled(true);
        pieChartTwo.setTouchEnabled(true);
        pieChartTwo.setOnChartValueSelectedListener(this);
        pieChartTwo.setElevation(20);

    }

    public void animateAndFixLegend(){
        pieChartOne.getLegend().setEnabled(false);
        pieChartTwo.getLegend().setEnabled(false);
        pieChartOne.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChartTwo.animateY(1400, Easing.EasingOption.EaseInOutQuad);

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
        if(countryOne != null) {
            if (dataYears.size() > 0 && dataYears.size() >= breakdownSeekBar.getProgress()) {
                int year = dataYears.get(breakdownSeekBar.getProgress());
                refreshCountry(countryOne, pieChartOne, year);
            }
        }
    }

    private void refreshCountryTwo(){
        if(countryTwo != null) {
            if (dataYears.size() > 0 && dataYears.size() >= breakdownSeekBar.getProgress()) {
                int year = dataYears.get(breakdownSeekBar.getProgress());
                refreshCountry(countryTwo, pieChartTwo, year);
            }
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
            float hydro = divideBigDecimal(hydroIndicator.getData(year), totalC);
            float bioFuel = divideBigDecimal(bioFuelIndicator.getData(year),totalC);
            float wind = divideBigDecimal(windIndicator.getData(year), totalC);
            float solar = divideBigDecimal(solarIndicator.getData(year), totalC);
            float geo = divideBigDecimal(geoIndicator.getData(year), totalC);
            float waste = divideBigDecimal(wasteIndicator.getData(year),totalC);
            float bio = divideBigDecimal(bioGasIndicator.getData(year),totalC);
            ArrayList<Entry> countryOneEntry = new ArrayList<>();
            ArrayList<String> newXAxis = new ArrayList<>();

            float other = 100 - (hydro+bioFuel+wind+solar+geo+waste+bio);
            ArrayList<Integer> colors = new ArrayList<>();
            if(hydro != 0){
                countryOneEntry.add(new Entry(hydro, 0));
                newXAxis.add(xAxis.get(0));
                colors.add(Color.parseColor("#0106FF"));
            }
            if(bioFuel != 0){
                countryOneEntry.add(new Entry(bioFuel, 1));
                newXAxis.add(xAxis.get(1));
                colors.add(Color.parseColor("#DEFF00"));
            }
            if(wind != 0){
                countryOneEntry.add(new Entry(wind, 2));
                newXAxis.add(xAxis.get(2));
                colors.add(Color.parseColor("#FE0000"));
            }
            if(solar != 0){
                countryOneEntry.add(new Entry(solar, 3));
                newXAxis.add(xAxis.get(3));
                colors.add(Color.parseColor("#00EAFF"));
            }
            if(geo != 0){
                countryOneEntry.add(new Entry(geo, 4));
                newXAxis.add(xAxis.get(4));
                colors.add(Color.parseColor("#C4C486"));
            }
            if(waste != 0){
                countryOneEntry.add(new Entry(waste, 5));
                newXAxis.add(xAxis.get(5));
                colors.add(Color.parseColor("#6C6B69"));
            }
            if(bio != 0){
                countryOneEntry.add(new Entry(bio, 6));
                newXAxis.add(xAxis.get(6));
                colors.add(Color.parseColor("#00ff99"));
            }
            if(other != 0){
                countryOneEntry.add(new Entry(other,7));
                newXAxis.add("Other");
                colors.add(Color.parseColor("#000000"));
            }


            // create pie data set
            PieDataSet dataSet = new PieDataSet(countryOneEntry, "");
            dataSet.setSliceSpace(6);
            dataSet.setSelectionShift(5);


            dataSet.setColors(colors );

            // instantiate pie data object now

            PieData data = new PieData(xAxis, dataSet);
            data.setValueTypeface(mTf);
            data.setValueTextColor(Color.parseColor("#FFFFFF"));
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(15f);

            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColorTransparent(true);

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.setHoleRadius(40f);
            pieChart.setTransparentCircleRadius(50f);

            breakdownYear.setText(year +"");

            pieChart.setData(data);
            pieChart.setDrawSliceText(false);

            // undo all highlights
            pieChart.highlightValues(null);

            // update pie chart
            pieChart.invalidate();
            pieChart.setVisibility(View.VISIBLE);
            if(country.equals(countryOne)){
                breakdownMissingOne.setVisibility(View.INVISIBLE);
            }else{
                breakdownMissingTwo.setVisibility(View.INVISIBLE);
            }
            animateAndFixLegend();
        }catch (Exception e){
            pieChart.setVisibility(View.INVISIBLE);
            if(country.equals(countryOne)){
                breakdownMissingOne.setVisibility(View.VISIBLE);
            }else{
                breakdownMissingTwo.setVisibility(View.VISIBLE);
            }
        }
    }

    public float divideBigDecimal(BigDecimal firstNumber,BigDecimal secondNumber){
        return (firstNumber).divide(secondNumber, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal("100")).floatValue();
    }


    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        Toast.makeText(getActivity(), entry.getVal()+"% "+xAxis.get(entry.getXIndex()),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
}
