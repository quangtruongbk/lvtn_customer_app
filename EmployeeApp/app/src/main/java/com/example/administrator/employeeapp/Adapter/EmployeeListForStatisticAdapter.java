package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.employeeapp.Model.SupportedModel.EmployeeListForStatistic;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;
import java.util.List;


public class EmployeeListForStatisticAdapter extends RecyclerView.Adapter<EmployeeListForStatisticAdapter.RecyclerViewHolder> {
    private List<EmployeeListForStatistic> employeeListForStatistics = new ArrayList<EmployeeListForStatistic>();
    private Context context;
    public EmployeeListForStatisticAdapter(List<EmployeeListForStatistic> data, Context context) {
        employeeListForStatistics = data;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.employee_list_for_statistic_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(employeeListForStatistics.get(position).getName());
        holder.emailTxt.setText(employeeListForStatistics.get(position).getAccountEmail());
        if(employeeListForStatistics.get(position).checkEditQueue()) holder.changeInfoQueueTxt.setVisibility(View.VISIBLE);
        if(employeeListForStatistics.get(position).checkControlQueue()) holder.controlQueueTxt.setVisibility(View.VISIBLE);
        if(employeeListForStatistics.get(position).checkCreateQueue()) holder.createQueueTxt.setVisibility(View.VISIBLE);
        if(employeeListForStatistics.get(position).checkEditBranch()) holder.changeInfoBranchTxt.setVisibility(View.VISIBLE);
        if(employeeListForStatistics.get(position).checkControlBranch()) holder.controlBranchTxt.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return employeeListForStatistics.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView emailTxt;
        TextView changeInfoQueueTxt;
        TextView controlQueueTxt;
        TextView createQueueTxt;
        TextView changeInfoBranchTxt;
        TextView controlBranchTxt;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            changeInfoQueueTxt = (TextView) itemView.findViewById(R.id.changeInfoQueueTxt);
            controlQueueTxt = (TextView) itemView.findViewById(R.id.controlQueueTxt);
            createQueueTxt = (TextView) itemView.findViewById(R.id.createQueueTxt);
            changeInfoBranchTxt = (TextView) itemView.findViewById(R.id.changeInfoBranchTxt);
            controlBranchTxt = (TextView) itemView.findViewById(R.id.controlBranchTxt);
        }
    }
}
