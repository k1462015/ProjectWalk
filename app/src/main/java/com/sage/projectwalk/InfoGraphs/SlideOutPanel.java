package com.sage.projectwalk.InfoGraphs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;


import com.sage.projectwalk.CountryAdapter;
import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.MainActivity;
import com.sage.projectwalk.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mihai on 28/11/2015.
 */
public class SlideOutPanel extends Fragment {
    ListView countryOption1;
    ListView countryOption2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_out_panel_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countryOption1 = (ListView) getView().findViewById(R.id.countryOption1);
        countryOption2 = (ListView) getView().findViewById(R.id.countryOption2);
        DataManager dataManager = new DataManager((MainActivity) getActivity());
        ArrayList<Country> countries = null;
        try {
            //Retrieves all countries in Country object form
            countries = dataManager.getCountryList();
            countryOption1.setAdapter(new CountryAdapter(getActivity(), R.layout.row_country, countries));
            countryOption2.setAdapter(new CountryAdapter(getActivity(), R.layout.row_country, countries));
        } catch (IOException e) {
            Log.e("MYAPP", "IOException when trying to retrieve countries list in ListFragment");
        } catch (JSONException e) {
            Log.e("MYAPP","JSONException when trying to retrieve countries list in ListFragment");
        }
    }
}