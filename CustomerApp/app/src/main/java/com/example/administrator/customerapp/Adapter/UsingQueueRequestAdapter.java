package com.example.administrator.customerapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.administrator.customerapp.Contract.CurrentQueueRequestContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;
import com.example.administrator.customerapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsingQueueRequestAdapter extends RecyclerView.Adapter<UsingQueueRequestAdapter.RecyclerViewHolder> {
    private ArrayList<SpecificQueueRequest> queueRequestList = new ArrayList<SpecificQueueRequest>();
    private Context context;
    private CurrentQueueRequestContract.Presenter queueRequestPresenter;
    private Account account;

    public UsingQueueRequestAdapter(ArrayList<SpecificQueueRequest> data, Context context, CurrentQueueRequestContract.Presenter presenter, Account account) {
        this.queueRequestList = data;
        this.context = context;
        this.account = account;
        this.queueRequestPresenter = presenter;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.other_using_queue_request_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueRequestList.get(position).getCustomerName());
        holder.queueNameTxt.setText("Hàng đợi: " + queueRequestList.get(position).getQueueName());
        holder.branchNameTxt.setText("Cơ sở: " + queueRequestList.get(position).getBranchName());
        holder.createTimeTxt.setText("Thời gian đăng ký: " + queueRequestList.get(position).getCreatedAt());
        holder.startTimeTxt.setText("Thời gian bắt đầu: " + queueRequestList.get(position).getStartTime());
        if (queueRequestList.get(position).getCustomerEmail() != null) {
            holder.emailTxt.setText("Email: " + queueRequestList.get(position).getCustomerEmail());
        }
        if (queueRequestList.get(position).getCustomerPhone() != null) {
            holder.phoneTxt.setText("Số điện thoại: " + queueRequestList.get(position).getCustomerPhone());
        }
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(queueRequestList.get(position).getId(), BarcodeFormat.QR_CODE, 300, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            holder.QRCodeImg.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
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
        TextView queueNameTxt;
        TextView branchNameTxt;
        TextView createTimeTxt;
        TextView startTimeTxt;
        LinearLayout queueRequestLinearLayout;
        LinearLayout moreLinearLayout;
        ImageView QRCodeImg;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            queueNameTxt = (TextView) itemView.findViewById(R.id.queueNameTxt);
            branchNameTxt = (TextView) itemView.findViewById(R.id.branchNameTxt);
            createTimeTxt = (TextView) itemView.findViewById(R.id.createTimeTxt);
            startTimeTxt = (TextView) itemView.findViewById(R.id.startTimeTxt);
            queueRequestLinearLayout = (LinearLayout) itemView.findViewById(R.id.queueRequestLinearLayout);
            moreLinearLayout = (LinearLayout) itemView.findViewById(R.id.moreLayout);
            QRCodeImg = (ImageView) itemView.findViewById(R.id.QRCodeImg);
        }
    }
}
