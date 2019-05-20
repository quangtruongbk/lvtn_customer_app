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
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Contract.ForgotPasswordContract;
import com.example.administrator.customerapp.Presenter.ForgotPasswordPresenter;
import com.example.administrator.customerapp.R;


public class ForgotPassword extends AppCompatActivity implements ForgotPasswordContract.View{
    private EditText emailTxt;
    private Button forgotPasswordBtn;
    private ForgotPasswordContract.Presenter forgotPasswordPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog.Builder resendEmailDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_forgot_password);
        Log.d("1abc", "ON CREATE");
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if(isLogin){
            Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        editor =sharedPreferences.edit();
        waitingDialogBuilder = new AlertDialog.Builder(this);
        noticeDialog = new AlertDialog.Builder(this);
        resendEmailDialog = new AlertDialog.Builder(this);
        forgotPasswordPresenter = new ForgotPasswordPresenter(this);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        forgotPasswordBtn = (Button) findViewById(R.id.forgotPasswordBtn);
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailTxt.getText().toString().trim())) {
                    emailTxt.setError("Không được để trống Email.");
                    return;
                } else {
                    showProgressBar();
                    forgotPasswordPresenter.forgotPassword(emailTxt.getText().toString());
                }
            }
        });

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
    public void openLogin() {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
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
