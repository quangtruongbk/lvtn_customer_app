package com.example.administrator.customerapp.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.administrator.customerapp.Activity.Login;
import com.example.administrator.customerapp.Activity.MainActivity;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.MainActivityContract;
import com.example.administrator.customerapp.Contract.SignUpContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private RetrofitInterface callAPIService;
    private MainActivityContract.View mView;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MainActivityPresenter(@NonNull MainActivityContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    /***************************************************
     Function: logout
     Creator: Quang Truong
     Description: Logout
     *************************************************/
    @Override
    public void logout() {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    sharedPreferences = context.getSharedPreferences("data", MODE_PRIVATE);
                    editor =sharedPreferences.edit();
                    editor.remove("MyAccount");
                    editor.putBoolean("isLogin", false);
                    editor.commit();
                    mView.openLoginActivity();
                }else if(response.code() == 500){
                    mView.showDialog("Đăng xuất thất bại do lỗi hệ thống", false);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }
}