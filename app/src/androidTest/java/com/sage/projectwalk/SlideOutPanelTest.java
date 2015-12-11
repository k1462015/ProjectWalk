package com.sage.projectwalk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.sage.projectwalk.CountryListPanel.CountryAdapter;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.InfoGraphs.SlideOutPanel;


/**
 * Created by Mihai on 09/12/2015.
 */
public class SlideOutPanelTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public SlideOutPanelTest (){
        super(MainActivity.class);
    }

    protected void setUp() throws Exception{
        super.setUp();
    }

    /**
     * Tests if selecting a country in the side panel
     * Changes country selected in the main activity
     * @throws Exception
     */
    public void testSelection() throws Throwable {
        final ListView countryOneListView = getActivity().menuFragment.getCountryOption1();
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                countryOneListView.performItemClick(countryOneListView.getAdapter().getView(3,null,null),3,countryOneListView.getItemIdAtPosition(3));
            }
        });
        getInstrumentation().waitForIdleSync();
        //Expected Country ISO Code
        String expectedCountryName = "Andorra";
        String currentCountryName = getActivity().getCountryOneHolder().getText().toString();
        assertTrue(currentCountryName.contains(expectedCountryName));
    }

    /**
     * Tests if fragment actually exists
     */
    public void testFragment(){
        getInstrumentation().waitForIdleSync();
        assertNotNull(getActivity().menuFragment);
    }

}



