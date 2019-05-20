package com.example.administrator.customerapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.administrator.customerapp.Contract.CurrentQueueRequestContract;
import com.example.administrator.customerapp.Contract.QueueRequestContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherQueueRequestAdapter extends RecyclerView.Adapter<OtherQueueRequestAdapter.RecyclerViewHolder> {
    private ArrayList<SpecificQueueRequest> queueRequestList = new ArrayList<SpecificQueueRequest>();
    private Context context;
    private CurrentQueueRequestContract.Presenter queueRequestPresenter;
    private AlertDialog.Builder editQueueRequestDialogBuilder;
    private AlertDialog editQueueRequestDialog;
    private Account account;

    public OtherQueueRequestAdapter(ArrayList<SpecificQueueRequest> data, Context context, CurrentQueueRequestContract.Presenter presenter, Account account) {
        this.queueRequestList = data;
        this.context = context;
        this.account = account;
        this.queueRequestPresenter = presenter;
        editQueueRequestDialogBuilder = new AlertDialog.Builder(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.other_queue_request_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueRequestList.get(position).getCustomerName());
        holder.queueNameTxt.setText("Hàng đợi: " + queueRequestList.get(position).getQueueName());
        holder.branchNameTxt.setText("Cơ sở: " + queueRequestList.get(position).getBranchName());
        if (queueRequestList.get(position).getCustomerEmail() != null) {
            holder.emailTxt.setText("Email: " + queueRequestList.get(position).getCustomerEmail());
        }
        if (queueRequestList.get(position).getCustomerPhone() != null) {
            holder.phoneTxt.setText("Số điện thoại: " + queueRequestList.get(position).getCustomerPhone());
        }
        holder.moreLinearLayout.setVisibility(View.VISIBLE);
        holder.moreLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.moreLinearLayout);
                popup.inflate(R.menu.queue_request_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editBtn:
                                showEditQueueRequestDialog(queueRequestList.get(position));
                                return true;
                            case R.id.cancelBtn:
                                queueRequestPresenter.cancelQueueRequest(account.getToken(), account.getId(), queueRequestList.get(position).getQueueID(), queueRequestList.get(position).getId());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
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
        TextView queueNameTxt;
        TextView branchNameTxt;
        LinearLayout queueRequestLinearLayout;
        LinearLayout moreLinearLayout;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            queueNameTxt = (TextView) itemView.findViewById(R.id.queueNameTxt);
            branchNameTxt = (TextView) itemView.findViewById(R.id.branchNameTxt);
            queueRequestLinearLayout = (LinearLayout) itemView.findViewById(R.id.queueRequestLinearLayout);
            moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.moreLayout);
        }
    }

    public void showEditQueueRequestDialog(final QueueRequest queueRequest) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.edit_queuerequest_dialog, null);
        editQueueRequestDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editQueueRequestDialog.dismiss();
            }
        });
        editQueueRequestDialogBuilder.setView(v);
        final EditText nameTxt = (EditText) v.findViewById(R.id.nameTxt);
        final EditText emailTxt = (EditText) v.findViewById(R.id.emailTxt);
        final EditText phoneTxt = (EditText) v.findViewById(R.id.phoneTxt);
        nameTxt.setText(queueRequest.getCustomerName());
        emailTxt.setText(queueRequest.getCustomerEmail());
        phoneTxt.setText(queueRequest.getCustomerPhone());
        editQueueRequestDialog = editQueueRequestDialogBuilder.show();
        Button createQueueRequestBtn = (Button) v.findViewById(R.id.createQueueRequestBtn);
        createQueueRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validFlag = true;
                String name = nameTxt.getText().toString().trim();
                String phone = phoneTxt.getText().toString().trim();
                String email = emailTxt.getText().toString().trim();
                if ((TextUtils.isEmpty(phone) || phone.length() < 8 || phone.length() > 15) && (TextUtils.isEmpty(email))) {
                    phoneTxt.setError("Bạn phải điền ít nhất một trong 2 số điện thoại hoặc email");
                    emailTxt.setError("Bạn phải điền ít nhất một trong 2 số điện thoại hoặc email");
                    validFlag = false;
                } else {
                    phoneTxt.setError(null);
                    emailTxt.setError(null);
                }
                Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                boolean b = m.find();
                if (b || TextUtils.isEmpty(name)) {
                    nameTxt.setError("Tên tồn tại ký tự đặc biệt hoặc bị để trống.");
                    validFlag = false;
                } else {
                    nameTxt.setError(null);
                }
                if (validFlag == true){
                    queueRequestPresenter.editQueueRequest(account.getToken(), account.getId(), queueRequest.getId(), name, phone, email);
                    if(editQueueRequestDialog.isShowing()) editQueueRequestDialog.dismiss();
                }
            }
        });
    }

}
