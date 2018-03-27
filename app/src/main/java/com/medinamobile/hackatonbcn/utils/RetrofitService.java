package com.medinamobile.hackatonbcn.utils;

import com.medinamobile.hackatonbcn.entities.Station;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Erick on 2/17/2018.
 */

public interface RetrofitService {

    @GET(APIUtils.URL_PATH_STATIONS)
    Call<ArrayList<Station>> getStationsList();

}
