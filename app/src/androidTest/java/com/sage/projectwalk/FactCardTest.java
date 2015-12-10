package com.sage.projectwalk;

import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import com.sage.projectwalk.InfoGraphs.FactCards;

/**
 * Created by Mihai on 09/12/2015.
 */
public class FactCardTest extends ActivityInstrumentationTestCase2<TestFragmentActivity> {
    private TestFragmentActivity testFragmentActivity;

    public FactCardTest(){

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

    public void testFragment4(){
        FactCards factTest = new FactCards(){

            public void testPreconditions(){
                assertNotNull(this);
            }


        };
        android.support.v4.app.Fragment frag = startFragment(factTest);}
}
