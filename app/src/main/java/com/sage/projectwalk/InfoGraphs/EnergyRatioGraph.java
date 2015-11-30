package com.sage.projectwalk.InfoGraphs;

import android.graphics.Color;
import android.os.Bundle;

import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.LineData;

import com.github.mikephil.charting.data.LineDataSet;
import com.sage.projectwalk.MainActivity;
import com.sage.projectwalk.R;


/**
 * Created by tahmidulislam on 27/11/2015.
 */
public class EnergyRatioGraph extends MainActivity{

    private RelativeLayout mainLayout;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_ratio);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        //create line chart
        mChart = new LineChart(this);

        //add the chart to the layout
        mainLayout.addView(mChart);

        //chart description
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data at the moment");
        //enable value highlighting
        mChart.setHighlightEnabled(true);

        //enable touch gestures
        mChart.setTouchEnabled(true);

        //enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        mChart.setPinchZoom(true);

        //data
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        //add data to line chart
        mChart.setData(data);

        //get legend object
        Legend l = mChart.getLegend();

        //customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.WHITE);

        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);


        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(120f);
        y1.setDrawGridLines(true);


        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);



        //method to add entry to the chart
    private void addEntry(){
        LineData data = mChart.getData();


    }







}
