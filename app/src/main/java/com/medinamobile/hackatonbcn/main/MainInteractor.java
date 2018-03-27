package com.medinamobile.hackatonbcn.main;

/**
 * Created by Erick on 3/10/2018.
 */

public interface MainInteractor {
    void getUpdates();
    void cancelUpdates();

    void getStats(int id);
}
