package com.example.administrator.customerapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Contract.QueueRequestContract;
import com.example.administrator.customerapp.Fragment.QueueRequestFragment;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Presenter.QueueRequestPresenter;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueueRequestAdapter extends RecyclerView.Adapter<QueueRequestAdapter.RecyclerViewHolder>{

    private ArrayList<QueueRequest> queueRequestList = new ArrayList<QueueRequest>();
    private Context context;
    private QueueRequestContract.Presenter queueRequestPresenter;
    private AlertDialog.Builder editQueueRequestDialogBuilder;
    private AlertDialog editQueueRequestDialog;
    private Account account;
    public QueueRequestAdapter(ArrayList<QueueRequest> data, Context context, QueueRequestContract.Presenter presenter, Account account) {
        this.queueRequestList = data;
        this.context = context;
        this.account = account;
        this.queueRequestPresenter = presenter;
        editQueueRequestDialogBuilder = new AlertDialog.Builder(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_request_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueRequestList.get(position).getCustomerName());
        if(queueRequestList.get(position).getCustomerEmail()!=null){
            if(queueRequestList.get(position).getCustomerEmail().length() > 4 && !queueRequestList.get(position).getAccountID().equals(account.getId())) {
                String temp = queueRequestList.get(position).getCustomerEmail().substring(0, 4);
                String censor = "";
                for(int i = 0; i < queueRequestList.get(position).getCustomerEmail().length() - 4; i++) censor=censor+"*";
                holder.emailTxt.setText("Email: " + temp + censor);
            }else{
                holder.emailTxt.setText("Email: " + queueRequestList.get(position).getCustomerEmail().toString());
            }
        }
        if(queueRequestList.get(position).getCustomerPhone()!=null){
            if(queueRequestList.get(position).getCustomerPhone().length() > 4 && !queueRequestList.get(position).getAccountID().equals(account.getId())){
                String temp = queueRequestList.get(position).getCustomerPhone().substring(queueRequestList.get(position).getCustomerPhone().length() - 4, queueRequestList.get(position).getCustomerPhone().length());
                String censor = "";
                for(int i = 0; i < queueRequestList.get(position).getCustomerPhone().length() - 4; i++) censor=censor+"*";
                holder.phoneTxt.setText("SĐT: " + censor + temp);
            }else{
                holder.phoneTxt.setText("SĐT: " + queueRequestList.get(position).getCustomerPhone().toString());
            }
        }
        holder.sttTxt.setText(Integer.toString(position + 1));
        Log.d("6abc", "position" + position + "acountID: " + queueRequestList.get(position).getAccountID() + " name: " + queueRequestList.get(position).getCustomerName());
        if(account.getId().equals(queueRequestList.get(position).getAccountID())){
            Log.d("6abc", "position" + position +  "Same Request acountID: " + queueRequestList.get(position).getAccountID() + " name: " + queueRequestList.get(position).getCustomerName());
            holder.queueRequestLinearLayout.setBackgroundColor(Color.rgb(135,206,250));
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
                                    cancelDialog(queueRequestList.get(position));
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();

                }
            });
        }else{
            holder.queueRequestLinearLayout.setBackgroundColor(Color.WHITE);
            holder.moreLinearLayout.setVisibility(View.GONE);
        }
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
        LinearLayout moreLinearLayout;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            sttTxt = (TextView) itemView.findViewById(R.id.STTTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
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
                if (validFlag == true)
                    queueRequestPresenter.editQueueRequest(account.getToken(), queueRequest.getId(), name, phone, email);
            }
        });
    }

    public void cancelDialog(final QueueRequest request){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog;
        builder.setMessage("Bạn có chắc chắn muốn hủy lượt đăng ký này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        queueRequestPresenter.cancelQueueRequest(account.getToken(), request.getQueueID(), request.getId());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        dialog = builder.create();
        dialog.show();
    }
}
