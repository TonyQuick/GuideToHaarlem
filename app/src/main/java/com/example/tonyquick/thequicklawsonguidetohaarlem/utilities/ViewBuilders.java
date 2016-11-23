package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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



        if (att.getPictureLocationSmall()!=null){
            //Todo code to get picture from server here
            View imageView = inf.inflate(R.layout.layout_image_view,layout,true);
            ImageView imageHolder = (ImageView) imageView.findViewById(R.id.image_thumbnail);

            StorageReference ref = FirebaseStorage.getInstance().getReference().child("Attractions").child(att.getId());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .fitCenter()
                    .into(imageHolder);

        }

        View description = inf.inflate(R.layout.layout_attraction_content,layout,false);
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
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_order_suggestions,att.getOrderSuggestions());
                }


                break;

            case MainActivity.STATE_BARS:

                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_vibe,att.getVibe());

                if (att.getSpeciality()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_speciality,att.getSpeciality());
                }

                if (att.getPriceRange()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_price_range,att.getPriceRange());
                }

                if (att.getCuisineType()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_cuisine,att.getCuisineType());
                }

                if (att.getOrderSuggestionsSize()>0){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_order_suggestions,att.getOrderSuggestions());
                }

                break;

            case MainActivity.STATE_COFFEE_SHOPS:

                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_vibe,att.getVibe());

                if (att.getSpeciality()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_speciality,att.getSpeciality());
                }

                if (att.getPriceRange()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_price_range,att.getPriceRange());
                }

                if (att.getOrderSuggestionsSize()>0){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_order_suggestions,att.getOrderSuggestions());
                }

                break;

            case MainActivity.STATE_CAFES:

                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_vibe,att.getVibe());

                if (att.getCuisineType()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_cuisine,att.getCuisineType());
                }

                if (att.getSpeciality()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_speciality,att.getSpeciality());
                }

                if (att.getPriceRange()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_price_range,att.getPriceRange());
                }

                if (att.getQuickEatOrSitdown()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_eat_type,att.getQuickEatOrSitdown());
                }

                if (att.getOrderSuggestionsSize()>0){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_order_suggestions,att.getOrderSuggestions());
                }

                break;

            case MainActivity.STATE_PHOTO_OPPORTUNITIES:

                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_subject,att.getSubject());
                if (att.getPhotoOrToDoSuggestionsSize()>0){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_suggestions,att.getPhotoOrToDoSuggestions());
                }

                break;

            case MainActivity.STATE_THINGS_TO_DO:

                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_price_range,att.getPriceRange());
                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_type,att.getThingToDoType());
                addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_time_required,att.getTimeRequired());

                if(att.getWhereToGetTickets()!=null){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_where_tickets,att.getWhereToGetTickets());
                }
                if (att.getPhotoOrToDoSuggestionsSize()>0){
                    addViewToLinearLayout(R.layout.layout_attraction_content,layout,R.string.card_suggestions,att.getPhotoOrToDoSuggestions());
                }

                break;
        }


        return layout;
    }

    private void addViewToLinearLayout(int layoutToInflateId, LinearLayout layoutToBeAddedTo, int titleId, String content){
        addSeperatingLine(layoutToBeAddedTo,inf);
        View component = inf.inflate(layoutToInflateId,layoutToBeAddedTo,false);
        addTextToView(component,titleId,content);
        layoutToBeAddedTo.addView(component);
    }

    private void addViewToLinearLayout(int layoutToInflateId, LinearLayout layoutToBeAddedTo, int titleId, ArrayList<String> content){
        addSeperatingLine(layoutToBeAddedTo,inf);
        View component = inf.inflate(layoutToInflateId,layoutToBeAddedTo,false);
        addTextToView(component,titleId,content);
        layoutToBeAddedTo.addView(component);
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
        lin.addView(inf.inflate(R.layout.separating_line,lin,false));
        return lin;
    }




}
