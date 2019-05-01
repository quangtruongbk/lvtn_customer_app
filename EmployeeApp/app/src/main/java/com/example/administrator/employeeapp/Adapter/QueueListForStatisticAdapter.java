package com.example.administrator.employeeapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.administrator.employeeapp.Contract.QueueContract;
import com.example.administrator.employeeapp.Fragment.QueueRequestFragment;
import com.example.administrator.employeeapp.Fragment.QueueStatisticFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;
import java.util.List;

public class QueueListForStatisticAdapter extends RecyclerView.Adapter<QueueListForStatisticAdapter.RecyclerViewHolder>{
    private List<Queue> queueList = new ArrayList<Queue>();
    private Context context;
    private String branchName;
    private Integer day;
    public QueueListForStatisticAdapter(List<Queue> data, Context context, String branchName, Integer day) {
        this.queueList = data;
        this.context = context;
        this.branchName = branchName;
        this.day = day;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_list_for_statistic_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueList.get(position).getName());
        holder.wholeQueueLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("queueID", queueList.get(position).getId());
                args.putString("queueName", queueList.get(position).getName());
                args.putString("branchName", branchName);
                args.putInt("day", day);
                QueueStatisticFragment queueStatisticFragment = new QueueStatisticFragment();
                queueStatisticFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueStatisticFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        LinearLayout wholeQueueLinearLayout;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            wholeQueueLinearLayout = (LinearLayout) itemView.findViewById(R.id.wholeQueueLinearLayout);
        }
    }

}
