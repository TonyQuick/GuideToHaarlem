package com.example.tonyquick.thequicklawsonguidetohaarlem.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.tonyquick.thequicklawsonguidetohaarlem.Manifest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;
import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * Created by Tony Quick on 21/10/2016.
 */

public class LocationService implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient mClient;
    private LocationServiceHandler listener;
    private Context context;


    public interface LocationServiceHandler{
        void locationUpdate(LatLng latLng);
        void connectionFailed(String error);

    }

    public LocationService(Context context, LocationServiceHandler listener){
        this.listener = listener;
        this.context = context;
        mClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mClient.connect();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, this);
            Log.d("AJQ","Location updates requested");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        listener.locationUpdate(new LatLng(location.getLatitude(),location.getLongitude()));
    }

    public void destroy(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mClient,this);
        mClient.disconnect();
    }


}
