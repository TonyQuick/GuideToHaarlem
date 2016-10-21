package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.MainMenuAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.decorators.HorizontalSpaceDecorator;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.MenuItem;

import java.util.ArrayList;


public class MainMenuFragment extends Fragment {


    private static final String ARG_MENU_ITEMS = "menu items param";
    private static final String ARG_MENU_LEFT = "menu item left";
    private static final String ARG_MENU_RIGHT = "menu item right";

    private ArrayList<MenuItem> menuItems;
    private MenuItem left, right;
    MainMenuAdapter menuAdapter;
    RecyclerView menuRecyclerView;
    CardView holderLeft;
    CardView holderRight;
    TextView rightHolderTitle, leftHolderTitle;
    SmallMenuIconClick listener;


    public MainMenuFragment() {
        // Required empty public constructor
    }


    public interface SmallMenuIconClick{
        void onSmallIconClicked(MenuItem m);
    }


    public static MainMenuFragment newInstance(ArrayList<MenuItem> menuItems, MenuItem left, MenuItem right) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MENU_ITEMS,menuItems);
        args.putParcelable(ARG_MENU_LEFT,left);
        args.putParcelable(ARG_MENU_RIGHT,right);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.menuItems = getArguments().getParcelableArrayList(ARG_MENU_ITEMS);
            this.left = getArguments().getParcelable(ARG_MENU_LEFT);
            this.right = getArguments().getParcelable(ARG_MENU_RIGHT);
        }
        listener = (MainActivity)getActivity();
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



        holderLeft = (CardView)v.findViewById(R.id.holder_left);
        holderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSmallIconClicked(left);
            }
        });
        holderRight = (CardView)v.findViewById(R.id.holder_right);
        holderRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSmallIconClicked(right);
            }
        });

        leftHolderTitle = (TextView) holderLeft.findViewById(R.id.menu_item_title);
        leftHolderTitle.setText(left.getTitle());
        rightHolderTitle = (TextView) holderRight.findViewById(R.id.menu_item_title);
        rightHolderTitle.setText(right.getTitle());






        return v;
    }


}
