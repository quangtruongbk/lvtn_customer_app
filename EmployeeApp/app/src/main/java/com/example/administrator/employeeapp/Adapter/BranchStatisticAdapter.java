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
import android.view.Menu;
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
import com.example.administrator.employeeapp.Fragment.QueueFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.R;
import com.example.administrator.employeeapp.Utils.GetAddressHelper;
import com.example.administrator.employeeapp.Utils.MyDatabaseHelper;

import java.util.ArrayList;


public class BranchStatisticAdapter extends RecyclerView.Adapter<BranchStatisticAdapter.RecyclerViewHolder> {
    private ArrayList<Branch> branchList = new ArrayList<Branch>();
    private Context context;
    private AlertDialog.Builder changeInfoBranchDialogBuilder;
    private AlertDialog changeInfoBranchDialog;
    private FragmentManager fragmentManager;
    private MyDatabaseHelper database;
    private HomeContract.Presenter homePresenter;
    private Account account;
    private Employee employee;
    public BranchStatisticAdapter(ArrayList<Branch> data, Context context, HomeContract.Presenter presenter, Account account, Employee employee) {
        this.branchList = data;
        this.context = context;
        this.database = new MyDatabaseHelper(context);
        changeInfoBranchDialogBuilder = new AlertDialog.Builder(context);
        homePresenter = presenter;
        this.account = account;
        this.employee = employee;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.branch_data_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(branchList.get(position).getName());
        if(employee.getRole().checkHideBranch(branchList.get(position).getId())){
            holder.wholeBranchRow.setVisibility(View.GONE);
        }
/*        holder.branchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1abc", "Vi Tri: " + Integer.toString(position));
                Bundle args = new Bundle();
                args.putString("branchID", branchList.get(position).getId());
                args.putString("branchName", branchList.get(position).getName());
                QueueFragment queueFragment = new QueueFragment();
                queueFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueFragment).addToBackStack(null).commit();
            }
        }); */
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        LinearLayout wholeBranchRow;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            wholeBranchRow = (LinearLayout) itemView.findViewById(R.id.wholeBranchRow);
        }
    }


}
