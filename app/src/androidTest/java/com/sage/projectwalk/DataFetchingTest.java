package com.sage.projectwalk;

import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by TahmidulIslam on 10/12/2015.
 */
public class DataFetchingTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public DataFetchingTest() {
        super(MainActivity.class);
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if correct country is retrieved
     * When given ISO code
     */
    public void testCountryByISO() throws Exception {
        getInstrumentation().waitForIdleSync();
        DataManager dataManager = this.getActivity().dataManager;
        Country bangladesh = dataManager.getCountryBaseData("BD");
        assertEquals("Bangladesh", bangladesh.getName());
    }

    /**
     * Checks if correct indicator is retrieved
     */
    public void testIndicatorRetrieval() throws IOException, JSONException {
        getInstrumentation().waitForIdleSync();
        DataManager dataManager = this.getActivity().dataManager;
        Country britain = dataManager.getCountryIndicator("GB", "3.1.3_HYDRO.CONSUM");
        assertNotNull(britain.getIndicators().get("3.1.3_HYDRO.CONSUM"));

    }

    /**
     * Tests if the data retrieved
     * Actually matches what is in the World Data Bank
     * @throws Exception
     */
    public void testCorrectDataRetrieved() throws Exception {
        getInstrumentation().waitForIdleSync();
        DataManager dataManager = this.getActivity().dataManager;
        //This is data we know actually exists in the world data bank
        int year = 2012;
        BigDecimal correctValue = new BigDecimal("16746.2375700735");
        String indicator = "3.1.3_HYDRO.CONSUM";

        Country britain = dataManager.getCountryIndicator("GB", indicator);
        assertEquals(britain.getIndicators().get(indicator).getData(year), correctValue);
    }

    /**
     * Test retrieving flag image using ISO code
     */
    public void testRetrievingFlag(){
        getInstrumentation().waitForIdleSync();
        Drawable britainFlag = this.getActivity().getFlagImage("GB");
        assertNotNull(britainFlag);
    }
}
