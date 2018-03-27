package com.medinamobile.hackatonbcn.entities;

/**
 * Created by Erick on 3/11/2018.
 */

public class Stat {
    private int stationId;
    private int hour;
    private double empty;
    private double full;
    private double normal;
    private int bikes;
    private int accidents;

    public Stat(){
        setHour(-1);
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getEmpty() {
        return empty;
    }

    public void setEmpty(double empty) {
        this.empty = empty;
    }

    public double getFull() {
        return full;
    }

    public void setFull(double full) {
        this.full = full;
    }

    public double getNormal() {
        return normal;
    }

    public void setNormal(double normal) {
        this.normal = normal;
    }

    public int getBikes() {
        return bikes;
    }

    public void setBikes(int bikes) {
        this.bikes = bikes;
    }

    public void setAccidents(int accidents) {
        this.accidents = accidents;
    }

    public int getAccidents() {
        return accidents;
    }
}
