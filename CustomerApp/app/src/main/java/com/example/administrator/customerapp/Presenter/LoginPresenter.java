package com.example.administrator.customerapp.Presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.administrator.customerapp.Activity.Login;
import com.example.administrator.customerapp.Activity.MainActivity;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.LoginContract;
import com.example.administrator.customerapp.Model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;


public class LoginPresenter implements LoginContract.Presenter {
    private RetrofitInterface callAPIService;
    private LoginContract.View mView;

    public LoginPresenter(@NonNull LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void logIn(String email, String password){
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.logIn(email, password).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.code() == 200) {
                    Account temp = new Account();
                    temp = response.body();
                    Log.d("1abc", response.toString());
                    if(response.body() != null) {
                        String tempBody  = response.body().toString();
                        Log.d("1abc", "Dang nhap thanh cong" + response.body().getStatus());
                    }
                    if(response.body().getStatus().equals("1")){
                        mView.openMainActivity(temp);
                    }

                    else if(response.body().getStatus().equals("-1")){
                        mView.showDialog("Tài khoản của bạn đã bị khóa!", false);
                    }

                    else if(response.body().getStatus().equals("-2")) {
                        mView.showDialog("Tài khoản chưa xác thực, hãy kiểm tra email của bạn để xác thực tài khoản!", true);
                    }
                    if(response.errorBody()!=null)
                        Log.d("1abc", response.errorBody().toString());

                }else if(response.code() == 401){
                    Log.d("1abc", "Đang nhap that bai");
                    mView.showDialog("Đăng nhập thất bại, hãy kiểm tra lại email và mật khẩu!", false);
                }else if(response.code() == 403){
                    Log.d("1abc", "Da dang nhap roi");
                    Log.d("1abc", "Da dang nhap roi");
                }else if(response.code() == 500){
                    mView.showDialog("Đăng nhập thất bại do lỗi hệ thống", false);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d("1abc", "error loading from API");
            }
        });
    }
}
