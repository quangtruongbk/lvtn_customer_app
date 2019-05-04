package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.HomeContract;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.SupportedModel.Address;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter implements HomeContract.Presenter {
    private RetrofitInterface callAPIService;
    private HomeContract.View mView;

    public HomePresenter(@NonNull HomeContract.View mView) {
        this.mView = mView;
    }

    /***************************************************
     Function: getBranchFromServer
     Creator: Quang Truong
     Description: get branch list
     *************************************************/
    @Override
    public void getBranchFromServer() {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getBranch().enqueue(new Callback<ArrayList<Branch>>() {
            @Override
            public void onResponse(Call<ArrayList<Branch>> call, Response<ArrayList<Branch>> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    ArrayList<Branch> newBranch = new ArrayList<Branch>();
                    newBranch = response.body();
                    if (newBranch != null) {
                        mView.setUpAdapter(newBranch);
                    }
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể lấy được danh sách cơ sở do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Branch>> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: getFilterList
     Creator: Quang Truong
     Description: get filter list for filter branch
     *************************************************/
    @Override
    public void getFilterList() {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getFilterList().enqueue(new Callback<ArrayList<Address>>() {
            @Override
            public void onResponse(Call<ArrayList<Address>> call, Response<ArrayList<Address>> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    ArrayList<Address> newAddress = new ArrayList<Address>();
                    newAddress = response.body();
                    if (newAddress != null) {
                        mView.setUpFilter(newAddress);
                    }
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể lấy được danh sách cơ sở do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Address>> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: filterBranch
     Creator: Quang Truong
     Description: Filter branch by location
     *************************************************/
    @Override
    public void filterBranch(String city, String district) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.filter(city, district, "app").enqueue(new Callback<ArrayList<Branch>>() {
            @Override
            public void onResponse(Call<ArrayList<Branch>> call, Response<ArrayList<Branch>> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    ArrayList<Branch> newBranch = new ArrayList<Branch>();
                    newBranch = response.body();
                    if (newBranch != null) {
                        mView.setUpAdapter(newBranch);
                    }
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể lọc được danh sách cơ sở do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Branch>> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

}
