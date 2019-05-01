package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.employeeapp.Contract.BranchListForStatisticContract;
import com.example.administrator.employeeapp.Fragment.BranchStatisticFragment;
import com.example.administrator.employeeapp.Fragment.QueueRequestFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.R;
import com.example.administrator.employeeapp.Utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class BranchStatisticAdapter extends RecyclerView.Adapter<BranchStatisticAdapter.RecyclerViewHolder> {
    private ArrayList<Branch> branchList = new ArrayList<Branch>();
    private Context context;
    private AlertDialog.Builder dayDialogBuilder;
    private AlertDialog dayDialog;
    private FragmentManager fragmentManager;
    private MyDatabaseHelper database;
    private BranchListForStatisticContract.Presenter branchForStatisticPresenter;
    private Account account;
    private Employee employee;
    public BranchStatisticAdapter(ArrayList<Branch> data, Context context, BranchListForStatisticContract.Presenter presenter, Account account, Employee employee) {
        this.branchList = data;
        this.context = context;
        this.database = new MyDatabaseHelper(context);
        dayDialogBuilder = new AlertDialog.Builder(context);
        branchForStatisticPresenter = presenter;
        this.account = account;
        this.employee = employee;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.branch_statistic_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(branchList.get(position).getName());
        if (!employee.getRole().checkControlBranch(branchList.get(position).getId())) {
            holder.wholeBranchRow.setVisibility(View.GONE);
        }
        holder.wholeBranchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDayDialog(branchList.get(position).getId(), branchList.get(position).getName());
            }
        });
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

    public void showDayDialog(final String branchID, final String branchName) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.statistic_day_dialog, null);
        Button statisticBtn;
        statisticBtn = v.findViewById(R.id.statisticBtn);
        dayDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dayDialog.dismiss();
            }
        });
        final Spinner daySpinner;
        daySpinner = v.findViewById(R.id.daySpinner);
        List<String> list = new ArrayList<>();
        list.add("7");
        list.add("30");
        list.add("60");
        list.add("90");
        list.add("120");
        list.add("270");
        list.add("365");
        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        daySpinner.setAdapter(adapter);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        statisticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayDialog.dismiss();
                Bundle args = new Bundle();
                args.putString("branchID", branchID);
                args.putString("branchName", branchName);
                args.putInt("day", Integer.parseInt(daySpinner.getSelectedItem().toString()));
                BranchStatisticFragment fragment = new BranchStatisticFragment();
                fragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, fragment).addToBackStack(null).commit();
            }
        });
        dayDialogBuilder.setView(v);
        dayDialog = dayDialogBuilder.show();
    }



}
