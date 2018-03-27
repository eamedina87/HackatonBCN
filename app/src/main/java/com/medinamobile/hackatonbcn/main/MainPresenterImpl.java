package com.medinamobile.hackatonbcn.main;

import com.medinamobile.hackatonbcn.entities.Stat;
import com.medinamobile.hackatonbcn.entities.Station;
import com.medinamobile.hackatonbcn.main.events.MainEvent;
import com.medinamobile.hackatonbcn.main.ui.MainView;
import com.medinamobile.hackatonbcn.utils.EventBus;
import com.medinamobile.hackatonbcn.utils.EventBusImpl;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by Erick on 3/10/2018.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainInteractor mInteractor;
    private EventBus mEventBus = EventBusImpl.getInstance();
    private MainView mView;


    public MainPresenterImpl(MainView view) {
        mInteractor = new MainInteractorImpl();
        mView = view;
    }

    @Override
    public void onCreate() {
        mEventBus.register(this);
    }

    @Override
    public void onPause() {
        mInteractor.cancelUpdates();
    }

    @Override
    public void onResume() {
        mInteractor.getUpdates();
        mView.showProgress();
    }

    @Override
    public void onDestroy() {
        mEventBus.unregister(this);
        mView = null;
    }

    @Subscribe
    @Override
    public void onEventMainThread(MainEvent event) {
        if (mView!=null){
            mView.hideProgress();
        }

        switch (event.getEventType()){

            case MainEvent.EVENT_STATIONS_SUCCESS:{
                onStationsSuccess(event.getStations());
                break;
            }
            case MainEvent.EVENT_STATIONS_EMPTY:{
                onStationsEmpty();
                break;
            }
            case MainEvent.EVENT_STATIONS_ERROR:{
                onStationsError(event.getError());
                break;
            }
            case MainEvent.EVENT_STATS_SUCCESS:{
                onStatsSuccess(event.getStats());
                break;
            }
            case MainEvent.EVENT_STATS_EMPTY:{
                onStatsEmpty();
                break;
            }
            case MainEvent.EVENT_STATS_ERROR:{
                onStatsError(event.getError());
                break;
            }
        }

    }

    private void onStatsSuccess(ArrayList<Stat> stats) {
        if (mView!=null){
            mView.onStatisticsSuccess(stats);
        }
    }

    private void onStatsEmpty() {
        if (mView!=null){
            mView.onStatisticsEmpty();
        }
    }

    private void onStatsError(String error) {
        if (mView!=null){
            mView.onStatisticsError(error);
        }
    }

    @Override
    public void getStats(int id) {
        mInteractor.getStats(id);
    }

    private void onStationsSuccess(ArrayList<Station> stations) {
        if (mView!=null){
            mView.onStationsLoaded(stations);
        }
    }

    private void onStationsEmpty() {
        if (mView!=null){
            mView.onStationsEmpty();
        }
    }

    private void onStationsError(String error) {
        if (mView!=null){
            mView.onStationsError(error);
        }
    }


}
