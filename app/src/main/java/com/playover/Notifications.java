package com.playover;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.playover.viewmodels.NotificationsViewModel;
import com.playover.viewmodels.AuthUserViewModel;

import java.util.Map;

public class Notifications extends FirebaseMessagingService {
    private final String TAG = "Notifications.java";
    private NotificationsViewModel notificationsVM;

    public Notifications() {
        super();
        notificationsVM = new NotificationsViewModel();
    }

    @Override
    public void onDeletedMessages() {
        Log.i(TAG, "Excess messages received or app not connected for a month, " +
                "messages deleted in FCM");
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map<String, String> messageData = message.getData();
        //Log.i(TAG, messageData.toString());
        String from = message.getNotification().getTitle();
        String msgTxt = message.getNotification().getBody();

        Intent intent = new Intent(this, MessagingActivity.class);
        if(messageData.containsKey("recipientUid")) {
            intent.putExtra("recipientUid", messageData.get("recipientUid"));
        }
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                getApplicationContext().getString(R.string.buddy_default_id))
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(from)
                .setContentText(msgTxt)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        int sequence;
        try {
            sequence = Integer.parseInt(messageData.get("msgSequence").trim());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            sequence = 1;
        }
        notificationManager.notify(sequence, notification);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "New FCM instance ID" + token);
        AuthUserViewModel authVM = new AuthUserViewModel();
        if(authVM == null) {
            Log.e(TAG, "Signed in user unavailable.");
            return;
        }
        if(authVM.getUser() == null) {
            Log.e(TAG, "User not found");
            return;
        }
        String uId = authVM.getUser().getUid();
        if(uId == null || uId.equals("")) {
            Log.e(TAG, "Failed to find uId.");
            return;
        }
        notificationsVM.setFCMinstanceID(uId, token, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Successfully set FCMinstanceID " + token);
                } else {
                    if (task.getException() != null)
                        Log.d(TAG, "Failed to set FCMinstanceID: " + task.getException());
                }
            }
        });
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        Log.e(TAG, "FCM error. ID: " + msgId + "Exception: " + exception.toString());
    }
}
