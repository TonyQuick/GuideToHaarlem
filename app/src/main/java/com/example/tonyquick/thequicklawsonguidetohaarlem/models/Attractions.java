package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public abstract class Attractions {

    private String title;
    private String urlImage;

    public Attractions(String title, String urlImage){
        this.title = title;
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
