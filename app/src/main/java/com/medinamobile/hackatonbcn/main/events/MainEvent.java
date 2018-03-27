package com.medinamobile.hackatonbcn.main.events;

import com.medinamobile.hackatonbcn.entities.Stat;
import com.medinamobile.hackatonbcn.entities.Station;
import com.medinamobile.hackatonbcn.utils.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Erick on 3/10/2018.
 */

public class MainEvent extends Event {
    public static final int EVENT_STATIONS_SUCCESS = 0;
    public static final int EVENT_STATIONS_EMPTY = 1;
    public static final int EVENT_STATIONS_ERROR = 2;
    public static final int EVENT_STATS_SUCCESS = 3;
    public static final int EVENT_STATS_EMPTY = 4;
    public static final int EVENT_STATS_ERROR = 5;
    private ArrayList<Station> stations;
    private ArrayList<Stat> stats;

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public void setStats(ArrayList<Stat> stats) {
        this.stats = stats;
    }
}
