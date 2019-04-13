package com.example.administrator.customerapp.Contract;
import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.Queue;

import java.util.ArrayList;

public interface QueueContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpAdapter(ArrayList<Queue> Queue);
    }

    interface Presenter extends BasePresenter {
        void getQueueFromServer(String branchID);
    }
}