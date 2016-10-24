package com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;


/**
 * Created by Tony Quick on 23/10/2016.
 */

public class AttractionViewHolder extends RecyclerView.ViewHolder {

    private Attraction attraction;
    private AttractionAdapter.OnClickAttractionCard listener;

    private LinearLayout contentHolder;
    private TextView title;

    public AttractionViewHolder(View itemView) {
        super(itemView);
        contentHolder = (LinearLayout)itemView.findViewById(R.id.content_container);
        title = (TextView)itemView.findViewById(R.id.attraction_title);


    }

    public void updateUI(Attraction att, String type, AttractionAdapter.OnClickAttractionCard listener) {
        this.attraction = att;


        switch (type) {
            case "Restaurants":
                buildRestaurant();
                break;
            case "Bars":
                buildBar();
                break;
            case "Cafes":
                buildCafe();
                break;
            case "Coffee Shops":
                buildCoffeeShop();
                break;
            case "Photo Opportunities":
                buildPhotoOpportunity();
                break;
            case "Things to do":
                buildThingsToDo();
                break;
        }
    }
    private void buildRestaurant() {
        //TODO inflate layout_attraction_contents for each attribute required, seperating line between each, add to contentHolder if possible

    }
    private void buildBar() {
    }
    private void buildCafe() {
    }
    private void buildCoffeeShop() {
    }
    private void buildPhotoOpportunity() {
    }
    private void buildThingsToDo() {
    }





}








