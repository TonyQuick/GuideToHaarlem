package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class Bars extends Attractions {
    private String closeTime;


    public Bars(String title, String urlImage, String closeTime) {
        super(title, urlImage);
        this.closeTime = closeTime;
    }
}
