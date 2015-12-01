package com.sage.projectwalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sage.projectwalk.Data.Country;

import java.util.ArrayList;

/**
 * This adapter allows a list view to correctly display the details about a country
 * Using the country object
 */
public class CountryAdapter extends ArrayAdapter<Country> {
    private final Context context;
    private final ArrayList<Country> data;
    private final int layoutResourceId;
    public static int missingFlags = 0;

    public CountryAdapter(Context context,int layoutResourceId,ArrayList<Country> data){
        super(context,layoutResourceId,data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
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

        //Finds the image for the flag in the drawable folder
        String uri = "drawable/"+country.getIsoCode().toLowerCase()+"_img";
        //This generates the resource Id for that flag image
        int imageResource = context.getResources().getIdentifier(uri,null,context.getPackageName());
        Drawable flag = null;
        try{
            flag = context.getResources().getDrawable(imageResource);
            holder.countryFlag.setImageDrawable(flag);
        }catch (Exception e){
            Log.i("MYAPP","couldn't find image for "+country.getName()+" ISO: "+country.getIsoCode());
            flag = context.getResources().getDrawable(R.drawable.missingflag);
            holder.countryFlag.setImageDrawable(flag);
            missingFlags++;
        }
        return row;
    }

    static class ViewHolder {
        ImageView countryFlag;
        TextView countryName;
        TextView capitalCity;
    }
}