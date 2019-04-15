package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.employeeapp.Fragment.QueueFragment;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.R;

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
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(branchList.get(position).getName());
        holder.addressTxt.setText("Địa chỉ: " + branchList.get(position).getAddress());
        holder.phoneTxt.setText("Số điện thoại: " + branchList.get(position).getPhone().toString());
        if(branchList.get(position).getStatus().toString() != null) {
            if(branchList.get(position).getStatus().toString().equals("0")) holder.statusTxt.setText("Tình trạng: Đóng cửa");
            if(branchList.get(position).getStatus().toString().equals("1")) holder.statusTxt.setText("Tình trạng: Đang nhận khách");
            if(branchList.get(position).getStatus().toString().equals("-1")) holder.statusTxt.setText("Tình trạng: Đã khóa");
        }
        holder.branchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1abc", "Vi Tri: " + Integer.toString(position));
                Bundle args = new Bundle();
                args.putString("branchID", branchList.get(position).getId());
                QueueFragment queueFragment = new QueueFragment();
                queueFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueFragment).addToBackStack(null).commit();
            }
        });
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.moreBtn);
                popup.inflate(R.menu.branch_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editBtn:
                                //handle menu3 click
                                return true;
                            case R.id.closeBranchBtn:
                                //handle menu3 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
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
        LinearLayout branchRow;
        ImageView moreBtn;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            addressTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            branchRow = (LinearLayout) itemView.findViewById(R.id.branchRow);
            moreBtn = (ImageView) itemView.findViewById(R.id.moreBtn);
        }
    }
}
