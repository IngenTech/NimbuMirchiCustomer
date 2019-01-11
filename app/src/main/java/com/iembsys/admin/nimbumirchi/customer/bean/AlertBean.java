package com.iembsys.admin.nimbumirchi.customer.bean;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Admin on 26-04-2017.
 */
public class AlertBean {


    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        DepartureTime = departureTime;
    }

    public String getHaltDuration() {
        return HaltDuration;
    }

    public void setHaltDuration(String haltDuration) {
        HaltDuration = haltDuration;
    }

    public LatLng getStartLatLong() {
        return latLng;
    }

    public void setStartLatLong(LatLng latitude) {
        latLng = latitude;
    }




    private String VehicleName;
    private String ArrivalTime;
    private String DepartureTime;
    private String HaltDuration;
    private LatLng latLng;
    private String place;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public AlertBean(String msg, String vehicleName, String arrivalTime, String departureTime, String haltDuration, LatLng startLatLng) {
        this.message = msg;
        this.VehicleName = vehicleName;
        this.ArrivalTime  = arrivalTime;
        this.DepartureTime = departureTime;
        this.HaltDuration = haltDuration;
        this.latLng = startLatLng;

    }
}
