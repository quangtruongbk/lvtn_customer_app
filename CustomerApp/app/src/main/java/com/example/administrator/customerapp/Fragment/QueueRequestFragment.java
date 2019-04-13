package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.customerapp.Adapter.QueueAdapter;
import com.example.administrator.customerapp.Adapter.QueueRequestAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.QueueContract;
import com.example.administrator.customerapp.Contract.QueueRequestContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Presenter.QueuePresenter;
import com.example.administrator.customerapp.Presenter.QueueRequestPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class QueueRequestFragment extends Fragment implements QueueRequestContract.View{

    private RetrofitInterface callAPIService;
    private RecyclerView queueRequestRecyclerView;
    private QueueRequestAdapter queueRequestAdapter;
    private ArrayList<QueueRequest> queueRequestArrayList;
    private QueueRequestContract.Presenter queueRequestPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog createQueueRequestDialog;
    private AlertDialog.Builder createQueueRequestDialogBuilder;
    private FloatingActionButton createQueueRequestFab;
    private SharedPreferences sharedPreferences;
    private Account account;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.queue_request_fragment, container, false);
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        queueRequestRecyclerView = (RecyclerView) view.findViewById(R.id.queueRequestRecyclerView);
        createQueueRequestFab = (FloatingActionButton) view.findViewById(R.id.createQueueRequestFab);
        assignDialog();
        queueRequestPresenter = new QueueRequestPresenter(this);
        String queueID = getArguments().getString("queueID");
        if(queueID!=null) {
            Log.d("1abc", queueID);
            queueRequestPresenter.getQueueRequestFromServer(queueID);
        }
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("null")) {
            account = gson.fromJson(accountString, Account.class);
        }

        createQueueRequestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateQueueRequestDialog();
            }
        });
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        createQueueRequestDialogBuilder = new AlertDialog.Builder(getActivity());

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
    public void setUpAdapter(ArrayList<QueueRequest> queueRequest){
        if(queueRequest != null && account != null) queueRequestAdapter = new QueueRequestAdapter(queueRequest, getActivity(), account);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        queueRequestRecyclerView.setLayoutManager(layoutManager);
        queueRequestRecyclerView.setAdapter(queueRequestAdapter);
    }

    public void showCreateQueueRequestDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.create_queuerequest_dialog, null);
        createQueueRequestDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createQueueRequestDialog.dismiss();
            }
        });
        createQueueRequestDialogBuilder.setView(v);
        final EditText nameTxt = (EditText) v.findViewById(R.id.nameTxt);
        final EditText emailTxt = (EditText) v.findViewById(R.id.emailTxt);
        final EditText phoneTxt = (EditText) v.findViewById(R.id.phoneTxt);
        nameTxt.setText(account.getName());
        emailTxt.setText(account.getEmail());
        phoneTxt.setText(account.getPhone());
        createQueueRequestDialog = createQueueRequestDialogBuilder.show();
        Button createQueueRequestBtn = (Button) v.findViewById(R.id.createQueueRequestBtn);
        createQueueRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validFlag = true;
                String name = nameTxt.getText().toString().trim();
                String phone = phoneTxt.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() < 8 || phone.length() > 15) {
                    phoneTxt.setError("Số điện thoại không đúng định dạng hoặc bị để trống");
                    validFlag = false;
                } else {
                    phoneTxt.setError(null);
                }
                Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                boolean b = m.find();
                if (b||TextUtils.isEmpty(name)) {
                    nameTxt.setError("Tên tồn tại ký tự đặc biệt hoặc bị để trống.");
                    validFlag = false;
                } else {
                    nameTxt.setError(null);
                }
               // if(validFlag == true) myAccountPresenter.changeInfo(account.getId(), name, phone);
            }
        });

    }

}
