package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Tony Quick on 26/10/2016.
 */

public class Distance {

    public static double distanceBetweenPoints(LatLng loc, LatLng loc2){

        double lat1 = loc.getLatitude();
        double lat2 = loc2.getLatitude();
        double lon1 = loc.getLongitude();
        double lon2 = loc.getLongitude();

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to meters
        distance = round(distance,2);

        return distance;
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }


}
