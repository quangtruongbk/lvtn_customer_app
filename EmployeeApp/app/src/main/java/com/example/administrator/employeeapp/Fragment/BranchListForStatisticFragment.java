package com.example.administrator.employeeapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.Adapter.BranchStatisticAdapter;
import com.example.administrator.employeeapp.Contract.BranchListForStatisticContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Presenter.BranchListForStatisticPresenter;
import com.example.administrator.employeeapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class BranchListForStatisticFragment extends Fragment implements BranchListForStatisticContract.View {
    private RecyclerView branchRecyclerView;
    private BranchStatisticAdapter branchAdapter;
    private ArrayList<Branch> branchArrayList;
    private BranchListForStatisticContract.Presenter branchForStatisticPresenter;
    private Account account;
    private Employee employee;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_branch_for_statistic_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Thống kê và dữ liệu");
        branchRecyclerView = (RecyclerView) view.findViewById(R.id.branchRecyclerView);
        assignDialog();
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        String employeeString = sharedPreferences.getString("Employee", "empty");
        employee = new Employee();
        if (!employeeString.equals("empty")) {
            employee = gson.fromJson(employeeString, Employee.class);
        }

        branchForStatisticPresenter = new BranchListForStatisticPresenter(this, account);
        branchForStatisticPresenter.getBranch();
        return view;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        waitingDialog = waitingDialogBuilder.create();
    }

    @Override
    public void showDialog(String message, Boolean isSuccess){
        LayoutInflater inflater = getLayoutInflater();
        if(isSuccess) {
            View layout = inflater.inflate(R.layout.custom_toast_success,
                    (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(getActivity());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }else{
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
    public void setUpAdapter(ArrayList<Branch> branch){
        if(branch != null) branchAdapter = new BranchStatisticAdapter(branch, getActivity(), branchForStatisticPresenter, account, employee);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        branchRecyclerView.setLayoutManager(layoutManager);
        branchRecyclerView.setAdapter(branchAdapter);
    }

}
