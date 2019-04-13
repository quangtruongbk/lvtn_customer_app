package com.example.administrator.customerapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.customerapp.Fragment.QueueRequestFragment;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;

public class QueueRequestAdapter extends RecyclerView.Adapter<QueueRequestAdapter.RecyclerViewHolder>{

    private ArrayList<QueueRequest> queueRequestList = new ArrayList<QueueRequest>();
    private Context context;
    private Account account;
    public QueueRequestAdapter(ArrayList<QueueRequest> data, Context context, Account account) {
        this.queueRequestList = data;
        this.context = context;
        this.account = account;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_request_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueRequestList.get(position).getCustomerName());
        if(queueRequestList.get(position).getCustomerEmail()!=null) holder.emailTxt.setText(queueRequestList.get(position).getCustomerEmail().toString());
        if(queueRequestList.get(position).getCustomerPhone()!=null) holder.phoneTxt.setText(queueRequestList.get(position).getCustomerPhone().toString());
        holder.sttTxt.setText(Integer.toString(position + 1));
        if(account.getId().equals(queueRequestList.get(position).getAccountID())) holder.queueRequestLinearLayout.setBackgroundColor(Color.rgb(0,96,225));
    }

    @Override
    public int getItemCount() {
        return queueRequestList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView emailTxt;
        TextView phoneTxt;
        TextView sttTxt;
        LinearLayout queueRequestLinearLayout;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            sttTxt = (TextView) itemView.findViewById(R.id.STTTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            queueRequestLinearLayout = (LinearLayout) itemView.findViewById(R.id.queueRequestLinearLayout);
        }
    }
}
