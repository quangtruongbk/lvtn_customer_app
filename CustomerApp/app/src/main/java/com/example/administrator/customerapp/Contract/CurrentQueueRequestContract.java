package com.example.administrator.customerapp.Contract;

import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;

import java.util.ArrayList;

public interface CurrentQueueRequestContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message, Boolean isSuccess);

        void showProgressBar();

        void hideProgressBar();

        void setUpView(SpecificQueueRequest queueRequest);

        void setUpAdapter(ArrayList<SpecificQueueRequest> specificQueueRequest);

        void setUpUsingAdapter(ArrayList<SpecificQueueRequest> specificQueueRequest);

        void resetFragment();

    }

    interface Presenter extends BasePresenter {
        void getCurrentQueueRequest(String token, String accountID, String email, String phone);

        void getRequestFromEmailPhone(String token, String accountID, String email, String phone);

        void editQueueRequest(String token, String accountID, String queueRequestID, String name, String phone, String email);

        void cancelQueueRequest(String token, String accountID, String queueID, String queueRequestID);

        void getUsingQueueRequest(String token, String accountID);
    }
}