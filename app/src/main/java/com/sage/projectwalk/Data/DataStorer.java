package com.sage.projectwalk.Data;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Tahmidul on 23/11/2015.
 */
public class DataStorer {

    /**
     *
     * @param context - Application Context
     * @param FILENAME - Name that you want to save file as
     * @param data - Actual data that is in JSON format
     */
    public void saveToFile(Context context,String FILENAME,String data){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(FILENAME + ".json", Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            Log.i("MYAPP","DATA SUCCESFULLY SAVED");
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Log.i("MYAPP", "fILE NOT FOUND EXCEPTION");
        }catch (IOException e){
            e.printStackTrace();
            Log.i("MYAPP", "IOEXCEPTION");
        }
    }

}
