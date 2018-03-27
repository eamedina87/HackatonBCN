package com.medinamobile.hackatonbcn.main.ui;

import com.medinamobile.hackatonbcn.entities.Stat;
import com.medinamobile.hackatonbcn.entities.Station;

import java.util.ArrayList;

/**
 * Created by Erick on 3/10/2018.
 */

public interface MainView {

    void showProgress();
    void hideProgress();

    void onStationsLoaded(ArrayList<Station> stations);
    void onStationsEmpty();
    void onStationsError(String error);

    void onStatisticsSuccess(ArrayList<Stat> stats);
    void onStatisticsEmpty();
    void onStatisticsError(String error);
}
