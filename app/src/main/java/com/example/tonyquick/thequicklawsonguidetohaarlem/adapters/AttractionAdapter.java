package com.example.tonyquick.thequicklawsonguidetohaarlem.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders.AttractionViewHolder;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 23/10/2016.
 */

public class AttractionAdapter extends RecyclerView.Adapter<AttractionViewHolder> {

    private ArrayList<Attraction> currentAttractions;
    private AttractionClickListener listener;
    private String type;




    public AttractionAdapter(ArrayList<Attraction> currentAttractions, AttractionClickListener listener, String type){
        this.currentAttractions = currentAttractions;
        this.listener = listener;
        this.type = type;
    }

    public void updateDataset(ArrayList<Attraction> att){
        this.currentAttractions = att;
    }



    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_attraction_mini,parent,false);
        return new AttractionViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AttractionViewHolder holder, int position) {
        holder.updateUI(currentAttractions.get(position),type,listener);
    }

    @Override
    public int getItemCount() {
        return currentAttractions.size();
    }



    public interface AttractionClickListener {
        void onAttractionClicked(Attraction a);


    }
}
