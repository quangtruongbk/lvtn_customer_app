package com.example.administrator.customerapp.Contract;
import com.example.administrator.customerapp.BasePresenter;
import com.example.administrator.customerapp.BaseView;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;

public interface CurrentQueueRequestContract {
    interface View extends BaseView<Presenter> {
        void showDialog(String message);
        void showProgressBar();
        void hideProgressBar();
        void setUpView(SpecificQueueRequest queueRequest);
    }

    interface Presenter extends BasePresenter {
        void getCurrentQueueRequest(String token, String accountID);
    }
}