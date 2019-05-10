package com.example.administrator.employeeapp.Contract;

import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Statistic;

public interface QueueStatisticContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);

        void showProgressBar();

        void hideProgressBar();

        void setUpComponent(Statistic statistic);

        void renderPieChart(Statistic statistic);

        void renderLineChart(Statistic statistic);
    }

    interface Presenter extends BasePresenter {
        void getQueueStatistic(String token, String queueID, Integer day);
    }
}