package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.CurrentQueueRequestContract;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentQueueRequestPresenter implements CurrentQueueRequestContract.Presenter{
    private RetrofitInterface callAPIService;
    private CurrentQueueRequestContract.View mView;
    public CurrentQueueRequestPresenter(@NonNull CurrentQueueRequestContract.View mView) {
        this.mView = mView;
    }
    /***************************************************
     Function: getCurrentQueueRequest
     Creator: Quang Truong
     Description: Get a request of account that still waiting
     *************************************************/
    @Override
    public void getCurrentQueueRequest(final String token, final String accountID, final String email, final String phone){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getCurrrentQueueRequest(token, accountID).enqueue(new Callback<SpecificQueueRequest>() {
            @Override
            public void onResponse(Call<SpecificQueueRequest> call, Response<SpecificQueueRequest> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    getRequestFromEmailPhone(token, accountID, email, phone);
                    SpecificQueueRequest newSpecificQueueRequest = new SpecificQueueRequest();
                    newSpecificQueueRequest = response.body();
                    mView.setUpView(newSpecificQueueRequest);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được yêu cầu hiện tại do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }
            @Override
            public void onFailure(Call<SpecificQueueRequest> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: getRequestFromEmailPhone
     Creator: Quang Truong
     Description: Get a request from email and phone
     *************************************************/
    @Override
    public void getRequestFromEmailPhone(String token, String accountID, String email, String phone){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getRequestFromEmailPhone(token, accountID, email, phone).enqueue(new Callback<ArrayList<SpecificQueueRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<SpecificQueueRequest>> call, Response<ArrayList<SpecificQueueRequest>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<SpecificQueueRequest> newSpecificQueueRequest = new ArrayList<SpecificQueueRequest>();
                    newSpecificQueueRequest = response.body();
                    mView.setUpAdapter(newSpecificQueueRequest);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được yêu cầu hiện tại do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<SpecificQueueRequest>> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: editQueueRequest
     Creator: Quang Truong
     Description: Change Info of a request
     *************************************************/
    @Override
    public void editQueueRequest(final String token, final String accountID, String queueRequestID, String name, final String phone, final String email) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.editQueueRequest(token, queueRequestID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Chỉnh sửa thành công", true);
                    mView.resetFragment();
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể chỉnh sửa yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                } else if (response.code() == 404) {
                    mView.showDialog("Không thử thực hiện tác vụ này, có vẻ như có gì đó đã thay đổi với lượt đăng ký!", false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: cancelQueueRequest
     Creator: Quang Truong
     Description: Cancel a request
     *************************************************/
    @Override
    public void cancelQueueRequest(final String token, final String accountID, String queueID, String queueRequestID) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.cancelQueueRequest(token, queueID, queueRequestID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Hủy yêu cầu thành công", true);
                    mView.resetFragment();
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể hủy yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                } else if (response.code() == 404) {
                    mView.showDialog("Không thể hủy yêu cầu do thông tin về yêu cầu đã thay đổi.", false);
                } else if (response.code() == 403) {
                    mView.showDialog("Bạn không được phép thực hiện tác vụ này!", false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

}
