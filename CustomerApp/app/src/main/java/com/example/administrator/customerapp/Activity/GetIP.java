package com.example.administrator.customerapp.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.customerapp.Contract.LoginContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Presenter.LoginPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;


public class GetIP extends AppCompatActivity{

    private EditText IPTxt;
    private Button IPBtn;
    public static String IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.ip);
        IPTxt = findViewById(R.id.IPTxt);
        IPBtn = findViewById(R.id.IPBtn);

        IPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetIP.IP = "http://" + IPTxt.getText().toString().trim();
                Intent intent = new Intent(GetIP.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
