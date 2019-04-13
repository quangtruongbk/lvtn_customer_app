package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.QueueContract;
import com.example.administrator.customerapp.Contract.QueueContract.View;
import com.example.administrator.customerapp.Contract.QueueRequestContract;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueRequestPresenter implements QueueRequestContract.Presenter{
    private RetrofitInterface callAPIService;
    private QueueRequestContract.View mView;
    public QueueRequestPresenter(@NonNull QueueRequestContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getQueueRequestFromServer(String queueID){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueueRequest(queueID).enqueue(new Callback<ArrayList<QueueRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueRequest>> call, Response<ArrayList<QueueRequest>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<QueueRequest> newQueueRequest = new ArrayList<QueueRequest>();
                    newQueueRequest = response.body();
                    if(newQueueRequest!=null) {
                        mView.setUpAdapter(newQueueRequest);
                    }
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được danh sách hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<QueueRequest>> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }
}
