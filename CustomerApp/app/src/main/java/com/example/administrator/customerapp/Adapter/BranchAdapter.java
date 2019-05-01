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
        holder.addressTxt.setText("Địa chỉ: " + branchList.get(position).getAddress().getRest() + ", " + branchList.get(position).getAddress().getWard() + ", " + branchList.get(position).getAddress().getDistrict() + ", " + branchList.get(position).getAddress().getCity());
        holder.phoneTxt.setText("Số điện thoại: " + branchList.get(position).getPhone().toString());
        if(branchList.get(position).getStatus().toString() != null) {
            if(branchList.get(position).getStatus().toString().equals("0")) holder.statusTxt.setText("Tình trạng: Đóng cửa");
            if(branchList.get(position).getStatus().toString().equals("1")) holder.statusTxt.setText("Tình trạng: Đang nhận khách");
            if(branchList.get(position).getStatus().toString().equals("-1")) holder.statusTxt.setText("Tình trạng: Đã khóa");
        }
        String openHourHour = branchList.get(position).getOpentime().split(":")[0];
        String openHourMinute = branchList.get(position).getOpentime().split(":")[1];
        String closeHourHour = branchList.get(position).getClosetime().split(":")[0];
        String closeHourMinute = branchList.get(position).getClosetime().split(":")[1];

        if (Integer.parseInt(openHourHour) < 10) openHourHour = "0" + openHourHour;
        if (Integer.parseInt(openHourMinute) < 10) openHourMinute = "0" + openHourMinute;
        if (Integer.parseInt(closeHourHour) < 10) closeHourHour = "0" + closeHourHour;
        if (Integer.parseInt(closeHourMinute) < 10) closeHourMinute = "0" + closeHourMinute;

        holder.openHourTxt.setText("Giờ hoạt động: " + openHourHour + ":" + openHourMinute + "-" + closeHourHour + ":" + closeHourMinute);
        holder.workingDateTxt.setText("Ngày hoạt động: " + branchList.get(position).getWorkingDate());
        holder.noteTxt.setText("Ghi chú: " + branchList.get(position).getNote());
        holder.branchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1abc", "Vi Tri: " + Integer.toString(position));
                Bundle args = new Bundle();
                Log.d("1abc", "BranchID: " + branchList.get(position).getId());
                Log.d("1abc", "BranchID: " + branchList.get(position).getName());
                args.putString("branchID", branchList.get(position).getId());
                args.putString("branchName", branchList.get(position).getName());
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
        TextView openHourTxt;
        TextView workingDateTxt;
        TextView noteTxt;
        RelativeLayout branchRow;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            addressTxt = (TextView) itemView.findViewById(R.id.addressTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            branchRow = (RelativeLayout) itemView.findViewById(R.id.branchRow);
            openHourTxt = (TextView) itemView.findViewById(R.id.openHoursTxt);
            workingDateTxt = (TextView) itemView.findViewById(R.id.openDayTxt);
            noteTxt = (TextView) itemView.findViewById(R.id.noteTxt);
        }
    }
}
