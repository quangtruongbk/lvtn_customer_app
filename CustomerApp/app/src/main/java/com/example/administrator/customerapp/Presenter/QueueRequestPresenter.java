package com.example.administrator.customerapp.Presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.customerapp.Activity.FirebaseMessageService;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class QueueRequestPresenter implements QueueRequestContract.Presenter {
    private RetrofitInterface callAPIService;
    private QueueRequestContract.View mView;
    private Socket mSocket;
    private String queueID;

    public QueueRequestPresenter(@NonNull QueueRequestContract.View mView, Socket mSocket, String queueID) {
        this.mView = mView;
        this.mSocket = mSocket;
        this.queueID = queueID;
    }

    /***************************************************
     Function: getQueueRequestFromServer
     Creator: Quang Truong
     Description: Get list of an queue request from QueueID
     *************************************************/
    @Override
    public void getQueueRequestFromServer(String queueID) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getQueueRequest(queueID, "0").enqueue(new Callback<ArrayList<QueueRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<QueueRequest>> call, Response<ArrayList<QueueRequest>> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    ArrayList<QueueRequest> newQueueRequest = new ArrayList<QueueRequest>();
                    newQueueRequest = response.body();
                    if (newQueueRequest != null) {
                        mView.setUpAdapter(newQueueRequest);
                    }
                } else if (response.code() == 500) {
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

    /***************************************************
     Function: createQueueRequest
     Creator: Quang Truong
     Description: Create a new QUeue Request
     *************************************************/
    @Override
    public void createQueueRequest(String token, String accountID, final String queueID, String name, String phone, String email) {
        Log.d("6abc", "createQueueRequest");
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.createQueueRequest(token, accountID, queueID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    //Log.d("6abc", "Create Request:P " + response.body());
                    mView.showDialog("Tạo lượt đăng ký thành công", true);
                    FirebaseMessaging.getInstance().subscribeToTopic(queueID)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        mView.showDialog("Lỗi ở Firebase Cloud Messasge", false);
                                    }
                                }
                            });
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể tạo lượt đăng ký do lỗi hệ thống. Xin vui lòng thử lại!", false);
                } else if (response.code() == 409) {
                    mView.showDialog("Không thể tạo lượt đăng ký do bạn đang có một lượt đăng ký còn chờ. Hãy kiểm tra tại: Đăng ký hiện tại của tôi.", false);
                } else if (response.code() == 503) {
                    mView.showDialog("Rất tiếc, hàng đợi đã đầy.", false);
                } else if (response.code() == 533) {
                    mView.showDialog("Rất tiếc, hàng đợi đã ngừng nhận khách.", false);
                } else if (response.code() == 403) {
                    mView.showDialog("Bạn không được phép thực hiện tác vụ này!", false);
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

    /***************************************************
     Function: editQueueRequest
     Creator: Quang Truong
     Description: Edit a QUeue Request
     *************************************************/
    @Override
    public void editQueueRequest(String token, String queueRequestID, String name, String phone, String email) {
        Log.d("6abc", "editQueueRequest");
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.editQueueRequest(token, queueRequestID, name, phone, email).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Chỉnh sửa thành công", true);
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể chỉnh sửa lượt đăng ký do lỗi hệ thống. Xin vui lòng thử lại!", false);
                } else if (response.code() == 404) {
                    mView.showDialog("Không thử thực hiện tác vụ này, có vẻ như có gì đó đã thay đổi với lượt đăng ký!", false);
                }
                else if (response.code() == 409) {
                    mView.showDialog("Thay đổi thất bại do email hoặc số điện thoại đã tồn tại một lượt đăng ký còn chờ!", false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: cancelQueueRequest
     Creator: Quang Truong
     Description: Cancel a request
     *************************************************/
    @Override
    public void cancelQueueRequest(String token, final String queueID, String queueRequestID) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.cancelQueueRequest(token, queueID, queueRequestID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Hủy lượt đăng ký thành công", true);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(queueID)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        mView.showDialog("Lỗi ở Firebase Cloud Messasge", false);
                                    }
                                }
                            });
                    mView.hideCountDown();
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể hủy lượt đăng ký do lỗi hệ thống. Xin vui lòng thử lại!", false);
                } else if (response.code() == 404) {
                    mView.showDialog("Không thể hủy lượt đăng ký do thông tin về lượt đăng ký đã thay đổi.", false);
                } else if (response.code() == 403) {
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
    public void disconnectSocket(Emitter.Listener onQueueChange) {
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
    public void listeningSocket(Emitter.Listener onQueueChange) {
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
