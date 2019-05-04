package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.administrator.customerapp.Adapter.QueueAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.HomeContract;
import com.example.administrator.customerapp.Contract.QueueContract;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.Queue;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueuePresenter implements QueueContract.Presenter{
    private RetrofitInterface callAPIService;
    private QueueContract.View mView;
    public QueuePresenter(@NonNull QueueContract.View mView) {
        this.mView = mView;
    }

    /***************************************************
     Function: getQueueFromServer
     Creator: Quang Truong
     Description: Get list of an queue from branchID
     *************************************************/
    @Override
    public void getQueueFromServer(String branchID){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueue(branchID).enqueue(new Callback<ArrayList<Queue>>() {
            @Override
            public void onResponse(Call<ArrayList<Queue>> call, Response<ArrayList<Queue>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<Queue> newQueue = new ArrayList<Queue>();
                    newQueue = response.body();
                    if(newQueue!=null) {
                        mView.setUpAdapter(newQueue);
                    }
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được danh sách hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Queue>> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }
}
