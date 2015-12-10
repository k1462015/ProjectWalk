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
public class RenewableTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
    private TestFragmentActivity testFragmentActivity;

    public RenewableTest(){

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

    public void testFragment5(){
        BreakdownPieChart renewableTest = new BreakdownPieChart(){

            public void testPreconditions() throws Exception{
                assertNotNull(this);
            }
            public void testPie() throws Exception {
                Country country1 = new Country();
                Country country2 = new Country();
                SeekBar seekBar = (SeekBar) getView().findViewById(R.id.unifiedSeekBar);

                Indicator countryOneIndicator = country1.getIndicators().get("3.1.3_HYDRO.CONSUM");

                Set<Integer> years = countryOneIndicator.getIndicatorData().keySet();

                Integer i = seekBar.getProgress();
                for (Integer year:years) {
                    assertEquals(year, i);
                }

                Indicator countryTwoIndicator = country2.getIndicators().get("3.1.3_HYDRO.CONSUM");

                Set<Integer> years2 = countryTwoIndicator.getIndicatorData().keySet();

                for (Integer year:years2) {
                    assertEquals(year, i);
                }
                assertEquals(2012, seekBar.getMax());

            }
        };
        android.support.v4.app.Fragment frag = startFragment(renewableTest); }

}
