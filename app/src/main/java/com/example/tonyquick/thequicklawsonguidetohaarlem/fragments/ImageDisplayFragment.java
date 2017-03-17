package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.ThumbnailAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.decorators.SpacingDecorator;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.AlbumItem;

import java.util.ArrayList;


public class ImageDisplayFragment extends Fragment {

    FloatingActionButton addImageButton;
    ImageDisplayFragmentHandler parentEventHandler;
    FrameLayout recyclerFrame;
    RecyclerView recyclerView;
    ThumbnailAdapter adapter;

    ArrayList<AlbumItem> data;

    public ImageDisplayFragment() {
        // Required empty public constructor
    }


    public static ImageDisplayFragment newInstance(ArrayList<AlbumItem> data) {
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MainActivity.ALBUM_DATA_KEY,data);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = savedInstanceState.getParcelableArrayList(MainActivity.ALBUM_DATA_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_display, container, false);

        addImageButton = (FloatingActionButton)v.findViewById(R.id.floating_add_image);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentEventHandler.addButtonClicked();
            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int noThumbs = screenWidth/150;

        Log.d("AJQ","Screen width "+screenWidth);
        Log.d("AJQ","No. thumbs "+noThumbs);


        if (data.size()>0) {
            recyclerView = new RecyclerView(getContext());
            recyclerFrame.addView(recyclerView);

            adapter = new ThumbnailAdapter(data, parentEventHandler, 150);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SpacingDecorator(10, SpacingDecorator.SPACE_DECORATOR_ORIENTATION_VERTICAL));
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), noThumbs);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }



        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof ImageDisplayFragmentHandler){
            parentEventHandler = (ImageDisplayFragmentHandler)getActivity();
        }else{
            //TODO handle error
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MainActivity.ALBUM_DATA_KEY,data);
        super.onSaveInstanceState(outState);
    }

    public interface ImageDisplayFragmentHandler{
        void addButtonClicked();
        void thumbnailSelected(AlbumItem a);


    }




}
