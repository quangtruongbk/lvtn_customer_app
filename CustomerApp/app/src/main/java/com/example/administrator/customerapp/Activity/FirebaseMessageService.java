package com.example.administrator.customerapp.Activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.administrator.customerapp.Model.Account;
import com.example.administrator.customerapp.Model.QueueRequest;
import com.example.administrator.customerapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Listens for squawk FCM messages both in the background and the foreground and responds
 * appropriately
 * depending on type of message
 */
public class FirebaseMessageService extends FirebaseMessagingService {

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private NotificationManager mNotificationManager;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        Account account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        if (data.get("type").equals("cancel")) {
            String requestListString = data.get("request");
            if (requestListString != null && !requestListString.equals("") && account != null) {
                String[] parts = requestListString.split(",");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals(account.getId())) {
                        if (i <= 4) {
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                                    PendingIntent.FLAG_ONE_SHOT);
                            createNotificationChannel();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "6abc")
                                    .setSmallIcon(R.drawable.queue)
                                    .setContentTitle("Có ai đó rời khỏi hàng đợi")
                                    .setContentText("Còn " + i + " người nữa là tới lượt bạn. Có vẻ như bạn đã sắp tới lượt rồi đó. Hãy chú ý theo dõi nhé!")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                            notificationManager.notify(0, builder.build());
                        }
                        break;
                    }
                }
            }
        } else if (data.get("type").equals("checkIn")) {
            String requestListString = data.get("request");
            if (requestListString != null && !requestListString.equals("") && account != null) {
                String[] parts = requestListString.split(",");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals(account.getId())) {
                        if (i <= 4) {
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                                    PendingIntent.FLAG_ONE_SHOT);
                            createNotificationChannel();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "6abc")
                                    .setSmallIcon(R.drawable.queue)
                                    .setContentTitle("Gần tới lượt")
                                    .setContentText("Còn " + i + " người nữa là tới lượt bạn. Có vẻ như bạn đã sắp tới lượt rồi đó. Hãy chú ý theo dõi nhé!")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                            notificationManager.notify(0, builder.build());
                        }
                        break;
                    }
                }
            }
        }else if (data.get("type").equals("notbusyqueue")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "6abc")
                    .setSmallIcon(R.drawable.queue)
                    .setContentTitle("Hàng đợi đã vắng")
                    .setContentText("Hàng đợi đã vắng. Ngay lúc này, bạn đã có thể sử dụng dịch vụ!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(0, builder.build());
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("6abc", "OnQueueChange", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
