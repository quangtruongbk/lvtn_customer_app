package com.example.administrator.employeeapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.Adapter.BranchAdapter;
import com.example.administrator.employeeapp.Adapter.QueueAdapter;
import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Contract.QueueContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.History;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.Model.Review;
import com.example.administrator.employeeapp.Presenter.QueuePresenter;
import com.example.administrator.employeeapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class QueueFragment extends Fragment implements QueueContract.View{
    private RetrofitInterface callAPIService;
    private RecyclerView queueRecyclerView;
    private QueueAdapter queueAdapter;
    private Button createQueueBtn;
    private TextView pathTxt;
    private ArrayList<Queue> queueArrayList;
    private QueueContract.Presenter queuePresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog createQueueDialog;
    private AlertDialog.Builder createQueueDialogBuilder;
    private SharedPreferences sharedPreferences;
    private Account account;
    private String branchID;
    private String branchName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.queue_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Hàng đợi");
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        queueRecyclerView = (RecyclerView) view.findViewById(R.id.queueRecyclerView);
        pathTxt = (TextView) view.findViewById(R.id.pathTxt);
        createQueueBtn = (Button) view.findViewById(R.id.createQueueBtn);
        assignDialog();
        queuePresenter = new QueuePresenter(this);
        branchID =  getArguments().getString("branchID");
        branchName = getArguments().getString("branchName");
        if(branchName!=null){
            pathTxt.setText(branchName);
        }
        if(branchID!=null) {
            Log.d("1abc", branchID);
            queuePresenter.getQueueFromServer(branchID);
        }
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }

        createQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateQueueDialog();
            }
        });
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        createQueueDialogBuilder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void showDialog(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
    public void setUpAdapter(ArrayList<Queue> queue){
        if(queue != null) queueAdapter = new QueueAdapter(queue, getActivity(), queuePresenter, account, branchName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        queueRecyclerView.setLayoutManager(layoutManager);
        queueRecyclerView.setAdapter(queueAdapter);
    }

    @Override
    public void showCreateQueueDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_create_queue, null);
        createQueueDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createQueueDialog.dismiss();
            }
        });
        createQueueDialogBuilder.setView(v);
        final EditText nameTxt = (EditText) v.findViewById(R.id.queueNameTxt);
        final EditText capacityTxt = (EditText) v.findViewById(R.id.queueCapacityTxt);
        final EditText waitingTimeTxt = (EditText) v.findViewById(R.id.timeWaitingTxt);
        Button createQueueBtn = (Button) v.findViewById(R.id.queueCreateBtn);
        createQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTxt.getText().toString().trim();
                String capacity = capacityTxt.getText().toString().trim();
                String waitingTime = waitingTimeTxt.getText().toString().trim();
                Boolean valueFlag = true;
                if (TextUtils.isEmpty(name)) {
                    nameTxt.setError("Tên hàng đợi không được để trống");
                    valueFlag = false;
                } else {
                    nameTxt.setError(null);
                }
                if (TextUtils.isEmpty(capacity)) {
                    capacityTxt.setError("Sức chứa không được để trống");
                    valueFlag = false;
                } else {
                    capacityTxt.setError(null);
                }
                if (TextUtils.isEmpty(waitingTime)) {
                    waitingTimeTxt.setError("Thời gian chờ/một người không được để trống");
                    valueFlag = false;
                } else {
                    waitingTimeTxt.setError(null);
                }
                if(valueFlag == true) queuePresenter.createQueue(account.getToken(), name, branchID, Integer.parseInt(capacity), Integer.parseInt(waitingTime));
            }
        });
        createQueueDialog = createQueueDialogBuilder.show();
    }

    @Override
    public void returnQueueFragment(String branchID){
        Bundle args = new Bundle();
        args.putString("branchID", branchID);
        QueueFragment queueFragment = new QueueFragment();
        queueFragment.setArguments(args);
        this.getFragmentManager().beginTransaction().replace(R.id.frameFragment, queueFragment).addToBackStack(null).commit();
    }

}
