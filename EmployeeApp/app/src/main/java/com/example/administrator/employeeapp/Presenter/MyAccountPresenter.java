package com.example.administrator.employeeapp.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Contract.MyAccountContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MyAccountPresenter implements MyAccountContract.Presenter {
    private RetrofitInterface callAPIService;
    private MyAccountContract.View mView;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyAccountPresenter(@NonNull MyAccountContract.View mView, Context context)
    {
        this.mView = mView;
        this.context = context;
    }

    @Override
    public void changeInfo(String token, String accountID, final String name, final String phone) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        sharedPreferences = context.getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        callAPIService.changeInfo(token, accountID, name, phone).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Cập nhật thông tin tài khoản thành công!");
                    String accountString = sharedPreferences.getString("MyAccount", "empty");
                    Gson gson = new Gson();
                    Account account = new Account();
                    if(!accountString.equals("empty")) {
                        account = gson.fromJson(accountString, Account.class);
                        account.setName(name);
                        account.setPhone(phone);
                        String json = gson.toJson(account);
                        editor.putString("MyAccount", json);
                        editor.commit();
                    }
                    mView.openMainActivity();
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể cập nhật được thông tin tài khoản do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                t.printStackTrace();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }

    @Override
    public void changePassword(String token, String accountID, String oldPassword, String newPassword) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.changePassword(token, accountID, oldPassword, newPassword).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Cập nhật mật khẩu thành công!");
                    mView.openMainActivity();
                }
                if (response.code() == 406) {
                    mView.showDialog("Xác thực mật khẩu cũ không trùng khớp, xin vui lòng thử lại!");
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể cập nhật được mật khẩu do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }


}
