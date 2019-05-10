package com.example.administrator.customerapp.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Contract.LoginContract;
import com.example.administrator.customerapp.Contract.SignUpContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Presenter.LoginPresenter;
import com.example.administrator.customerapp.Presenter.SignUpPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp extends AppCompatActivity implements SignUpContract.View {

    private EditText emailTxt;
    private EditText nameTxt;
    private EditText phoneTxt;
    private EditText passwordTxt;
    private EditText confirmPasswordTxt;
    private Button signUpBtn;

    private ProgressBar progressBar;
    private SignUpContract.Presenter signUpPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_signup);
        signUpPresenter = new SignUpPresenter(this);
        assignDialog();
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        nameTxt = (EditText) findViewById(R.id.nameTxt);
        phoneTxt = (EditText) findViewById(R.id.phoneTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        confirmPasswordTxt = (EditText) findViewById(R.id.confirmPasswordTxt);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()) return;
                showProgressBar();
                signUpPresenter.signUp(emailTxt.getText().toString().trim(), nameTxt.getText().toString().trim(), phoneTxt.getText().toString().trim(), passwordTxt.getText().toString());
            }
        });
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(this);
        progressBar = findViewById(R.id.progressBar);
        waitingDialogBuilder = new AlertDialog.Builder(this);
    }
    private boolean validateForm() {
        //Bo sung phone validate, chieu dai cua password
        Boolean validFlag = true;
        String email = emailTxt.getText().toString().trim();
        String name = nameTxt.getText().toString().trim();
        String phone = phoneTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString();
        String confirPassWord = confirmPasswordTxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Không được để trống Email.");
            validFlag = false;
        } else {
            emailTxt.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordTxt.setError("Mật khẩu phải 6 ký tự trở lên");
            validFlag = false;
        } else {
            passwordTxt.setError(null);
        }

        if (TextUtils.isEmpty(phone) || phone.length() < 8 || phone.length() > 15) {
            phoneTxt.setError("Số điện thoại không đúng định dạng hoặc bị để trống");
            validFlag = false;
        } else {
            phoneTxt.setError(null);
        }
        if (confirPassWord.length() < 6 || !confirPassWord.equals(password)) {
            confirmPasswordTxt.setError("Xác nhận mật khẩu không đúng");
            validFlag = false;
        } else {
            confirmPasswordTxt.setError(null);
        }

        Log.d("1abc", "1");
        Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        boolean b = m.find();
        if (b) {
            nameTxt.setError("Tên tồn tại ký tự đặc biệt");
            validFlag = false;
        } else {
            nameTxt.setError(null);
        }
        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Không được để trống tên.");
            validFlag = false;
        } else {
            nameTxt.setError(null);
        }

        return validFlag;
    }

    @Override
    public void showDialog(String message, Boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        if (isSuccess) {
            View layout = inflater.inflate(R.layout.custom_toast_success,
                    (ViewGroup) this.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(this);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            View layout = inflater.inflate(R.layout.custom_toast_fail,
                    (ViewGroup) this.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(this);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();
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

}
