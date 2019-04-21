package com.example.administrator.employeeapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.administrator.employeeapp.Activity.MainActivity;
import com.example.administrator.employeeapp.Adapter.BranchRoleAdapter;
import com.example.administrator.employeeapp.Adapter.HistoryAdapter;
import com.example.administrator.employeeapp.Contract.MyAccountContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Model.History;
import com.example.administrator.employeeapp.Model.SupportedModel.BranchRole;
import com.example.administrator.employeeapp.Presenter.MyAccountPresenter;
import com.example.administrator.employeeapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class MyAccountFragment extends Fragment implements MyAccountContract.View {
    private TextView emailTxt;
    private TextView nameTxt;
    private TextView phoneTxt;
    private TextView createBranchTxt;
    private Button changeInfoBtn;
    private Button changePasswordBtn;
    private RecyclerView roleRecyclerView;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog changeInfoDialog;
    private AlertDialog.Builder changeInfoDialogBuilder;
    private AlertDialog changePasswordDialog;
    private AlertDialog.Builder changePasswordDialogBuilder;
    private MyAccountPresenter myAccountPresenter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Account account;
    private Employee employee;
    private BranchRoleAdapter branchRoleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.my_account_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Tài khoản của tôi");
        nameTxt = (TextView) view.findViewById(R.id.nameTxt);
        emailTxt = (TextView) view.findViewById(R.id.emailTxt);
        phoneTxt = (TextView) view.findViewById(R.id.phoneTxt);
        createBranchTxt = (TextView) view.findViewById(R.id.createBranchTxt);
        changeInfoBtn = (Button) view.findViewById(R.id.changeInfoBtn);
        changePasswordBtn = (Button) view.findViewById(R.id.changePasswordBtn);
        roleRecyclerView = (RecyclerView) view.findViewById(R.id.roleRecyclerView);
        roleRecyclerView.setNestedScrollingEnabled(false);
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        String employeeString = sharedPreferences.getString("Employee", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        employee = new Employee();
        if (!employeeString.equals("empty")) {
            employee = gson.fromJson(employeeString, Employee.class);
        }
        nameTxt.setText(account.getName());
        emailTxt.setText(account.getEmail());
        phoneTxt.setText(account.getPhone());
        if(employee.getRole().getCreateBranch().equals("1")) createBranchTxt.setVisibility(View.VISIBLE);
        assignDialog();
        myAccountPresenter = new MyAccountPresenter(this, getActivity(), account, employee);
        changeInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeInfoDialog();
            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });
        myAccountPresenter.setUpRole(account, employee);
        return view;
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        changeInfoDialogBuilder = new AlertDialog.Builder(getActivity());
        changePasswordDialogBuilder = new AlertDialog.Builder(getActivity());
    }

    public void showChangeInfoDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.change_account_info_dialog, null);
        changeInfoDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeInfoDialog.dismiss();
            }
        });
        changeInfoDialogBuilder.setView(v);
        final TextView nameTxt = (TextView) v.findViewById(R.id.nameTxt);
        final TextView phoneTxt = (TextView) v.findViewById(R.id.phoneTxt);
        nameTxt.setText(account.getName());
        phoneTxt.setText(account.getPhone());
        changeInfoDialog = changeInfoDialogBuilder.show();
        Button changeInfoBtn = (Button) v.findViewById(R.id.changeInfoBtn);
        changeInfoBtn.setOnClickListener(new View.OnClickListener() {
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
                if(validFlag == true) myAccountPresenter.changeInfo(account.getToken(), account.getId(), name, phone);
            }
        });

    }

    public void showChangePasswordDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.change_account_password_dialog, null);
        changePasswordDialogBuilder.setView(v);
        changePasswordDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changePasswordDialog.dismiss();
            }
        });
        Button changePasswordBtn = (Button) v.findViewById(R.id.changePasswordBtn);
        final TextView oldPasswordTxt = (TextView) v.findViewById(R.id.oldPasswordTxt);
        final TextView newPasswordTxt = (TextView) v.findViewById(R.id.newPasswordTxt);
        final TextView confirmNewPasswordTxt = (TextView) v.findViewById(R.id.confirmPasswordTxt);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validFlag = true;
                String password = newPasswordTxt.getText().toString();
                String confirPassWord = confirmNewPasswordTxt.getText().toString();

                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    newPasswordTxt.setError("Mật khẩu phải 6 ký tự trở lên");
                    validFlag = false;
                } else {
                    newPasswordTxt.setError(null);
                }
                if (confirPassWord.length() < 6 || !confirPassWord.equals(password)) {
                    confirmNewPasswordTxt.setError("Xác nhận mật khẩu mới không đúng");
                    validFlag = false;
                } else {
                    confirmNewPasswordTxt.setError(null);
                }
                if(validFlag == true) myAccountPresenter.changePassword(account.getToken(), account.getId(), oldPasswordTxt.getText().toString(), newPasswordTxt.getText().toString());
            }
        });
        changePasswordDialog = changePasswordDialogBuilder.show();
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
    public void openMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void setUpRoleAdapter(ArrayList<Branch> branch){
        if(branch != null && account!=null) branchRoleAdapter = new BranchRoleAdapter(branch, getActivity(), myAccountPresenter, account, employee);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        roleRecyclerView.setLayoutManager(layoutManager);
        if(branchRoleAdapter != null)roleRecyclerView.setAdapter(branchRoleAdapter);
    }
}
