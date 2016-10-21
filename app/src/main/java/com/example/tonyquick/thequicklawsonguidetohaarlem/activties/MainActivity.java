package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
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
    private ArrayList<MenuItem> menuItems;
    private MenuItem menuLeft, menuRight;



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
        MapFragmentMain mapFrag = MapFragmentMain.newInstance(i.getTitle());
        fragMan.beginTransaction().replace(R.id.main_frame,mapFrag).addToBackStack("main").commit();


    }

    @Override
    public void onSmallIconClicked(MenuItem m) {
        Intent i = new Intent(this.getApplicationContext(), AdminActivity.class);
        startActivity(i);
        Log.d("AJQ","Menu item clicked: "+m.getTitle());
    }
}
