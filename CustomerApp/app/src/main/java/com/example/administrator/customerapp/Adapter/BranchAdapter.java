package com.example.administrator.customerapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.customerapp.Fragment.QueueFragment;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;


public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.RecyclerViewHolder> {

    private ArrayList<Branch> branchList = new ArrayList<Branch>();
    private Context context;
    private FragmentManager fragmentManager;

    public BranchAdapter(ArrayList<Branch> data, Context context) {
        this.branchList = data;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.branch_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(branchList.get(position).getName());
        holder.addressTxt.setText("Địa chỉ: " + branchList.get(position).getAddress());
        holder.phoneTxt.setText("Số điện thoại: " + branchList.get(position).getPhone().toString());
        holder.statusTxt.setText("Tình trạng: " + branchList.get(position).getStatus().toString());
        holder.branchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1abc", "Vi Tri: " + Integer.toString(position));
                Bundle args = new Bundle();
                Log.d("1abc", "BranchID: " + branchList.get(position).getId());
                Log.d("1abc", "BranchID: " + branchList.get(position).getName());
                args.putString("branchID", branchList.get(position).getId());
                QueueFragment queueFragment = new QueueFragment();
                queueFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView addressTxt;
        TextView statusTxt;
        TextView phoneTxt;
        RelativeLayout branchRow;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            addressTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            branchRow = (RelativeLayout) itemView.findViewById(R.id.branchRow);
        }
    }
}
