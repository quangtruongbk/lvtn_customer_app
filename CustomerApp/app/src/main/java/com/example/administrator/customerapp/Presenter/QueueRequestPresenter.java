package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Contract.QueueContract;
import com.example.administrator.customerapp.Contract.QueueContract.View;
import com.example.administrator.customerapp.Contract.QueueRequestContract;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class QueueRequestPresenter implements QueueRequestContract.Presenter{
    private RetrofitInterface callAPIService;
    private QueueRequestContract.View mView;
    private Socket mSocket;
    private String queueID;
    public QueueRequestPresenter(@NonNull QueueRequestContract.View mView, Socket mSocket, String queueID) {
        this.mView = mView;
        this.mSocket = mSocket;
        this.queueID = queueID;
    }

    @Override
    public void getQueueRequestFromServer(String queueID){
        Log.d("6abc", "getQueueRequestFromServer");
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueueRequest(queueID, "0").enqueue(new Callback<ArrayList<QueueRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueRequest>> call, Response<ArrayList<QueueRequest>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<QueueRequest> newQueueRequest = new ArrayList<QueueRequest>();
                    newQueueRequest = response.body();
                    if(newQueueRequest!=null) {
                        mView.setUpAdapter(newQueueRequest);
                    }
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được danh sách hàng đợi do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<QueueRequest>> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    @Override
    public void createQueueRequest(String token, String accountID, String queueID, String name, String phone, String email){
        Log.d("6abc", "createQueueRequest");
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.createQueueRequest(token, accountID, queueID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Tạo yêu cầu thành công", true);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể tạo yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }else if(response.code() == 409){
                    mView.showDialog("Không thể tạo yêu cầu do bạn đang có một yêu cầu chưa được hoàn tất.", false);
                }
                else if(response.code() == 403){
                    mView.showDialog("Bạn không được phép thực hiện tác vụ này!", false);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    @Override
    public void editQueueRequest(String token, String queueRequestID, String name, String phone, String email){
        Log.d("6abc", "editQueueRequest");
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.editQueueRequest(token, queueRequestID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Chỉnh sửa thành công", true);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể tạo yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }else if(response.code() == 404){
                    mView.showDialog("Không thử thực hiện tác vụ này, có vẻ như có gì đó đã thay đổi với lượt đăng ký!", false);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    @Override
    public void cancelQueueRequest(String token, String queueRequestID){
        Log.d("6abc", "cancelQueueRequest");
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.cancelQueueRequest(token, queueRequestID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Hủy yêu cầu thành công", true);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể hủy yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }else if(response.code() == 404){
                    mView.showDialog("Không thể huy yêu cầu do thông tin về yêu cầu đã thay đổi.", false);
                }
                else if(response.code() == 403){
                    mView.showDialog("Bạn không được phép thực hiện tác vụ này!", false);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    @Override
    public void disconnectSocket(Emitter.Listener onQueueChange){
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("6abc", "socket disconnected");
            }
        });
        try {
            JSONObject j = new JSONObject();
            j.put("queueID", queueID);
            mSocket.emit("leave", j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.disconnect();
        mSocket.off("onQueueChange", onQueueChange);
    }

    @Override
    public void listeningSocket(Emitter.Listener onQueueChange){
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("6abc", "socket connected");
            }
        });
        try {
            JSONObject j = new JSONObject();
            j.put("queueID", queueID);
            mSocket.emit("joinroom", j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.connect();
    }

}
