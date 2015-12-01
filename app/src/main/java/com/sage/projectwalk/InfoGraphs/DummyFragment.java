package com.sage.projectwalk.InfoGraphs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sage.projectwalk.R;

/**
 * Created by Mihai on 01/12/2015.
 */
public class DummyFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dummy_layout, container, false);

    }
}