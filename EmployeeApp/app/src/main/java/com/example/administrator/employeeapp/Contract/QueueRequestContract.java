package com.example.administrator.employeeapp.Contract;
import com.example.administrator.employeeapp.BasePresenter;
import com.example.administrator.employeeapp.BaseView;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.github.nkzawa.emitter.Emitter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Header;

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
        void createQueueRequest(String token, String accountID, String queueID, String name, String phone, String email);
        void disconnectSocket(Emitter.Listener onQueueChange);
        void listeningSocket(Emitter.Listener onQueueChange);
    }
}