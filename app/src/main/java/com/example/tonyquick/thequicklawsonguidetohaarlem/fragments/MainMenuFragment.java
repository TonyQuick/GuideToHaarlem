package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.decorators.HorizontalSpaceDecorator;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;

import java.util.ArrayList;


public class MainMenuFragment extends Fragment {


    private static final String ARG_MENU_ITEMS = "menu items param";

    private ArrayList<MenuItem> menuItems;
    MainMenuAdapter menuAdapter;
    RecyclerView menuRecyclerView;


    public MainMenuFragment() {
        // Required empty public constructor
    }


    public static MainMenuFragment newInstance(ArrayList<MenuItem> menuItems) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MENU_ITEMS,menuItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.menuItems = getArguments().getParcelableArrayList(ARG_MENU_ITEMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu_fragment, container, false);
        menuRecyclerView =(RecyclerView) v.findViewById(R.id.recycler_main_menu);
        //menuRecyclerView.addItemDecoration(new );

        menuAdapter = new MainMenuAdapter(menuItems,(MainActivity)getActivity());

        menuRecyclerView.setAdapter(menuAdapter);
        menuRecyclerView.addItemDecoration(new HorizontalSpaceDecorator(30));

        LinearLayoutManager man = new LinearLayoutManager(getContext());
        man.setOrientation(LinearLayoutManager.HORIZONTAL);
        menuRecyclerView.setLayoutManager(man);







        return v;
    }


}
