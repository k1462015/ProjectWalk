package com.sage.projectwalk.InfoGraphs;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tahmidulislam on 27/11/2015.
 */

public class FactCards extends Fragment{

    private TextView factBody;
    private TextView factTitle;
    private Country mCountryOne;
    private Country mCountryTwo;
    Animation slideOutAnimation;
    Animation slideInFromLeftAnim;
    RelativeLayout relativeLayout;
    ArrayList<String> factTitles;
    ArrayList<String> facts;


    public void changeFact(){
        //Randomly select a fact
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(facts.size());
        factTitle.setText(factTitles.get(randomNumber));
        factBody.setText(facts.get(randomNumber));
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fact_cards, container, false);

        factBody = (TextView) view.findViewById(R.id.factBody);
        factTitle = (TextView) view.findViewById(R.id.factTitle);

        populateFacts();

        slideOutAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right);
        slideOutAnimation.setDuration(300);
        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                changeFact();
                relativeLayout.startAnimation(slideInFromLeftAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideInFromLeftAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left);
        slideInFromLeftAnim.setDuration(300);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.factCardsLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                relativeLayout.startAnimation(slideOutAnimation);
            }
        });


        return view;


    }

    /**
     * Populates the general facts
     * Which are stored in the assets folder
     */
    public void populateFacts(){
        factTitles = new ArrayList<>();
        facts = new ArrayList<>();

        factTitles = getFileAsset("factTitles.txt");
        facts = getFileAsset("facts.txt");
        changeFact();

    }

    public ArrayList<String> getFileAsset(String fileName){
        //Load from assets folder
        AssetManager assetManager = getActivity().getAssets();
        try{
            ArrayList<String> fileData = new ArrayList<>();
            InputStream input = assetManager.open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //Reads contents of file, line by line
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(line.length() > 0){
                    fileData.add(line);
                }
            }
            return fileData;
        }catch (Exception e){
            return null;
        }
    }

}

