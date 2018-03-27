package com.medinamobile.hackatonbcn.entities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.medinamobile.hackatonbcn.R;

/**
 * Created by Erick on 3/10/2018.
 */

public class Station implements Parcelable{

    public static final String STATUS_OPEN = "OPN";
    public static final String STATUS_CLOSED = "CLS";
    public static final String STATION_TYPE_BIKE = "BIKE";


    private int id;
    private int district;
    private double lon;
    private double lat;
    private int bikes;
    private int slots;
    private int zip;
    private String address;
    private String addressNumber;
    private String nearbyStations;
    private String status;
    private String name;
    private String stationType;

    public Station(){

    }


    protected Station(Parcel in) {
        id = in.readInt();
        district = in.readInt();
        lon = in.readDouble();
        lat = in.readDouble();
        bikes = in.readInt();
        slots = in.readInt();
        zip = in.readInt();
        address = in.readString();
        addressNumber = in.readString();
        nearbyStations = in.readString();
        status = in.readString();
        name = in.readString();
        stationType = in.readString();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getNearbyStations() {
        return nearbyStations;
    }

    public void setNearbyStations(String nearbyStations) {
        this.nearbyStations = nearbyStations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getBikes() {
        return bikes;
    }

    public void setBikes(int bikes) {
        this.bikes = bikes;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(district);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeInt(bikes);
        dest.writeInt(slots);
        dest.writeInt(zip);
        dest.writeString(address);
        dest.writeString(addressNumber);
        dest.writeString(nearbyStations);
        dest.writeString(status);
        dest.writeString(name);
        dest.writeString(stationType);
    }

    public String getOcuppancy() {
        return getBikes() + "/" + getSlots();
    }

    public String getStatusComplete(Context mContext) {
        if (getStatus().equals(STATUS_OPEN))
            return mContext.getResources().getString(R.string.status_open);
        if (getStatus().equals(STATUS_CLOSED))
            return mContext.getResources().getString(R.string.status_closed);
        return null;
    }

    public String getBikesString(Context mContext) {
        return mContext.getResources().getString(R.string.detail_bikes, getBikes());
    }

    public String getSlotsString(Context mContext) {
        return mContext.getResources().getString(R.string.detail_slots, getSlots());
    }
}
