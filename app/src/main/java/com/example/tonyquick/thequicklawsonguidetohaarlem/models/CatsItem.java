package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

/**
 * Created by Tony Quick on 03/11/2016.
 */

public class CatsItem {

    private String url;
    private String title;
    private String driveLocation;

    public CatsItem(String title, String driveLocation) {
        this.title = title;
        this.driveLocation = driveLocation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDriveLocation() {
        return driveLocation;
    }

    public void setDriveLocation(String driveLocation) {
        this.driveLocation = driveLocation;
    }
}
