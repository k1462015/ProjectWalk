package com.sage.projectwalk.InfoGraphs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.projectwalk.OnSwipeTouchListener;
import com.sage.projectwalk.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by tahmidulislam on 27/11/2015.
 */

public class FactCards extends Fragment{

    private TextView factBody;
    private TextView factTitle;
    Animation slideOutAnimation;
    Animation slideInFromLeftAnim;
    Animation slideOutRight;
    Animation slideInFromRightAnim;
    RelativeLayout relativeLayout;
    ArrayList<String> factTitles;
    ArrayList<String> facts;
    int currentFact = 0;



    public void nextFact(){
        if(currentFact == facts.size() - 1){
            currentFact = 0;
        }else{
            currentFact++;
        }
        changeFact();
    }

    public void prevFact(){
        if(currentFact == 0){
            currentFact = facts.size() - 1;
        }else{
            currentFact--;
        }
        changeFact();
    }
    public void changeFact(){
        factTitle.setText(factTitles.get(currentFact));
        factBody.setText(facts.get(currentFact));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading InfoGraph");
        progressDialog.setMessage("Please be patient...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.fact_cards, container, false);

        factBody = (TextView) view.findViewById(R.id.factBody);
        factBody.setMovementMethod(new ScrollingMovementMethod());
        factTitle = (TextView) view.findViewById(R.id.factTitle);
        populateFacts();

        //Left Swipe

        slideOutAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_right);
        slideOutAnimation.setDuration(300);
        slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                prevFact();
                relativeLayout.startAnimation(slideInFromLeftAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideInFromLeftAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left);
        slideInFromLeftAnim.setDuration(300);

        //Right Swipe

        slideOutRight = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_left);
        slideOutRight.setDuration(300);
        slideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                nextFact();
                relativeLayout.startAnimation(slideInFromRightAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideInFromRightAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        slideInFromRightAnim.setDuration(300);


        relativeLayout = (RelativeLayout) view.findViewById(R.id.factCardsLayout);

        relativeLayout.setOnTouchListener(new SwipeListener(container.getContext()));
//        factBody.setOnTouchListener(new SwipeListener(container.getContext()));
        factTitle.setOnTouchListener(new SwipeListener(container.getContext()));
        progressDialog.hide();
        return view;


    }

    private class SwipeListener extends OnSwipeTouchListener {

        public SwipeListener(Context context) {
            super(context);
        }



        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            relativeLayout.startAnimation(slideOutRight);
        }

        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            relativeLayout.startAnimation(slideOutAnimation);
        }
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
