package com.example.administrator.employeeapp.Contract;
import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.History;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.example.administrator.employeeapp.Model.Review;

import java.util.ArrayList;

public interface HistoryContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpAdapter(ArrayList<History> historyList);
        void showFullHistoryDialog(Review review, Account account, History history);
    }

    interface Presenter extends BasePresenter {
        void getHistoryFromServer(String token, String accountID);
        void getReview(String queueRequestID, Account account, History history);
        void createReview(String token, String accountID, String queueRequestID, Float waitingScore, Float serviceScore, Float spaceScore, String comment);
    }
}