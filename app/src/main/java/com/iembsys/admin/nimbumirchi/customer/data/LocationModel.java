package com.iembsys.admin.nimbumirchi.customer.data;

/**
 * Created by Admin on 29-12-2016.
 */

public class LocationModel {

    private String stateId;
    private String stateName;
    private String cityId;
    private String cityName;
    private String locationName;

    public LocationModel(String stateId,
                         String stateName,
                         String cityId,
                         String cityName,
                         String locationName) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.locationName = locationName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
