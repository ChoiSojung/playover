package com.playover;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.playover.viewmodels.AuthUserViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.uiautomator.UiDevice.getInstance;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CheckInDialogTest {
    private static AuthUserViewModel authUserViewModel;
    private static final String PLAYOVER_SAMPLE_PACKAGE
            = "com.playover";

    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen(){
        //Initialize UiDevice instance
        mDevice = getInstance(getInstrumentation());

        //Start from home screen
        mDevice.pressHome();

        //Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        //Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(PLAYOVER_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        //Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PLAYOVER_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
        authUserViewModel = new AuthUserViewModel();
    }


    @Test
    public void checkPreconditions(){
        assertThat(mDevice, notNullValue());
    }


    @Test
    public void testCheckInDialogLaunch_sameActivity() throws UiObjectNotFoundException {
        if (checkForUser()) {
            try {
                UiObject checkOut = new UiObject(new UiSelector().text("CHECK OUT"));
                checkOut.click();
            } catch (UiObjectNotFoundException e) {

            } finally {
                UiCollection hotels = new UiCollection(new UiSelector()
                        .className("android.widget.LinearLayout"));
                UiObject hotel = hotels.getChild(new UiSelector().index(0));
                hotel.click();
                UiObject2 confirm = mDevice.findObject(By.text("CONFIRM"));
                confirm.click();
            }

        }
    }

    @Test
    public void testCheckInDialogCheckOutReminderNotification() throws UiObjectNotFoundException{
        if (checkForUser()) {
            try {
                UiObject checkOut = new UiObject(new UiSelector().text("CHECK OUT"));
                checkOut.click();
            } catch (UiObjectNotFoundException e) {

            } finally {

                UiCollection hotels = new UiCollection(new UiSelector()
                        .className("android.widget.LinearLayout"));
                UiObject hotel = hotels.getChild(new UiSelector().index(0));
                hotel.click();
                UiObject confirm = new UiObject(new UiSelector().text("CONFIRM"));
                confirm.click();

                UiDevice device = mDevice.getInstance(InstrumentationRegistry.getInstrumentation());
                device.openNotification();
                device.wait(Until.hasObject(By.textStartsWith("Playover")), 1000);
                UiObject notificationTitle = new UiObject(new UiSelector().text("Scheduled Checkout"));
                assertEquals("Scheduled Checkout", notificationTitle.getText());
            }
        }
    }



    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    //helper
    private void clearAllNotifications() throws UiObjectNotFoundException{
        UiDevice device = mDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.textStartsWith("Playover")), 1000);

    }

    //helper
    private boolean checkForUser() {
        return (authUserViewModel.getUser() != null);
    }

}
