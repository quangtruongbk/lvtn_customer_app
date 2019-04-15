package com.example.administrator.employeeapp.Fragment;

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
import android.widget.TextView;

import com.example.administrator.employeeapp.Activity.MainActivity;
import com.example.administrator.employeeapp.Adapter.BranchAdapter;
import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Presenter.HomePresenter;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeContract.View {
    private RecyclerView branchRecyclerView;
    private BranchAdapter branchAdapter;
    private ArrayList<Branch> branchArrayList;
    private HomeContract.Presenter homePresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Trang chủ");
        branchRecyclerView = (RecyclerView) view.findViewById(R.id.branchRecyclerView);
        assignDialog();
        homePresenter = new HomePresenter(this);
        homePresenter.getBranchFromServer();
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
    public void setUpAdapter(ArrayList<Branch> branch){
        Log.d("1abc", "Branch: " + branch.get(0).getName());
        if(branch != null) branchAdapter = new BranchAdapter(branch, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        branchRecyclerView.setLayoutManager(layoutManager);
        branchRecyclerView.setAdapter(branchAdapter);
    }

}
