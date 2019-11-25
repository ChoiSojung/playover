package com.playover;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.playover.viewmodels.AuthUserViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MessagingBubblesTest {

    private static AuthUserViewModel authUserViewModel = new AuthUserViewModel();
    private Intent testIntent;

    @Rule public ActivityTestRule<MessagingActivity> activityTestRule =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
            @Override
            protected Intent getActivityIntent() {
            testIntent = new Intent();
            testIntent.putExtra("recipientUids", "c4C0VrwVkDWpJi8D9d3s2U7cxpr2");
            return testIntent;
        }
    };

    @Rule public ActivityTestRule<MessagingActivity> activityTestRuleForDefaultGroupName =
            new ActivityTestRule<MessagingActivity>(MessagingActivity.class, true, false) {
                @Override
                protected Intent getActivityIntent() {
                    testIntent = new Intent();
                    testIntent.putExtra("recipientUids", "0ZGitIcZ7gbTrAQWUJleHjON1ML2");
                    return testIntent;
                }
            };

    private boolean checkForUser() {
        return (authUserViewModel.getUser() != null);
    }

/*    @Test
    public void testMessaging() {
        if (checkForUser()) {
            activityTestRule.launchActivity(testIntent);
            onView(withId(R.id.msg_type)).perform(typeText("Hello, There"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.btn_chat_send)).perform(click());
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
            onView(withId(R.id.msg_type)).perform(typeText("Howdy"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.btn_chat_send)).perform(click());
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(1).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(1))
                        .check(matches(isDisplayed()));
            }
        }
    }*/

    @Test
    public void testNewThreadWithDefaultName(){
        activityTestRuleForDefaultGroupName.launchActivity(testIntent);
        onView(withId(R.id.group_name)).check(matches(withText("Jillian Joyce and Flight Crew")));
    }

 /*   @Test
    public void testEmptyMessageSend(){
        activityTestRule.launchActivity(testIntent);
        onView(withId(R.id.btn_chat_send)).perform(click());
        onView(withText(R.string.message)).inRoot(new ToastMatcher()).check(matches(withText("Please input a message")));
    }*/

 /*   @Test
    public void testCreateNewThread() throws InterruptedException{
        activityTestRuleForDefaultGroupName.launchActivity(testIntent);
        String testMessage = "this is a new group";
        Thread.sleep(2000);
        onView(withId(R.id.msg_type)).perform(typeText(testMessage));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_chat_send)).perform(click());
        if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
            onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                    .check(matches(hasDescendant(withText(testMessage))));
        }
    }*/

    @Test
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
            onView(withId(R.id.messaging_content)).check(matches(isDisplayed()));
            onView(withId(R.id.messaging_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_messaging)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);
            onView(withId(R.id.lblogin_main)).perform(MainActivityTest.clickClickableSpan("Sign In"));
            // old account melsmail@hotmail.com
            onView(withId(R.id.email_login)).perform(replaceText("jjtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            //test123
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
        }
    }

}
