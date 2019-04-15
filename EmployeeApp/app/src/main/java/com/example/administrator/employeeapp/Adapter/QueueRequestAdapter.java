package com.example.administrator.employeeapp.Adapter;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.Contract.QueueRequestContract;
import com.example.administrator.employeeapp.Fragment.QueueRequestFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.Model.QueueRequest;
import com.example.administrator.employeeapp.R;

import java.util.ArrayList;

public class QueueRequestAdapter extends RecyclerView.Adapter<QueueRequestAdapter.RecyclerViewHolder> {

    private ArrayList<QueueRequest> queueRequestList = new ArrayList<QueueRequest>();
    private Context context;
    private Account account;
    private QueueRequestContract.Presenter queueRequestPresenter;
    private AlertDialog.Builder sendEmailDialogBuilder;
    private AlertDialog sendEmailDialog;
    private static String remainerMessage = "Hệ thống quản lý hàng đợi xin thông báo: Yêu cầu của bạn đang chuẩn bị tới lượt, xin vui lòng hãy đến ngay cơ sở để chuẩn bị. Xin cảm ơn quý khách.";
    private static String accidentMessage = "Hệ thống quản lý hàng đợi xin thông báo: Do sự cố ngoài ý muốn mà hệ thống  buộc lòng phải hủy yêu cầu của bạn. Mong quý khách thông cảm.";

    public QueueRequestAdapter(ArrayList<QueueRequest> data, QueueRequestContract.Presenter presenter, Context context, Account account) {
        this.queueRequestList = data;
        this.context = context;
        this.account = account;
        this.queueRequestPresenter = presenter;
        sendEmailDialogBuilder = new AlertDialog.Builder(context);
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
        if (queueRequestList.get(position).getCustomerEmail() != null)
            holder.emailTxt.setText(queueRequestList.get(position).getCustomerEmail().toString());
        if (queueRequestList.get(position).getCustomerPhone() != null)
            holder.phoneTxt.setText(queueRequestList.get(position).getCustomerPhone().toString());
        holder.sttTxt.setText(Integer.toString(position + 1));
        if (account.getId().equals(queueRequestList.get(position).getAccountID()))
            holder.queueRequestLinearLayout.setBackgroundColor(Color.rgb(135, 206, 250));
        holder.moreLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.moreLinearLayout);
                popup.inflate(R.menu.queue_request_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.receiveCustomerBtn:
                                //handle menu1 click
                                return true;
                            case R.id.callCustomerBtn:
                                if (holder.phoneTxt.getText() == null || holder.phoneTxt.getText().equals("")) {
                                    Toast.makeText(context, "Không có số điện thoại của khách", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + holder.phoneTxt.getText()));
                                context.startActivity(intent);
                                return true;
                            case R.id.emailCustomerBtn:
                                if (holder.emailTxt.getText() == null || holder.emailTxt.getText().equals("")) {
                                    Toast.makeText(context, "Không có email của khách", Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                                showSendEmailDialog(queueRequestList.get(position));
                                return true;
                            case R.id.editBtn:
                                //handle menu3 click
                                return true;
                            case R.id.cancelBtn:
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

    public void showSendEmailDialog(QueueRequest queueRequest) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.send_email_dialog, null);
        sendEmailDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendEmailDialog.dismiss();
            }
        });
        sendEmailDialogBuilder.setView(v);
        final EditText emailTxt = (EditText) v.findViewById(R.id.emailTxt);
        final EditText messageTxt = (EditText) v.findViewById(R.id.messageTxt);
        RadioGroup messageRadioGroup = (RadioGroup) v.findViewById(R.id.messageRadioGroup);
        messageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.remainerBtn:
                        messageTxt.setText(remainerMessage);
                        return;
                    case R.id.accidentBtn:
                        messageTxt.setText(accidentMessage);
                        return;
                    case R.id.otherBtn:
                        messageTxt.setText("");
                    default:
                        messageTxt.setText("");
                        return;
                }
            }
        });
        emailTxt.setText(queueRequest.getCustomerEmail());
        Button sendEmailBtn = (Button) v.findViewById(R.id.sendEmailBt);
        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString().trim();
                String message = messageTxt.getText().toString().trim();
                Boolean valueFlag = true;
                if (TextUtils.isEmpty(email)) {
                    emailTxt.setError("Email không được để trống");
                    valueFlag = false;
                } else {
                    emailTxt.setError(null);
                }
                if (TextUtils.isEmpty(message)) {
                    messageTxt.setError("Thông điệp không được để trống");
                    valueFlag = false;
                } else {
                    messageTxt.setError(null);
                }
                if (valueFlag == true) {
                    queueRequestPresenter.sendEmail(account.getToken(), email, message);
                }
            }
        });
        sendEmailDialog = sendEmailDialogBuilder.show();
    }

}
