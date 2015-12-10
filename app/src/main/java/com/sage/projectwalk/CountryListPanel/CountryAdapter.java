package com.sage.projectwalk.CountryListPanel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;
import com.sage.projectwalk.Data.Indicator;
import com.sage.projectwalk.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * This adapter allows a list view to correctly display the details about a country
 * Using the country object
 */
public class CountryAdapter extends ArrayAdapter<Country> {
    private final Context context;
    private final ArrayList<Country> data;
    private final int layoutResourceId;
    private final int listIdentifier;
    private static int selectedIndex1;
    private static int selectedIndex2;

    public CountryAdapter(Context context,int layoutResourceId,ArrayList<Country> data,int listIdentifier){
        super(context,layoutResourceId,data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.listIdentifier = listIdentifier;
    }

    public static int getSelectedIndex1() {
        return selectedIndex1;
    }

    public static void setSelectedIndex1(int selectedIndex1) {
        CountryAdapter.selectedIndex1 = selectedIndex1;
    }

    public static int getSelectedIndex2() {
        return selectedIndex2;
    }

    public static void setSelectedIndex2(int selectedIndex2) {
        CountryAdapter.selectedIndex2 = selectedIndex2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);

            holder = new ViewHolder();
            holder.countryFlag = (ImageView)row.findViewById(R.id.countryFlag);
            holder.countryName = (TextView)row.findViewById(R.id.countryName);
            holder.capitalCity = (TextView)row.findViewById(R.id.capitalCity);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }

        Country country = data.get(position);
        holder.countryName.setText(country.getName());
        holder.capitalCity.setText(country.getCapitalCity());

        holder.position = position;

        //Finds the image for the flag in the drawable folder
        String uri = "drawable/"+country.getIsoCode().toLowerCase()+"_img";
        new FlagImageTask(position,holder,context).execute(uri);

        if(listIdentifier == 1){
            if(position == selectedIndex1){
                row.setBackgroundColor(Color.parseColor("#F79722"));
            }else{
                row.setBackgroundColor(Color.parseColor("#1B9AAA"));
            }
        }
        if(listIdentifier == 2){
            if(position == selectedIndex2){
                row.setBackgroundColor(Color.parseColor("#F79722"));
            }else{
                row.setBackgroundColor(Color.parseColor("#1B9AAA"));
            }
        }
        return row;
    }

    /**
     * This AsyncTask fetches a countries flag
     * This prevents the list from lagging when initially loading
     */
    private static class FlagImageTask extends AsyncTask<String,Void,Drawable>{
        private int mPosition;
        private ViewHolder mHolder;
        private Context context;

        public FlagImageTask(int mPosition,ViewHolder mHolder,Context context){
            this.mPosition = mPosition;
            this.mHolder = mHolder;
            this.context = context;
        }

        @Override
        protected Drawable doInBackground(String... drawableURI) {
            //This generates the resource Id for that flag image
            int imageResource = context.getResources().getIdentifier(drawableURI[0],null,context.getPackageName());
            return context.getResources().getDrawable(imageResource);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            if(mHolder.position == mPosition && drawable != null){
                mHolder.countryFlag.setImageDrawable(drawable);
            }
        }
    }

    static class ViewHolder {
        ImageView countryFlag;
        TextView countryName;
        TextView capitalCity;
        int position;
    }
}