package com.example.administrator.customerapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.customerapp.Fragment.QueueFragment;
import com.example.administrator.customerapp.Fragment.QueueRequestFragment;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.R;
import java.util.ArrayList;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.RecyclerViewHolder>{

    private ArrayList<Queue> queueList = new ArrayList<Queue>();
    private Context context;
    public QueueAdapter(ArrayList<Queue> data, Context context) {
        this.queueList = data;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueList.get(position).getName());
        holder.numberTxt.setText("Số lượng (Chưa có): " );
        holder.statusTxt.setText("Tình trạng: " + queueList.get(position).getStatus().toString());
        holder.queueRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("queueID", queueList.get(position).getId());
                QueueRequestFragment queueRequestFragment = new QueueRequestFragment();
                queueRequestFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueRequestFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView numberTxt;
        TextView statusTxt;
        RelativeLayout queueRelativeLayout;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            numberTxt = (TextView) itemView.findViewById(R.id.numberTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            queueRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.queueRelativeLayout);
        }
    }
}
