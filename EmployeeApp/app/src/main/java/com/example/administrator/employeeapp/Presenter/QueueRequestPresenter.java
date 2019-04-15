package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.QueueContract;
import com.example.administrator.employeeapp.Contract.QueueContract.View;
import com.example.administrator.employeeapp.Contract.QueueRequestContract;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.Model.QueueRequest;

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

    @Override
    public void sendEmail(String token, String email, String message){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.sendEmail(token, email, message).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Gửi email thành công");
                }else if(response.code() == 500){
                    mView.showDialog("Không thể gửi email do lỗi hệ thống. Xin vui lòng thử lại!");
                }else if(response.code() == 403){
                    mView.showDialog("Bạn không được phép thực hiện tác vụ này!");
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
