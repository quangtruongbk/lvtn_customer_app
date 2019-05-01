package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Statistic;

import java.util.ArrayList;

public interface BranchStatisticContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);

        void showProgressBar();

        void hideProgressBar();

        void setUpComponent(Statistic statistic);

        void renderPieChart(Statistic statistic);

        void renderLineChart(Statistic statistic);
    }

    interface Presenter extends BasePresenter {
        void getBranchStatistic(String token, String branchID, Integer day);
    }
}