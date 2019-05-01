package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.BranchListForStatisticContract;
import com.example.administrator.employeeapp.Contract.BranchStatisticContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Statistic;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchStatisticPresenter implements BranchStatisticContract.Presenter{
    private RetrofitInterface callAPIService;
    private BranchStatisticContract.View mView;
    private Account account;
    public BranchStatisticPresenter(@NonNull BranchStatisticContract.View mView, Account account) {
        this.mView = mView;
        this.account = account;
    }

    @Override
    public void getBranchStatistic(String token, String branchID, Integer day){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getBranchStatistic(token, branchID, day).enqueue(new Callback<Statistic>() {
            @Override
            public void onResponse(Call<Statistic> call, Response<Statistic> response) {
                mView.hideProgressBar();
                Log.d("6abc", "After Get branch statistic" + response.code());
                if(response.code() == 200) {
                    Statistic statistic = new Statistic();
                    statistic = response.body();
                    Log.d("6abc", statistic.getNoOfDone().toString());
                    if(statistic!=null) {
                        mView.renderPieChart(statistic);
                        mView.renderLineChart(statistic);
                        mView.setUpComponent(statistic);
                    }
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được dữ liệu và thống kê của cơ sở do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<Statistic> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

}
