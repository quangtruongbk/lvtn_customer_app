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
        holder.roleTxt.setText(employeeListForStatistics.get(position).getName());
        if(employeeListForStatistics.get(position).checkEditQueue()) holder.roleTxt.setText("Chỉnh sửa thông tin hàng đợi\n");
        if(employeeListForStatistics.get(position).checkControlQueue()) holder.roleTxt.setText("Quản lý hàng đợi\n");
        if(employeeListForStatistics.get(position).checkCreateQueue()) holder.roleTxt.setText("Tạo hàng đợi\n");
        if(employeeListForStatistics.get(position).checkEditBranch()) holder.roleTxt.setText("Chỉnh sửa thông tin cơ sở\n");
        if(employeeListForStatistics.get(position).checkControlBranch()) holder.roleTxt.setText("Quản lý cả cơ sở\n");
    }

    @Override
    public int getItemCount() {
        return employeeListForStatistics.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView emailTxt;
        TextView roleTxt;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            roleTxt = (TextView) itemView.findViewById(R.id.roleTxt);
        }
    }
}
