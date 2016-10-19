package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class MenuItems {

    private String title;
    private String drawableImage;

    public MenuItems(String title, String drawableImage) {
        this.title = title;
        this.drawableImage = drawableImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDrawableImage() {
        return drawableImage;
    }

    public static ArrayList<MenuItems> getMenuItems(){

        ArrayList<MenuItems> items = new ArrayList<>();

        items.add(new MenuItems("Restaurants",null));
        items.add(new MenuItems("Bars",null));
        items.add(new MenuItems("Cafes",null));
        items.add(new MenuItems("Coffee Shops",null));
        items.add(new MenuItems("Photo Opportunities",null));
        items.add(new MenuItems("Things to do",null));
        items.add(new MenuItems("Getting Around",null));
        items.add(new MenuItems("Dutch Taste Test",null));
        items.add(new MenuItems("Cats of Haarlem",null));


        return items;

    }



}
