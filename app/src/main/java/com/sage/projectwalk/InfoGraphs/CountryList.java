package com.sage.projectwalk.InfoGraphs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.CountryAdapter;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.MainActivity;
import com.sage.projectwalk.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by TahmidulIslam on 30/11/2015.
 */
public class CountryList extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_country,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataManager dataManager = new DataManager((MainActivity) getActivity());
                    //Retrieves all countries in Country object form
                    ArrayList<Country> countries = dataManager.getCountryList();
                    setListAdapter(new CountryAdapter(getActivity(), R.layout.row_country, countries));
                } catch (IOException e) {
                    Log.e("MYAPP","IOException when trying to retrieve countries list in ListFragment");
                } catch (JSONException e) {
                    Log.e("MYAPP","JSONException when trying to retrieve countries list in ListFragment");
                }
            }
        });
        t.start();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Country chosenCountry = (Country) l.getItemAtPosition(position);
        super.onListItemClick(l, v, position, id);
        //This toast is just here to make sure each list item
        //Is linked to the correct country
        Toast.makeText(getActivity(),"Country: "+chosenCountry.getName(),Toast.LENGTH_SHORT).show();
    }


}
