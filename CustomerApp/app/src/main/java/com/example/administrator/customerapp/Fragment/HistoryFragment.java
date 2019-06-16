package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Adapter.HistoryAdapter;
import com.example.administrator.customerapp.Adapter.QueueRequestAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.HistoryContract;
import com.example.administrator.customerapp.Contract.QueueRequestContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Model.Review;
import com.example.administrator.customerapp.Presenter.HistoryPresenter;
import com.example.administrator.customerapp.Presenter.QueueRequestPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment implements HistoryContract.View{

    private RetrofitInterface callAPIService;
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private ArrayList<History> historyArrayList;
    private HistoryContract.Presenter historyPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog fullHistoryDialog;
    private AlertDialog.Builder fullHistoryDialogBuidler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Account account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Lịch sử");
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.historyRequestRecyclerView);
        assignDialog();
        historyPresenter = new HistoryPresenter(this);

        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        historyPresenter.getHistoryFromServer(account.getToken(), account.getId());
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        fullHistoryDialogBuidler = new AlertDialog.Builder(getActivity());
        waitingDialog = waitingDialogBuilder.create();
    }

    @Override
    public void showDialog(String message, Boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        if (isSuccess) {
            View layout = inflater.inflate(R.layout.custom_toast_success,
                    (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(getActivity());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            View layout = inflater.inflate(R.layout.custom_toast_fail,
                    (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(getActivity());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    public void showProgressBar() {
        if (waitingDialog != null) {
            if (!waitingDialog.isShowing()) {
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    waitingDialogBuilder.setView(R.layout.waiting_dialog);
                    waitingDialogBuilder.setCancelable(false);
                    waitingDialog = waitingDialogBuilder.show();
                } else {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    waitingDialogBuilder.setView(inflater.inflate(R.layout.waiting_dialog, null));
                    waitingDialogBuilder.setCancelable(false);
                    waitingDialog = waitingDialogBuilder.show();
                }
            }
        }
    }

    @Override
    public void hideProgressBar() {
        waitingDialog.dismiss();
    }

    @Override
    public void setUpAdapter(ArrayList<History> historyRequest){
        if(historyRequest != null && account!=null) historyAdapter = new HistoryAdapter(historyRequest, getActivity(), historyPresenter, account);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setAdapter(historyAdapter);
    }

    @Override
    public void showFullHistoryDialog(Review review, Account account, History history) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_dialog, null);
        fullHistoryDialogBuidler.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fullHistoryDialog.dismiss();
            }
        });
        fullHistoryDialogBuidler.setView(v);
        TextView nameTxt = (TextView) v.findViewById(R.id.nameTxt);
        TextView emailTxt = (TextView) v.findViewById(R.id.emailTxt);
        TextView phoneTxt = (TextView) v.findViewById(R.id.phoneTxt);
        TextView timeTxt = (TextView) v.findViewById(R.id.timeTxt);
        TextView statusTxt = (TextView) v.findViewById(R.id.statusTxt);
        TextView branchNameTxt = (TextView) v.findViewById(R.id.branchNameTxt);
        TextView queueNameTxt = (TextView) v.findViewById(R.id.queueNameTxt);
        TextView startTimeTxt = (TextView) v.findViewById(R.id.startTimeTxt);
        TextView endTimeTxt = (TextView) v.findViewById(R.id.endTimeTxt);
        TextView waitingScoreTxt = (TextView) v.findViewById(R.id.waitingScoreTxt);
        TextView serviceScoreTxt = (TextView) v.findViewById(R.id.serviceScoreTxt);
        TextView spaceScoreTxt = (TextView) v.findViewById(R.id.spaceScoreTxt);
        TextView commentTxt = (TextView) v.findViewById(R.id.commentTxt);
        TextView ratingBtn = (Button) v.findViewById(R.id.ratingBtn);
        nameTxt.setText("Tên: " + history.getCustomerName());
        phoneTxt.setText("SĐT: " + history.getCustomerPhone());
        emailTxt.setText("Email: " + history.getCustomerEmail());
        timeTxt.setText("Thời gian đăng ký: " + history.getCreatedAt());
        statusTxt.setText("Trạng thái: " + history.getStatus()); //Bo sung status
        branchNameTxt.setText("Cơ sở: " + history.getBranchName());
        queueNameTxt.setText("Hàng đợi: " + history.getQueueName());
        startTimeTxt.setText("Thời gian bắt đầu: " + history.getStartTime());
        endTimeTxt.setText("Thời gian kết thúc: " + history.getEndTime());
        waitingScoreTxt.setText("Điểm thời gian chờ đợi: " + review.getWaitingScore());
        serviceScoreTxt.setText("Điểm phục vụ: " + review.getServiceScore());
        spaceScoreTxt.setText("Điểm không gian: " + review.getSpaceScore());
        commentTxt.setText("Nhận xét: " + review.getComment());
        fullHistoryDialog = fullHistoryDialogBuidler.show();
    }
}
