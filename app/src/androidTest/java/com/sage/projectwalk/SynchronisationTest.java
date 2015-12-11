package com.sage.projectwalk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.Data.DataRetriever;

public class SynchronisationTest extends ActivityInstrumentationTestCase2<MainActivity>{


    public SynchronisationTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    /**
     * This tests if the application can start synchronising
     * With the world data bank
     */
    public void testSynchronisation() throws Throwable {
        final DataManager dataManager = getActivity().dataManager;
        DataRetriever dataRetriever = dataManager.synchronizeData();
        //Checks if the data retriever exists
        assertNotNull(dataRetriever);
        if(getActivity().checkInternetConnection()){
            assertEquals(dataRetriever.getStatus(), AsyncTask.Status.RUNNING);
        }
    }

}
