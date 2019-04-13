package com.example.administrator.customerapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Contract.HistoryContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.RecyclerViewHolder> {

    private ArrayList<History> historyList = new ArrayList<History>();
    private Context context;
    private AlertDialog.Builder ratingDialogBuilder;
    private AlertDialog ratingDialog;
    private AlertDialog reviewDialog;
    private AlertDialog.Builder reviewDialogBuilder;
    private AlertDialog.Builder fullHistoryDialogBuidler;
    private AlertDialog fullHistoryDialog;
    private HistoryContract.Presenter historyPresenter;
    private Account account;
    public HistoryAdapter(ArrayList<History> data, Context context, HistoryContract.Presenter historyPresenter, Account account) {
        this.historyList = data;
        this.context = context;
        this.historyPresenter = historyPresenter;
        this.account = account;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_row, parent, false);
        reviewDialogBuilder = new AlertDialog.Builder(this.context);
        fullHistoryDialogBuidler = new AlertDialog.Builder(this.context);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText("Tên: " + historyList.get(position).getCustomerName());
        if (historyList.get(position).getCustomerEmail() != null)
            holder.emailTxt.setText("Email: " + historyList.get(position).getCustomerEmail().toString());
        if (historyList.get(position).getCustomerPhone() != null)
            holder.phoneTxt.setText("Số điện thoại: " + historyList.get(position).getCustomerPhone().toString());
        if (historyList.get(position).getStatus() != null) {
            if (historyList.get(position).getStatus().equals("0"))
                holder.statusTxt.setText("Trạng thái: Đang chờ");
            else if (historyList.get(position).getStatus().equals("-1"))
                holder.statusTxt.setText("Trạng thái: Đã hủy");
            else if (historyList.get(position).getStatus().equals("1"))
                holder.statusTxt.setText("Trạng thái: Đang sử dụng");
            else if (historyList.get(position).getStatus().equals("2")) {
                holder.statusTxt.setText("Trạng thái: Đã xong");
                holder.ratingBtn.setVisibility(View.VISIBLE);
        //        holder.startTimeTxt.setVisibility(View.VISIBLE);
        //        holder.endTimeTxt.setVisibility(View.VISIBLE);
            } else if (historyList.get(position).getStatus().equals("3")) {
                holder.statusTxt.setText("Trạng thái: Đã xong (Đã đánh giá)");
         //       holder.startTimeTxt.setVisibility(View.VISIBLE);
         //       holder.endTimeTxt.setVisibility(View.VISIBLE);
            }
        }
        if (historyList.get(position).getCreatedAt() != null)
            holder.timeTxt.setText("Thời gian đăng ký: " + historyList.get(position).getCreatedAt());
    /*    if (historyList.get(position).getStartTime() != null && !historyList.get(position).getStartTime().equals("-1"))
            holder.startTimeTxt.setText("Thời gian vào: " + historyList.get(position).getStartTime());
        if (historyList.get(position).getEndTime() != null && !historyList.get(position).getEndTime().equals("-1"))
            holder.endTimeTxt.setText("Thời gian ra: " + historyList.get(position).getEndTime());
        if (historyList.get(position).getBranchName() != null) */
            holder.branchNameTxt.setText("Cơ sở: " + historyList.get(position).getBranchName());
        if (historyList.get(position).getQueueName() != null)
            holder.queueNameTxt.setText("Hàng đợi: " + historyList.get(position).getQueueName());
        if(historyList.get(position).getStatus().equals("2")) {
            holder.historyRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullHistoryForNotCommentDialog(historyList.get(position));
                }
            });
        }

        if(historyList.get(position).getStatus().equals("3")) {
            holder.historyRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyPresenter.getReview(historyList.get(position).getId(), account, historyList.get(position));
                }
            });
        }

        holder.ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog(historyList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView emailTxt;
        TextView phoneTxt;
        TextView timeTxt;
        TextView statusTxt;
        TextView branchNameTxt;
        TextView queueNameTxt;
        TextView startTimeTxt;
        TextView endTimeTxt;
        Button ratingBtn;
        RelativeLayout historyRelativeLayout;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            emailTxt = (TextView) itemView.findViewById(R.id.emailTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            timeTxt = (TextView) itemView.findViewById(R.id.timeTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            branchNameTxt = (TextView) itemView.findViewById(R.id.branchNameTxt);
            queueNameTxt = (TextView) itemView.findViewById(R.id.queueNameTxt);
            startTimeTxt = (TextView) itemView.findViewById(R.id.startTimeTxt);
            endTimeTxt = (TextView) itemView.findViewById(R.id.endTimeTxt);
            ratingBtn = (Button) itemView.findViewById(R.id.ratingBtn);
            historyRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.historyRelativeLayout);
        }
    }

    public void showRatingDialog(final String queueRequestID) {
        ratingDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.rating_dialog, null);
        ratingDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ratingDialog.dismiss();
            }
        });
        ratingDialogBuilder.setView(v);
        ratingDialog = ratingDialogBuilder.show();
        Button reviewBtn = v.findViewById(R.id.reviewBtn);
        final RatingBar waitingScore = v.findViewById(R.id.watingRatingStar);
        waitingScore.setRating(1.0f);
        waitingScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override public void onRatingChanged(RatingBar ratingBar, float rating,
                                                  boolean fromUser) {
                if(rating<1.0f)
                    ratingBar.setRating(1.0f);
            }
        });

        final RatingBar serviceScore = v.findViewById(R.id.serviceRatingStar);
        serviceScore.setRating(1.0f);
        serviceScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override public void onRatingChanged(RatingBar ratingBar, float rating,
                                                  boolean fromUser) {
                if(rating<1.0f)
                    ratingBar.setRating(1.0f);
            }
        });
        final RatingBar spaceScore = v.findViewById(R.id.spaceRatingStar);
        spaceScore.setRating(1.0f);
        spaceScore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override public void onRatingChanged(RatingBar ratingBar, float rating,
                                                  boolean fromUser) {
                if(rating<1.0f)
                    ratingBar.setRating(1.0f);
            }
        });
        final EditText commentTxt = v.findViewById(R.id.commentTxt);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment;
                if (commentTxt.getText().length() == 0)  comment = "";
                else comment = commentTxt.getText().toString();
                historyPresenter.createReview(account.getId(), queueRequestID, waitingScore.getRating(), serviceScore.getRating(), spaceScore.getRating(),comment);
            }
        });
    }

    public void showFullHistoryForNotCommentDialog( History history) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.history_dialog, null);
        fullHistoryDialogBuidler.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                fullHistoryDialog.dismiss();
            }
        });
        fullHistoryDialogBuidler.setView(v);
        TextView nameTxt = (TextView) v.findViewById(R.id.nameTxt);
        TextView emailTxt = (TextView) v.findViewById(R.id.emailTxt);
        TextView phoneTxt = (TextView) v.findViewById(R.id.phoneTxt);
        TextView timeTxt = (TextView) v.findViewById(R.id.timeTxt);
        TextView statusTxt = (TextView) v.findViewById(R.id.statusTxt);
        TextView branchNameTxt = (TextView) v.findViewById(R.id.branchNameTxt);
        TextView queueNameTxt = (TextView) v.findViewById(R.id.queueNameTxt);
        TextView startTimeTxt = (TextView) v.findViewById(R.id.startTimeTxt);
        TextView endTimeTxt = (TextView) v.findViewById(R.id.endTimeTxt);
        TextView waitingScoreTxt = (TextView) v.findViewById(R.id.waitingScoreTxt);
        TextView serviceScoreTxt = (TextView) v.findViewById(R.id.serviceScoreTxt);
        TextView spaceScoreTxt = (TextView) v.findViewById(R.id.spaceScoreTxt);
        TextView commentTxt = (TextView) v.findViewById(R.id.commentTxt);
        TextView ratingBtn = (Button) v.findViewById(R.id.ratingBtn);
        nameTxt.setText("Tên: " + history.getCustomerName());
        phoneTxt.setText("SĐT: " + history.getCustomerPhone());
        emailTxt.setText("Email: " + history.getCustomerEmail());
        timeTxt.setText("Thời gian đăng ký: " + history.getCreatedAt());
        statusTxt.setText("Trạng thái: " + history.getStatus()); //Bo sung status
        branchNameTxt.setText("Cơ sở: " + history.getBranchName());
        queueNameTxt.setText("Hàng đợi: " + history.getQueueName());
        startTimeTxt.setText("Thời gian băt đầu: " + history.getStartTime());
        endTimeTxt.setText("Thời gian kết thúc: " + history.getEndTime());
        waitingScoreTxt.setVisibility(View.GONE);
        serviceScoreTxt.setVisibility(View.GONE);
        spaceScoreTxt.setVisibility(View.GONE);
        commentTxt.setVisibility(View.GONE);
        fullHistoryDialog = fullHistoryDialogBuidler.show();
    }
}
