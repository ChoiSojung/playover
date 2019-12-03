package app.playover.viewmodels;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import app.playover.datamodels.UserDataModel;
import app.playover.models.Person;
import app.playover.R;

import java.util.ArrayList;
import java.util.function.Consumer;

public class NotificationsViewModel {

    private String TAG = "NotificationsViewModel";
    private UserDataModel userDataModel;

    // constructor
    public NotificationsViewModel() {
        userDataModel = new UserDataModel();
    }

    public void setFCMinstanceID(String uId, String FCMinstanceID, OnCompleteListener<Void>activityCallBack){
        userDataModel.updateFBinstanceID(uId, FCMinstanceID, activityCallBack);
    }

    //temporary hack, get whole Person object and pluck FCMinstanceID from that
    public void getFCMinstanceID(String uId, Consumer<String> responseCallback) {
        userDataModel.getUser(uId,
                (DataSnapshot dataSnapshot) -> {
                    Person user = new Person();
                    for (DataSnapshot matchesSnapshot : dataSnapshot.getChildren()) {
                        user = matchesSnapshot.getValue(Person.class);
                    }
                    responseCallback.accept(user.getFBinstanceID());
                },
                (databaseError -> System.out.println("Error getting user: " + databaseError))
        );
    }

    /**
     * Notification channels plan: Combinations of Scope (who) and Importance (what)
     * Scopes: Buddy (contact), Channel (group), Crew (workmates), Organization (domain),
     * Global (all users)
     * Importance: Default (most messages), Priority (work information),
     * Emergency (tornado, flood, etc...)
     * Some of these won't be used initially, and a placeholder comment will be present
     */
    public void createMyNotificationChannels(Context context) {
        String channelID;
        CharSequence channelName;
        String channelDescription;
        int channelImportance;
        NotificationChannel nextChannel;
        ArrayList<NotificationChannel> channels = new ArrayList<>();

        if(context == null) {
            Log.e(TAG, "Did not receive context from construction. Cannot access R.");
            return;
        }

        // Notification channels added at API 26 (Oreo)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Buddy_Default
            channelID = context.getString(R.string.buddy_default_id);
            channelName = context.getString(R.string.buddy_default_name);
            channelDescription = context.getString(R.string.buddy_default_desc);
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Buddy_Priority
            channelID = context.getString(R.string.buddy_priority_id);
            channelName = context.getString(R.string.buddy_priority_name);
            channelDescription = context.getString(R.string.buddy_priority_desc);
            channelImportance = NotificationManager.IMPORTANCE_HIGH;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Buddy_Emergency
            channelID = context.getString(R.string.buddy_emergency_id);
            channelName = context.getString(R.string.buddy_emergency_name);
            channelDescription = context.getString(R.string.buddy_emergency_desc);
            channelImportance = NotificationManager.IMPORTANCE_MAX;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Channel_Default
            channelID = context.getString(R.string.channel_default_id);
            channelName = context.getString(R.string.channel_default_name);
            channelDescription = context.getString(R.string.channel_default_desc);
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Channel_Priority
            channelID = context.getString(R.string.channel_priority_id);
            channelName = context.getString(R.string.channel_priority_name);
            channelDescription = context.getString(R.string.channel_priority_desc);
            channelImportance = NotificationManager.IMPORTANCE_HIGH;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Channel_Emergency
            channelID = context.getString(R.string.channel_emergency_id);
            channelName = context.getString(R.string.channel_emergency_name);
            channelDescription = context.getString(R.string.channel_emergency_desc);
            channelImportance = NotificationManager.IMPORTANCE_MAX;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Crew_Default
            channelID = context.getString(R.string.crew_default_id);
            channelName = context.getString(R.string.crew_default_name);
            channelDescription = context.getString(R.string.crew_default_desc);
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Crew_Priority
            channelID = context.getString(R.string.crew_priority_id);
            channelName = context.getString(R.string.crew_priority_name);
            channelDescription = context.getString(R.string.crew_priority_desc);
            channelImportance = NotificationManager.IMPORTANCE_HIGH;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Crew_Emergency
            channelID = context.getString(R.string.crew_emergency_id);
            channelName = context.getString(R.string.crew_emergency_name);
            channelDescription = context.getString(R.string.crew_emergency_desc);
            channelImportance = NotificationManager.IMPORTANCE_MAX;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Global_Default
            channelID = context.getString(R.string.global_default_id);
            channelName = context.getString(R.string.global_default_name);
            channelDescription = context.getString(R.string.global_default_desc);
            channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            /*
            // Global_Priority
            channelID = myContext.getString(R.string.global_priority_id);
            channelName = myContext.getString(R.string.global_priority_name);
            channelDescription = myContext.getString(R.string.global_priority_desc);
            channelImportance = NotificationManager.IMPORTANCE_HIGH;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);

            // Global_Emergency
            channelID = myContext.getString(R.string.global_emergency_id);
            channelName = myContext.getString(R.string.global_emergency_name);
            channelDescription = myContext.getString(R.string.global_emergency_desc);
            channelImportance = NotificationManager.IMPORTANCE_MAX;
            nextChannel = new NotificationChannel(channelID, channelName, channelImportance);
            nextChannel.setDescription(channelDescription);
            channels.add(nextChannel);
            */

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            try {
                notificationManager.createNotificationChannels(channels);
            } catch (Exception e) {
                Log.e(TAG, "Failed to create notification channels" + e.toString());
            }
        }
    }
}
