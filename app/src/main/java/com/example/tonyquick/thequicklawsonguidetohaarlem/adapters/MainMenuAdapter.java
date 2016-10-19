package com.example.tonyquick.thequicklawsonguidetohaarlem.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;
import com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders.MainMenuViewHolder;

import java.util.ArrayList;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuViewHolder>{

    private ArrayList<MenuItem> menuItems;
    private OnMenuItemClickListener listener;


    public interface OnMenuItemClickListener{
        void menuItemClicked(MenuItem i);
    }



    public MainMenuAdapter(ArrayList<MenuItem> menuItems, OnMenuItemClickListener listener){
        this.menuItems=menuItems;
        this.listener=listener;
    }



    @Override
    public MainMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_main_menu_item,parent,false);
        return new MainMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainMenuViewHolder holder, int position) {
        holder.updateInterface(menuItems.get(position),listener);


    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }
}
