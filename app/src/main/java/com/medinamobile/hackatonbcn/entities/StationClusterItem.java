package com.medinamobile.hackatonbcn.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Erick on 3/10/2018.
 */

public class StationClusterItem implements ClusterItem {


    private final Station station;

    public StationClusterItem(Station mStation) {
        this.station = mStation;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(station.getLat(), station.getLon());
    }


    public Station getStation(){
        return this.station;
    }
}
