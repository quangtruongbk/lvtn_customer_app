package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.customerapp.Adapter.HistoryAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.CurrentQueueRequestContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;
import com.example.administrator.customerapp.Presenter.CurrentQueueRequestPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static android.content.Context.MODE_PRIVATE;

public class CurrentQueueRequestFragment extends Fragment implements CurrentQueueRequestContract.View {

    private RetrofitInterface callAPIService;
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private TextView nameTxt;
    private TextView emailTxt;
    private TextView phoneTxt;
    private TextView timeTxt;
    private TextView statusTxt;
    private TextView branchNameTxt;
    private TextView queueNameTxt;
    private Button goToQueueBtn;
    private ImageView QRCodeImg;
    private LinearLayout currentQueueRequestLinearLayout;
    private TextView noCurrentRequestTxt;
    private CurrentQueueRequestContract.Presenter currentQueueRequestPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog fullHistoryDialog;
    private AlertDialog.Builder fullHistoryDialogBuidler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Account account;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.current_queue_request_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Lịch sử");
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        nameTxt = (TextView) view.findViewById(R.id.nameTxt);
        emailTxt = (TextView) view.findViewById(R.id.emailTxt);
        phoneTxt = (TextView) view.findViewById(R.id.phoneTxt);
        timeTxt = (TextView) view.findViewById(R.id.timeTxt);
        statusTxt = (TextView) view.findViewById(R.id.statusTxt);
        branchNameTxt = (TextView) view.findViewById(R.id.branchNameTxt);
        queueNameTxt = (TextView) view.findViewById(R.id.queueNameTxt);
        goToQueueBtn = (Button) view.findViewById(R.id.goToQueueBtn);
        QRCodeImg = (ImageView) view.findViewById(R.id.QRCodeImg);
        currentQueueRequestLinearLayout = (LinearLayout) view.findViewById(R.id.currentQueueRequestLinearLayout);
        noCurrentRequestTxt = (TextView) view.findViewById(R.id.noCurrentRequestTxt);
        assignDialog();
        currentQueueRequestPresenter = new CurrentQueueRequestPresenter(this);
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("null")) {
            account = gson.fromJson(accountString, Account.class);
        }
        currentQueueRequestPresenter.getCurrentQueueRequest(account.getToken(), account.getId());
        return view;
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        fullHistoryDialogBuidler = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void showDialog(String message) {
        noticeDialog.setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        noticeDialog.show();
    }

    @Override
    @TargetApi(21)
    public void showProgressBar() {
        waitingDialogBuilder.setView(R.layout.waiting_dialog);
        waitingDialogBuilder.setCancelable(false);
        waitingDialog = waitingDialogBuilder.show();
    }

    @Override
    public void hideProgressBar() {
        waitingDialog.dismiss();
    }

    @Override
    public void setUpView(SpecificQueueRequest queueRequest) {
        if(queueRequest == null || queueRequest.getId() == null){
            currentQueueRequestLinearLayout.setVisibility(View.GONE);
            noCurrentRequestTxt.setVisibility(View.VISIBLE);
            return;
        }
        nameTxt.setText("Tên: " + queueRequest.getCustomerName());
        emailTxt.setText("Email: " + queueRequest.getCustomerEmail());
        phoneTxt.setText("SĐT: " + queueRequest.getCustomerPhone());
        timeTxt.setText("Thời gian đăng ký: " + queueRequest.getCreatedAt().toString());
        if (queueRequest.getStatus().equals("0")) statusTxt.setText("Tình trạng: Đang đợi");
        if (queueRequest.getStatus().equals("1"))
            statusTxt.setText("Tình trạng: Đang sử dụng dịch vụ");
        branchNameTxt.setText("Tên cơ sở: " + queueRequest.getBranchName());
        queueNameTxt.setText("Tên hàng đợi: " + queueRequest.getQueueName());
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(queueRequest.getId(), BarcodeFormat.QR_CODE, 300, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            QRCodeImg.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
