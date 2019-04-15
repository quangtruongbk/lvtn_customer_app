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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueFragment extends Fragment implements QueueContract.View{

    private RetrofitInterface callAPIService;
    private RecyclerView queueRecyclerView;
    private QueueAdapter queueAdapter;
    private ArrayList<Queue> queueArrayList;
    private QueueContract.Presenter queuePresenter;
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
        assignDialog();
        queuePresenter = new QueuePresenter(this);
        String branchID = getArguments().getString("branchID");
        if(branchID!=null) {
            Log.d("1abc", branchID);
            queuePresenter.getQueueFromServer(branchID);
        }
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
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
    public void setUpAdapter(ArrayList<Queue> queue){
        if(queue != null) queueAdapter = new QueueAdapter(queue, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        queueRecyclerView.setLayoutManager(layoutManager);
        queueRecyclerView.setAdapter(queueAdapter);
    }
}
