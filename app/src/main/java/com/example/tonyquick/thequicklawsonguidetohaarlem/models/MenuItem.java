package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class MenuItem implements Parcelable {

    private String title;
    private String drawableImage;

    public MenuItem(String title, String drawableImage) {
        this.title = title;
        this.drawableImage = drawableImage;
    }

    protected MenuItem(Parcel in) {
        title = in.readString();
        drawableImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(drawableImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };





    public String getTitle() {
        return title;
    }

    public String getDrawableImage() {
        return drawableImage;
    }





    public static ArrayList<MenuItem> getMenuItems(){

        ArrayList<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem("Restaurants",null));
        items.add(new MenuItem("Bars",null));
        items.add(new MenuItem("Cafes",null));
        items.add(new MenuItem("Coffee Shops",null));
        items.add(new MenuItem("Photo Opportunities",null));
        items.add(new MenuItem("Things to do",null));
        items.add(new MenuItem("Dutch Taste Test",null));


        return items;

    }
    public static MenuItem getMenuLeft(){
        return new MenuItem("Cats of Haarlem",null);
    }

    public static  MenuItem getMenuRight(){
        return new MenuItem("Getting Around",null);
    }

}
