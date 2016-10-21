package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.LocationService;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragmentMain extends Fragment implements OnMapReadyCallback, LocationService.LocationServiceHandler{

    private static final String ARG_DISPLAY_TYPE = "disp type";

    private String displayType;
    MapView mMap;
    MapboxMap mMapboxMap;
    LocationService locService;
    LatLng currentLoc;



    public MapFragmentMain() {
        // Required empty public constructor
    }


    public static MapFragmentMain newInstance(String param1) {
        MapFragmentMain fragment = new MapFragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_DISPLAY_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLocationService();
        if (getArguments() != null) {
            displayType = getArguments().getString(ARG_DISPLAY_TYPE);

        }
        MapboxAccountManager.start(getContext(),getString(R.string.access_token));





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_fragment_main, container, false);
        mMap = (MapView)v.findViewById(R.id.map_view);
        mMap.onCreate(savedInstanceState);

        mMap.getMapAsync(this);

        return v;
    }



    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mMapboxMap = mapboxMap;

        Log.d("AJQ","map first");
        if (currentLoc!=null) {
            CameraPosition camPos = new CameraPosition.Builder().target(currentLoc).zoom(12.8).build();
            mMapboxMap.setCameraPosition(camPos);

        }else{
            CameraPosition camPos = new CameraPosition.Builder().target(new LatLng(52.380987,4.637727)).zoom(12.8).build();
            mMapboxMap.setCameraPosition(camPos);

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
        locService.destroy();


    }


    @Override
    public void locationUpdate(LatLng latLng) {
        //TODO update map with user location
        Log.d("AJQ","loc update first");
        currentLoc = latLng;
        if (mMapboxMap!=null){
            //move icon


        }



    }

    @Override
    public void connectionFailed(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }


    public void startLocationService(){
        Boolean permissionGranted = ((MainActivity)getActivity()).checkPermissions();
        if (permissionGranted){
            locService = new LocationService(getContext(),this);
        }


    }


}
