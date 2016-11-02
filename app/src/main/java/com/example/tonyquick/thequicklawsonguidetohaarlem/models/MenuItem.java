package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;

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

        items.add(new MenuItem(MainActivity.STATE_RESTAURANTS,null));
        items.add(new MenuItem(MainActivity.STATE_BARS,null));
        items.add(new MenuItem(MainActivity.STATE_CAFES,null));
        items.add(new MenuItem(MainActivity.STATE_COFFEE_SHOPS,null));
        items.add(new MenuItem(MainActivity.STATE_PHOTO_OPPORTUNITIES,null));
        items.add(new MenuItem(MainActivity.STATE_THINGS_TO_DO,null));
        items.add(new MenuItem(MainActivity.STATE_DUTCH_TASTE,null));


        return items;

    }
    public static MenuItem getMenuLeft(){
        return new MenuItem(MainActivity.STATE_CATS,null);
    }

    public static  MenuItem getMenuRight(){
        return new MenuItem(MainActivity.STATE_GETTING_AROUND,null);
    }

}
