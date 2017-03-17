package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class MenuItem implements Parcelable {

    private String title;
    private int drawableImage;

    public MenuItem(String title, int drawableImage) {
        this.title = title;
        this.drawableImage = drawableImage;
    }

    protected MenuItem(Parcel in) {
        title = in.readString();
        drawableImage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(drawableImage);
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

    public int getDrawableImage() {
        return drawableImage;
    }





    public static ArrayList<MenuItem> getMenuItems(){

        ArrayList<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem(MainActivity.STATE_RESTAURANTS, R.drawable.restaurant));
        items.add(new MenuItem(MainActivity.STATE_BARS,R.drawable.bar));
        items.add(new MenuItem(MainActivity.STATE_CAFES,R.drawable.cafe));
        items.add(new MenuItem(MainActivity.STATE_COFFEE_SHOPS,R.drawable.coffee_shop));
        items.add(new MenuItem(MainActivity.STATE_PHOTO_OPPORTUNITIES,R.drawable.photo_ops));
        items.add(new MenuItem(MainActivity.STATE_THINGS_TO_DO,R.drawable.things_to_do));
        items.add(new MenuItem(MainActivity.STATE_DUTCH_TASTE,0));


        return items;

    }
    public static MenuItem getMenuLeft(){
        return new MenuItem(MainActivity.STATE_ALBUM,R.drawable.cats_of_haarlem);
    }

    public static  MenuItem getMenuRight(){
        return new MenuItem(MainActivity.STATE_GETTING_AROUND,R.drawable.getting_around);
    }

}
