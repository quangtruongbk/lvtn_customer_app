package com.example.administrator.employeeapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.employeeapp.R;


public class GetIP extends AppCompatActivity {

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
