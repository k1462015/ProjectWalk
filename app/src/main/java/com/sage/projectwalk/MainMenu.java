package com.sage.projectwalk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.sage.projectwalk.Data.DataManager;
import com.sage.projectwalk.Data.DataRetriever;

public class MainMenu extends AppCompatActivity {
    public ProgressDialog progressDialog;
    DataManager dataManager;
    DataRetriever dataRetriever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dataManager = new DataManager(this);

        //Get required views
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    public void viewInfoGraphic(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void syncData(View view){

        progressDialog.setTitle("Synchronizing Data");
        progressDialog.setMessage("Retrieving latest data from World Data Bank");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    if(dataRetriever != null && dataRetriever.getStatus() == AsyncTask.Status.RUNNING){
                        builder.setMessage("Are you sure you want to cancel the data fetching?")
                                .setTitle("Warning")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Does nothing
                                    }
                                });
                        builder.create().show();
                    }
                }
                return true;
            }
        });
        progressDialog.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                dataRetriever = dataManager.synchronizeData();
            }
        });
        thread.start();
    }

}
