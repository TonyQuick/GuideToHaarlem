package com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;



/**
 * Created by Tony Quick on 23/10/2016.
 */

public class AttractionViewHolder extends RecyclerView.ViewHolder {

    private Attraction attraction;
    private AttractionAdapter.AttractionClickListener listener;

    private TextView title, attributeTitle, attributeContent, distanceAway;


    public AttractionViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.mini_title_text);
        attributeTitle = (TextView)itemView.findViewById(R.id.attribute_title_text);
        attributeContent = (TextView)itemView.findViewById(R.id.attribute_contents_text);
        distanceAway = (TextView)itemView.findViewById(R.id.distance_content);




    }

    public void updateUI(final Attraction att, String type, final AttractionAdapter.AttractionClickListener listener) {
        this.attraction = att;
        title.setText(attraction.getTitle());
        this.listener = listener;
        if (attraction.getDistanceAway()!=null){
            String temp = (String.valueOf(attraction.getDistanceAway()))+"km";
            distanceAway.setText(temp);
        }


        switch (type) {
            case "Restaurants":
                attributeTitle.setText(R.string.cuisine);
                attributeContent.setText(attraction.getCuisineType());
                break;
            case "Bars":
            case "Cafes":
            case "Coffee Shops":
                attributeTitle.setText(R.string.vibe);
                attributeContent.setText(attraction.getVibe());
                break;
            case "Photo Opportunities":
                attributeTitle.setText(R.string.subject);
                attributeContent.setText(attraction.getSubject());
                break;
            case "Things to do":
                attributeTitle.setText(R.string.type);
                attributeContent.setText(attraction.getThingToDoType());
                break;
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAttractionClicked(AttractionViewHolder.this.attraction);
            }
        });


    }





}








