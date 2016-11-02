package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

import java.util.ArrayList;


public class Attraction {

    private String title, description, vibe, priceRange, quickEatOrSitdown, cuisineType, speciality, timeRequired, whereToGetTickets, subject, thingToDoType;
    private double lat;
    private double lon;
    private Double distanceAway;
    private ArrayList<String> photoOrToDoSuggestions, orderSuggestions;
    private String pictureLocation;
    private boolean bar, restaurant, thingToDo, cafe, coffeeShop, photoOpportunity;
    private String id;

    //required for firebase object conversion
    private Attraction(){
        this.photoOrToDoSuggestions=new ArrayList<>();
        this.orderSuggestions=new ArrayList<>();
    }

    private Attraction(AttractionBuilder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.vibe = builder.vibe;
        this.priceRange = builder.priceRange;
        this.quickEatOrSitdown = builder.quickEatOrSitdown;
        this.cuisineType = builder.cuisineType;
        this.speciality = builder.speciality;
        this.timeRequired = builder.timeRequired;
        this.whereToGetTickets = builder.whereToGetTickets;
        this.subject = builder.subject;
        this.thingToDoType = builder.thingToDoType;
        this.lat = builder.lat;
        this.lon = builder.lon;
        this.distanceAway = builder.distanceAway;
        this.photoOrToDoSuggestions = builder.photoOrToDoSuggestions;
        this.orderSuggestions = builder.orderSuggestions;
        this.pictureLocation = builder.pictureLocation;
        this.bar = builder.bar;
        this.restaurant = builder.restaurant;
        this.thingToDo = builder.thingToDo;
        this.cafe = builder.cafe;
        this.coffeeShop = builder.coffeeShop;
        this.photoOpportunity = builder.photoOpportunity;
    }

    public String getId(){
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getVibe() {
        return vibe;
    }

    public String getSubject() {
        return subject;
    }


    public String getPriceRange() {
        return priceRange;
    }

    public String getQuickEatOrSitdown() {
        return quickEatOrSitdown;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getSpeciality() {
        return speciality;
    }


    public String getTimeRequired() {
        return timeRequired;
    }

    public String getWhereToGetTickets() {
        return whereToGetTickets;
    }



    public String getThingToDoType() {
        return thingToDoType;
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


    public int getOrderSuggestionsSize(){
        return orderSuggestions.size();
    }

    public ArrayList<String> getOrderSuggestions(){
        return orderSuggestions;
    }

    public int getPhotoOrToDoSuggestionsSize(){
        return photoOrToDoSuggestions.size();
    }

    public ArrayList<String> getPhotoOrToDoSuggestions(){
        return photoOrToDoSuggestions;
    }

    //used in distance calculations
    public Double getDistanceAway() {
        return distanceAway;
    }



    public void setDistanceAway(double distanceAway) {
        this.distanceAway = distanceAway;
    }

    public void setId(String id){
        this.id=id;
    }


    public static class AttractionBuilder{
        private String title, description, vibe, priceRange, quickEatOrSitdown, cuisineType, speciality, timeRequired, whereToGetTickets, subject, thingToDoType;
        private double lat;
        private double lon;
        private Double distanceAway;
        private ArrayList<String> orderSuggestions,photoOrToDoSuggestions;
        private String pictureLocation;
        private boolean bar, restaurant, thingToDo, cafe, coffeeShop, photoOpportunity;

        public AttractionBuilder(String title, double lat, double lon, String description) {
            this.description = description;
            this.title = title;
            this.lon = lon;
            this.lat = lat;
            restaurant=false;
            bar=false;
            cafe=false;
            coffeeShop=false;
            photoOpportunity=false;
            thingToDo=false;
            this.orderSuggestions=new ArrayList<>();
            this.photoOrToDoSuggestions = new ArrayList<>();
        }

        public AttractionBuilder addRestaurant(String cuisineType, String vibe, String speciality, String quickEatOrSitdown, String priceRange, ArrayList<String> orderSuggestions){
            this.restaurant = true;
            this.cuisineType = cuisineType;
            this.vibe=vibe;
            this.speciality=speciality;
            this.quickEatOrSitdown = quickEatOrSitdown;
            this.priceRange=priceRange;
            if (orderSuggestions != null){
                this.orderSuggestions = orderSuggestions;
            }
            return this;
        }

        public AttractionBuilder addBar(String vibe, String speciality, String cuisineType, String priceRange,ArrayList<String> orderSuggestions) {
            this.bar = true;
            this.vibe = vibe;
            this.speciality = speciality;
            this.cuisineType = cuisineType;
            this.priceRange = priceRange;
            if (orderSuggestions != null){
                this.orderSuggestions = orderSuggestions;
            }

            return this;
        }
        public AttractionBuilder addCafe(String vibe, String speciality, String cuisineType, String quickEatOrSitdown, String priceRange,ArrayList<String> orderSuggestions){
            this.cafe=true;
            this.vibe = vibe;
            this.speciality=speciality;
            this.cuisineType = cuisineType;
            this.quickEatOrSitdown = quickEatOrSitdown;
            this.priceRange=priceRange;
            if (orderSuggestions != null){
                this.orderSuggestions = orderSuggestions;
            }

            return this;
        }
        public AttractionBuilder addCoffeeShop(String vibe, String speciality, String priceRange, ArrayList<String>orderSuggestions){
            this.coffeeShop=true;
            this.vibe = vibe;
            this.speciality=speciality;
            this.priceRange=priceRange;
            if (orderSuggestions != null){
                this.orderSuggestions = orderSuggestions;
            }
            return this;
        }
        public AttractionBuilder addThingToDo(String thingToDoType, String priceRange, String timeRequired, String whereToGetTickets, ArrayList<String> photoOrToDoSuggestions){
            this.thingToDo=true;
            this.thingToDoType=thingToDoType;
            this.priceRange=priceRange;
            this.timeRequired=timeRequired;
            this.whereToGetTickets=whereToGetTickets;
            if (photoOrToDoSuggestions != null) {
                this.photoOrToDoSuggestions = photoOrToDoSuggestions;
            }

            return this;
        }
        public AttractionBuilder addPhotoOpporunity(ArrayList<String> photoOrToDoSuggestions){
            this.photoOpportunity=true;
            if (photoOrToDoSuggestions != null) {
                this.photoOrToDoSuggestions = photoOrToDoSuggestions;
            }
            return this;

        }



        public AttractionBuilder addPicture(String pictureLocation){
            this.pictureLocation=pictureLocation;

            return this;
        }

        public Attraction getAttraction(){
            return new Attraction(this);
        }

    }







}
