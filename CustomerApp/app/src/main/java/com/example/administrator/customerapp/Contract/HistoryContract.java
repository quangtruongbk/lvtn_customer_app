package com.example.administrator.customerapp.Contract;
import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Model.Review;

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
        void getHistoryFromServer(String accountID);
        void getReview(String queueRequestID, Account account, History history);
        void createReview(String accountID, String queueRequestID, Float waitingScore, Float serviceScore, Float spaceScore, String comment);
    }
}