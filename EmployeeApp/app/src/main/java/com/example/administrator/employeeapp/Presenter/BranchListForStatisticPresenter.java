package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.BranchListForStatisticContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchListForStatisticPresenter implements BranchListForStatisticContract.Presenter{
    private RetrofitInterface callAPIService;
    private BranchListForStatisticContract.View mView;
    private Account account;
    public BranchListForStatisticPresenter(@NonNull BranchListForStatisticContract.View mView, Account account) {
        this.mView = mView;
        this.account = account;
    }
    /***************************************************
     Function: getBranch
     Creator: Quang Truong
     Description: Get Branch list
     *************************************************/
    @Override
    public void getBranch(){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getBranch().enqueue(new Callback<ArrayList<Branch>>() {
            @Override
            public void onResponse(Call<ArrayList<Branch>> call, Response<ArrayList<Branch>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<Branch> newBranch = new ArrayList<Branch>();
                    newBranch = response.body();
                    if(newBranch!=null) {
                        mView.setUpAdapter(newBranch);
                    }
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được danh sách cơ sở do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Branch>> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

}
