package com.sage.projectwalk;

import android.support.v4.app.Fragment;
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
public class SlideOutPanelTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public SlideOutPanelTest (){
        super(MainActivity.class);
    }

    protected void setUp() throws Exception{
        super.setUp();
    }

    public void testName() throws Exception{
        getInstrumentation().waitForIdleSync();
        getActivity().menuFragment.getCountryOption1().performClick();

            assertEquals("", "Albania", getActivity().getCountryOneHolder().getText().subSequence(0,7));
    }

    public void testFragment(){
        getInstrumentation().waitForIdleSync();
        assertNotNull(getActivity().menuFragment);
    }

}



