package com.example.tonyquick.thequicklawsonguidetohaarlem.services;

import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 23/10/2016.
 */

public class AttractionList implements GetFirebaseData.FirebaseDataHandlerInterface {

    private ArrayList<Attraction> attractions, suggestions;
    private static AttractionList sInstance;
    private Attraction currentAttractionInScope;
    private GetFirebaseData serverHandler;



    public static AttractionList getInstance(){
        if (sInstance==null){
            sInstance = new AttractionList();
        }

        return sInstance;


    }

    private AttractionList(){
        attractions = new ArrayList<>();
        serverHandler = new GetFirebaseData(this);
        serverHandler.addChangeListener();
    }



    public ArrayList<Attraction> getList(){
        return attractions;
    }

    public int getSize(){
        return attractions.size();
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
                case MainActivity.STATE_BARS:
                    if (attraction.isBar()) {
                        tempList.add(attraction);
                    }
                    break;
                case MainActivity.STATE_RESTAURANTS:
                    if (attraction.isRestaurant()) {
                        tempList.add(attraction);
                    }
                    break;

                case MainActivity.STATE_CAFES:
                    if (attraction.isCafe()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Coffee Shops":
                    if (attraction.isCoffeeShop()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Photo Opportunities":
                    if (attraction.isPhotoOpportunity()) {
                        tempList.add(attraction);
                    }
                    break;

                case "Things to do":
                    if (attraction.isThingToDo()) {
                        tempList.add(attraction);
                    }
                    break;
            }
        }

        return tempList;
    }




    public Attraction getCurrentAttractionInScope() {
        return currentAttractionInScope;
    }

    public void setCurrentAttractionInScope(Attraction currentAttractionInScope) {
        this.currentAttractionInScope = currentAttractionInScope;
    }



    @Override
    public void childAdded(Attraction a) {
        Log.d("Ajq","Child added called");
        attractions.add(a);
    }

    @Override
    public void childChanged(Attraction a) {
        for (int i=0;i<attractions.size(); i++){
            if (attractions.get(i).getId().equals(a.getId())){
                attractions.set(i,a);
            }
        }
    }

    @Override
    public void childRemoved(String id) {
        for (Attraction temp : attractions){
            if (temp.getId().equals(id)){
                attractions.remove(temp);
                return;
            }
        }
    }




    @Override
    public void suggestionAdded(Attraction a) {
        suggestions.add(a);
    }

    @Override
    public void suggestionRemoved(String id) {
        for (Attraction temp : suggestions){
            if (temp.getId().equals(id)){
                suggestions.remove(temp);
                return;
            }
        }
    }

    public void populateSuggestions(){
        suggestions = new ArrayList<>();
        serverHandler.addChangeListenerSuggestions();
    }

    public ArrayList<Attraction> getSuggestions(){
        return suggestions;
    }


}
