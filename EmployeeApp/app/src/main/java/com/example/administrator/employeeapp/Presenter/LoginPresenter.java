package com.example.administrator.employeeapp.Presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.administrator.employeeapp.Activity.Login;
import com.example.administrator.employeeapp.Activity.MainActivity;
import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.LoginContract;
import com.example.administrator.employeeapp.Model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {
    private RetrofitInterface callAPIService;
    private LoginContract.View mView;
    private Account account;
    public LoginPresenter(@NonNull LoginContract.View mView) {
        this.mView = mView;
        this.account = new Account();
    }

    @Override
    public void logIn(String email, String password){
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.logIn(email, password, "employee").enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    if(response.body()!=null) account = response.body();
                    if(response.body().getStatus().equals("1")){
                        mView.openMainActivity(account);
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
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }

        @Override
    public void resendVerifyEmail() {
            if (account == null) {
                mView.showDialog("Gặp lỗi khi gửi lại email", false);
            } else {
                String accountID = account.getId();
                String email = account.getEmail();
                Log.d("1abc", "AccountID: " + accountID);
                callAPIService = APIClient.getClient().create(RetrofitInterface.class);
                callAPIService.resendVerifyEmail(accountID, email).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        mView.hideProgressBar();
                        if (response.code() == 200) {
                            mView.showDialog("Gửi Email xác thực thành công. Vui lòng kiểm tra hộp thư của bạn!", false);
                        } else if (response.code() == 500) {
                            mView.showDialog("Gửi Email xác thực bị lỗi do lỗi hệ thống!", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mView.hideProgressBar();
                        Log.d("1abc", "error loading from API" + call);
                    }
                });
            }
        }

    @Override
    public void getAccount(String token, String accountID) {
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getAccount(token, accountID).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    if(response.body()!=null) account = response.body();
                    mView.openMainActivity(account);
                }else if(response.code() == 500){
                    mView.showDialog("Lấy dữ liệu tài khoản thất bại do lỗi hệ thống", false);
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }

}
