package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.Contract.QueueRequestContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;

public class OnGoingQueueRequestAdapter extends RecyclerView.Adapter<OnGoingQueueRequestAdapter.RecyclerViewHolder> {

    private ArrayList<QueueRequest> queueRequestList = new ArrayList<QueueRequest>();
    private Context context;
    private Account account;
    private QueueRequestContract.Presenter queueRequestPresenter;

    public OnGoingQueueRequestAdapter(ArrayList<QueueRequest> data, QueueRequestContract.Presenter presenter, Context context, Account account) {
        this.queueRequestList = data;
        this.context = context;
        this.account = account;
        this.queueRequestPresenter = presenter;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.on_going_queue_request_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueRequestList.get(position).getCustomerName());
        if (queueRequestList.get(position).getCustomerEmail() != null)
            holder.emailTxt.setText(queueRequestList.get(position).getCustomerEmail().toString());
        if (queueRequestList.get(position).getCustomerPhone() != null)
            holder.phoneTxt.setText(queueRequestList.get(position).getCustomerPhone().toString());
        holder.moreLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.moreLinearLayout);
                popup.inflate(R.menu.ongoing_queue_request_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.checkoutCustomerBtn:
                                Log.d("6abc", "queueRequestList.get(position).getId()" + queueRequestList.get(position).getId());
                                queueRequestPresenter.checkInOut(account.getToken(), queueRequestList.get(position).getId(), "1");
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
        return queueRequestList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView emailTxt;
        TextView phoneTxt;
        LinearLayout queueRequestLinearLayout;
        LinearLayout moreLinearLayout;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            queueRequestLinearLayout = (LinearLayout) itemView.findViewById(R.id.queueRequestLinearLayout);
            moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.moreLayout);
        }
    }

}
