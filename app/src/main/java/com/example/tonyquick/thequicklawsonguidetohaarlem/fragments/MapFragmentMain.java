package com.example.tonyquick.thequicklawsonguidetohaarlem.fragments;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tonyquick.thequicklawsonguidetohaarlem.R;
import com.example.tonyquick.thequicklawsonguidetohaarlem.activties.MainActivity;
import com.example.tonyquick.thequicklawsonguidetohaarlem.adapters.AttractionAdapter;
import com.example.tonyquick.thequicklawsonguidetohaarlem.interfaces.PermissionsHandler;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.AttractionList;
import com.example.tonyquick.thequicklawsonguidetohaarlem.services.LocationService;
import com.example.tonyquick.thequicklawsonguidetohaarlem.utilities.Distance;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragmentMain extends Fragment implements OnMapReadyCallback, LocationService.LocationServiceRequestHandler {

    private static final String ARG_CONTENT_TO_DISPLAY = "content show";
    private static final String ARG_DISPLAY_TYPE = "disp type";

    private String contentToDisplay;
    MapView mMap;
    MapboxMap mMapboxMap;
    LocationService locService;
    LatLng currentLoc;
    MarkerOptions userMarkerOptions;
    Marker userMarker;
    Marker selectedLocation;
    ArrayList<Attraction> workingDataset;
    WeakHashMap<Marker,Attraction> hashMap;
    private LatLng currentMarkerSelected;
    Button doneButton;
    ImageButton currentLocButton;
    TextView instructionsText;
    private boolean directionsRequired = false;
    Marker destination;
    AttractionAdapter.AttractionClickListener listener;
    private DirectionsRoute route;

    public static final String MAP_STATE_SHOW_ATT = "show att";
    public static final String MAP_STATE_SELECT_LOC = "select loc";
    public static final String MAP_STATE_DIRECTIONS = "show directions";

    private String state;


    public MapFragmentMain() {
        // Required empty public constructor
    }


    public static MapFragmentMain newInstance(String param1, String state) {
        MapFragmentMain fragment = new MapFragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT_TO_DISPLAY, param1);
        args.putString(ARG_DISPLAY_TYPE, state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLocationService();
        if (getArguments() != null) {
            contentToDisplay = getArguments().getString(ARG_CONTENT_TO_DISPLAY);
            state = getArguments().getString(ARG_DISPLAY_TYPE);

        }
        MapboxAccountManager.start(getContext(),getString(R.string.access_token));


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Context altContext = new ContextThemeWrapper(getContext(),R.style.AppAltTheme);
        LayoutInflater altInflater = inflater.cloneInContext(altContext);

        View v = altInflater.inflate(R.layout.fragment_map_fragment_main, container, false);

        mMap = (MapView)v.findViewById(R.id.map_view);
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);

        instructionsText = (TextView)v.findViewById(R.id.map_view_instructions);
        instructionsText.setVisibility(View.INVISIBLE);
        doneButton = (Button)v.findViewById(R.id.coord_selector_done_button);
        doneButton.setAlpha(0);
        doneButton.setTranslationY(180);
        currentLocButton =(ImageButton)v.findViewById(R.id.set_to_current_location_button);
        currentLocButton.setVisibility(View.INVISIBLE);


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
        if (state.equals(MAP_STATE_SHOW_ATT)) {
            addMarkersForAttractions();
        }else if(state.equals(MAP_STATE_SELECT_LOC)){
            locationSelectorSetup();
        }else if(state.equals(MAP_STATE_DIRECTIONS)){
            directionsRequired = true;
            setupDirections();
            Log.d("AjQ","Directions required set to true");

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
        locService.suspendUpdates();


    }


    @Override
    public void locationUpdate(LatLng latLng) {


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

            if (directionsRequired){
                try {
                    Log.d("AjQ","Directions request being called");
                    directionsRequest();
                }catch (ServicesException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Unable to obtain directions",Toast.LENGTH_SHORT).show();
                }
                directionsRequired=false;
            }

        }
    }

    @Override
    public void connectionFailed(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }


    public void startLocationService(){

        Boolean permissionGranted = ((PermissionsHandler) getActivity()).checkPermissionsFromFrag();
        if (permissionGranted) {
            locService = new LocationService(getContext(), this);
        }
    }


    private void addMarkersForAttractions(){
        hashMap = new WeakHashMap<>();
        workingDataset = AttractionList.getInstance().subList(contentToDisplay);

        for (Attraction att: workingDataset){
            MarkerOptions op = new MarkerOptions().position(new LatLng(att.getLat(),att.getLon())).title(att.getTitle()).snippet(att.getTitle());
            Marker temp = mMapboxMap.addMarker(op);
            hashMap.put(temp,att);


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

    }

    private void locationSelectorSetup(){
        mMapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                updateSelectedLocationMarker(point);
            }
        });
        currentLocButton.setVisibility(View.VISIBLE);
        instructionsText.setVisibility(View.VISIBLE);

        currentLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMarker!=null){
                    updateSelectedLocationMarker(userMarker.getPosition());
                }
            }
        });

        final CoordinateGetterCallback callbackListener = (CoordinateGetterCallback)getActivity();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackListener.coordinatesSelectedCallback(selectedLocation.getPosition());
            }
        });

    }

    private void updateSelectedLocationMarker(LatLng pos) {
        if(selectedLocation==null) {

            MarkerOptions op = new MarkerOptions().setPosition(pos);
            selectedLocation = mMapboxMap.addMarker(op);
            doneButton.animate().translationY(0).alpha(1).setDuration(1000).start();


        }else{
            selectedLocation.setPosition(pos);

        }
        mMapboxMap.selectMarker(selectedLocation);
    }

    private void directionsRequest() throws ServicesException{
        final Position originPos = Position.fromCoordinates(currentLoc.getLongitude(),currentLoc.getLatitude());
        final Position destinationPos = Position.fromCoordinates(destination.getPosition().getLongitude(),destination.getPosition().getLatitude());

        MapboxDirections directionsClient = new MapboxDirections.Builder()
                .setOrigin(originPos)
                .setDestination(destinationPos)
                .setAccessToken(getString(R.string.access_token))
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .build();
        directionsClient.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body()==null){
                    Log.e("AJQ","No directions contained in response, check token");
                    return;

                }else if (response.body().getRoutes().size()<1){
                    Log.d("AJQ","No routes between 2 locations");
                    return;
                }
                Log.d("AjQ","Size of routes; "+String.valueOf(response.body().getRoutes().size()));
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(currentLoc)
                        .include(new LatLng(destination.getPosition().getLatitude(),destination.getPosition().getLongitude()))
                        .build();
                route = response.body().getRoutes().get(0);
                drawRoute(route, bounds);




            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e("Ajq","Directions request failure: " + t.getMessage());
                Toast.makeText(getContext(),"Error: "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

    private void drawRoute(DirectionsRoute dir,LatLngBounds bounds){
        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
        Log.d("AJQ","Route length: "+dir.getDistance() + " meters long");
        List<Position> coords = lineString.getCoordinates();
        LatLng[] points = new LatLng[coords.size()];
        for (int i = 0; i<coords.size();i++){
            points[i] = new LatLng(coords.get(i).getLatitude(),coords.get(i).getLongitude());
        }
        Log.d("AJQ","Size of latlng array "+points.length);

        PolylineOptions polyOpts = new PolylineOptions()
                .add(points)
                .color(ContextCompat.getColor(getContext(),R.color.map_route))
                .width(5);
        mMapboxMap.addPolyline(polyOpts);
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,200));


    }


    private void setupDirections(){
        Attraction current = AttractionList.getInstance().getCurrentAttractionInScope();

        String title = getString(R.string.map_directions_to)+current.getTitle();
        instructionsText.setText(title);
        instructionsText.setVisibility(View.VISIBLE);
        MarkerOptions destinationOptions = new MarkerOptions().setPosition(new LatLng(current.getLat(),current.getLon()));
        this.destination = mMapboxMap.addMarker(destinationOptions);

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


    public interface CoordinateGetterCallback{
        void coordinatesSelectedCallback(LatLng location);

    }



}
