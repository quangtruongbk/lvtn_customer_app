package com.example.administrator.customerapp.Contract;
import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.Review;

import java.util.ArrayList;

public interface CurrentQueueRequestContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpAdapter(ArrayList<History> historyList);
    }

    interface Presenter extends BasePresenter {
        void getCurrentQueueRequest(String token, String accountID);
    }
}