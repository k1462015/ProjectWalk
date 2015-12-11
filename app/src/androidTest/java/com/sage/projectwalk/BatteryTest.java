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


    public BatteryTest (){
        super(MainActivity.class);
    }

    protected void setUp() throws Exception{
        super.setUp();
    }



    public void testPercentage(){
        getInstrumentation().waitForIdleSync();

          SeekBar seekBar= getActivity().getUnifiedSeekBar();
                seekBar.setProgress(2012);




                float i = getActivity().batteryGraph.getCountryOne().getIndicators().get("3.1_RE.CONSUMPTION").getData(2012).intValue() *100f
                        / getActivity().batteryGraph.getCountryOne().getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION").getData(2012).intValue();
                float j = getActivity().batteryGraph.getCountryTwo().getIndicators().get("3.1_RE.CONSUMPTION").getData(2012).intValue() *100f
                        / getActivity().batteryGraph.getCountryTwo().getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION").getData(2012).intValue();

                String per1 = "" +i;
                String per2 = "" +j;

                assertEquals("Correct percentage showing", per1 , getActivity().batteryGraph.getCountryOnePercent().getText() );
                assertEquals("Correct percentage showing", per2, getActivity().batteryGraph.getCountryTwoPercent().getText());
                assertEquals(2012, seekBar.getMax());

            }


        }





