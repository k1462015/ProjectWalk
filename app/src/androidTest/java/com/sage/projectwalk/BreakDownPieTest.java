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

    public BreakDownPieTest(){
        super(MainActivity.class);
    }

    protected void setUp() throws Exception{
        super.setUp();
    }


    public void testPie() throws Exception {
        getInstrumentation().waitForIdleSync();
        getActivity().menuFragment.getCountryOption1().performClick();

        assertEquals("Country Name", getActivity().getCountryOneHolder().getText().subSequence(0, 7), getActivity().breakdownPieChart.getCountryOne().getName());
        assertEquals("Country Name", getActivity().getCountryTwoHolder().getText().subSequence(0, 7), getActivity().breakdownPieChart.getCountryTwo().getName());

    }

    public void testFragment(){
        getInstrumentation().waitForIdleSync();
        assertNotNull(getActivity().breakdownPieChart);
    }

    }


