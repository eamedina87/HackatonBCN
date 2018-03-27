package com.medinamobile.hackatonbcn.main;

import com.medinamobile.hackatonbcn.main.events.MainEvent;

/**
 * Created by Erick on 3/10/2018.
 */

public interface MainPresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void onEventMainThread(MainEvent event);

    void getStats(int id);
}
