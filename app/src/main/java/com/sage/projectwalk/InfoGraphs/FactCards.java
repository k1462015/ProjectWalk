package com.sage.projectwalk.InfoGraphs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sage.projectwalk.R;

import java.util.Random;

/**
 * Created by tahmidulislam on 27/11/2015.
 */

public class FactCards extends Fragment{

    private TextView mfactLabel;

    private String[] Facts = {
            "Germany's capital is Berlin.",
            "Germany's estimated population is 81 million.",
            "German is the official and predominant spoken language in Germany."};

    public String getFact() {

        String fact = "";
        //Randomly select a fact
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(Facts.length);
        fact = Facts[randomNumber];
        return fact;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fact_cards, container, false);


        mfactLabel = (TextView) view.findViewById(R.id.factTextView);
        Button mshowFactsButton = (Button) view.findViewById(R.id.showFactsButton);
        mshowFactsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String fact = getFact();
                //Update the label with a fact from the array
                mfactLabel.setText(fact);
            }
        });


        return view;


    }

    }

