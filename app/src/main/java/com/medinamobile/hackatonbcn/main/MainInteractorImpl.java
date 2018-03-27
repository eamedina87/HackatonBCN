package com.medinamobile.hackatonbcn.main;

/**
 * Created by Erick on 3/10/2018.
 */

class MainInteractorImpl implements MainInteractor {

    private MainRepository mRepository;


    public MainInteractorImpl() {
        mRepository = new MainRepositoryImpl();
    }

    @Override
    public void getUpdates() {
        mRepository.getUpdates();
    }

    @Override
    public void cancelUpdates() {
        mRepository.cancelUpdates();
    }

    @Override
    public void getStats(int id) {
        mRepository.getStats(id);
    }
}
