package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.LocationService;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.InfoWindow;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragmentMain extends Fragment implements OnMapReadyCallback, LocationService.LocationServiceRequestHandler {

    private static final String ARG_DISPLAY_TYPE = "disp type";

    private String displayType;
    MapView mMap;
    MapboxMap mMapboxMap;
    LocationService locService;
    LatLng currentLoc;
    MarkerOptions userMarkerOptions;
    Marker userMarker;
    ArrayList<Attraction> workingDataset;
    WeakHashMap<Marker,Attraction> hashMap;
    private LatLng currentMarkerSelected;

    AttractionAdapter.AttractionClickListener listener;


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
        workingDataset = AttractionList.getInstance().subList(displayType);
        View v = inflater.inflate(R.layout.fragment_map_fragment_main, container, false);


        mMap = (MapView)v.findViewById(R.id.map_view);
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);

        return v;
    }



    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mMapboxMap = mapboxMap;


        if (currentLoc!=null) {
            CameraPosition camPos = new CameraPosition.Builder().target(currentLoc).zoom(12.8).build();
            mMapboxMap.setCameraPosition(camPos);

        }else{
            CameraPosition camPos = new CameraPosition.Builder().target(new LatLng(52.380987,4.637727)).zoom(12.8).build();
            mMapboxMap.setCameraPosition(camPos);

        }
        listener = (MainActivity)getActivity();
        this.mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                if (currentMarkerSelected==null) {
                    currentMarkerSelected=marker.getPosition();
                    Toast.makeText(getContext(), "Press marker again to view full details", Toast.LENGTH_LONG).show();
                    marker.showInfoWindow(mMapboxMap,mMap);
                    marker.getInfoWindow();

                }else{
                    if (currentMarkerSelected.getLatitude() == marker.getPosition().getLatitude() && currentMarkerSelected.getLongitude() == marker.getPosition().getLongitude()) {
                        listener.onAttractionClicked(hashMap.get(marker));
                        currentMarkerSelected = null;
                    } else {
                        marker.showInfoWindow(mMapboxMap,mMap);
                        marker.getInfoWindow();
                        if (currentMarkerSelected != null) {
                            Log.d("Ajq", "memory lat " + currentMarkerSelected.getLatitude() + "lon " + currentMarkerSelected.getLongitude());
                        }
                        Log.d("Ajq", "clicker lat " + marker.getPosition().getLatitude() + "lon " + marker.getPosition().getLongitude());
                        currentMarkerSelected = marker.getPosition();

                        Toast.makeText(getContext(), "Press marker again to view full details", Toast.LENGTH_LONG).show();
                    }


                }
            return true;
            }
        });
        addMarkersForAttractions();




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
        locService.suspendUpdates();


    }


    @Override
    public void locationUpdate(LatLng latLng) {
        //TODO update map with user location

        currentLoc = latLng;
        if (mMapboxMap!=null){
            if (userMarkerOptions ==null){
                Icon userIcon = IconFactory.getInstance(getContext())
                        .fromDrawable(ContextCompat.getDrawable(getContext(),R.drawable.location_icon));


                userMarkerOptions = new MarkerOptions().position(latLng).icon(userIcon);
                userMarker = mMapboxMap.addMarker(userMarkerOptions);
                mMapboxMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),100);

            }else{
                ValueAnimator markerAnimator = ObjectAnimator.ofObject(userMarker,"position",new LatLngEvaluator(), userMarker.getPosition(),latLng);
                markerAnimator.setDuration(1000);
                markerAnimator.start();
            }


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


    private void addMarkersForAttractions(){
        hashMap = new WeakHashMap<>();

        for (Attraction att: workingDataset){
            MarkerOptions op = new MarkerOptions().position(new LatLng(att.getLat(),att.getLon())).title(att.getTitle()).snippet(att.getTitle());
            Marker temp = mMapboxMap.addMarker(op);
            hashMap.put(temp,att);


        }


    }





    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
        // Method is used to interpolate the marker animation.

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }

    }



}
