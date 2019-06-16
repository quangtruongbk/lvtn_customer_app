package com.example.administrator.employeeapp.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.LoginContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Presenter.LoginPresenter;
import com.example.administrator.employeeapp.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.lang.annotation.Target;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements LoginContract.View{

    private EditText emailTxt;
    private EditText passwordTxt;
    private TextView forgotPasswordTxt;
    private Button loginBtn;
    private ProgressBar progressBar;
    private LoginContract.Presenter loginPresenter;
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
        this.setContentView(R.layout.activity_login);
        noticeDialog = new AlertDialog.Builder(this);
        resendEmailDialog = new AlertDialog.Builder(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        editor =sharedPreferences.edit();
        waitingDialogBuilder = new AlertDialog.Builder(this);
        waitingDialog = waitingDialogBuilder.create();
        showProgressBar();
        loginPresenter = new LoginPresenter(this);
        Boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if(isLogin){
            String accountString = sharedPreferences.getString("MyAccount", "empty");
            Gson gson = new Gson();
            Account account = new Account();
            if(!accountString.equals("empty")) {
                account = gson.fromJson(accountString, Account.class);
            }
            loginPresenter.getAccount(account.getToken(), account.getId());
        }else {
            hideProgressBar();
            emailTxt = (EditText) findViewById(R.id.emailTxt);
            passwordTxt = (EditText) findViewById(R.id.passwordTxt);
            forgotPasswordTxt = (TextView) findViewById(R.id.forgotPasswordTxt);
            loginBtn = (Button) findViewById(R.id.login_btn);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressBar();
                    loginPresenter.logIn(emailTxt.getText().toString(), passwordTxt.getText().toString());
                }
            });
            forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, ForgotPassword.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(waitingDialog.isShowing()) waitingDialog.dismiss();
    }
    @Override
    public void showDialog(String message, Boolean isVerifyEmail){
        if(isVerifyEmail){
            resendEmailDialog.setMessage(message)
                .setPositiveButton("Gửi lại Email xác thực", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showProgressBar();
                        loginPresenter.resendVerifyEmail();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
            resendEmailDialog.show();
        }
        else{
            noticeDialog.setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
            noticeDialog.show();
        }
    }

    @Override
    public void openMainActivity(Account account, Employee employee) {
        editor.putBoolean("isLogin", true);
        Gson gson = new Gson();
        String accountJSON = gson.toJson(account);
        editor.putString("MyAccount", accountJSON);
        String employeeJSON = gson.toJson(employee);
        editor.putString("Employee", employeeJSON);
        editor.commit();
        Intent intent = new Intent(Login.this, MainActivity.class);
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
        if(waitingDialog.isShowing()) waitingDialog.dismiss();
    }
}
