package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.AttractionEditorFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.DisplayAttraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.GettingAroundFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MainMenuFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MapFragmentMain;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ShowCategory;
import com.example.tonyquick.thequicklawsonguidetohaarlem.interfaces.PermissionsHandler;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.GetFirebaseData;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.MenuHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public static final String STATE_ALBUM = "Album";
    public static final String STATE_GETTING_AROUND = "Getting Around";
    public static final String ALBUM_DATA_KEY = "albumdata";


    private FirebaseDatabase mDatabase;
    private DatabaseReference mDbReference;

    private FragmentManager fragMan;
    private MapFragmentMain mapFrag, directionsMapFrag;
    private ShowCategory showCategory;
    AttractionEditorFragment attractionEditorFragment;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private AuthListener authListener;

    private MenuHandler menuHandler;

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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

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
    protected void onStart() {
        super.onStart();
        authListener = new AuthListener();
        mAuth.addAuthStateListener(new AuthListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener!=null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void menuItemClicked(MenuItem i) {
        currentState = i.getTitle();
        showCategory = ShowCategory.newInstance(i.getTitle());
        Log.d("AJQ",i.getTitle());
        fragMan.beginTransaction().replace(R.id.main_frame,showCategory,"showCat").setCustomAnimations(R.anim.fade_in,R.anim.fade_out).addToBackStack("main").commit();
        //FragmentTransaction fragTrans = fragMan.beginTransaction();
        //fragTrans.replace(R.id.main_frame,showCategory,"showCat");
        //fragTrans.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onSmallIconClicked(MenuItem m) {


        currentState = m.getTitle();
        if (currentState.equals(MainActivity.STATE_GETTING_AROUND)){
            GettingAroundFragment gettingAroundFragment = GettingAroundFragment.newInstance();
            fragMan.beginTransaction().replace(R.id.main_frame,gettingAroundFragment,"gettingaroundfrag").addToBackStack("maintogettingaround").commit();
        }else if (currentState.equals(MainActivity.STATE_ALBUM)){
            Intent i = new Intent(this,AlbumActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList(ALBUM_DATA_KEY,list.getAlbumImages());
            i.putExtras(b);
            startActivity(i);

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
        fragMan.beginTransaction().replace(R.id.main_frame,mapFrag).addToBackStack("editor").commit();
    }

    @Override
    public void deleteSuggestion(Attraction current) {

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

        menuHandler = new MenuHandler(menu,getMenuInflater());

        if (mUser!=null){
            menuHandler.hideOption(R.id.action_login);
        }else{
            menuHandler.hideOption(R.id.action_admin_access);
            menuHandler.hideOption(R.id.action_logout);
        }



        return true;
    }




    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_admin_access:

                if (mUser != null) {
                    Intent i = new Intent(this, AdminActivity.class);
                    startActivity(i);
                } else {

                    LoginDialogFrag loginFrag = new LoginDialogFrag();
                    loginFrag.show(getFragmentManager(), null);

                }
                break;


            case R.id.action_logout:
                mAuth.signOut();
                menuHandler.showOption(R.id.action_login);
                menuHandler.hideOption(R.id.action_logout);
                menuHandler.hideOption(R.id.action_admin_access);
                break;


            case R.id.action_login:
                LoginDialogFrag loginFrag = new LoginDialogFrag();
                loginFrag.show(getFragmentManager(), null);
                break;

        }





        return super.onOptionsItemSelected(item);
    }


    class AuthListener implements FirebaseAuth.AuthStateListener {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            mUser = firebaseAuth.getCurrentUser();

        }
    }

    public class LoginDialogFrag extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.login_form, null));
            builder.setPositiveButton("Confirm",new DialogClick());
            builder.setNegativeButton("Cancel",null);

            return builder.create();

        }

        private class DialogClick implements Dialog.OnClickListener{
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText usernameEditText = (EditText)((AlertDialog) dialog).findViewById(R.id.username_edittext);
                EditText passwordEditText = (EditText)((AlertDialog) dialog).findViewById(R.id.password_edittext);

                String username = usernameEditText.getText().toString();
                username = username.trim();
                String password = passwordEditText.getText().toString();

                if (username.equals("")){
                    Toast.makeText(getApplicationContext(),"Username cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")){
                    Toast.makeText(getApplicationContext(),"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            menuHandler.showOption(R.id.action_logout);
                            menuHandler.hideOption(R.id.action_login);
                            menuHandler.showOption(R.id.action_admin_access);
                        }else{
                            Toast.makeText(getApplicationContext(),"Login failed. Please check credentials",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

    }




}
