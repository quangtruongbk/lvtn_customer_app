package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.LoginContract;
import com.example.administrator.employeeapp.Contract.SignUpContract;
import com.example.administrator.employeeapp.Model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPresenter implements SignUpContract.Presenter {
    private RetrofitInterface callAPIService;
    private SignUpContract.View mView;
    public SignUpPresenter(@NonNull SignUpContract.View mView) {
        this.mView = mView;
    }

    /***************************************************
     Function: signUp
     Creator: Quang Truong
     Description: Create a new account
     *************************************************/
    @Override
    public void signUp(String email, String name, String phone, String password) {
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.signUp(email, name, phone, password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Đăng ký thành công! Xin vui lòng kiểm tra hộp thư để xác thực tài khoản!");
                    mView.openLoginActivity();
                }else if(response.code() == 409){
                    mView.showDialog("Email đã được sử dụng, xin vui lòng sử dụng một Email khác!");
                }else if(response.code() == 500){
                    mView.showDialog("Đăng ký thất bại do lỗi hệ thống");
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