package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

/**
 * Created by Tony Quick on 23/10/2016.
 */

public class Attraction {

    private String title, vibe, priceRange, quickEatorSitdown, typeOfFood, speciality, suggestions, timeTakes, whereToGetTickets;
    private double lat;
    private double lon;
    private String pictureLocation;
    private boolean bar, restaurant, thingToDo, cafe, coffeeShop, photoOpportunity;



    public Attraction(String title, double lat, double lon) {
        this.title = title;
        this.lat = lat;
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public String getVibe() {
        return vibe;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public String getQuickEatorSitdown() {
        return quickEatorSitdown;
    }

    public String getTypeOfFood() {
        return typeOfFood;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public String getTimeTakes() {
        return timeTakes;
    }

    public String getWhereToGetTickets() {
        return whereToGetTickets;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getPictureLocation() {
        return pictureLocation;
    }

    public boolean isBar() {
        return bar;
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public boolean isThingToDo() {
        return thingToDo;
    }

    public boolean isCafe() {
        return cafe;
    }

    public boolean isCoffeeShop() {
        return coffeeShop;
    }

    public boolean isPhotoOpportunity() {
        return photoOpportunity;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setVibe(String vibe) {
        this.vibe = vibe;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public void setQuickEatorSitdown(String quickEatorSitdown) {
        this.quickEatorSitdown = quickEatorSitdown;
    }

    public void setTypeOfFood(String typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public void setTimeTakes(String timeTakes) {
        this.timeTakes = timeTakes;
    }

    public void setWhereToGetTickets(String whereToGetTickets) {
        this.whereToGetTickets = whereToGetTickets;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setPictureLocation(String pictureLocation) {
        this.pictureLocation = pictureLocation;
    }

    public void setBar(boolean bar) {
        this.bar = bar;
    }

    public void setRestaurant(boolean restaurant) {
        this.restaurant = restaurant;
    }

    public void setThingToDo(boolean thingToDo) {
        this.thingToDo = thingToDo;
    }

    public void setCafe(boolean cafe) {
        this.cafe = cafe;
    }

    public void setCoffeeShop(boolean coffeeShop) {
        this.coffeeShop = coffeeShop;
    }

    public void setPhotoOpportunity(boolean photoOpportunity) {
        this.photoOpportunity = photoOpportunity;
    }
}
