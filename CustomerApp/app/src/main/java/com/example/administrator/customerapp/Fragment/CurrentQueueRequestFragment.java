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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.customerapp.Adapter.HistoryAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.HistoryContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.Review;
import com.example.administrator.customerapp.Presenter.HistoryPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CurrentQueueRequestFragment extends Fragment implements HistoryContract.View{

    private RetrofitInterface callAPIService;
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private TextView nameTxt;
    private TextView emailTxt;
    private TextView phoneTxt;
    private TextView timeTxt;
    private TextView statusTxt;
    private TextView branchNameTxt;
    private TextView queueNameTxt;
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
        View view = inflater.inflate(R.layout.current_queue_request_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Lịch sử");
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.historyRequestRecyclerView);
        nameTxt = (TextView) view.findViewById(R.id.nameTxt);
        emailTxt = (TextView) view.findViewById(R.id.emailTxt);
        phoneTxt = (TextView) view.findViewById(R.id.phoneTxt);
        timeTxt = (TextView) view.findViewById(R.id.timeTxt);
        statusTxt = (TextView) view.findViewById(R.id.statusTxt);
        branchNameTxt = (TextView) view.findViewById(R.id.branchNameTxt);
        queueNameTxt = (TextView) view.findViewById(R.id.queueNameTxt);
        assignDialog();
        historyPresenter = new HistoryPresenter(this);
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("null")) {
            account = gson.fromJson(accountString, Account.class);
        }
        historyPresenter.getHistoryFromServer(account.getToken(), account.getId());
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        fullHistoryDialogBuidler = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void showDialog(String message){
        noticeDialog.setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        noticeDialog.show();
    }

    @Override
    @TargetApi(21)
    public void showProgressBar() {
        waitingDialogBuilder.setView(R.layout.waiting_dialog);
        waitingDialogBuilder.setCancelable(false);
        waitingDialog = waitingDialogBuilder.show();
    }

    @Override
    public void hideProgressBar() {
        waitingDialog.dismiss();
    }

    @Override
    public void setUpAdapter(ArrayList<History> historyRequest){
        for(int i = 0; i < historyRequest.size(); i++) Log.d("1abc", "history: " + historyRequest.get(i).getCustomerName());
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
        startTimeTxt.setText("Thời gian băt đầu: " + history.getStartTime());
        endTimeTxt.setText("Thời gian kết thúc: " + history.getEndTime());
        waitingScoreTxt.setText("Điểm thời gian chờ đợi: " + review.getWaitingScore());
        serviceScoreTxt.setText("Điểm phục vụ: " + review.getServiceScore());
        spaceScoreTxt.setText("Điểm không gian: " + review.getSpaceScore());
        commentTxt.setText("Nhận xét: " + review.getComment());

        fullHistoryDialog = fullHistoryDialogBuidler.show();
    }
}
