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
public class BatteryTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {

    private TestFragmentActivity testFragmentActivity;

    public BatteryTest (){
        super(TestFragmentActivity.class);

    }

    protected void setUp() throws Exception{

        super.setUp();
        testFragmentActivity = getActivity();

    }

    private android.support.v4.app.Fragment startFragment(android.support.v4.app.Fragment fragment){

        FragmentTransaction transaction = testFragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_test_fragment_linearlayout, fragment, "tag");
        transaction.commit();

        getInstrumentation().waitForIdleSync();
        android.support.v4.app.Fragment frag = testFragmentActivity.getSupportFragmentManager().findFragmentByTag("tag");

        return frag;
    }


    public void testFragment(){

      BatteryGraph batteryTest = new BatteryGraph(){

            public void testPreconditions(){
               assertNotNull(this);
            }

            public void testPercentage(){

                SeekBar batteryYearSeekBar = (SeekBar) getView().findViewById(R.id.unifiedSeekBar);
                batteryYearSeekBar.setProgress(2012);
                Country country1 = new Country();
                Country country2 = new Country();



                float i = country1.getIndicators().get("3.1_RE.CONSUMPTION").getData(batteryYearSeekBar.getProgress()).intValue() *100f
                        / country1.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION").getData(batteryYearSeekBar.getProgress()).intValue();
                float j = country2.getIndicators().get("3.1_RE.CONSUMPTION").getData(batteryYearSeekBar.getProgress()).intValue() *100f
                        / country2.getIndicators().get("8.1.1_FINAL.ENERGY.CONSUMPTION").getData(batteryYearSeekBar.getProgress()).intValue();

                String per1 = "" +i;
                String per2 = "" +j;

                TextView countryOne = (TextView) getView().findViewById(R.id.countryOnePercent);
                TextView coutnryTwo = (TextView) getView().findViewById(R.id.countryTwoPercent);

                assertEquals("Correct percentage showing", per1 , countryOne.getText() );
                assertEquals("Correct percentage showing", per2, coutnryTwo.getText());
                assertEquals(2012, batteryYearSeekBar.getMax());

            }


        };
        android.support.v4.app.Fragment frag = startFragment(batteryTest); }


    }





