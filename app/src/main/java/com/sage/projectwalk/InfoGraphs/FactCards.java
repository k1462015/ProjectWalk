package com.sage.projectwalk.InfoGraphs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.R;

import java.util.Random;

/**
 * Created by tahmidulislam on 27/11/2015.
 */

public class FactCards extends Fragment{

    private TextView mfactLabel;
    private Country mCountryOne;
    private Country mCountryTwo;
    Animation slideOutAnimation;
    Animation slideInFromLeftAnim;
    RelativeLayout relativeLayout;

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

        mfactLabel = (TextView) view.findViewById(R.id.textView);

        slideOutAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right);
        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relativeLayout.startAnimation(slideInFromLeftAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideInFromLeftAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left);


        relativeLayout = (RelativeLayout) view.findViewById(R.id.factCardsLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                relativeLayout.startAnimation(slideOutAnimation);
                String fact = getFact();
                //Update the label with a fact from the array
                mfactLabel.setText(fact);
            }
        });


        return view;


    }

    }

