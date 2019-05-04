package com.example.administrator.customerapp.Model.SupportedModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilterAddress {
    private String city;
    private ArrayList<String> district;
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getDistrict() {
        return district;
    }

    public void setDistrict(ArrayList<String> district) {
        this.district = district;
    }

}
