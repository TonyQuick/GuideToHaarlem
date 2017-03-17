package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.tonyquick.thequicklawsonguidetohaarlem.interfaces.PermissionsHandler;
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
    private View recyclerHolder, noContentHolder;
    private RecyclerView recyclerView;
    private Boolean hasData = false;
    private LayoutInflater inflater;
    private TextView titleText;
    private RadioGroup sortViewRadio;
    private RadioButton alphabetically, distanceAway;
    private LatLng loc=null;
    private LocationService locationService;
    private Button refreshdataset, showOnMap;
    private ShowCategoryEventListener listener;

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

        Button makeSuggestion = (Button)v.findViewById(R.id.make_suggestion_button);
        makeSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.makeSuggestionsButtonClicked();
            }
        });

        refreshDataset();
        getLocationUpdate();



        return v;
    }

    //on attach check that host activity implements interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShowCategoryEventListener){
            listener = (ShowCategoryEventListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnseeDataOnMapButtonClickListener");
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        currentDataState = STATE_NO_DATA;
    }

    //initial view loader
    private void setupContentView(){

        //if no data exists, load placeholder layout
        if (workingDataset.size()==0){
            noContentHolder = inflater.inflate(R.layout.layout_no_content,null);
            contentHolder.addView(noContentHolder);
            currentDataState = STATE_NO_DATA;

        //if data exists load recycler view using dataset and add to view
        }else{
            hasData = true;

            if(noContentHolder!=null){
                if (noContentHolder.getParent()==contentHolder){
                    contentHolder.removeView(noContentHolder);
                }
            }

            recyclerHolder = getActivity().getLayoutInflater().inflate(R.layout.recycler_view_general,contentHolder,false);
            contentHolder.addView(recyclerHolder);
            recyclerView = (RecyclerView) recyclerHolder.findViewById(R.id.recycler_general);

            adapter = new AttractionAdapter(workingDataset,((MainActivity)getActivity()),typeToDisplay);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SpacingDecorator(8,SpacingDecorator.SPACE_DECORATOR_ORIENTATION_VERTICAL));
            LinearLayoutManager man = new LinearLayoutManager(getContext());
            man.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(man);
            currentDataState = STATE_DATA;

        }

    }

    //reload dataset from shared list

    public void refreshDataset(){
        workingDataset = AttractionList.getInstance().subList(typeToDisplay);
        refreshInterface();

    }

    /**
     * pair of methods to update card items in recycler view
     * refreshInterface reloads the recyclerview and notifies of the changed dataset
     * updateInterfaceItems updates each item in recycler view and refreshes individually
     */


    public void refreshInterface(){

        if (workingDataset.size()>1) {
            workingDataset = Sorters.sort(workingDataset, currentSortState);
        }
        //if this is being called for first time
        if (currentDataState.equals(STATE_NO_DATA)) {
            setupContentView();
        }
        //else update recyclerview
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

    //for each attraction in current dataset, update distance away
    public void updateAttractionDistances(){
        for(Attraction att: workingDataset){
            att.setDistanceAway(Distance.distanceBetweenPoints(loc,new LatLng(att.getLat(),att.getLon())));
        }

    }

    //call to get devices position
    public void getLocationUpdate(){
        if (((PermissionsHandler) getActivity()).checkPermissionsFromFrag()) {
            locationService = new LocationService(getContext(), this);
        }
    }

    //use position to update interface with distances to attractions, reorder if appropriate
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

    //set sort state based on radio buttons
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

    //interface for callbacks from this fragment, must be implemented by host activity
    public interface ShowCategoryEventListener {
        void seeDataOnMapButtonClicked();
        void makeSuggestionsButtonClicked();
    }

}
