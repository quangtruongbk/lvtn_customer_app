package com.example.administrator.employeeapp.Contract;
import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.Model.QueueRequest;

import java.util.ArrayList;

public interface QueueRequestContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpAdapter(ArrayList<QueueRequest> QueueRequest);
    }

    interface Presenter extends BasePresenter {
        void getQueueRequestFromServer(String queueID);
        void sendEmail(String token, String email, String message);
    }
}