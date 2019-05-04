package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.CreateBranchContract;
import com.example.administrator.employeeapp.Contract.SignUpContract;
import com.example.administrator.employeeapp.Model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBranchPresenter implements CreateBranchContract.Presenter {
    private RetrofitInterface callAPIService;
    private CreateBranchContract.View mView;
    private Account account;

    public CreateBranchPresenter(@NonNull CreateBranchContract.View mView, Account account) {
        this.mView = mView;
        this.account = account;
    }

    /***************************************************
     Function: createBranch
     Creator: Quang Truong
     Description: create new Branch
     *************************************************/
    @Override
    public void createBranch(String token, String name, String city, String district, String ward, String restAddress, String phone,
                             Integer capacity, String openHour, String closeHour, String workingDay, String note) {
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.createBranch(account.getToken(), name, city, district, ward, restAddress, phone, capacity, openHour, closeHour, workingDay, note).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Tạo cơ sở mới thành công!");
                } else if (response.code() == 500) {
                    mView.showDialog("Tạo cơ sở mới thất bại do lỗi hệ thống");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại");
            }
        });
    }
}