package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.decorators.SpacingDecorator;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowCategory extends Fragment implements AttractionAdapter.OnClickAttractionCard {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATA_TO_DISPLAY = "to display";
    private String typeToDisplay;
    private FrameLayout contentHolder;
    private View contentInHolder;
    private RecyclerView recyclerView;
    private Boolean hasData = false;
    private LayoutInflater inflater;
    private TextView titleText;
    private RadioGroup sortViewRadio;
    private RadioButton alphabetically, distanceAway;

    private ArrayList<Attraction> content;


    public ShowCategory() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShowCategory newInstance(String typeToDisplay) {
        ShowCategory fragment = new ShowCategory();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_TO_DISPLAY, typeToDisplay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeToDisplay = getArguments().getString(ARG_DATA_TO_DISPLAY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.fragment_show_category, container, false);
        titleText = (TextView)v.findViewById(R.id.title_text);

        titleText.setText(typeToDisplay);

        RadioButtonListener radiolist = new RadioButtonListener();
        sortViewRadio = (RadioGroup)v.findViewById(R.id.radio_group_sort);
        sortViewRadio.setOnCheckedChangeListener(radiolist);
        alphabetically = (RadioButton)v.findViewById(R.id.alphabetically_radio);
        distanceAway = (RadioButton)v.findViewById(R.id.by_distance_radio);


        contentHolder = (FrameLayout)v.findViewById(R.id.show_attractions);
        setupRecyclerView();







        return v;
    }


    private void setupRecyclerView(){

        content = AttractionList.getInstance().subList(typeToDisplay);
        if (content.size()==0){
            contentInHolder = inflater.inflate(R.layout.lay_no_content,null);
            contentHolder.addView(contentInHolder);

        }else{
            hasData = true;
            contentInHolder = getActivity().getLayoutInflater().inflate(R.layout.recycler_view_general,null);
            recyclerView = (RecyclerView)contentInHolder.findViewById(R.id.recycler_general);
            contentHolder.addView(contentInHolder);

            AttractionAdapter adapter = new AttractionAdapter(content,this,typeToDisplay);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SpacingDecorator(5,SpacingDecorator.SPACE_DECORATOR_ORIENTATION_VERTICAL));
            LinearLayoutManager man = new LinearLayoutManager(getContext());
            man.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(man);




        }




    }


    @Override
    public void onClickAttraction() {

    }



    class RadioButtonListener implements RadioGroup.OnCheckedChangeListener{


        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            setupRecyclerView();
        }
    }
}
