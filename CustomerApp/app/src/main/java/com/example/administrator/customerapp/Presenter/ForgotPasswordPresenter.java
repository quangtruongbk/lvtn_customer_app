package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.ForgotPasswordContract;
import com.example.administrator.customerapp.Contract.LoginContract;
import com.example.administrator.customerapp.Model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private RetrofitInterface callAPIService;
    private ForgotPasswordContract.View mView;
    private Account account;
    public ForgotPasswordPresenter(@NonNull ForgotPasswordContract.View mView) {
        this.mView = mView;
        this.account = new Account();
    }

    /***************************************************
     Function: forgotPassword
     Creator: Quang Truong
     Description: Reset Password
     *************************************************/
    @Override
    public void forgotPassword(String email){
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.forgotPassword(email, "customer").enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Email reset lại mật khẩu đã được gửi tới email của bạn, hãy kiểm tra và xác nhận!", true);
                    mView.openLogin();
                }else if(response.code() == 404){
                    mView.showDialog("Email không tồn tại, hãy thử lại", false);
                }else if(response.code() == 500){
                    mView.showDialog("Reset lại mật khẩu thất bại do lỗi hệ thống.", false);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }

}
