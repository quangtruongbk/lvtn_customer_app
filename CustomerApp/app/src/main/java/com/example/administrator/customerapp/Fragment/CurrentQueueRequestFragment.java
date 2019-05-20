package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Adapter.HistoryAdapter;
import com.example.administrator.customerapp.Adapter.OtherQueueRequestAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.CurrentQueueRequestContract;
import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.History;
import com.example.administrator.customerapp.Model.SupportedModel.SpecificQueueRequest;
import com.example.administrator.customerapp.Presenter.CurrentQueueRequestPresenter;
import com.example.administrator.customerapp.R;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CurrentQueueRequestFragment extends Fragment implements CurrentQueueRequestContract.View {

    private RetrofitInterface callAPIService;
    private RecyclerView otherRequestRecyclerView;
    private LinearLayout otherRequestLinear;
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
    private SpecificQueueRequest specificQueueRequest;
    private OtherQueueRequestAdapter adapter;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.current_queue_request_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Lượt đăng ký hiện tại");
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
        otherRequestLinear = (LinearLayout) view.findViewById(R.id.otherRequestLinear);
        otherRequestRecyclerView = (RecyclerView) view.findViewById(R.id.otherRequestRecyclerView);
        assignDialog();
        currentQueueRequestPresenter = new CurrentQueueRequestPresenter(this);
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        currentQueueRequestPresenter.getCurrentQueueRequest(account.getToken(), account.getId(), account.getEmail(), account.getPhone());
        goToQueueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("queueID", specificQueueRequest.getQueueID());
                args.putString("queueName", specificQueueRequest.getQueueName());
                args.putString("branchName", specificQueueRequest.getBranchName());
                QueueRequestFragment queueRequestFragment = new QueueRequestFragment();
                queueRequestFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueRequestFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity =(Activity) context;
        }
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        fullHistoryDialogBuidler = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void showDialog(String message, Boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        if (isSuccess) {
            View layout = inflater.inflate(R.layout.custom_toast_success,
                    (ViewGroup) mActivity.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            View layout = inflater.inflate(R.layout.custom_toast_fail,
                    (ViewGroup) mActivity.findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(mActivity);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
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
        if(waitingDialog.isShowing()) waitingDialog.dismiss();
    }

    @Override
    public void setUpView(SpecificQueueRequest queueRequest) {
        specificQueueRequest = queueRequest;
        if (queueRequest == null || queueRequest.getId() == null) {
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

    @Override
    public void setUpAdapter(ArrayList<SpecificQueueRequest> specificQueueRequest){
        for(int i = 0 ; i < specificQueueRequest.size(); i++)         Log.d("6abc", "Specific: " + specificQueueRequest.get(i).getCustomerName());
        if(specificQueueRequest != null && account!=null){
            Log.d("6abc", "SET setVisibility");
            if(specificQueueRequest.size() > 0 ) otherRequestLinear.setVisibility(View.VISIBLE);
            adapter = new OtherQueueRequestAdapter(specificQueueRequest, getActivity(), currentQueueRequestPresenter, account);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        otherRequestRecyclerView.setLayoutManager(layoutManager);
        otherRequestRecyclerView.setAdapter(adapter);
        if(waitingDialog.isShowing()) waitingDialog.dismiss();
    }

    @Override
    public void resetFragment(){
        FragmentTransaction tr = getFragmentManager().beginTransaction();
        tr.replace(R.id.frameFragment, new CurrentQueueRequestFragment() );
        tr.commit();
    }

}
