package com.sage.projectwalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sage.projectwalk.Data.DataManager;


public class MainMenu extends AppCompatActivity {
    DataManager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dataManager = new DataManager(this);
    }

    public void viewInfoGraphic(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void syncData(View view){
        dataManager.synchronizeData();
    }

}
