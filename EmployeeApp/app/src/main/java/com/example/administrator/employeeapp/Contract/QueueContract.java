package com.example.administrator.employeeapp.Contract;
import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Queue;

import java.util.ArrayList;

public interface QueueContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpAdapter(ArrayList<Queue> Queue);
        void showCreateQueueDialog();
        void returnQueueFragment(String branchID);
    }

    interface Presenter extends BasePresenter {
        void getQueueFromServer(String branchID);
        void createQueue(String token, String queueName, String branchID, Integer capacity, Integer waitingTime);
        void changeInfoQueue(String token,String branchID, String queueID, String queueName, Integer capacity, Integer waitingTime);
        void changeQueueStatus(String token, String branchID, String queueID, String status);
    }
}