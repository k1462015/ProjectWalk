package com.sage.projectwalk;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Tahmidul on 27/11/2015.
 */
public class InfoGraphic extends Fragment{
    Button clickButton;
    onButtonClickListener bListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            bListener = (onButtonClickListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement onButtonClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainViewGroup = inflater.inflate(R.layout.info_graphic,container,false);
        clickButton = (Button) mainViewGroup.findViewById(R.id.clickButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bListener.onButtonPressed("He pressed me!!!!");
            }
        });
        return mainViewGroup;
    }

    public interface onButtonClickListener{
        public void onButtonPressed(String response);
    }
}
