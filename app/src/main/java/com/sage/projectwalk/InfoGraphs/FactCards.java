package com.sage.projectwalk.InfoGraphs;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    private ImageView imageHolder;
    private Country mCountryOne;
    private Country mCountryTwo;
    Animation slideOutAnimation;
    Animation slideInFromLeftAnim;
    Animation slideOutRight;
    Animation slideInFromRightAnim;
    RelativeLayout relativeLayout;
    ArrayList<String> factTitles;
    ArrayList<String> facts;

    public void changeFact(){
        //Randomly select a fact
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(facts.size());
        factTitle.setText(factTitles.get(randomNumber));
        factBody.setText(facts.get(randomNumber));
        //This generates the resource Id for that flag image
        int imageResource = getActivity().getResources().getIdentifier("drawable/fact"+randomNumber,null,getActivity().getPackageName());
        Drawable factImage = getActivity().getResources().getDrawable(imageResource);
        imageHolder.setImageDrawable(factImage);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fact_cards, container, false);

        factBody = (TextView) view.findViewById(R.id.factBody);
        factTitle = (TextView) view.findViewById(R.id.factTitle);
        imageHolder = (ImageView) view.findViewById(R.id.imageHolder);
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
                changeFact();
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
                changeFact();
                relativeLayout.startAnimation(slideInFromRightAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideInFromRightAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        slideInFromRightAnim.setDuration(300);


        relativeLayout = (RelativeLayout) view.findViewById(R.id.factCardsLayout);

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(container.getContext()) {
            @Override
            public void onSwipeRight() {
                relativeLayout.startAnimation(slideOutAnimation);
            }
            @Override
            public void onSwipeLeft() {
                relativeLayout.startAnimation(slideOutRight);
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
