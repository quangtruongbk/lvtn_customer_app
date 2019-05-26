package com.example.administrator.employeeapp.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.employeeapp.Contract.ForgotPasswordContract;
import com.example.administrator.employeeapp.Presenter.ForgotPasswordPresenter;
import com.example.administrator.employeeapp.R;


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
        waitingDialog = waitingDialogBuilder.create();
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
    public void showDialog(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openLogin() {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
        finish();
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
                    LayoutInflater inflater = this.getLayoutInflater();
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
}
