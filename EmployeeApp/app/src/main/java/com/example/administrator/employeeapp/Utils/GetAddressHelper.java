package com.example.administrator.employeeapp.Utils;

import java.util.ArrayList;

public class GetAddressHelper {
    private ArrayList<String> cityName;
    private ArrayList<String> cityID;
    private ArrayList<String> districtName;
    private ArrayList<String> districtID;
    private ArrayList<String> wardName;

    public ArrayList<String> getWardName() {
        return wardName;
    }

    public void setWardName(ArrayList<String> wardName) {
        this.wardName = wardName;
    }

    public ArrayList<String> getWardID() {
        return wardID;
    }

    public void setWardID(ArrayList<String> wardID) {
        this.wardID = wardID;
    }

    private ArrayList<String> wardID;

    public ArrayList<String> getDistrictName() {
        return districtName;
    }

    public void setDistrictName(ArrayList<String> districtName) {
        this.districtName = districtName;
    }

    public ArrayList<String> getDistrictID() {
        return districtID;
    }

    public void setDistrictID(ArrayList<String> districtID) {
        this.districtID = districtID;
    }

    public GetAddressHelper(ArrayList<String>  cityName, ArrayList<String>  cityID) {
        this.cityName = cityName;
        this.cityID = cityID;
    }
    public GetAddressHelper(String provinceID, ArrayList<String>  districtName, ArrayList<String>  districtID) {
        this.districtName = districtName;
        this.districtID = districtID;
    }
    public GetAddressHelper(){};
    public ArrayList<String>  getCityName() {
        return cityName;
    }

    public void setCityName(ArrayList<String>  cityName) {
        this.cityName = cityName;
    }

    public ArrayList<String>  getCityID() {
        return cityID;
    }

    public void setCityID(ArrayList<String>  cityID) {
        this.cityID = cityID;
    }
}
