package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Activity.MainActivity;
import com.example.administrator.customerapp.Contract.MyAccountContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Presenter.MyAccountPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class MyAccountFragment extends Fragment implements MyAccountContract.View {
    private TextView emailTxt;
    private TextView nameTxt;
    private TextView phoneTxt;
    private Button changeInfoBtn;
    private Button changePasswordBtn;
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
        changeInfoBtn = (Button) view.findViewById(R.id.changeInfoBtn);
        changePasswordBtn = (Button) view.findViewById(R.id.changePasswordBtn);
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        nameTxt.setText(account.getName());
        emailTxt.setText(account.getEmail());
        phoneTxt.setText(account.getPhone());
        assignDialog();
        myAccountPresenter = new MyAccountPresenter(this, getActivity());
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
        return view;
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        changeInfoDialogBuilder = new AlertDialog.Builder(getActivity());
        changePasswordDialogBuilder = new AlertDialog.Builder(getActivity());
        waitingDialog = waitingDialogBuilder.create();
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
                Pattern phoneP = Pattern.compile("[0-9]{8,15}$");
                Matcher phoneM;
                boolean phoneB = true;
                if(phone != null && !phone.equals("")){
                    phoneM = phoneP.matcher(phone);
                    phoneB = phoneM.find();
                }
                if (TextUtils.isEmpty(phone) || phone.length() < 8 || phone.length() > 15 || !phoneB) {
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
    public void openMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
