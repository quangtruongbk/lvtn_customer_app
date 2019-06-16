package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Adapter.BranchAdapter;
import com.example.administrator.customerapp.Adapter.QueueAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.HomeContract;
import com.example.administrator.customerapp.Contract.QueueContract;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Presenter.QueuePresenter;
import com.example.administrator.customerapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueFragment extends Fragment implements QueueContract.View{

    private RetrofitInterface callAPIService;
    private RecyclerView queueRecyclerView;
    private TextView pathTxt;
    private QueueAdapter queueAdapter;
    private ArrayList<Queue> queueArrayList;
    private QueueContract.Presenter queuePresenter;
    private String branchName;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;

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
        assignDialog();
        queuePresenter = new QueuePresenter(this);
        String branchID = getArguments().getString("branchID");
        branchName = getArguments().getString("branchName");
        if(branchName!=null){
            pathTxt.setText(branchName);
        }
        if(branchID!=null) {
            queuePresenter.getQueueFromServer(branchID);
        }
        Log.d("6abc", "onCreateView");
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
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
    public void setUpAdapter(ArrayList<Queue> queue){
        if(queue != null){
            ArrayList<Queue> temp = new ArrayList<>();
            for(int i = 0; i <queue.size();i++){
                if(!queue.get(i).getStatus().equals("-1")) temp.add(queue.get(i));
            }
            queueAdapter = new QueueAdapter(temp, getActivity(), branchName);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        queueRecyclerView.setLayoutManager(layoutManager);
        queueRecyclerView.setAdapter(queueAdapter);
    }
}
