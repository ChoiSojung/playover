package com.playover;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.playover.viewmodels.AuthUserViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class GroupMessagingBubblesTest {

    private static AuthUserViewModel authUserViewModel = new AuthUserViewModel();
    private Intent testIntent;
    private Intent testIntent2;
    private String testAccount1 = "jjtest@fake.com";
    private String testAccount2 = "zztest@fake.com";
    private String testAccount3 = "syatwork@gmail.com";
    private String password = "Passw0rd!";

    private Calendar calendar = Calendar.getInstance();
    private TimeZone timeZone = calendar.getTimeZone();
    private DateFormat format = new SimpleDateFormat("dd/MM/yy");
    private String currentDate = format.format(calendar.getTime());

    @Rule
    public ActivityTestRule<MessagingActivity> activityTestRule =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
                @Override
                protected Intent getActivityIntent() {
                    testIntent = new Intent();
                    testIntent.putExtra("recipientUids",
                            "c4C0VrwVkDWpJi8D9d3s2U7cxpr2,dHCtVaM2q9XA7pJ1PGZ3vdhTe1v2");
                    return testIntent;
                }
            };

    @Rule
    public ActivityTestRule<MessagingActivity> activityTestRule2 =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
                @Override
                protected Intent getActivityIntent() {
                    testIntent2 = new Intent();
                    testIntent2.putExtra("recipientUids",
                            "c4C0VrwVkDWpJi8D9d3s2U7cxpr2,Vts6RaoOXmfzipdl3ZG1EzqOMiw2");
                    return testIntent2;
                }
    };

    @Rule
    public ActivityTestRule<MessagingActivity> activityTestRule3 =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
                @Override
                protected Intent getActivityIntent() {
                    testIntent = new Intent();
                    testIntent.putExtra("recipientUids",
                            "Vts6RaoOXmfzipdl3ZG1EzqOMiw2,dHCtVaM2q9XA7pJ1PGZ3vdhTe1v2");
                    return testIntent;
                }
    };

    @Rule
    public ActivityTestRule<MessagingActivity> activityTestRuleForGroupName =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
                @Override
                protected Intent getActivityIntent() {
                    testIntent = new Intent();
                    testIntent.putExtra("recipientUids", "0ZGitIcZ7gbTrAQWUJleHjON1ML2,2oKJ0oESD0VUqz3tkhs1N9FXdbn1");
                    return testIntent;
                }
    };

    @Rule
    public ActivityTestRule<MessagingActivity> activityTestRuleWithFencePostUidsString =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
                @Override
                protected Intent getActivityIntent() {
                    testIntent = new Intent();
                    testIntent.putExtra("recipientUids", ",c4C0VrwVkDWpJi8D9d3s2U7cxpr2,,dHCtVaM2q9XA7pJ1PGZ3vdhTe1v2,");
                    return testIntent;
                }
    };

    private boolean checkForUser() {
        return (authUserViewModel.getUser() != null);
    }

    private void signOutFromMessaging(){
        onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
        onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
    }

    private void signInWithUser(String email, String password){
        onView(withId(R.id.lblogin_main)).perform(MainActivityTest.clickClickableSpan("Sign In"));
        onView(withId(R.id.email_login)).perform(replaceText(email)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password_login)).perform(replaceText(password)).perform(ViewActions.closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
    }

    private  void testSendMessage(ActivityTestRule activityTestRule, Intent intent, String testMessage){

        if (checkForUser()) {
            activityTestRule.launchActivity(intent);
            onView(withId(R.id.msg_type)).perform(typeText(testMessage));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.btn_chat_send)).perform(click());
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(hasDescendant(withText(testMessage))));
            }
        }
    }

    private void testRecieveMessage(ActivityTestRule activityTestRule, Intent intent, String testMessage){

        if (checkForUser()) {
            activityTestRule.launchActivity(intent);
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(hasDescendant(withText(testMessage))));
            }
        }
    }

    @Test
    public void testMessaging()  throws InterruptedException {
        String testMessage = "Hello, everyone.";

        if(checkForUser()){
            testSendMessage(activityTestRule, testIntent, testMessage);
        }


/*
        signOutFromMessaging();
        Thread.sleep(20000);
        signInWithUser(testAccount2, password);
        Thread.sleep(5000);
        */
/*testSendMessage(activityTestRule2, testIntent2, "Hi, Jillian");*//*

        testRecieveMessage(activityTestRule2, testIntent2, testMessage);
*/


/*        Thread.sleep(2000);
        signOutFromMessaging();
        Thread.sleep(2000);
        signInWithUser(testAccount3, password);
        Thread.sleep(5000);
        if (checkForUser()) {
            activityTestRule3.launchActivity(testIntent);
            onView(withId(R.id.msg_type)).perform(typeText("What's up, guys?"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.btn_chat_send)).perform(click());
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
            onView(withId(R.id.msg_type)).perform(typeText("Play is Over, get back to work."));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.btn_chat_send)).perform(click());
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(1).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(1))
                        .check(matches(isDisplayed()));
            }
        }
        Thread.sleep(2000);
        signOutFromMessaging();
        Thread.sleep(2000);
        signInWithUser(testAccount1, password);*/
    }


    @Test
    public void testNewGroupWithDefaultNameWithEmptyGroupName(){
        if (checkForUser()){
            activityTestRuleForGroupName.launchActivity(testIntent);
            try {
                onView(withText("CREATE"))
                        .inRoot(isDialog())
                        .check(matches(isDisplayed()))
                        .perform(click());
                onView(withId(R.id.group_name)).check(matches(withText("Group created on " + currentDate)));
            }
            catch (Exception e) {
                Log.i("The group already exist", " for test new group with default name when group name is empty.");
            }
        }

    }

    @Test
    public void testNewGroupWithDefaultNameWithCancel(){
        if (checkForUser()){
            activityTestRuleForGroupName.launchActivity(testIntent);
            try {
                onView(withText("CANCEL"))
                        .inRoot(isDialog())
                        .check(matches(isDisplayed()))
                        .perform(click());
                onView(withId(R.id.group_name)).check(matches(withText("Group created on " + currentDate)));
            }
            catch (Exception e) {
                Log.i("The group already exist", " for test new group with default name when group name dialog is canceled.");
            }
        }

    }

    @Test
    public void testCreateNewGroup() throws InterruptedException{
        if (checkForUser()){
            activityTestRuleForGroupName.launchActivity(testIntent);
            try {
                String testMessage = "this is a new group";
                onView(withText("CANCEL"))
                        .inRoot(isDialog())
                        .check(matches(isDisplayed()))
                        .perform(click());
                Thread.sleep(2000);
                onView(withId(R.id.msg_type)).perform(typeText(testMessage));
                Espresso.closeSoftKeyboard();
                onView(withId(R.id.btn_chat_send)).perform(click());
                if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                    onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                            .check(matches(hasDescendant(withText(testMessage))));
                }
            }
            catch (Exception e) {
                    Log.i("The group already exist", " for test new group with default name when group name is empty.");
                }
        }
    }

