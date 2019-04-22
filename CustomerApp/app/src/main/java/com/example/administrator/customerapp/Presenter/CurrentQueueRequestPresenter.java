package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.CurrentQueueRequestContract;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentQueueRequestPresenter implements CurrentQueueRequestContract.Presenter{
    private RetrofitInterface callAPIService;
    private CurrentQueueRequestContract.View mView;
    public CurrentQueueRequestPresenter(@NonNull CurrentQueueRequestContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getCurrentQueueRequest(String token, String accountID){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getCurrrentQueueRequest(token, accountID).enqueue(new Callback<SpecificQueueRequest>() {
            @Override
            public void onResponse(Call<SpecificQueueRequest> call, Response<SpecificQueueRequest> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    SpecificQueueRequest newSpecificQueueRequest = new SpecificQueueRequest();
                    newSpecificQueueRequest = response.body();
                    mView.setUpView(newSpecificQueueRequest);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được yêu cầu hiện tại do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }
            @Override
            public void onFailure(Call<SpecificQueueRequest> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }
}
