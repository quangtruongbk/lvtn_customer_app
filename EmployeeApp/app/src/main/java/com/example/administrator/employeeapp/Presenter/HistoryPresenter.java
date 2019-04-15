package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.HistoryContract;
import com.example.administrator.employeeapp.Contract.QueueRequestContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.History;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.example.administrator.employeeapp.Model.Review;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryPresenter implements HistoryContract.Presenter{
    private RetrofitInterface callAPIService;
    private HistoryContract.View mView;
    public HistoryPresenter(@NonNull HistoryContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getHistoryFromServer(String token, String accountID){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getHistory(token, accountID).enqueue(new Callback<ArrayList<History>>() {
            @Override
            public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<History> newHistory = new ArrayList<History>();
                    newHistory = response.body();
                    Log.d("1abc", "newHistory.size: " + newHistory.size());
                    for(int i = 0; i < newHistory.size(); i++){
                        Log.d("1abc", newHistory.get(i).getBranchName() + newHistory.get(i).getCustomerName());
                    }
                    if(newHistory!=null) {
                        mView.setUpAdapter(newHistory);
                    }
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được lịch sử do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }
            @Override
            public void onFailure(Call<ArrayList<History>> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }

    @Override
    public void createReview(String token, String accountID, String queueRequestID, Float waitingScore, Float serviceScore, Float spaceScore, String comment){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.createReview(token, accountID, queueRequestID, waitingScore, serviceScore, spaceScore, comment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Tạo đánh giá thành công!");
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được lịch sử do lỗi hệ thống. Xin vui lòng thử lại!");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!");
            }
        });
    }

    @Override
    public void getReview(String queueRequestID, final Account account, final History history) {
        mView.showProgressBar();
        Log.d("1abc", "getReview: " + queueRequestID);
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getReview(queueRequestID).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                Log.d("1abc", "getReview SUCCESS" );
                mView.hideProgressBar();
                if (response.code() == 200) {
                    Review newReview = new Review();
                    newReview = response.body();
                    Log.d("1abc", "newReivew: " + newReview.getWaitingScore());
                    if (newReview != null) {
                        mView.showFullHistoryDialog(newReview, account, history);
                       }
                    } else if (response.code() == 500) {
                        mView.showDialog("Không thể lấy được đánh giá do lỗi hệ thống. Xin vui lòng thử lại!");
                    }
                }
                @Override
                public void onFailure (Call < Review > call, Throwable t){
                    mView.hideProgressBar();
                    mView.showDialog("Không thể kết nối được với máy chủ!");
                }
        });
    }
}
