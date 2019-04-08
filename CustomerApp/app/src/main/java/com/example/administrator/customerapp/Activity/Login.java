package com.example.administrator.customerapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.LoginContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Presenter.LoginPresenter;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements LoginContract.View{

    private EditText usernameTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private LoginContract.Presenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_login);
        Toast.makeText(this, "START LOGIN", Toast.LENGTH_SHORT).show();
        Log.d("1abc", "ON CREATE");
        loginPresenter = new LoginPresenter(this);
        usernameTxt = (EditText) findViewById(R.id.emailTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.logIn(usernameTxt.getText().toString(), passwordTxt.getText().toString());
            }
        });
    }

    @Override
    public void showDialog(String message, Boolean isVerifyEmail){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        if(isVerifyEmail){
            builder.setMessage(message)
                .setPositiveButton("Gửi lại Email xác thực", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
        }
        else{
                builder.setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
        }
    }

    @Override
    public void openMainActivity(Account account) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("Account", account);
        startActivity(intent);
        finish();
    }
}
