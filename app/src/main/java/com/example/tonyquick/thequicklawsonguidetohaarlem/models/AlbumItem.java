package com.example.tonyquick.thequicklawsonguidetohaarlem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Tony Quick on 03/11/2016.
 */

public class AlbumItem implements Parcelable{

    private String urlSmall;
    private String urlLarge;
    private String title;
    private String id;


    public AlbumItem(String urlLarge, String urlSmall, String title) {
        this.urlLarge = urlLarge;
        this.urlSmall = urlSmall;
        this.title = title;
    }
    public AlbumItem(String title){
        this.title=title;
    }

    public AlbumItem(String title, String id){
        this.title=title;
        this.id=id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getUrlLarge() {
        return urlLarge;
    }

    public void setUrlLarge(String urlLarge) {
        this.urlLarge = urlLarge;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    //parcelable implementation

    protected AlbumItem(Parcel in) {
        urlSmall = in.readString();
        urlLarge = in.readString();
        title = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(urlSmall);
        dest.writeString(urlLarge);
        dest.writeString(title);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumItem> CREATOR = new Creator<AlbumItem>() {
        @Override
        public AlbumItem createFromParcel(Parcel in) {
            return new AlbumItem(in);
        }

        @Override
        public AlbumItem[] newArray(int size) {
            return new AlbumItem[size];
        }
    };


}
