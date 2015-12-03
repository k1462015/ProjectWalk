package com.sage.projectwalk.InfoGraphs;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;


import com.github.mikephil.charting.data.LineData;

import com.github.mikephil.charting.data.LineDataSet;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.util.ArrayList;


/**
 * Created by tahmidulislam on 27/11/2015.
 */

public class EnergyRatioGraph extends Fragment {

    private RelativeLayout mainLayout;
    private LineChart mChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.energy_ratio,container,false);

        mainLayout = (RelativeLayout) v.findViewById(R.id.mainLayout);




        //create line chart
        mChart = new LineChart(getActivity());
        mainLayout = (RelativeLayout) v.findViewById(R.id.mainLayout);
        //add the chart to the layout
        mainLayout.addView(mChart);

        //chart description
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data at the moment");
        //enable value highlighting
        //mChart.setHighlightEnabled(true);
        mChart.setMinimumHeight(330);
        mChart.setMinimumWidth(600);
        //enable touch gestures
        mChart.setTouchEnabled(true);

        //enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        mChart.setPinchZoom(true);


        // Initialises two example countries
        // Once you have completed the implemention behind this fragment
        // I will create a link between the activity and this fragment
        // So the fragment changes data according to selected country
        // DO NOT CHANGE
        // <<<<<<<<<<EXAMPLE>>>>>>>>>>>>>>>>
        Country countryOne = new Country();
        countryOne.setName("United States");
        countryOne.setIsoCode("US");
        Country countryTwo = new Country();
        countryTwo.setName("Russia");
        countryTwo.setIsoCode("RU");
        Indicator indicator = new Indicator();
        indicator.setName("3.1.3_HYDRO.CONSUM");
        indicator.addData(2015, 34395.0);
        indicator.addData(2014, 2434.2);
        indicator.addData(2013, 323123.2);
        indicator.addData(2012, 321321.0);
        countryOne.addIndicator(indicator);
        countryTwo.addIndicator(indicator);




        //data
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        ArrayList<Entry> dataset1 = new ArrayList<Entry>();
        dataset1.add(new Entry(1f, 0));
        dataset1.add(new Entry(2f, 1));
        dataset1.add(new Entry(3f, 2));
        dataset1.add(new Entry(4f, 3));
        dataset1.add(new Entry(5f, 4));
        dataset1.add(new Entry(6f, 5));
        dataset1.add(new Entry(7f, 6));


        ArrayList<Entry> dataset2 = new ArrayList<Entry>();
        dataset2.add(new Entry(3f, 0));
        dataset2.add(new Entry(4f, 1));
        dataset2.add(new Entry(5f, 2));
        dataset2.add(new Entry(6f, 6));
        dataset2.add(new Entry(7f, 8));
        dataset2.add(new Entry(8f, 9));
        dataset2.add(new Entry(9f, 8));


        String[] xAxis = new String[] {"0", "1", "2", "3", "4", "5", "6", "8", "9"};


        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet> ();

        LineDataSet lDataSet1 = new LineDataSet(dataset1, "DataSet1");
        lDataSet1.setColor(Color.RED);
        lDataSet1.setCircleColor(Color.RED);
        lines.add(lDataSet1);

        LineDataSet lDataSet2 = new LineDataSet(dataset2, "DataSet2");
        lDataSet2.setColor(Color.BLUE);
        lDataSet2.setCircleColor(Color.BLUE);
        lines.add(lDataSet2);



        mChart.setData(new LineData(xAxis, lines));





        //get legend object
        Legend l = mChart.getLegend();

        //customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);




        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setEnabled(true);
        mChart.getXAxis().setTextColor(Color.BLACK);
        mChart.getAxisLeft().setTextColor(Color.BLACK);

        mChart.animateXY(1500, 2000);





        //Example of how to extract data
        // <<<<EXAMPLE>>>
        // /Let's say I want data for the indicator 3.1.3_HYDRO.CONSUM of countryOne//
        // /of the year 2014
        // Double value = countryOne.getIndicators().get("3.1.3_HYDRO.CONSUM").getData(2014);
        // Let's say I want all the possible years of data the indicator 3.1.3_HYDRO.CONSUM
        // hasSet<Integer> allYear = countryOne.getIndicators().get("3.1.3_HYDRO.CONSUM").getIndicatorData().keySet();
        // And then to extract the years, use enhanced for loop
        // for (Integer year:allYear)
        // {
        // Log.i("MYAPP",year);}
        // <<<<EXAMPLE>>>






        return v;



    }



}
