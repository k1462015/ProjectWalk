package com.sage.projectwalk;

import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;


/**
 * Created by Mihai on 07/12/2015.
 */
public class BatteryTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public BatteryTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Adjusts seekbar, and checks
     * if battery renewable energy percentage is correct
     * @throws Throwable
     */
    public void testPercentage() throws Throwable {
        getInstrumentation().waitForIdleSync();
        final SeekBar seekBar = getActivity().getUnifiedSeekBar();
        seekBar.setProgress(seekBar.getMax());
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().refreshAllFragments(seekBar.getProgress());
            }
        });
        //Initially application loads data for Albania
        float expecRenewPercentCountryOne = getActivity().batteryGraph.getCountryOne().getIndicators().get("3.1_RE.CONSUMPTION").getData(2012).intValue() * 100f
                / getActivity().batteryGraph.getCountryOne().getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION").getData(2012).intValue();
        float expecRenewPercentCountryTwo = getActivity().batteryGraph.getCountryTwo().getIndicators().get("3.1_RE.CONSUMPTION").getData(2012).intValue() * 100f
                / getActivity().batteryGraph.getCountryTwo().getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION").getData(2012).intValue();

        assertEquals("Incorrect % for country 1", ((int) expecRenewPercentCountryOne)+"%", getActivity().batteryGraph.getCountryOnePercent().getText());
        assertEquals("Incorrect % for country 1", ((int) expecRenewPercentCountryTwo)+"%", getActivity().batteryGraph.getCountryTwoPercent().getText());

    }

    /**
     * Checks if fragment actually exists
     */
    public void testFragment() {
        getInstrumentation().waitForIdleSync();
        assertNotNull(getActivity().batteryGraph);
    }

}





