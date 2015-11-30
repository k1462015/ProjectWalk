package com.sage.projectwalk.InfoGraphs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sage.projectwalk.R;

/**
 * Created by tahmidulislam on 27/11/2015.
 */
public class EnergyRatioGraph extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.energy_ratio,container,false);
    }
}
