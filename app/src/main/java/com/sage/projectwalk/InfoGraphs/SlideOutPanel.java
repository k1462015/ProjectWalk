package com.sage.projectwalk.InfoGraphs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    CountryListListener countryListListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_out_panel_layout, container, false);
        return view;
    }

    public interface CountryListListener{
        void onCountryOption1Selected(Country country);
        void onCountryOption2Selected(Country country);
        void hideShowButton();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countryOption1 = (ListView) getView().findViewById(R.id.countryOption1);
        countryOption1.setOnItemClickListener(new ListViewListenerOne());
        countryOption2 = (ListView) getView().findViewById(R.id.countryOption2);
        countryOption2.setOnItemClickListener(new ListViewListenerTwo());
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

        Button panelSwitch = (Button) getView().findViewById(R.id.panelSwitch);
        panelSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });
        RelativeLayout outsideView = (RelativeLayout) getView().findViewById(R.id.outsideView);
        outsideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });
    }

    public void hidePanel(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.out));
        fragmentTransaction.commit();
        countryListListener.hideShowButton();
    }

    private class ListViewListenerOne implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Country selectedCountry = (Country) countryOption1.getItemAtPosition(position);
            countryListListener.onCountryOption1Selected(selectedCountry);
        }
    }
    private class ListViewListenerTwo implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Country selectedCountry = (Country) countryOption1.getItemAtPosition(position);
            countryListListener.onCountryOption2Selected(selectedCountry);

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            countryListListener = (CountryListListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}