package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Contract.MyAccountContract;
import com.example.administrator.employeeapp.Fragment.QueueFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.R;
import com.example.administrator.employeeapp.Utils.GetAddressHelper;
import com.example.administrator.employeeapp.Utils.MyDatabaseHelper;

import java.util.ArrayList;


public class BranchRoleAdapter extends RecyclerView.Adapter<BranchRoleAdapter.RecyclerViewHolder> {
    private ArrayList<Branch> branchList = new ArrayList<Branch>();
    private Context context;
    private MyAccountContract.Presenter homePresenter;
    private Account account;
    private Employee employee;
    public BranchRoleAdapter(ArrayList<Branch> data, Context context, MyAccountContract.Presenter presenter, Account account, Employee employee) {
        this.branchList = data;
        this.context = context;
        homePresenter = presenter;
        this.account = account;
        this.employee = employee;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.branch_role_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(branchList.get(position).getName());
        String role = getBranchRole(branchList.get(position).getId());
        if(role != null){
            if(role.charAt(0) == '1'){
                holder.changeInfoQueueTxt.setVisibility(View.VISIBLE);
            }
            if(role.charAt(1) == '1'){
                holder.controlQueueTxt.setVisibility(View.VISIBLE);
            }
            if(role.charAt(2) == '1'){
                holder.createQueueTxt.setVisibility(View.VISIBLE);
            }
            if(role.charAt(3) == '1'){
                holder.changeInfoBranchTxt.setVisibility(View.VISIBLE);
            }
            if(role.charAt(4) == '1'){
                holder.controlBranchTxt.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView changeInfoQueueTxt;
        TextView controlQueueTxt;
        TextView createQueueTxt;
        TextView changeInfoBranchTxt;
        TextView controlBranchTxt;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            changeInfoQueueTxt = (TextView) itemView.findViewById(R.id.changeInfoQueueTxt);
            controlQueueTxt = (TextView) itemView.findViewById(R.id.controlQueueTxt);
            createQueueTxt = (TextView) itemView.findViewById(R.id.createQueueTxt);
            changeInfoBranchTxt = (TextView) itemView.findViewById(R.id.changeInfoBranchTxt);
            controlBranchTxt = (TextView) itemView.findViewById(R.id.controlBranchTxt);
        }
    }

    public String getBranchRole(String branchID){
        for(int i = 0; i < employee.getRole().getBranchRole().size(); i++){
            if(employee.getRole().getBranchRole().get(i).getBranchID().equals(branchID)) return employee.getRole().getBranchRole().get(i).getRole();
        }
        return null;
    }
}
