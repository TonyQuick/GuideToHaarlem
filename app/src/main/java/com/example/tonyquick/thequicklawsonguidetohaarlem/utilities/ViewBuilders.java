package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Tony Quick on 28/10/2016.
 */

public class ViewBuilders {

    private Context context;
    private LayoutInflater inf;

    public  ViewBuilders(Context c, LayoutInflater inf){
        this.context=c;
        this.inf=inf;
    }

    public LinearLayout attractionCardContentBuilder(String type, Attraction att, LinearLayout layout){


        View description, vibe, priceRange, quickEatOrSitdown, cuisineType, speciality, suggestions, timeRequired, whereToGetTickets, subject, thingToDoType, imageHolder;
        int count = 0;

        if (att.getPictureLocation()!=null){
            //Todo code to get picture from server here

        }

        description = inf.inflate(R.layout.layout_attraction_content,layout,false);
        addTextToView(description,R.string.card_description,att.getDescription());
        layout.addView(description);

        switch (type) {

            case MainActivity.STATE_RESTAURANTS:


                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_cuisine,att.getCuisineType());

                if (att.getSpeciality()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_speciality,att.getSpeciality());
                }

                if(att.getVibe()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_vibe,att.getVibe());
                }

                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_eat_type,att.getQuickEatOrSitdown());

                if (att.getPriceRange()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_price_range,att.getPriceRange());
                }

                if (att.getOrderSuggestionsSize()>0){
                    addSeperatingLine(layout,inf);
                    suggestions = inf.inflate(R.layout.layout_attraction_content,layout,false);
                    addTextToView(suggestions,R.string.card_suggestions,att.getOrderSuggestions());
                    layout.addView(suggestions);
                }



                break;

            case MainActivity.STATE_BARS:

                addSeperatingLine(layout,inf);
                vibe = inf.inflate(R.layout.layout_attraction_content,layout);
                addTextToView(vibe,R.string.card_vibe,att.getVibe());







                break;

            case MainActivity.STATE_COFFEE_SHOPS:


                break;

            case MainActivity.STATE_CAFES:


                break;

            case MainActivity.STATE_PHOTO_OPPORTUNITIES:


                break;

            case MainActivity.STATE_THINGS_TO_DO:


                break;
        }


        return layout;
    }

    private void addViewToLinearLayout(int layoutToInflateId, LinearLayout layoutToBeAddedTo, int titleId, String content){
        addSeperatingLine(layoutToBeAddedTo,inf);
        View temp = inf.inflate(layoutToInflateId,layoutToBeAddedTo,false);
        addTextToView(temp,titleId,content);
        layoutToBeAddedTo.addView(temp);
    }

    private void addViewToLinearLayout(int layoutToInflateId, LinearLayout layoutToBeAddedTo, int titleId, ArrayList<String> content){
        addSeperatingLine(layoutToBeAddedTo,inf);
        View temp = inf.inflate(layoutToInflateId,layoutToBeAddedTo,false);
        addTextToView(temp,titleId,content);
        layoutToBeAddedTo.addView(temp);
    }



    private void addTextToView(View v, int titleId, String content){
        TextView title =(TextView) v.findViewById(R.id.description);
        TextView description =(TextView) v.findViewById(R.id.contents);
        title.setText(context.getString(titleId));
        description.setText(content);

    }

    private void addTextToView(View v, int titleId, ArrayList<String> content){
        TextView title =(TextView) v.findViewById(R.id.description);
        TextView description =(TextView) v.findViewById(R.id.contents);
        title.setText(context.getString(titleId));
        description.setText("\u2022 " +content.get(0));
        for (int i=1;i<content.size();i++){
            description.append(System.getProperty("line.separator"));
            description.append("\u2022 " +content.get(i));
        }


    }

    private static LinearLayout addSeperatingLine(LinearLayout lin, LayoutInflater inf){
        lin.addView(inf.inflate(R.layout.seperating_line,lin,false));
        return lin;
    }




}
