package com.example.tonyquick.thequicklawsonguidetohaarlem.services;

import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 23/10/2016.
 */

public class AttractionList {

    private ArrayList<Attraction> attractions;
    private static AttractionList sInstance;


    public static AttractionList getInstance(){
        if (sInstance==null){
            sInstance = new AttractionList();
        }

        return sInstance;

    }

    private AttractionList(){
        attractions = new ArrayList<>();
    }

    public ArrayList<Attraction> getList(){
        return attractions;
    }

    public Attraction getAttraction(int index){
        return attractions.get(index);
    }

    public void addAttraction(Attraction att){
        attractions.add(att);
    }




    public ArrayList<Attraction> subList(String attractionType) {

        ArrayList<Attraction> tempList = new ArrayList<>();

        for (Attraction attraction : attractions) {
            switch (attractionType) {
                case "Bar":
                    if (attraction.isBar()) {
                        tempList.add(attraction);
                    }
                    break;
                case "Restaurants":
                    if (attraction.isRestaurant()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Cafes":
                    if (attraction.isBar()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Coffee Shops":
                    if (attraction.isBar()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Photo Opportunities":
                    if (attraction.isBar()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Things to do":
                    if (attraction.isBar()) {
                        tempList.add(attraction);
                    }
                    break;
            }
        }

        return tempList;
    }



}