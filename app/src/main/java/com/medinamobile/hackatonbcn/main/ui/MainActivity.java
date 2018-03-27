package com.medinamobile.hackatonbcn.main.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.medinamobile.hackatonbcn.R;
import com.medinamobile.hackatonbcn.detail.ui.DetailActivity;
import com.medinamobile.hackatonbcn.entities.Stat;
import com.medinamobile.hackatonbcn.entities.Station;
import com.medinamobile.hackatonbcn.entities.StationClusterItem;
import com.medinamobile.hackatonbcn.entities.StationRenderer;
import com.medinamobile.hackatonbcn.main.MainPresenter;
import com.medinamobile.hackatonbcn.main.MainPresenterImpl;
import com.medinamobile.hackatonbcn.main.list.ListFragment;
import com.medinamobile.hackatonbcn.main.list.StationsAdapter;
import com.medinamobile.hackatonbcn.utils.MyUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, MainView, ClusterManager.OnClusterItemClickListener<StationClusterItem>, TabLayout.OnTabSelectedListener, StationsAdapter.StationsCallbacks, View.OnClickListener {

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.station_detail)
    ConstraintLayout mBottomSheet;
    @BindView(R.id.detail_station_name)
    TextView mStationName;
    @BindView(R.id.detail_distance)
    TextView mStationDistance;
    @BindView(R.id.detail_status)
    TextView mStationStatus;
    @BindView(R.id.detail_bikes)
    TextView mStationBikes;
    @BindView(R.id.detail_slots)
    TextView mStationSlots;
    @BindView(R.id.detail_description)
    TextView mDescription;
    @BindView(R.id.detail_graph)
    GraphView mGraph;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.list)
    RecyclerView mList;
    @BindView(R.id.btnBikes)
    ImageView mBtnBikes;
    @BindView(R.id.btnEmpty)
    ImageView mBtnEmpty;
    @BindView(R.id.btnFull)
    ImageView mBtnFull;
    @BindView(R.id.btnNormal)
    ImageView mBtnNormal;



    private static final int REQUEST_CODE_FINE_LOCATION = 0;
    private GoogleMap mMap;

    private MainPresenter mPresenter;
    private boolean isMapReady = false;
    private boolean areStationsLoaded = false;
    private ClusterManager<StationClusterItem> mClusterManager;
    private Context mContext;
    private ArrayList<Station> mStations;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallbacks;
    private Location mLocation;
    private boolean isDetailDisplayed = false;
    private BottomSheetBehavior<ConstraintLayout> mBottomBehaviour;
    private Station mSelectedStation;
    private boolean isMapSelected = true;
    private ArrayList<Stat> mStats;

    //todo restore saved instance state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = MainActivity.this;



        SupportMapFragment mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);




        mTabLayout.addOnTabSelectedListener(this);



        mPresenter = new MainPresenterImpl(this);
        mPresenter.onCreate();

        mBottomBehaviour = BottomSheetBehavior.from(mBottomSheet);
        mBottomBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationCallbacks = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult==null)
                    return;

                mLocation = locationResult.getLocations().get(0);

            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        if (mRequestingLocationUpdates){
            startLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (mStations!=null)
            outState.putParcelableArrayList("stations", mStations);
        outState.putBoolean("stationsFlag", areStationsLoaded);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("stations"))
            mStations = savedInstanceState.getParcelableArrayList("stations");
        if (savedInstanceState.containsKey("stationsFlag"))
            areStationsLoaded = savedInstanceState.getBoolean("stationsFlag");
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (isLocationPermissionGranted()){
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallbacks,
                    null /* Looper */);
        }

    }

    private boolean isLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_FINE_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallbacks);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isDetailDisplayed){
                    //Hide Detail
                    mBottomBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });


        LatLng center = new LatLng(41.38, 2.17);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12.0f));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (isLocationPermissionGranted()) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);



        if (areStationsLoaded){
            //todo call the method that displays Stations in map

        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationUpdates();
                    mMap.setMyLocationEnabled(true);

                } else {


                }
                return;
            }
        }
    }


    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStationsLoaded(ArrayList<Station> stations) {
        //TODO
        areStationsLoaded = true;
        this.mStations = stations;
        if (isMapReady){
            mClusterManager = new ClusterManager<>(this, mMap);
            mClusterManager.setOnClusterItemClickListener(this);
            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);
            StationRenderer mRenderer = new StationRenderer(mContext,mMap, mClusterManager);
            mClusterManager.setRenderer(mRenderer);
            for(Station mStation : stations){
                StationClusterItem mClusterItem = new StationClusterItem(mStation);
                mClusterManager.addItem(mClusterItem);
            }
        }
        setupList();

    }

    @Override
    public void onStationsEmpty() {

    }

    @Override
    public void onStationsError(String error) {

    }

    @Override
    public void onStatisticsSuccess(ArrayList<Stat> stats) {
        //if (isMapSelected){
        mStats = stats;
        drawBikes();

        /*} else {
            mStations.get(stats.get(0).getStationId()-1);

        }*/
    }

    private void drawBikes() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>();
        for (Stat mStat:mStats){
            if (mStat.getHour()!=-1) {
                series.appendData(new DataPoint(mStat.getHour(), mStat.getBikes()), false, 24);
            } else {
                mDescription.setText(mContext.getString(R.string.detail_accidents, mStat.getAccidents()));
            }

        }
        mGraph.addSeries(series);
        mGraph.setTitle("Available bikes per hour (historic)");
    }

    @Override
    public void onStatisticsEmpty() {

    }

    @Override
    public void onStatisticsError(String error) {

    }

    @Override
    public boolean onClusterItemClick(StationClusterItem stationClusterItem) {
        /*Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("station", stationClusterItem.getStation());
        if (mLocation!=null) {
            Location stationLocation = new Location("");
            stationLocation.setLatitude(stationClusterItem.getStation().getLat());
            stationLocation.setLongitude(stationClusterItem.getStation().getLon());
            intent.putExtra("distance", mLocation.distanceTo(stationLocation)) ;
        }
        startActivity(intent);*/
        mSelectedStation = stationClusterItem.getStation();
        showDetails(mSelectedStation);
        mPresenter.getStats(mSelectedStation.getId());
        return true;
    }

    private void showDetails(Station station) {
        isDetailDisplayed = true;
        mBottomBehaviour = BottomSheetBehavior.from(mBottomSheet);
        mBottomBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        mStationName.setText(station.getName());
        mStationStatus.setText(station.getStatusComplete(mContext));
        mStationBikes.setText(station.getBikesString(mContext));
        mStationSlots.setText(station.getSlotsString(mContext));
        mStationDistance.setText(MyUtils.getDistanceString(mContext, mLocation, station));
        mBtnBikes.setOnClickListener(this);
        mBtnEmpty.setOnClickListener(this);
        mBtnFull.setOnClickListener(this);
        mBtnNormal.setOnClickListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){
            case 0 : {//Map
                if (!isMapSelected){
                    mList.setVisibility(View.GONE);
                    isMapSelected = true;
                }
                break;
            }
            case 1 : {//List
                if (isMapSelected) {
                    mList.setVisibility(View.VISIBLE);
                    isMapSelected = false;
                }
                break;
            }
        }
    }

    private void setupList() {
        mList.setAdapter(new StationsAdapter(mStations, this, mLocation));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onStationClicked(Station station) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnBikes){}

    }
}
