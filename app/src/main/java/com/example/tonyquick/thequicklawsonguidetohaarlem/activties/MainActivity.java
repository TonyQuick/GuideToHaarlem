package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.AttractionEditorFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.DisplayAttraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MainMenuFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MapFragmentMain;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ShowCategory;
import com.example.tonyquick.thequicklawsonguidetohaarlem.interfaces.PermissionsHandler;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.GetFirebaseData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MainMenuAdapter.OnMenuItemClickListener,
        MainMenuFragment.SmallMenuIconClick,
        AttractionAdapter.AttractionClickListener,
        ShowCategory.ShowCategoryEventListener,
        DisplayAttraction.DirectionsOnMapListener,
        AttractionEditorFragment.AttractionEditorListener ,
        PermissionsHandler,
        MapFragmentMain.CoordinateGetterCallback{

    public static final String STATE_RESTAURANTS = "Restaurants";
    public static final String STATE_BARS = "Bars";
    public static final String STATE_COFFEE_SHOPS = "Coffee Shops";
    public static final String STATE_CAFES = "Cafes";
    public static final String STATE_PHOTO_OPPORTUNITIES = "Photo Opportunities";
    public static final String STATE_THINGS_TO_DO = "Things to do";
    public static final String STATE_DUTCH_TASTE = "Dutch Taste Test";
    public static final String STATE_CATS = "Cats of Haarlem";
    public static final String STATE_GETTING_AROUND = "Getting Around";


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbReference;

    private FragmentManager fragMan;
    private MapFragmentMain mapFrag, directionsMapFrag;
    private ShowCategory showCategory;
    AttractionEditorFragment attractionEditorFragment;


    private DisplayAttraction displayAttraction;
    private ArrayList<MenuItem> menuItems;
    private MenuItem menuLeft, menuRight;
    private AttractionList list;
    FrameLayout attractionCardFrame, shadeFrame;
    public static final int PERMISSION_COARSE_LOCATION = 101;
    public static final int PERMISSION_FINE_LOCATION = 102;

    private int screenWidth, screenHeight;

    private String currentState = null;
    private MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        attractionCardFrame = (FrameLayout)findViewById(R.id.display_attraction_card_frame);
        attractionCardFrame.setTranslationX(screenWidth);
        shadeFrame = (FrameLayout)findViewById(R.id.shade_frame);
        //shadeFrame.setVisibility(View.INVISIBLE);



        menuItems = MenuItem.getMenuItems();
        menuRight = MenuItem.getMenuRight();
        menuLeft = MenuItem.getMenuLeft();

        list = AttractionList.getInstance();

        Attraction att1 = new Attraction.AttractionBuilder("Jopenkerk",52.3798,4.6357,"Old Church transformed into a restaurant/brewery. Excellent food and beer in a lively atmosphere.")
            .addRestaurant("General/ Dutch","Casual",null,"Both","€15", new ArrayList<String>(Arrays.asList("Veal steak","IPA")))
            .getAttraction();


/*

        Attraction att2 = new Attraction.AttractionBuilder("Juboraj",60.0,4.6,"Decent curry house")
                .addRestaurant("Indian","Casual",null,"Sit down",null,null)
                .getAttraction();


        Attraction att3 = new Attraction.AttractionBuilder("Pizza Express",52.3763,4.6322,"")
                .addRestaurant("Italian",null,null,"Quick Eat",null,null)
                .getAttraction();



        Attraction att4= new Attraction.AttractionBuilder("McDonalds",50,25.0,"")
                .addRestaurant("American",null,null,"Takeaway","€5",null)
                .getAttraction();


        list.addAttraction(att1);
        list.addAttraction(att2);
        list.addAttraction(att3);
        list.addAttraction(att4);*/
        Log.d("AJQ"," "+list.getSize());
        if (list.getSize()==0) {
            GetFirebaseData hype = new GetFirebaseData();
        }

        mDatabase = FirebaseDatabase.getInstance();
        mDbReference = mDatabase.getReference();
       /* DatabaseReference attractionsRef = mDbReference.child("Attractions");
        DatabaseReference newRecord = attractionsRef.push();
        att1.setId(newRecord.getKey());
        newRecord.setValue(att1);

*/


        fragMan = getSupportFragmentManager();
        MainMenuFragment mainFrag = (MainMenuFragment)fragMan.findFragmentById(R.id.main_frame);
        if (mainFrag==null){
            mainFrag = MainMenuFragment.newInstance(menuItems,menuLeft,menuRight);
            fragMan.beginTransaction().add(R.id.main_frame,mainFrag).commit();

        }

    }

    @Override
    public void menuItemClicked(MenuItem i) {
        currentState = i.getTitle();
        showCategory = ShowCategory.newInstance(i.getTitle());
        Log.d("AJQ",i.getTitle());
        fragMan.beginTransaction().replace(R.id.main_frame,showCategory,"showCat").setCustomAnimations(R.anim.fade_in,R.anim.fade_out).addToBackStack("main").commit();
        FragmentTransaction fragTrans = fragMan.beginTransaction();
        fragTrans.replace(R.id.main_frame,showCategory,"showCat");
        fragTrans.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);

        Log.d("AJQ","here");

    }

    @Override
    public void onSmallIconClicked(MenuItem m) {

        currentState = m.getTitle();
        mapFrag = MapFragmentMain.newInstance(STATE_RESTAURANTS,MapFragmentMain.MAP_STATE_SHOW_ATT);
        fragMan.beginTransaction().replace(R.id.main_frame,mapFrag,"mapfrag").commit();
        Log.d("Ajq","assume we are doing this");

    }


    @Override
    public Boolean checkPermissionsFromFrag() {
        Boolean permissionPreviouslyGranted = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionPreviouslyGranted = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }
        return permissionPreviouslyGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Fragment fragy = fragMan.findFragmentById(R.id.main_frame);
                    if(fragy instanceof MapFragmentMain){
                        mapFrag.startLocationService();
                    }
                    if(fragy instanceof ShowCategory){
                        showCategory.getLocationUpdate();
                    }

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if(attractionCardFrame.getTranslationX()==0.0){
            Fragment temp = getSupportFragmentManager().findFragmentById(R.id.display_attraction_card_frame);
            if(temp instanceof DisplayAttraction) {
                attractionCardFrame.animate().translationX(screenWidth);
                shadeFrame.animate().alpha(0.0f);
                //shadeFrame.setVisibility(View.INVISIBLE);

            }else{
                super.onBackPressed();
            }

        }else{
            super.onBackPressed();
        }


    }

    @Override
    public void onAttractionClicked(Attraction a) {

        list.setCurrentAttractionInScope(a);
        displayAttraction = DisplayAttraction.newInstance(currentState);
        fragMan.beginTransaction().add(R.id.display_attraction_card_frame,displayAttraction,"displayAttraction").commit();


        shadeFrame.setVisibility(View.VISIBLE);
        attractionCardFrame.animate().translationX(0);
        shadeFrame.animate().alpha(0.8f);


    }

    @Override
    public void seeDataOnMapButtonClicked() {
        mapFrag = MapFragmentMain.newInstance(currentState,MapFragmentMain.MAP_STATE_SHOW_ATT);
        fragMan.beginTransaction().replace(R.id.main_frame,mapFrag).addToBackStack("ShowCategory").commit();


    }

    @Override
    public void makeSuggestionsButtonClicked() {
        attractionEditorFragment = AttractionEditorFragment.newInstance(AttractionEditorFragment.STATE_ATTRACTION_EDITOR_SUGGESTION,currentState);
        fragMan.beginTransaction().replace(R.id.main_frame,attractionEditorFragment).addToBackStack("ShowCategory").commit();

    }

    @Override
    public void editorFinish() {
        fragMan.popBackStack();
    }

    @Override
    public void getCoordsFromMap() {
        mapFrag = MapFragmentMain.newInstance(null,MapFragmentMain.MAP_STATE_SELECT_LOC);
        fragMan.beginTransaction().replace(R.id.admin_content_frame,mapFrag).addToBackStack("editor").commit();
    }




    @Override
    public void onDirectionsOnMapClicked() {
        directionsMapFrag = MapFragmentMain.newInstance(currentState,MapFragmentMain.MAP_STATE_DIRECTIONS);
        fragMan.beginTransaction().replace(R.id.display_attraction_card_frame,directionsMapFrag).addToBackStack("displayAttraction").commit();
        //TODO need to change to be map frag to handle directions
    }

    @Override
    public void coordinatesSelectedCallback(LatLng location) {
        fragMan.popBackStackImmediate();
        attractionEditorFragment.setCoords(location);

    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_admin_access) {
            //TODO login stuff with username and password, move below to response from login request

            Intent i = new Intent(this, AdminActivity.class);
            startActivity(i);
            

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
