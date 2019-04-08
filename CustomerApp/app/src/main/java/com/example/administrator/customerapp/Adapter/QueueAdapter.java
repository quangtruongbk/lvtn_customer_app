package com.example.administrator.customerapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;
import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.RecyclerViewHolder>{

    private List<Queue> queueList = new ArrayList<Queue>();

    public QueueAdapter(List<Queue> data) {
        this.queueList = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.nameTxt.setText(queueList.get(position).getName());
        holder.numberTxt.setText("Số lượng (Chưa có): " );
        holder.statusTxt.setText("Tình trạng: " + queueList.get(position).getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView numberTxt;
        TextView statusTxt;
        TextView phoneTxt;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            numberTxt = (TextView) itemView.findViewById(R.id.numberTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
        }
    }
}
