package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;

/**
 * Created by Tony Quick on 28/11/2016.
 */

public class MenuHandler {

    Menu menu;

    public MenuHandler(Menu menu, MenuInflater inf){
        this.menu=menu;
        inf.inflate(R.menu.options,this.menu);


    }

    public void hideOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    public void showOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }




}
