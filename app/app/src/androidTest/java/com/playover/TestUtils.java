package com.playover;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import java.util.concurrent.CountDownLatch;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class TestUtils {
    public static void rotateScreen(Activity activity) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final int orientation = InstrumentationRegistry.getTargetContext()
                .getResources()
                .getConfiguration()
                .orientation;
        final int newOrientation = (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        activity.setRequestedOrientation(newOrientation);
        getInstrumentation().waitForIdle(new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Screen rotation failed", e);
        }
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}

