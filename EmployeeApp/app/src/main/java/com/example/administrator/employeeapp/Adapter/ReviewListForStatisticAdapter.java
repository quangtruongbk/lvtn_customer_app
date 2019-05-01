package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.employeeapp.Model.SupportedModel.EmployeeListForStatistic;
import com.example.administrator.employeeapp.Model.SupportedModel.ReviewListForStatistic;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;
import java.util.List;


public class ReviewListForStatisticAdapter extends RecyclerView.Adapter<ReviewListForStatisticAdapter.RecyclerViewHolder> {
    private List<ReviewListForStatistic> data = new ArrayList<ReviewListForStatistic>();
    private Context context;
    public ReviewListForStatisticAdapter(List<ReviewListForStatistic> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_list_for_statistic_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText("Tên khách hàng: " + data.get(position).getCustomerName());
        holder.queueNameTxt.setText("Tên hàng đợi: " + data.get(position).getQueueName());
        holder.waitingScoreTxt.setText("Thời gian chờ đợi: " + data.get(position).getWaitingScore() + " điểm");
        holder.serviceScoreTxt.setText("Chất lượng dịch vụ: " + data.get(position).getServiceScore() + " điểm");
        holder.spaceScoreTxt.setText("Không gian: " + data.get(position).getSpaceScore() + " điểm");
        holder.commentTxt.setText("Nhận xét: " + data.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView queueNameTxt;
        TextView waitingScoreTxt;
        TextView serviceScoreTxt;
        TextView spaceScoreTxt;
        TextView commentTxt;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            queueNameTxt = (TextView) itemView.findViewById(R.id.queueNameTxt);
            waitingScoreTxt = (TextView) itemView.findViewById(R.id.waitingScoreTxt);
            serviceScoreTxt = (TextView) itemView.findViewById(R.id.serviceScoreTxt);
            spaceScoreTxt = (TextView) itemView.findViewById(R.id.spaceScoreTxt);
            commentTxt = (TextView) itemView.findViewById(R.id.commentTxt);
        }
    }
}
