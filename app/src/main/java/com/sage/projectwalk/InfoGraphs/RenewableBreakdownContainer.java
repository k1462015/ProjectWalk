package com.sage.projectwalk.InfoGraphs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.sage.projectwalk.R;

import java.util.ArrayList;

/**
 * Created by tahmidulislam on 27/11/2015.
 */
public class RenewableBreakdownContainer extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.renewable_breakdown_graph,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HorizontalBarChart RenewableChart = (HorizontalBarChart) getView().findViewById(R.id.BreakDownChart);
        BarData barData = new BarData(getXAxisValues(),getDataSet());
        RenewableChart.setData(barData);
        RenewableChart.animateXY(2000, 2000);
        RenewableChart.invalidate();
    }

    //need it to be so that it gets the data from the indicators, at the moment it is hard coded
    private ArrayList<BarDataSet> getDataSet(){
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valuesCountryA = new ArrayList<>();

        //Entry for Hydro Energy Consumption
        BarEntry HCA = new BarEntry(30.000f, 0);
        valuesCountryA.add(HCA);

        //Entry for Liquid Biofuel Consumption
        BarEntry LBA = new BarEntry(15.000f, 1);
        valuesCountryA.add(LBA);

        //Entry for Wind Energy Consumption
        BarEntry WindA = new BarEntry(20.000f, 2);
        valuesCountryA.add(WindA);

        //Entry for Solar Energy Consumption
        BarEntry SA = new BarEntry(25.000f, 3);
        valuesCountryA.add(SA);

        //Entry for Geothermal Energy Consumption
        BarEntry GtA = new BarEntry(4.000f, 4);
        valuesCountryA.add(GtA);

        //Entry for Waste Energy Consumption
        BarEntry WasteA = new BarEntry(6.000f, 5);
        valuesCountryA.add(WasteA);

        //Entry for Biogas Energy Consumption
        BarEntry BA = new BarEntry(2.000f, 6);
        valuesCountryA.add(BA);


        ArrayList<BarEntry> valuesCountryB = new ArrayList<>();

        //Entry for Hydro Energy Consumption
        BarEntry HCB = new BarEntry(30.000f, 0);
        valuesCountryB.add(HCB);

        //Entry for Liquid Biofuel Consumption
        BarEntry LBB = new BarEntry(15.000f, 1);
        valuesCountryB.add(LBB);

        //Entry for Wind Energy Consumption
        BarEntry WindB = new BarEntry(20.000f, 2);
        valuesCountryB.add(WindB);

        //Entry for Solar Energy Consumption
        BarEntry SB = new BarEntry(25.000f, 3);
        valuesCountryB.add(SB);

        //Entry for Geothermal Energy Consumption
        BarEntry GtB = new BarEntry(4.000f, 4);
        valuesCountryB.add(GtB);

        //Entry for Waste Energy Consumption
        BarEntry WasteB = new BarEntry(6.000f, 5);
        valuesCountryB.add(WasteB);

        //Entry for Biogas Energy Consumption
        BarEntry BB = new BarEntry(2.000f, 6);
        valuesCountryB.add(BB);

        BarDataSet barDataSetA = new BarDataSet(valuesCountryA, "Country A");
        barDataSetA.setColor(Color.rgb(0,0,155));

        BarDataSet barDataSetB = new BarDataSet(valuesCountryB, "Country B");
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

    //where the Values of the Individual Renewable Energy Values will go for Country A in TJ
    double IndividualConsumValsA[] = new double[22];
    double IndividualConsumPercentagesA[] = new double [22];

    //where the Values of the Individual Renewable Energy Values will go for Country B in TJ
    double IndividualConsumValsB[] = new double[22];
    double IndividualConsumPercentagesB[] = new double[22];

    //the final conumed value of Renewable Energy for Country A in TJ
    private double FinalRenewableConsumA;

    //the final conumed value of Renewable Energy for Country B in TJ
    private double FinalRenewableConsumB;

    public void calculatePercentage(){
        for(int i = 0; i <= IndividualConsumValsA.length; i++){
            IndividualConsumPercentagesA[i] = (IndividualConsumValsA[i]/FinalRenewableConsumA)/100;
            IndividualConsumPercentagesB[i] = (IndividualConsumValsB[i]/FinalRenewableConsumA)/100;
        }

    }
//    public LineDataSet renewableBreakDownDataSet(ArrayList<Entry> yValues){
//        ArrayList<Entry> HydroConsum = new ArrayList<Entry>();
//        ArrayList<Entry> BiofuelsConsum = new ArrayList<Entry>();
//        ArrayList<Entry> WindConsum = new ArrayList<Entry>();
//        ArrayList<Entry> SolarConsum = new ArrayList<Entry>();
//        ArrayList<Entry> GeothermalConsum = new ArrayList<Entry>();
//        ArrayList<Entry> WasteConsum = new ArrayList<Entry>();
//        ArrayList<Entry> BiogasConsum = new ArrayList<Entry>();
//        ArrayList<Entry> FinalRenewableConsum = new ArrayList<Entry>();
//
//
//
//
//
//    }


}