/*    @Test
    public void testNewGroupCustomName() throws InterruptedException{
        activityTestRule4.launchActivity(testIntent);
        Thread.sleep(2000);
        typeText("Test Group smallTalk");
        Espresso.closeSoftKeyboard();
        onView(withText("CREATE"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.group_name)).check(matches(withText("Test Group smallTalk")));
    }*/

    @Test
    public void testMessagingWithContainminatentent(){
        String testMessage = "Is this cleaned?";
        if (checkForUser()){
            testSendMessage(activityTestRuleWithFencePostUidsString, testIntent, testMessage);
        }
    }

 /*   @Test
    public void testMessagingDrawerMessaging() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_messaging));
            Espresso.pressBack();
        }
    }
    @Test
    public void testMessagingDrawerProfile() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
            Espresso.pressBack();
        }
    }
    @Test
    public void testMessagingDrawerCheckin() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
            Espresso.pressBack();
        }
    }
    @Test
    public void testMessagingDrawerSettings() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
            Espresso.pressBack();
        }
    }
    @Test
    public void testMessagingDrawerBuddies() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            Espresso.pressBack();
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Espresso.pressBack();
        }
    }
    @Test
    public void testMessagingDrawerDiscounts() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Espresso.pressBack();
        }
    }

    @Test
    public void testMessagingDrawerSignOut() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            Thread.sleep(2000);
            signOutFromMessaging();
            Thread.sleep(2000);
            signInWithUser(testAccount1,password);
            Thread.sleep(5000);
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
        }
    }*/

}
