package com.playover;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.playover.viewmodels.NotificationsViewModel;
import com.playover.viewmodels.AuthUserViewModel;

public class Notifications extends FirebaseMessagingService {
    private final String TAG = "Notifications.java";
    private NotificationsViewModel notificationsVM;

    public Notifications() {
        super();
        notificationsVM = new NotificationsViewModel();
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "New FCM instance ID" + token);
        AuthUserViewModel authVM = new AuthUserViewModel();
        if(authVM == null) {
            Log.e(TAG, "Signed in user unavailable.");
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
}
