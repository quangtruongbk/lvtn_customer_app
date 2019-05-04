package com.example.administrator.customerapp.Contract;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.SupportedModel.Address;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Query;

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);

        void showProgressBar();

        void hideProgressBar();

        void setUpAdapter(ArrayList<Branch> branch);

        void setUpFilter(ArrayList<Address> addresses);

    }

    interface Presenter extends BasePresenter {
        void getBranchFromServer();

        void getFilterList();

        void filterBranch(String city, String district);

    }
}