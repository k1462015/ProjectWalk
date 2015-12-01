package com.sage.projectwalk;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sage.projectwalk.Data.DataRetriever;
import com.sage.projectwalk.InfoGraphs.BatteryGraph;
import com.sage.projectwalk.InfoGraphs.DummyFragment;
import com.sage.projectwalk.InfoGraphs.EnergyRatioGraph;
import com.sage.projectwalk.InfoGraphs.FactCards;
import com.sage.projectwalk.InfoGraphs.RenewableBreakdownContainer;
import com.sage.projectwalk.InfoGraphs.SlideOutPanel;


public class MainActivity extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Gets required fragment stuff
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        //Adds all fragments to main activity
        BatteryGraph batteryGraph = new BatteryGraph();
        EnergyRatioGraph energyRatioGraph = new EnergyRatioGraph();
        FactCards factCards = new FactCards();
        RenewableBreakdownContainer renewableBreakdownContainer = new RenewableBreakdownContainer();

        //Adds all fragments to corresponding containers
        fragmentTransaction.add(R.id.batteryGraphContainer,batteryGraph);
        fragmentTransaction.add(R.id.energyRatioContainer,energyRatioGraph);
        fragmentTransaction.add(R.id.factCardsContainer,factCards);
        fragmentTransaction.add(R.id.renewableSourcesContainer, renewableBreakdownContainer);
        fragmentTransaction.commit();

        button = (Button)findViewById(R.id.button2);

    }


    public void openSlideFragment(View v) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SlideOutPanel menuFragment = new SlideOutPanel();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, 0);
        fragmentTransaction.add(R.id.out, menuFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        button.setVisibility(View.INVISIBLE);

    }

    public void closeSlideFragment(View v){
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DummyFragment menuFragment = new DummyFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, 0);
        fragmentTransaction.replace(R.id.out, menuFragment);
        fragmentTransaction.commit();
        button.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




 /**   private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return new BatteryGraph();
                case 1:
                    return new SlideOutPanel();


            default: return new SlideOutPanel();
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    } **/

}


