package com.example.administrator.employeeapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.QueueRequestContract;
import com.example.administrator.employeeapp.Model.QueueRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void getQueueRequestFromServer(final String queueID){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueueRequest(queueID, "0").enqueue(new Callback<ArrayList<QueueRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueRequest>> call, Response<ArrayList<QueueRequest>> response) {
                if(response.code() == 200) {
                    Log.d("6abc", "2");
                    ArrayList<QueueRequest> newQueueRequest = new ArrayList<QueueRequest>();
                    newQueueRequest = response.body();
                    if(newQueueRequest!=null) {
                        mView.setUpAdapter(newQueueRequest);
                    }
                    Log.d("6abc", "getOnGoingQueueRequestFromServer");
                    getOnGoingQueueRequestFromServer(queueID);
                }else if(response.code() == 500){
                    mView.hideProgressBar();
                    mView.showDialog("Không thể lấy được danh sách yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<QueueRequest>> call, Throwable t) {
                mView.hideProgressBar();
                Log.d("6abc", "4");
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    @Override
    public void getOnGoingQueueRequestFromServer(String queueID){
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueueRequest(queueID, "1").enqueue(new Callback<ArrayList<QueueRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueRequest>> call, Response<ArrayList<QueueRequest>> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    ArrayList<QueueRequest> newQueueRequest = new ArrayList<QueueRequest>();
                    newQueueRequest = response.body();
                    if(newQueueRequest!=null) {
                        mView.setUpOnGoingRequestAdapter(newQueueRequest);
                    }
                    Log.d("6abc", "6");
                }else if(response.code() == 500){
                    mView.showDialog("Không thể lấy được danh sách yêu cầu đang sử dụng dịch vụ do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<QueueRequest>> call, Throwable t) {
                mView.hideProgressBar();
                Log.d("6abc", "8");
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    @Override
    public void sendEmail(String token, String email, String message){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.sendEmail(token, email, message).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Gửi email thành công", true);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể gửi email do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }else if(response.code() == 403){
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
    public void createQueueRequest(String token, String accountID, String queueID, String name, String phone, String email){
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.createQueueRequest(token, "-1", queueID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Tạo yêu cầu thành công", true);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể tạo yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }else if(response.code() == 409){
                    mView.showDialog("Không thể tạo yêu cầu do số điện thoại hoặc email đang có một yêu cầu chưa được hoàn tất.", false);
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
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.editQueueRequest(token, queueRequestID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if(response.code() == 200) {
                    mView.showDialog("Chỉnh sửa thành công", true);
                }else if(response.code() == 500){
                    mView.showDialog("Không thể chỉnh sửa yêu cầu do lỗi hệ thống. Xin vui lòng thử lại!", false);
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
                    mView.showDialog("Không thử thực hiện tác vụ này, có vẻ như có gì đó đã thay đổi với lượt đăng ký!", false);
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
    public void checkInOut(String token, String queueRequestID, String type){
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        mView.showProgressBar();
        if(type.equals("0")) {
            Log.d("6abc", "showprogress 2");
            callAPIService.checkInOut(token, queueRequestID, "0").enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mView.hideProgressBar();
                    if (response.code() == 200) {
                        mView.showDialog("Đón khách thành công", true);
                    } else if (response.code() == 500) {
                        mView.showDialog("Không thể đón khách do lỗi hệ thống. Xin vui lòng thử lại!", false);
                    } else if (response.code() == 403) {
                        mView.showDialog("Bạn không được phép thực hiện tác vụ này!", false);
                    }else if (response.code() == 404) {
                        mView.showDialog("Không thử thực hiện tác vụ này, có vẻ như có gì đó đã thay đổi với lượt đăng ký!", false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    mView.hideProgressBar();
                    mView.showDialog("Không thể kết nối được với máy chủ!", false);
                }
            });
        } else{
            Log.d("6abc", "showprogress 3");
            callAPIService.checkInOut(token, queueRequestID, "1").enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mView.hideProgressBar();
                    if (response.code() == 200) {
                        mView.showDialog("Xác nhận khách dùng xong thành công", true);
                    } else if (response.code() == 500) {
                        mView.showDialog("Không thể đón khách do lỗi hệ thống. Xin vui lòng thử lại!", false);
                    } else if (response.code() == 403) {
                        mView.showDialog("Bạn không được phép thực hiện tác vụ này!", false);
                    }else if (response.code() == 404) {
                        mView.showDialog("Không thử thực hiện tác vụ này, có vẻ như có gì đó đã thay đổi với lượt đăng ký!", false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    mView.hideProgressBar();
                    mView.showDialog("Không thể kết nối được với máy chủ!", false);
                }
            });
        }
    }

    @Override
    public void disconnectSocket(Emitter.Listener onQueueChange){
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("5abc", "socket disconnected");
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
                Log.d("5abc", "socket connected");
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
