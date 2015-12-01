package com.sage.projectwalk.InfoGraphs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



import com.sage.projectwalk.R;

/**
 * Created by Mihai on 28/11/2015.
 */
public class SlideOutPanel extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_out_panel_layout, container, false);

//
//        String[] values = {"United Kingdom", "United States", "Kenya", "Albania", "Australia"};
//        Spinner spinner = (Spinner) view.findViewById(R.id.countries);
//        Spinner spinner1 = (Spinner) view.findViewById(R.id.countries2);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_item, values );
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
//        spinner1.setAdapter(adapter);

        return view;
    }
}