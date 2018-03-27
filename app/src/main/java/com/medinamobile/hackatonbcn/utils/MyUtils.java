package com.medinamobile.hackatonbcn.utils;

import android.content.Context;
import android.location.Location;

import com.medinamobile.hackatonbcn.R;
import com.medinamobile.hackatonbcn.entities.Station;

/**
 * Created by Erick on 3/10/2018.
 */

public class MyUtils {
    public static String getDistanceString (Context context, Location location, Station station){
        if (location!=null) {
            Location stationLocation = new Location("");
            stationLocation.setLatitude(station.getLat());
            stationLocation.setLongitude(station.getLon());
            float distance = location.distanceTo(stationLocation);
            if (distance/1000>1){
                //distance in Km
                float value = distance / 1000;
                String distanceString = String.format("%.1f Km.", value);
                return context.getResources().getString(R.string.detail_distance, distanceString);
            } else {
                //distance in m
                String distanceString = String.format("%.0f m.", distance);
                return context.getResources().getString(R.string.detail_distance, distanceString);
            }
        }
        return "";
    }

}
