package com.playover;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.playover.viewmodels.NotificationsViewModel;

/**
 * Tests the Notifications.java and NotificationsViewModel.java files
 */
@RunWith(RobolectricTestRunner.class)
public class NotificationsTest {
    private FirebaseMessagingService notifications;
    private NotificationsViewModel notificationsViewModel;
    private Context robolectricContext;
    NotificationManager notificationManager;

    @Before
    public void initialize() {
        notifications = new Notifications();
        notificationsViewModel = new NotificationsViewModel();
        robolectricContext = RuntimeEnvironment.application;
        notificationManager = robolectricContext.getSystemService(NotificationManager.class);
    }

    @Test
    public void verifyChannelsCreated() {
        notificationsViewModel.createMyNotificationChannels(robolectricContext);
        NotificationChannel notificationChannel;

        //Check for expected channels
        notificationChannel = notificationManager.getNotificationChannel("buddy_default");
        assertNotNull(notificationChannel);
        assertTrue(notificationChannel.getId().equals("buddy_default"));
        assertTrue(notificationChannel.getName().equals("Buddies normal priority"));
        assertTrue(notificationChannel.getDescription().equals("Your friends and contacts, normal messages"));
        assertTrue(notificationChannel.getImportance() == NotificationManager.IMPORTANCE_DEFAULT);
    }
}
