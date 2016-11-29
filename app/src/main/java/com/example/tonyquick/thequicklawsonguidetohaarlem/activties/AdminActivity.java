package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionGenericAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.AdminMainMenuFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.AttractionEditSelector;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.AttractionEditorFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MapFragmentMain;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ShowCategory;
import com.example.tonyquick.thequicklawsonguidetohaarlem.interfaces.PermissionsHandler;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.GetFirebaseData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.concurrent.TimeUnit;

import static com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity.PERMISSION_FINE_LOCATION;

public class AdminActivity extends AppCompatActivity implements AdminMainMenuFragment.MenuButtonListener,
        AttractionEditorFragment.AttractionEditorListener,
        AttractionGenericAdapter.AttractionGenClickListener ,
        AttractionEditSelector.ConfirmDeleteListener,
        MapFragmentMain.CoordinateGetterCallback ,
        PermissionsHandler{

    FragmentManager fragMan;
    AttractionEditorFragment attractionEditorFragment;
    AttractionEditSelector attractionEditSelector;
    MapFragmentMain mapFragmentMain;

    String editSelectorStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        AttractionList.getInstance().populateSuggestions();

        fragMan = this.getSupportFragmentManager();
        AdminMainMenuFragment adminFrag = (AdminMainMenuFragment)fragMan.findFragmentById(R.id.admin_content_frame);
        if (adminFrag==null){
            adminFrag = new AdminMainMenuFragment();
            fragMan.beginTransaction().add(R.id.admin_content_frame,adminFrag).commit();

        }

    }




    @Override
    public void onMenuButtonClicked(View v) {
        switch (v.getId()){
            case (R.id.create_attraction_button):
                attractionEditorFragment = AttractionEditorFragment.newInstance(AttractionEditorFragment.STATE_ATTRACTION_EDITOR_CREATE,null);
                fragMan.beginTransaction().replace(R.id.admin_content_frame, attractionEditorFragment).addToBackStack("menu").commit();
                break;

            case (R.id.edit_attraction_button):
                editSelectorStatus = AttractionEditSelector.STATE_EDIT;
                attractionEditSelector = AttractionEditSelector.newInstance(editSelectorStatus);
                fragMan.beginTransaction().replace(R.id.admin_content_frame,attractionEditSelector).addToBackStack("menu").commit();
                break;


            case (R.id.delete_attraction_button):
                attractionEditSelector = AttractionEditSelector.newInstance(AttractionEditSelector.STATE_DELETE);
                fragMan.beginTransaction().replace(R.id.admin_content_frame,attractionEditSelector).addToBackStack("menu").commit();
                break;

            case (R.id.manage_suggestions_button):
                editSelectorStatus = AttractionEditSelector.STATE_SUGGESTIONS;
                attractionEditSelector = AttractionEditSelector.newInstance(editSelectorStatus);
                fragMan.beginTransaction().replace(R.id.admin_content_frame,attractionEditSelector).addToBackStack("menu").commit();
                break;
        }

    }

    @Override
    public void editorFinish() {

        fragMan.popBackStack();

    }

    @Override
    public void getCoordsFromMap() {
        mapFragmentMain = MapFragmentMain.newInstance(null,MapFragmentMain.MAP_STATE_SELECT_LOC);
        fragMan.beginTransaction().replace(R.id.admin_content_frame,mapFragmentMain).addToBackStack("editor").commit();
    }

    @Override
    public void deleteSuggestion(Attraction a) {
        fragMan.popBackStack();
        final Attraction att = a;
        final DatabaseReference suggestionsRef = FirebaseDatabase.getInstance().getReference().child(GetFirebaseData.SUGGESTIONS_REFERENCE);
        DatabaseReference deleteRef = suggestionsRef.child(a.getId());
        deleteRef.removeValue();
        Snackbar.make(findViewById(R.id.relative_layout_admin),"Suggestion has been deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestionsRef.child(att.getId()).setValue(att);
            }
        }).show();
    }


    @Override
    public void onAttractionGenClicked(Attraction a) {
        AttractionList.getInstance().setCurrentAttractionInScope(a);
        fragMan.popBackStack();
        if (editSelectorStatus.equals(AttractionEditSelector.STATE_EDIT)) {
            attractionEditorFragment = AttractionEditorFragment.newInstance(AttractionEditorFragment.STATE_ATTRACTION_EDITOR_EDIT, null);
        }else{
            attractionEditorFragment = AttractionEditorFragment.newInstance(AttractionEditorFragment.STATE_ATTRACTION_EDITOR_MANAGE_SUGGESTIONS,null);
        }
        fragMan.beginTransaction().replace(R.id.admin_content_frame, attractionEditorFragment).addToBackStack("menu").commit();
    }

    @Override
    public void onDeleteConfirmed(Attraction a) {
        fragMan.popBackStack();
        final Attraction att = a;
        final DatabaseReference attractionsRef = FirebaseDatabase.getInstance().getReference().child("Attractions");
        DatabaseReference deleteRef = attractionsRef.child(a.getId());
        deleteRef.removeValue();
        Snackbar.make(findViewById(R.id.relative_layout_admin),"Attraction has been deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attractionsRef.child(att.getId()).setValue(att);
            }
        }).show();

    }

    @Override
    public void coordinatesSelectedCallback(LatLng location) {
        fragMan.popBackStackImmediate();


        attractionEditorFragment.setCoords(location);
        Log.d("Ajq",location.toString());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Fragment fragy = fragMan.findFragmentById(R.id.admin_content_frame);
                    if(fragy instanceof MapFragmentMain){
                        mapFragmentMain.startLocationService();
                    }


                }
                break;
        }
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
}
