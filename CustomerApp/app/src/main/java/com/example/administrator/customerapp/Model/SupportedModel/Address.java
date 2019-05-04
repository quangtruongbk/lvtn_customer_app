package com.example.administrator.customerapp.Model.SupportedModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Address {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("ward")
    @Expose
    private String ward;
    @SerializedName("rest")
    @Expose
    private String rest;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public ArrayList<String> getDistrictFromCityFilter(ArrayList<Address> address, String city){
        for(int i = 0; i < address.size(); i++){
            if(address.get(i).getCity().equals(city)) {
                String[] temp = address.get(i).getDistrict().split(",");
                temp[0] = "Chọn quận/huyện";
                ArrayList<String> district = new ArrayList<>();
                for(int j = 0; j < temp.length; j++){
                    district.add(temp[j]);
                }
                return district;
            }
        }
        return null;
    }

}
