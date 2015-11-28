package com.sage.projectwalk.Data;

import java.util.ArrayList;

public class DataManager {

    /**
     * Fetched a country object
     * According to isoCode
     * @param isoCode - Country iso code
     * @return Country object
     */
    public static Country getCountry(String isoCode){
        Country country = null;

        return country;
    }

    /**
     * Fetches list of country objects from file
     * @return Arraylist containing country objects
     */
    public static ArrayList<Country> getCountryList(){
        ArrayList<Country> countries = new ArrayList<>();

        return countries;
    }

    /**
     * Goes to the world data site and fetches all data
     */
    public static void synchronizeData(){
        //First refresh the countries.json file

        //Then for each indicator
        //Update it's isoCode_indicator.json file
    }

    /**
     * Checks if there is any data on the current device
     * @return True: If there is data False: No data found
     */
    public static boolean checkIfDataExists(){

    }





}
