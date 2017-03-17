package com.example.tonyquick.thequicklawsonguidetohaarlem.viewholders;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ImageDisplayFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.AlbumItem;
import com.firebase.ui.storage.images.FirebaseImageLoader;


import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.R.attr.bitmap;

/**
 * Created by Tony Quick on 15/03/2017.
 */

public class ThumbnailViewHolder extends RecyclerView.ViewHolder {

    private ImageDisplayFragment.ImageDisplayFragmentHandler listener;
    private AlbumItem albumItem;
    private ImageView image;


    public ThumbnailViewHolder(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.thumbnail_image_view);
    }

    public void updateUI(final AlbumItem albumItem, final ImageDisplayFragment.ImageDisplayFragmentHandler listener){
        this.listener = listener;
        this.albumItem=albumItem;

        Glide.with(itemView.getContext())
                .load(albumItem.getUrlSmall())
                .asBitmap()
                .centerCrop()
                .transform(new RoundedCornersTransformation(itemView.getContext(),5,0))
                .into(image);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.thumbnailSelected(albumItem);
            }
        });

    }





}
