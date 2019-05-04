package com.example.administrator.employeeapp.Presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.administrator.employeeapp.Adapter.QueueAdapter;
import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Contract.QueueContract;
import com.example.administrator.employeeapp.Fragment.QueueFragment;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueuePresenter implements QueueContract.Presenter {
    private RetrofitInterface callAPIService;
    private QueueContract.View mView;

    public QueuePresenter(@NonNull QueueContract.View mView) {
        this.mView = mView;
    }

    /***************************************************
     Function: getQueueFromServer
     Creator: Quang Truong
     Description: Get queue list of a branch
     *************************************************/
    @Override
    public void getQueueFromServer(String branchID) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueue(branchID).enqueue(new Callback<ArrayList<Queue>>() {
            @Override
            public void onResponse(Call<ArrayList<Queue>> call, Response<ArrayList<Queue>> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    ArrayList<Queue> newQueue = new ArrayList<Queue>();
                    newQueue = response.body();
                    if (newQueue != null) {
                        mView.setUpAdapter(newQueue);
                    }
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể lấy được danh sách hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Queue>> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: createQueue
     Creator: Quang Truong
     Description: Create a new Queue
     *************************************************/
    @Override
    public void createQueue(String token, String queueName, final String branchID, Integer capacity, Integer waitingTime) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.createQueue(token, queueName, branchID, capacity, waitingTime).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Tạo hàng đợi thành công!", true);
                    mView.returnQueueFragment(branchID);

                } else if (response.code() == 500) {
                    mView.showDialog("Không thể tạo được hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!", false);
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
     Function: changeQueueStatus
     Creator: Quang Truong
     Description: Change status of a queue
     *************************************************/
    @Override
    public void changeQueueStatus(String token, final String branchID, String queueID, final String status) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.changeQueueStatus(token, queueID, status).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    if (status.equals("0")) mView.showDialog("Dừng nhận khách thành công!", true);
                    else if (status.equals("1"))
                        mView.showDialog("Hàng đợi bắt đầu đón khách!", true);
                    else if (status.equals("-1")) mView.showDialog("Hàng đợi đã khóa!", true);
                    mView.returnQueueFragment(branchID);
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể đổi trạng thái hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!", false);
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
     Function: changeInfoQueue
     Creator: Quang Truong
     Description: Change info of a queue
     *************************************************/
    @Override
    public void changeInfoQueue(String token, final String branchID, String queueID, String queueName, Integer capacity, Integer waitingTime) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.changeInfoQueue(token, queueID, queueName, capacity, waitingTime).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Chỉnh sửa thông tin hàng đợi thành công!", true);
                    mView.returnQueueFragment(branchID);

                } else if (response.code() == 500) {
                    mView.showDialog("Không thể chỉnh sửa được thông tin hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!", false);
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
