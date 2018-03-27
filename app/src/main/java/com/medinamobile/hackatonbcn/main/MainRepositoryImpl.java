package com.medinamobile.hackatonbcn.main;

import android.provider.Telephony;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.medinamobile.hackatonbcn.entities.Stat;
import com.medinamobile.hackatonbcn.entities.Station;
import com.medinamobile.hackatonbcn.main.events.MainEvent;
import com.medinamobile.hackatonbcn.utils.APIUtils;
import com.medinamobile.hackatonbcn.utils.EventBus;
import com.medinamobile.hackatonbcn.utils.EventBusImpl;
import com.medinamobile.hackatonbcn.utils.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Erick on 3/10/2018.
 */

class MainRepositoryImpl implements MainRepository {


    @Override
    public void getUpdates() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUtils.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        RetrofitService mService = retrofit.create(RetrofitService.class);
        Call<ArrayList<Station>> mCall = mService.getStationsList();
        mCall.enqueue(new Callback<ArrayList<Station>>() {
            @Override
            public void onResponse(Call<ArrayList<Station>> call, Response<ArrayList<Station>> response) {
                if (response.isSuccessful()){
                    ArrayList<Station> stations = response.body();
                    if (stations.size()==0){
                        MainEvent event = new MainEvent();
                        event.setEventType(MainEvent.EVENT_STATIONS_EMPTY);
                        postEvent(event);
                    } else {
                        MainEvent event = new MainEvent();
                        event.setEventType(MainEvent.EVENT_STATIONS_SUCCESS);
                        event.setStations(stations);
                        postEvent(event);
                    }

                } else {
                    MainEvent event = new MainEvent();
                    event.setEventType(MainEvent.EVENT_STATIONS_ERROR);
                    postEvent(event);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Station>> call, Throwable t) {
                MainEvent event = new MainEvent();
                event.setEventType(MainEvent.EVENT_STATIONS_ERROR);
                event.setError(t.getLocalizedMessage());
                postEvent(event);
            }
        });
    }

    private void postEvent(MainEvent event) {
        EventBus mEventBus = EventBusImpl.getInstance();
        mEventBus.post(event);
    }

    @Override
    public void cancelUpdates() {

    }

    @Override
    public void getStats(int id) {
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        database.getReference("probabilities").child(String.valueOf(395)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ArrayList<Stat> mStats = new ArrayList<>();
                    for (DataSnapshot child:dataSnapshot.getChildren()){
                        if (child.getKey().equals("accidents")){
                            Stat stat = new Stat();
                            stat.setAccidents(Integer.parseInt(child.getValue().toString()));
                            mStats.add(stat);
                        } else {
                            String[] values = child.getValue().toString().split(";");
                            if (values.length > 0) {
                                Stat stat = new Stat();
                                stat.setHour(Integer.parseInt(child.getKey()));
                                stat.setEmpty(Double.parseDouble(values[0]));
                                stat.setFull(Double.parseDouble(values[1]));
                                stat.setNormal(Double.parseDouble(values[2]));
                                stat.setBikes(Integer.parseInt(values[3]));
                                stat.setStationId(Integer.parseInt(dataSnapshot.getKey()));
                                mStats.add(stat);
                            }
                        }
                    }
                    if (mStats.size()==0){
                        //Empty
                        MainEvent event = new MainEvent();
                        event.setEventType(MainEvent.EVENT_STATS_EMPTY);
                        postEvent(event);
                    } else {
                        MainEvent event = new MainEvent();
                        event.setEventType(MainEvent.EVENT_STATS_SUCCESS);
                        event.setStats(mStats);
                        postEvent(event);
                    }

                } else {
                    //Empty
                    MainEvent event = new MainEvent();
                    event.setEventType(MainEvent.EVENT_STATS_EMPTY);
                    postEvent(event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error
                MainEvent event = new MainEvent();
                event.setEventType(MainEvent.EVENT_STATS_ERROR);
                event.setError(databaseError.getMessage());
                postEvent(event);
            }
        });
    }
}
