package com.example.administrator.employeeapp.Fragment;

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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.Adapter.OnGoingQueueRequestAdapter;
import com.example.administrator.employeeapp.Adapter.QueueRequestAdapter;
import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.QueueRequestContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.example.administrator.employeeapp.Presenter.QueueRequestPresenter;
import com.example.administrator.employeeapp.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.gson.Gson;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.nkzawa.socketio.client.Socket;
import static android.content.Context.MODE_PRIVATE;

public class QueueRequestFragment extends Fragment implements QueueRequestContract.View {
    private RetrofitInterface callAPIService;
    private RecyclerView queueRequestRecyclerView;
    private RecyclerView ongoingQueueRequestRecyclerView;
    private TextView numberOfPeopleTxt;
    private TextView pathTxt;
    private QueueRequestAdapter queueRequestAdapter;
    private OnGoingQueueRequestAdapter ongoingQueueRequestAdapter;
    private ArrayList<QueueRequest> queueRequestArrayList;
    private ArrayList<QueueRequest> ongoingQueueRequestArrayList;
    private QueueRequestContract.Presenter queueRequestPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog createQueueRequestDialog;
    private AlertDialog.Builder createQueueRequestDialogBuilder;
    private FloatingActionButton createQueueRequestFab;
    private SharedPreferences sharedPreferences;
    private Account account;
    private String queueID;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.2:3000");
        } catch (URISyntaxException e) {
            Log.d("5abc", e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.queue_request_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Lượt yêu cầu");
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        queueRequestRecyclerView = (RecyclerView) view.findViewById(R.id.queueRequestRecyclerView);
        ongoingQueueRequestRecyclerView = (RecyclerView) view.findViewById(R.id.onGoingQueueRequestRecyclerView);
        createQueueRequestFab = (FloatingActionButton) view.findViewById(R.id.createQueueRequestFab);
        numberOfPeopleTxt = (TextView) view.findViewById(R.id.numberOfPeopleTxt);
        pathTxt = (TextView) view.findViewById(R.id.pathTxt);
        queueID = getArguments().getString("queueID");
        assignDialog();
        queueRequestPresenter = new QueueRequestPresenter(this, mSocket, queueID);
        if (queueID != null) {
            Log.d("1abc", queueID);
            queueRequestPresenter.getQueueRequestFromServer(queueID);
        }
        String branchName = getArguments().getString("branchName");
        String queueName = getArguments().getString("queueName");
        if (branchName != null && queueName != null) {
            pathTxt.setText(branchName + " > " + queueName);
        }
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }

        createQueueRequestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateQueueRequestDialog();
            }
        });

        queueRequestPresenter.listeningSocket(onQueueChange);
        mSocket.on("onQueueChange", onQueueChange);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        queueRequestPresenter.disconnectSocket(onQueueChange);

    }

  /*  @Override
    public void onResume() {
        super.onResume();
        if (!mSocket.connected()) {
            queueRequestPresenter.listeningSocket(onQueueChange);
            mSocket.on("onQueueChange", onQueueChange);
        }
    } */

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        createQueueRequestDialogBuilder = new AlertDialog.Builder(getActivity());
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
    @TargetApi(21)
    public void showProgressBar() {
        waitingDialogBuilder.setView(R.layout.waiting_dialog);
        waitingDialogBuilder.setCancelable(false);
        waitingDialog = waitingDialogBuilder.show();
    }

    @Override
    public void hideProgressBar() {
        if(waitingDialog != null){
            if(waitingDialog.isShowing()) waitingDialog.dismiss();
        }
    }

    @Override
    public void setUpAdapter(ArrayList<QueueRequest> queueRequest) {
        if (queueRequest != null && account != null) {
            numberOfPeopleTxt.setText(Integer.toString(queueRequest.size()));
            queueRequestAdapter = new QueueRequestAdapter(queueRequest, queueRequestPresenter, getActivity(), account);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        queueRequestRecyclerView.setLayoutManager(layoutManager);
        queueRequestRecyclerView.setNestedScrollingEnabled(false);
        queueRequestRecyclerView.setAdapter(queueRequestAdapter);
    }

    @Override
    public void setUpOnGoingRequestAdapter(ArrayList<QueueRequest> queueRequest) {
        if (queueRequest != null && account != null) {
            Log.d("6abc", "onGoingQueueRequestRecyclerView " + queueRequest.size());
            ongoingQueueRequestAdapter = new OnGoingQueueRequestAdapter(queueRequest, queueRequestPresenter, getActivity(), account);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ongoingQueueRequestRecyclerView.setLayoutManager(layoutManager);
        ongoingQueueRequestRecyclerView.setNestedScrollingEnabled(false);
        ongoingQueueRequestRecyclerView.setAdapter(ongoingQueueRequestAdapter);
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
        createQueueRequestDialog = createQueueRequestDialogBuilder.show();
        Button createQueueRequestBtn = (Button) v.findViewById(R.id.createQueueRequestBtn);
        createQueueRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validFlag = true;
                String name = nameTxt.getText().toString().trim();
                String phone = phoneTxt.getText().toString().trim();
                String email = emailTxt.getText().toString().trim();
                if ((TextUtils.isEmpty(phone) || phone.length() < 8 || phone.length() > 15) && (TextUtils.isEmpty(email))) {
                    phoneTxt.setError("Bạn phải điền ít nhất một trong 2 số điện thoại hoặc email");
                    emailTxt.setError("Bạn phải điền ít nhất một trong 2 số điện thoại hoặc email");
                    validFlag = false;
                } else {
                    phoneTxt.setError(null);
                    emailTxt.setError(null);
                }
                Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                boolean b = m.find();
                if (b || TextUtils.isEmpty(name)) {
                    nameTxt.setError("Tên tồn tại ký tự đặc biệt hoặc bị để trống.");
                    validFlag = false;
                } else {
                    nameTxt.setError(null);
                }
                if (validFlag == true)
                    queueRequestPresenter.createQueueRequest(account.getToken(), account.getId(), queueID, name, phone, email);
            }
        });
    }

    private Emitter.Listener onQueueChange = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    queueRequestPresenter.getQueueRequestFromServer(queueID);
                    Log.d("6abc", "On queue change");
                }
            });
        }
    };
}
