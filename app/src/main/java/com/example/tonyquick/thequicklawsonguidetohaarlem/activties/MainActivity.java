package com.example.tonyquick.thequicklawsonguidetohaarlem.activties;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.MainMenuFragment;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainMenuAdapter.OnMenuItemClickListener {

    private FragmentManager fragMan;
    private ArrayList<MenuItem> menuItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuItems = MenuItem.getMenuItems();

        fragMan = getSupportFragmentManager();
        MainMenuFragment mainFrag = (MainMenuFragment)fragMan.findFragmentById(R.id.main_frame);
        if (mainFrag==null){
            mainFrag = MainMenuFragment.newInstance(menuItems);
            fragMan.beginTransaction().add(R.id.main_frame,mainFrag).commit();


        }





    }


    @Override
    public void menuItemClicked(MenuItem i) {
        Log.d("AJQ","Menu item clicked: "+i.getTitle());
    }
}
