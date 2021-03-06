package com.example.administrator.employeeapp.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.employeeapp.Contract.MainActivityContract;
import com.example.administrator.employeeapp.Fragment.BranchListForStatisticFragment;
import com.example.administrator.employeeapp.Fragment.CreateBranchFragment;
import com.example.administrator.employeeapp.Fragment.HomeFragment;
import com.example.administrator.employeeapp.Fragment.MyAccountFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Presenter.MainActivityPresenter;
import com.example.administrator.employeeapp.R;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainActivityContract.View {
    private FragmentManager framentManager;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MainActivityContract.Presenter mainActivityPresenter;
    private Account account;
    private Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_main);

        //Get Account Infomation
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();

        if(!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        String employeeString = sharedPreferences.getString("Employee", "empty");
        employee = new Employee();
        if(!employeeString.equals("empty")) {
            employee = gson.fromJson(employeeString, Employee.class);
        }
        editor = sharedPreferences.edit();
        mainActivityPresenter = new MainActivityPresenter(this, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView nameTxt = (TextView) headerView.findViewById(R.id.nameTxt);
        TextView emailTxt = (TextView) headerView.findViewById(R.id.emailTxt);
        nameTxt.setText(account.getName());
        emailTxt.setText(account.getEmail());

        assignDialog();
        Button menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });

        Menu navMenu = navigationView.getMenu();
        if(employee.getRole().getCreateBranch().equals("0")) navMenu.findItem(R.id.navCreateBranch).setVisible(false);

        framentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = framentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameFragment, new HomeFragment());
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.commit();
    }

    public void openMenu(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        else if (framentManager.getBackStackEntryCount() > 0) {
            Log.i("1abc", "popping backstack");
            framentManager.popBackStack();
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = framentManager.beginTransaction();

        if (id == R.id.navHome) {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frameFragment, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.navAccount) {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frameFragment, new MyAccountFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.navCreateBranch) {
            showProgressBar();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frameFragment, new CreateBranchFragment());
            fragmentTransaction.addToBackStack(null);
            hideProgressBar();
            fragmentTransaction.commit();
        } else if (id == R.id.navData) {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frameFragment, new BranchListForStatisticFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.navLogout) {
            mainActivityPresenter.logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void assignDialog(){
        noticeDialog = new AlertDialog.Builder(this);
        waitingDialogBuilder = new AlertDialog.Builder(this);
        waitingDialog = waitingDialogBuilder.create();
    }

    @Override
    public void showDialog(String message){
        noticeDialog.setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        noticeDialog.show();
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

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

}
