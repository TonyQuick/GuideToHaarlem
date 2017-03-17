package com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionGenericAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;

/**
 * Created by Tony Quick on 14/11/2016.
 */

public class AttractionGenericViewHolder extends ViewHolder {
    AttractionGenericAdapter.AttractionGenClickListener listener;
    Attraction att;
    TextView title;
    TextView types;
    Context c;
    private Boolean hasType = false;


    public AttractionGenericViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.card_att_generic_title);
        types = (TextView)itemView.findViewById(R.id.card_att_generic_types);

    }

    public void updateUI(Attraction a, final AttractionGenericAdapter.AttractionGenClickListener listener, Context c){
        this.att=a;
        this.c = c;
        title.setText(att.getTitle());
        types.setText(R.string.attraction_no_types_set);

        hasType = false;

        if (att.isRestaurant()){
            appendString(R.string.menu_type_restaurant);
        }
        if (att.isBar()){
            appendString(R.string.menu_item_bar);
        }
        if (att.isCafe()){
            appendString(R.string.menu_item_cafe);
        }
        if (att.isCoffeeShop()){
            appendString(R.string.menu_item_coffee_shop);
        }
        if (att.isThingToDo()){
            appendString(R.string.menu_item_thing_to_do);
        }
        if (att.isPhotoOpportunity()){
            appendString(R.string.menu_item_photo_op);
        }
        if (!hasType) {
            types.setText(R.string.attraction_no_types_set);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AJQ","Listener pressed");
                listener.onAttractionGenClicked(att);
            }
        });

    }

    private void appendString(int stringId){
        if (hasType){
            types.append(System.getProperty("line.separator"));
            types.append(c.getResources().getString(stringId));
        }else {
            types.setText(c.getResources().getString(stringId));
            hasType=true;
        }
    }


}
