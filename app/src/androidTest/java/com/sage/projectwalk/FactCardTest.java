package com.sage.projectwalk;

import android.test.ActivityInstrumentationTestCase2;

import com.sage.projectwalk.InfoGraphs.FactCards;

/**
 * Created by TahmidulIslam on 11/12/2015.
 */
public class FactCardTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public FactCardTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Checks if the next fact method works
     * @throws Throwable
     */
    public void testNextFact() throws Throwable {
        final FactCards factCards = this.getActivity().factCards;
        int currentFactCard = factCards.currentFact;
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                factCards.nextFact();
            }
        });

        getInstrumentation().waitForIdleSync();
        int expectedFactCard = currentFactCard + 1;
        assertEquals(expectedFactCard, factCards.currentFact);
    }

    /**
     * Checks if the next fact method works
     * @throws Throwable
     */
    public void testPrevFact() throws Throwable {
        final FactCards factCards = this.getActivity().factCards;
        int currentFactCard = factCards.currentFact;
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                factCards.prevFact();
            }
        });

        getInstrumentation().waitForIdleSync();
        int expectedFactCard = currentFactCard - 1;
        if(currentFactCard == 0) {
            expectedFactCard = factCards.facts.size() - 1;
        }
        assertEquals(expectedFactCard,factCards.currentFact);
    }


}
