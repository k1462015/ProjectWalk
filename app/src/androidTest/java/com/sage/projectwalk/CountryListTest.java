package com.sage.projectwalk;

import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.InfoGraphs.SlideOutPanel;


/**
 * Created by Mihai on 09/12/2015.
 */
public class CountryListTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {

    private TestFragmentActivity testFragmentActivity;

    public CountryListTest (){
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

        SlideOutPanel countryTest = new SlideOutPanel(){

            public void testPreconditions(){
                assertNotNull(this);
            }

            public void countryNameTest(){
                ListView listView = (ListView) getView().findViewById(R.id.countryOption1);
                TextView textView = (TextView) getView().findViewById(R.id.countryOneHolder);
                Country country = new Country();
                country = (Country) listView.getItemAtPosition(0);

                CountryListTest.assertEquals(country.getName(), textView.getText());
            }

        };
        android.support.v4.app.Fragment frag = startFragment(countryTest);
    }


}
