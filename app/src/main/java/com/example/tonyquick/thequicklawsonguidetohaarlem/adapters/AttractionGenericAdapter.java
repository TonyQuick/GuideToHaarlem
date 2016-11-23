package com.example.tonyquick.thequicklawsonguidetohaarlem.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders.AttractionGenericViewHolder;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 14/11/2016.
 */

public class AttractionGenericAdapter extends RecyclerView.Adapter<AttractionGenericViewHolder> {

    private ArrayList<Attraction> dataset;
    AttractionGenClickListener listener;
    Context c;

    public AttractionGenericAdapter(ArrayList<Attraction> dataset,AttractionGenClickListener listener, Context c){
        this.dataset=dataset;
        this.listener=listener;
        this.c = c;
    }



    @Override
    public AttractionGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_attraction_generic,parent,false);
        return new AttractionGenericViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AttractionGenericViewHolder holder, int position) {
        holder.updateUI(dataset.get(position),listener,c);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public interface AttractionGenClickListener{
        void onAttractionGenClicked(Attraction a);
    }


}
