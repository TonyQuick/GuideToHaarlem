package com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;

/**
 * Created by Tony Quick on 19/10/2016.
 */

public class MainMenuViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    ImageView image;
    int position;
    MenuItem item;


    public MainMenuViewHolder(View itemView) {
        super(itemView);

        title = (TextView)itemView.findViewById(R.id.menu_item_title);
        image = (ImageView)itemView.findViewById(R.id.menu_item_image);

    }

    public void updateInterface(MenuItem item, final MainMenuAdapter.OnMenuItemClickListener listener){
        this.item = item;
        title.setText(item.getTitle());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.menuItemClicked(MainMenuViewHolder.this.item);
            }
        });
    }


}
