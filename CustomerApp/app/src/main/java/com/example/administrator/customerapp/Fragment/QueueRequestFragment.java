package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Activity.GetIP;
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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class QueueRequestFragment extends Fragment implements QueueRequestContract.View {

    private RetrofitInterface callAPIService;
    private RecyclerView queueRequestRecyclerView;
    private QueueRequestAdapter queueRequestAdapter;
    private TextView numberOfPeopleTxt;
    private TextView pathTxt;
    private TextView timeCountDownTxt;
    private LinearLayout timeCountDownLinear;
    private ArrayList<QueueRequest> queueRequestArrayList;
    private QueueRequestContract.Presenter queueRequestPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog createQueueRequestDialog;
    private AlertDialog.Builder createQueueRequestDialogBuilder;
    private FloatingActionButton createQueueRequestFab;
    private SharedPreferences sharedPreferences;
    private String queueID;
    private String branchName;
    private String queueName;
    private Account account;
    private Activity mActivity;
    private CountDownTimer countDownTimer;
    private Socket mSocket;

    {
        try {
            //  mSocket = IO.socket("http://192.168.1.9:3000");
            mSocket = IO.socket(GetIP.IP + ":3000");
        } catch (URISyntaxException e) {
            Log.d("5abc", e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.queue_request_fragment, container, false);
        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Lượt đăng ký");
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        queueRequestRecyclerView = (RecyclerView) view.findViewById(R.id.queueRequestRecyclerView);
        numberOfPeopleTxt = (TextView) view.findViewById(R.id.numberOfPeopleTxt);
        pathTxt = (TextView) view.findViewById(R.id.pathTxt);
        timeCountDownTxt = (TextView) view.findViewById(R.id.timeCountDownTxt);
        timeCountDownLinear = (LinearLayout) view.findViewById(R.id.timeCountDownLinear);
        createQueueRequestFab = (FloatingActionButton) view.findViewById(R.id.createQueueRequestFab);
        assignDialog();
        queueID = getArguments().getString("queueID");
        branchName = getArguments().getString("branchName");
        queueName = getArguments().getString("queueName");
        queueRequestPresenter = new QueueRequestPresenter(this, mSocket, queueID);
        if (queueID != null) {
            Log.d("1abc", queueID);
            queueRequestPresenter.getQueueRequestFromServer(queueID);
        }

        if (branchName != null && queueName != null) {
            pathTxt.setText(branchName + " > " + queueName);
        }
        sharedPreferences = this.mActivity.getSharedPreferences("data", MODE_PRIVATE);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mSocket.connected()) {
            queueRequestPresenter.listeningSocket(onQueueChange);
            mSocket.on("onQueueChange", onQueueChange);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(mActivity);
        waitingDialogBuilder = new AlertDialog.Builder(mActivity);
        createQueueRequestDialogBuilder = new AlertDialog.Builder(mActivity);
        waitingDialog = waitingDialogBuilder.create();
    }

    @Override
    public void showDialog(String message, Boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        if (isSuccess) {
            View layout = inflater.inflate(R.layout.custom_toast_success,
                    (ViewGroup) mActivity.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            View layout = inflater.inflate(R.layout.custom_toast_fail,
                    (ViewGroup) mActivity.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(mActivity);
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
               /* waitingDialogBuilder.setView(R.layout.waiting_dialog);
                waitingDialogBuilder.setCancelable(false);
                waitingDialog = waitingDialogBuilder.show(); */
                LayoutInflater inflater = mActivity.getLayoutInflater();
                waitingDialogBuilder.setView(inflater.inflate(R.layout.waiting_dialog, null));
                waitingDialogBuilder.setCancelable(false);
                waitingDialog = waitingDialogBuilder.show();
            }
        }
    }

    @Override
    public void hideProgressBar() {
        if (waitingDialog != null) {
            if (waitingDialog.isShowing()) waitingDialog.dismiss();
        }
        Log.d("6abc", "Hide progress bar");
    }

    @Override
    public void setUpAdapter(ArrayList<QueueRequest> queueRequest) {
        if (queueRequest != null && account != null) {
            numberOfPeopleTxt.setText(Integer.toString(queueRequest.size()));
            queueRequestAdapter = new QueueRequestAdapter(queueRequest, mActivity, queueRequestPresenter, account);
            for (int i = 0; i < queueRequest.size(); i++) {
                if (queueRequest.get(i).getAccountID().equals(account.getId())) {
                    Log.d("6abc", "equals: " + account.getId());
                    timeCountDownLinear.setVisibility(View.VISIBLE);
                    long distance = 0;
                    distance = queueRequest.get(i).getExpiredDate() - System.currentTimeMillis();
                    Log.d("6abc", "distance truoc if: " + distance);
                    if (distance > 0) {
                        Log.d("6abc", "distance: " + distance / 1000 / 60);
                        countDownTimer = new CountDownTimer(distance, 1000) {

                            public void onTick(long millisUntilFinished) {
                                Log.d("6abc", "millisUntilFinished: " + millisUntilFinished);
                                timeCountDownTxt.setText("" + ((millisUntilFinished - millisUntilFinished % 60) / 60 / 1000));
                            }

                            public void onFinish() {
                                timeCountDownTxt.setText("0");
                            }

                        }.start();
                    } else {
                        timeCountDownTxt.setText("0");
                    }
                }
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        queueRequestRecyclerView.setLayoutManager(layoutManager);
        queueRequestRecyclerView.setAdapter(queueRequestAdapter);
    }

    @Override
    public void hideCountDown() {
        if (countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
            Log.d("6abc", "cancel");
        }
        timeCountDownLinear.setVisibility(View.INVISIBLE);
    }

    public void showCreateQueueRequestDialog() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.create_queuerequest_dialog, null);
        createQueueRequestDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createQueueRequestDialog.dismiss();
            }
        });
        createQueueRequestDialogBuilder.setView(v);
        final EditText nameTxt = (EditText) v.findViewById(R.id.nameTxt);
        final TextView emailTxt = (TextView) v.findViewById(R.id.emailTxt);
        final TextView phoneTxt = (TextView) v.findViewById(R.id.phoneTxt);
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
                if (validFlag == true) {
                    queueRequestPresenter.createQueueRequest(account.getToken(), account.getId(), queueID, name, phone, email);
                    if (createQueueRequestDialog.isShowing()) createQueueRequestDialog.dismiss();
                }
            }
        });
    }

    private Emitter.Listener onQueueChange = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    queueRequestPresenter.getQueueRequestFromServer(queueID);
                }
            });
        }
    };

    @Override
    public void refreshFragment(){
        Bundle args = new Bundle();
        args.putString("queueName", queueName);
        args.putString("queueID", queueID);
        args.putString("branchName", branchName);
        QueueRequestFragment queueRequestFragment = new QueueRequestFragment();
        queueRequestFragment.setArguments(args);
        this.getFragmentManager().beginTransaction().replace(R.id.frameFragment, queueRequestFragment).addToBackStack(null).commit();

    }
}
