package com.sage.projectwalk.InfoGraphs;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;


import com.github.mikephil.charting.data.LineData;

import com.github.mikephil.charting.data.LineDataSet;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * Created by tahmidulislam on 27/11/2015.
 */

public class EnergyRatioGraph extends Fragment {

    private RelativeLayout mainLayout;
    private LineChart mChart;
    ArrayList<LineDataSet> lineDataSets;
    String[] xValues;
    TextView yAxis;
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
        mChart.setDescription("Energy Ratio");
        mChart.setDrawBorders(true);
        mChart.setNoDataTextDescription("Please select a country from the side panel!");

        //enable touch gestures
        mChart.setTouchEnabled(true);

        //enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        mChart.setPinchZoom(true);

        //To hold all data sets
        lineDataSets = new ArrayList<>();
        xValues = null;
        //get legend object
        Legend l = mChart.getLegend();

        //customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);


        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setEnabled(true);
        mChart.getXAxis().setTextColor(Color.BLACK);
        mChart.getXAxis().setDrawGridLines(false);

        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getAxisLeft().setTextColor(Color.BLACK);

        mChart.animateXY(1500, 2000);
        return v;
    }

    public void updateCountryOne(Country country){
        Indicator energyRatioIndicator = country.getIndicators().get("8.1.2_FINAL.ENERGY.INTENSITY");
        if(energyRatioIndicator != null){
            //Get data values
            ArrayList<Entry> dataset = new ArrayList<Entry>();

            //Get X Axis Values
            Set<Integer> allYears = energyRatioIndicator.getIndicatorData().keySet();
            xValues = new String[allYears.size()];
            int index = 0;
            for (Integer year:allYears){
                xValues[index] = year.toString();
                index++;
            }
            Arrays.sort(xValues);

            //Now extract values
            for (int i = 0;i < xValues.length;i++){
                Integer year = Integer.parseInt(xValues[i]);
                BigDecimal bigDecimal = energyRatioIndicator.getData(year);
                dataset.add(new Entry(bigDecimal.intValue(),i));
                Log.i("MYAPP",bigDecimal.intValue()+"");
            }
            LineDataSet lDataSet1 = new LineDataSet(dataset, country.getName());
            lDataSet1.setColor(Color.RED);
            lDataSet1.setCircleColor(Color.RED);
            lDataSet1.setCircleColorHole(Color.RED);

            if(lineDataSets.size() > 0){
                lineDataSets.remove(0);
            }
            lineDataSets.add(0, lDataSet1);

            mChart.removeAllViews();
            addYLabel();
            mChart.setData(new LineData(xValues, lineDataSets));
            mChart.animateXY(1500, 2000);
            Log.i("MYAPP","Updated country 1 energy ratio");
        }else{
            Log.e("MYAPP","Could not find indicator data for energy ratio C1");
        }

    }

    public void updateCountryTwo(Country country){
        Indicator energyRatioIndicator = country.getIndicators().get("8.1.2_FINAL.ENERGY.INTENSITY");
        if(energyRatioIndicator != null){
            //Get data values
            ArrayList<Entry> dataset = new ArrayList<Entry>();

            //Check if there are X axis values from country 1
            //Get X Axis Values
            if(xValues == null){
                Set<Integer> allYears = energyRatioIndicator.getIndicatorData().keySet();
                xValues = new String[allYears.size()];
                int index = 0;
                for (Integer year:allYears){
                    xValues[index] = year.toString();
                    index++;
                }
                Arrays.sort(xValues);
            }

            //Now extract values
            for (int i = 0;i < xValues.length;i++){
                Integer year = Integer.parseInt(xValues[i]);
                try {
                    BigDecimal bigDecimal = energyRatioIndicator.getData(year);
                    dataset.add(new Entry(bigDecimal.intValue(), i));
                }catch (NullPointerException e){

                }
            }
            LineDataSet lDataSet1 = new LineDataSet(dataset, country.getName());
            lDataSet1.setColor(Color.BLUE);
            lDataSet1.setCircleColor(Color.BLUE);
            lDataSet1.setCircleColorHole(Color.BLUE);
            if(lineDataSets.size() > 1){
                lineDataSets.remove(1);
            }
            lineDataSets.add(1, lDataSet1);

            mChart.removeAllViews();
            addYLabel();
            mChart.setData(new LineData(xValues, lineDataSets));
            mChart.refreshDrawableState();
            mChart.animateXY(1500, 2000);
            Log.i("MYAPP", "Updated country 2 energy ratio");
        } else{
            Log.e("MYAPP","Could not find indicator data for energy ratio C2");
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
                mChart.setMinimumHeight(getView().getHeight()-30);
                mChart.setMinimumWidth(getView().getWidth()-15);
            }
        });

    }

    public void addYLabel(){
        TextView xAxisName = new TextView(getActivity());
        xAxisName.setText("TJ ()");
        xAxisName.setActivated(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        params.setMargins(0, 0, 0, 20);

        mChart.addView(xAxisName, params);
    }
}
