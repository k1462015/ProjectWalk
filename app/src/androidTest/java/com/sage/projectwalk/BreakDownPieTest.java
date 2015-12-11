package com.sage.projectwalk;

import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.SeekBar;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.InfoGraphs.BreakdownPieChart;

import java.util.Set;

/**
 * Created by Mihai on 09/12/2015.
 */
public class BreakDownPieTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public BreakDownPieTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Tests whether the current selected country in the main activity
     * Matches the country being uses in the breakdown pie chart fragment
     *
     * @throws Throwable
     */
    public void testPieChartCountries() throws Throwable {
        getInstrumentation().waitForIdleSync();

        //First get the pie charts countries
        BreakdownPieChart breakdownPieChart = getActivity().breakdownPieChart;
        Country pieChartCountryOne = breakdownPieChart.getCountryOne();
        Country pieChartCountryTwo = breakdownPieChart.getCountryTwo();

        //Then get the main activities country names
        String countryOneName = getActivity().getCountryOneHolder().getText().toString();
        String countryTwoName = getActivity().getCountryTwoHolder().getText().toString();

        assertTrue(countryOneName.contains(pieChartCountryOne.getName()));
        assertTrue(countryTwoName.contains(pieChartCountryTwo.getName()));
    }

    public void testFragment() {
        getInstrumentation().waitForIdleSync();
        assertNotNull(getActivity().breakdownPieChart);
    }

}


