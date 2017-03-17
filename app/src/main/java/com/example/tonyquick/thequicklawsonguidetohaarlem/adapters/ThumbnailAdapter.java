package com.example.tonyquick.thequicklawsonguidetohaarlem.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ImageDisplayFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.AlbumItem;
import com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders.AttractionViewHolder;
import com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders.ThumbnailViewHolder;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 15/03/2017.
 */

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailViewHolder> {

    private ArrayList<AlbumItem> data;
    private ImageDisplayFragment.ImageDisplayFragmentHandler listener;
    private int size;


    public ThumbnailAdapter(ArrayList<AlbumItem> data, ImageDisplayFragment.ImageDisplayFragmentHandler listener, int size){
        this.data=data;
        this.listener=listener;
        this.size=size;

    }


    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_thumnail,parent,false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        holder.updateUI(data.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
