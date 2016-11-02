package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.decorators.SpacingDecorator;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.LocationService;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.Distance;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.Sorters;
import com.mapbox.mapboxsdk.geometry.LatLng;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowCategory extends Fragment implements LocationService.LocationServiceRequestHandler {
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
    private LatLng loc=null;
    private LocationService locationService;
    private Button refreshdataset, showOnMap;
    private OnSeeDataOnMapButtonClickListener listener;

    public static final String STATE_ALPHABETICALLY = "alphabetically";
    public static final String STATE_DISTANCE = "distance";
    public static final String STATE_NO_DATA = "nodata";
    public static final String STATE_DATA = "data";


    private String currentSortState = STATE_ALPHABETICALLY;
    private String currentDataState = STATE_NO_DATA;

    private AttractionAdapter adapter;

    private ArrayList<Attraction> workingDataset;


    public ShowCategory() {
        // Required empty public constructor
        typeToDisplay = MainActivity.STATE_BARS;
    }

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

        sortViewRadio = (RadioGroup)v.findViewById(R.id.radio_group_sort);
        sortViewRadio.setOnCheckedChangeListener(new RadioButtonListener());
        distanceAway = (RadioButton)v.findViewById(R.id.by_distance_radio);


        contentHolder = (FrameLayout)v.findViewById(R.id.show_attractions);

        showOnMap = (Button)v.findViewById(R.id.map_view_button);
        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.seeDataOnMapButtonClicked();
            }
        });


        refreshdataset = (Button)v.findViewById(R.id.refresh_button);
        refreshdataset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationUpdate();
                refreshDataset();
            }
        });

        refreshDataset();
        getLocationUpdate();



        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSeeDataOnMapButtonClickListener){
            listener = (OnSeeDataOnMapButtonClickListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnseeDataOnMapButtonClickListener");
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        currentDataState = STATE_NO_DATA;
    }

    private void setupContentView(){


        if (workingDataset.size()==0){
            contentInHolder = inflater.inflate(R.layout.layout_no_content,null);
            contentHolder.addView(contentInHolder);
            currentDataState = STATE_NO_DATA;


        }else{
            hasData = true;
            contentInHolder = getActivity().getLayoutInflater().inflate(R.layout.recycler_view_general,null);
            recyclerView = (RecyclerView)contentInHolder.findViewById(R.id.recycler_general);
            contentHolder.addView(contentInHolder);

            adapter = new AttractionAdapter(workingDataset,((MainActivity)getActivity()),typeToDisplay);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SpacingDecorator(8,SpacingDecorator.SPACE_DECORATOR_ORIENTATION_VERTICAL));
            LinearLayoutManager man = new LinearLayoutManager(getContext());
            man.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(man);
            currentDataState = STATE_DATA;

        }

    }

    public void refreshDataset(){
        workingDataset = AttractionList.getInstance().subList(typeToDisplay);
        refreshInterface();

    }


    public void refreshInterface(){

        if (workingDataset.size()>1) {
            workingDataset = Sorters.sort(workingDataset, currentSortState);
        }
        if (currentDataState.equals(STATE_NO_DATA)) {
            setupContentView();
        }
        else{
            adapter.updateDataset(workingDataset);
            adapter.notifyDataSetChanged();

        }
    }

    public void updateInterfaceItems(){
        for (int i=0;i<workingDataset.size();i++){
            adapter.notifyItemChanged(i,workingDataset.get(i));

        }
    }


    public void updateAttractionDistances(){
        for(Attraction att: workingDataset){
            att.setDistanceAway(Distance.distanceBetweenPoints(loc,new LatLng(att.getLat(),att.getLon())));
            Log.d("AJQ", "yoyoyo");
        }

    }

    public void getLocationUpdate(){
        if (((MainActivity) getActivity()).checkPermissions()) {
            locationService = new LocationService(getContext(), this);
        }
    }

    @Override
    public void locationUpdate(LatLng latLng) {
        locationService.suspendUpdates();
        loc = latLng;
        updateAttractionDistances();

        if(currentDataState.equals(STATE_DATA)){
            if(currentSortState.equals(STATE_ALPHABETICALLY)) {
                updateInterfaceItems();
            }else{
                refreshInterface();
            }
        }

        distanceAway.setEnabled(true);


    }

    @Override
    public void connectionFailed(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }


    class RadioButtonListener implements RadioGroup.OnCheckedChangeListener{


        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==R.id.alphabetically_radio){
                currentSortState =STATE_ALPHABETICALLY;
            }else{
                currentSortState =STATE_DISTANCE;
            }
            refreshInterface();
        }
    }

    public interface OnSeeDataOnMapButtonClickListener{
        void seeDataOnMapButtonClicked();
    }

}
