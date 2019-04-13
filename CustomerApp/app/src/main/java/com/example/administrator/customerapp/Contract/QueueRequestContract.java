package com.example.administrator.customerapp.Contract;
import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;

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
    }
}