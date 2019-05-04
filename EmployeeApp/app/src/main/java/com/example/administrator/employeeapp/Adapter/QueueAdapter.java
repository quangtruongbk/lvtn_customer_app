package com.example.administrator.employeeapp.Adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.employeeapp.Contract.QueueContract;
import com.example.administrator.employeeapp.Fragment.QueueFragment;
import com.example.administrator.employeeapp.Fragment.QueueRequestFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.R;
import java.util.ArrayList;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.RecyclerViewHolder>{
    private ArrayList<Queue> queueList = new ArrayList<Queue>();
    private Context context;
    private Account account;
    private Employee employee;
    private QueueContract.Presenter queuePresenter;
    private AlertDialog changeInfoQueueDialog;
    private AlertDialog.Builder changeInfoQueueDialogBuilder;
    private String branchName;
    private String branchID;
    private LayoutInflater systemService;

    public QueueAdapter(ArrayList<Queue> data, Context context, QueueContract.Presenter presenter, Account account, Employee employee, String branchID, String branchName) {
        this.queueList = data;
        this.context = context;
        this.account = account;
        this.queuePresenter = presenter;
        changeInfoQueueDialogBuilder = new AlertDialog.Builder(context);
        this.branchName = branchName;
        this.branchID = branchID;
        this.employee = employee;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.queue_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(queueList.get(position).getName());
        if(queueList.get(position).getStatus().toString() != null) {
            if(queueList.get(position).getStatus().toString().equals("0")) holder.statusTxt.setText("Tình trạng: Ngừng nhận khách");
            if(queueList.get(position).getStatus().toString().equals("1")) holder.statusTxt.setText("Tình trạng: Đang nhận khách");
            if(queueList.get(position).getStatus().toString().equals("2")) holder.statusTxt.setText("Tình trạng: Đang nhận khách (Đang vắng)");
            if(queueList.get(position).getStatus().toString().equals("-1")) holder.statusTxt.setText("Tình trạng: Đã khóa");
        }
        holder.queueLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("queueID", queueList.get(position).getId());
                args.putString("queueName", queueList.get(position).getName());
                args.putString("branchName", branchName);
                QueueRequestFragment queueRequestFragment = new QueueRequestFragment();
                queueRequestFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueRequestFragment).addToBackStack(null).commit();
            }
        });
        if(!employee.getRole().checkControlQueue(branchID)){
            holder.wholeQueueLinearLayout.setClickable(false);
            holder.queueLinearLayout.setClickable(false);
            holder.wholeQueueLinearLayout.setBackgroundColor(Color.rgb(202,204,206));
        }
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(context, holder.moreBtn);
                        popup.inflate(R.menu.queue_menu);
                        Menu popupMenu = popup.getMenu();
                        if(!employee.getRole().checkEditQueue(branchID)) popupMenu.findItem(R.id.editBtn).setVisible(false);
                        if(!employee.getRole().checkControlQueue(branchID)) popupMenu.findItem(R.id.closeOpenQueueBtn).setVisible(false);
                        if(queueList.get(position).getStatus().toString().equals("0"))
                            popupMenu.findItem(R.id.closeOpenQueueBtn).setTitle("Bắt đầu nhận khách");
                        else if(queueList.get(position).getStatus().toString().equals("1"))
                            popupMenu.findItem(R.id.closeOpenQueueBtn).setTitle("Dừng nhận khách");
                        else if(queueList.get(position).getStatus().toString().equals("-1"))
                            popupMenu.findItem(R.id.closeOpenQueueBtn).setTitle("Mở khóa");
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.editBtn:
                                        showChangeInfoQueueDialog(queueList.get(position));
                                        return true;
                                    case R.id.closeOpenQueueBtn:
                                        if(queueList.get(position).getStatus().toString().equals("0")) queuePresenter.changeQueueStatus(account.getToken(),queueList.get(position).getBranchID().toString(), queueList.get(position).getId().toString(), "1");
                                        else if(queueList.get(position).getStatus().toString().equals("1")) queuePresenter.changeQueueStatus(account.getToken(), queueList.get(position).getBranchID().toString(), queueList.get(position).getId().toString(), "0");
                                        else if(queueList.get(position).getStatus().toString().equals("-1")) queuePresenter.changeQueueStatus(account.getToken(), queueList.get(position).getBranchID().toString(), queueList.get(position).getId().toString(), "0");
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
        });
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView statusTxt;
        LinearLayout queueLinearLayout;
        LinearLayout wholeQueueLinearLayout;
        ImageView moreBtn;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            queueLinearLayout = (LinearLayout) itemView.findViewById(R.id.queueLinearLayout);
            wholeQueueLinearLayout = (LinearLayout) itemView.findViewById(R.id.wholeQueueLinearLayout);
            moreBtn = (ImageView) itemView.findViewById(R.id.moreBtn);
        }
    }

    public void showChangeInfoQueueDialog(final Queue queue) {
        LayoutInflater inflater = systemService;
        View v = inflater.inflate(R.layout.activity_changeinfo_queue, null);
        changeInfoQueueDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeInfoQueueDialog.dismiss();
            }
        });
        changeInfoQueueDialogBuilder.setView(v);
        final EditText nameTxt = (EditText) v.findViewById(R.id.queueNameTxt);
        final EditText capacityTxt = (EditText) v.findViewById(R.id.queueCapacityTxt);
        final EditText waitingTimeTxt = (EditText) v.findViewById(R.id.timeWaitingTxt);
        nameTxt.setText(queue.getName());
        capacityTxt.setText(queue.getCapacity().toString());
        waitingTimeTxt.setText(queue.getWaitingTime().toString());
        Button changeInfoQueueBtn = (Button) v.findViewById(R.id.changeInfoQueueBtn);
        changeInfoQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTxt.getText().toString().trim();
                String capacity = capacityTxt.getText().toString().trim();
                String waitingTime = waitingTimeTxt.getText().toString().trim();
                Boolean valueFlag = true;
                if (TextUtils.isEmpty(name)) {
                    nameTxt.setError("Tên hàng đợi không được để trống");
                    valueFlag = false;
                } else {
                    nameTxt.setError(null);
                }
                if (TextUtils.isEmpty(capacity)) {
                    capacityTxt.setError("Sức chứa không được để trống");
                    valueFlag = false;
                } else {
                    capacityTxt.setError(null);
                }
                if (TextUtils.isEmpty(waitingTime)) {
                    waitingTimeTxt.setError("Thời gian chờ/một người không được để trống");
                    valueFlag = false;
                } else {
                    waitingTimeTxt.setError(null);
                }
                if(valueFlag == true){
                    queuePresenter.changeInfoQueue(account.getToken(), queue.getBranchID(), queue.getId(), name, Integer.parseInt(capacity), Integer.parseInt(waitingTime));
                    changeInfoQueueDialog.dismiss();
                }
            }
        });
        changeInfoQueueDialog = changeInfoQueueDialogBuilder.show();
    }

}
