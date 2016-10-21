package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MainMenuFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MapFragmentMain;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainMenuAdapter.OnMenuItemClickListener, MainMenuFragment.SmallMenuIconClick {

    private FragmentManager fragMan;
    private MapFragmentMain mapFrag;
    private ArrayList<MenuItem> menuItems;
    private MenuItem menuLeft, menuRight;
    public static final int PERMISSION_COARSE_LOCATION = 101;
    public static final int PERMISSION_FINE_LOCATION = 102;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        menuItems = MenuItem.getMenuItems();
        menuRight = MenuItem.getMenuRight();
        menuLeft = MenuItem.getMenuLeft();

        fragMan = getSupportFragmentManager();
        MainMenuFragment mainFrag = (MainMenuFragment)fragMan.findFragmentById(R.id.main_frame);
        if (mainFrag==null){
            mainFrag = MainMenuFragment.newInstance(menuItems,menuLeft,menuRight);
            fragMan.beginTransaction().add(R.id.main_frame,mainFrag).commit();


        }





    }


    @Override
    public void menuItemClicked(MenuItem i) {
        Log.d("AJQ","Menu item clicked: "+i.getTitle());
        mapFrag = MapFragmentMain.newInstance(i.getTitle());
        fragMan.beginTransaction().replace(R.id.main_frame,mapFrag,"map frag").addToBackStack("main").commit();


    }

    @Override
    public void onSmallIconClicked(MenuItem m) {
        Intent i = new Intent(this.getApplicationContext(), AdminActivity.class);
        startActivity(i);
        Log.d("AJQ","Menu item clicked: "+m.getTitle());
    }

    public Boolean checkPermissions() {
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
                }
                break;


        }




    }
}
