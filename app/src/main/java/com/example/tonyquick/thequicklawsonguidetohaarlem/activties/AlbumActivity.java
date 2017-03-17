package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ImageAddFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ImageDisplayFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ImageLargeDisplayFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.AlbumItem;

import java.util.ArrayList;


public class AlbumActivity extends AppCompatActivity implements ImageDisplayFragment.ImageDisplayFragmentHandler, ImageAddFragment.ImageAddFragmentEventHandler {

    ImageAddFragment imageAddFragment;
    ImageDisplayFragment imageMenuFragment;
    ImageLargeDisplayFragment imageSliderFrag;
    FrameLayout contentFrame, secondaryFrame, shadeFrame;
    FragmentManager fragMan;
    FloatingActionButton addButton;

    ArrayList<AlbumItem> dataset;


    int screenWidth, screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        dataset = this.getIntent().getExtras().getParcelableArrayList(MainActivity.ALBUM_DATA_KEY);

        contentFrame = (FrameLayout)findViewById(R.id.main_content_frame);
        secondaryFrame = (FrameLayout)findViewById(R.id.secondary_content_frame);
        shadeFrame = (FrameLayout)findViewById(R.id.shade_frame);
        shadeFrame.setAlpha(0.0f);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        secondaryFrame.setTranslationX(screenWidth);
        secondaryFrame.setAlpha(0.0f);

        fragMan = getSupportFragmentManager();
        imageMenuFragment=(ImageDisplayFragment)fragMan.findFragmentById(R.id.main_content_frame);
        if (imageMenuFragment==null){
            imageMenuFragment=ImageDisplayFragment.newInstance(dataset);
            fragMan.beginTransaction().add(R.id.main_content_frame,imageMenuFragment).commit();
        }



    }


    @Override
    public void onBackPressed() {


        if (secondaryFrame.getTranslationX()==0.0){
            secondaryFrame.animate().translationX(screenWidth).alpha(0.0f);
            shadeFrame.animate().alpha(0.0f).setDuration(500);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public void addButtonClicked() {
        imageAddFragment = ImageAddFragment.newInstance();
        fragMan.beginTransaction().add(R.id.secondary_content_frame,imageAddFragment).commit();
        secondaryFrame.animate().translationX(0).alpha(1.0f).setDuration(500);
        shadeFrame.animate().alpha(0.8f).setDuration(500);
/**
        imageSliderFrag = ImageLargeDisplayFragment.newInstance(null,null);
        imageSliderFrag.show(fragMan,"showingImage");
**/
    }

    @Override
    public void thumbnailSelected(AlbumItem a) {
        //TODO fullscreen image stuff
    }

    @Override
    public void imageAdded() {
        secondaryFrame.animate().translationX(screenWidth).alpha(0.0f).setDuration(500);
        shadeFrame.animate().alpha(0.0f).setDuration(500);
        fragMan.popBackStack();

    }
}
