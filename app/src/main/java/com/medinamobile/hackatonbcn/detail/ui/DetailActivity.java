package com.medinamobile.hackatonbcn.detail.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import com.medinamobile.hackatonbcn.R;
import com.medinamobile.hackatonbcn.entities.Station;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erick on 3/10/2018.
 */

public class DetailActivity extends AppCompatActivity implements DetailView{

    @BindView(R.id.detail_bikes)
    TextView mBikes;
    //@BindView(R.id.detail_nearest)
    //RecyclerView mNearest;
    @BindView(R.id.detail_slots)
    TextView mSlots;
    @BindView(R.id.detail_status)
    TextView mStatus;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Station mStation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.bind(this);
        getIntentData();
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mStation.getName());

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent!=null && intent.getExtras()!=null && intent.hasExtra("station")){
            mStation = intent.getParcelableExtra("station");
            populateTexts();
        }
    }

    private void populateTexts() {
//        mStatus.setText(mStation.getStatus());
//        mSlots.setText(mStation.getSlots());
//        mBikes.setText(mStation.getBikes());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
