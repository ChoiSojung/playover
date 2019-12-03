package com.playover;


import android.Manifest;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class FrancescoLogin {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);


    @Test
    public void loginUser() throws InterruptedException {
        Thread.sleep(3000);
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);
            onView(withId(R.id.email_login)).perform(replaceText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(3000);
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
        } catch (Exception ex) {
            onView(withId(R.id.lblogin_main)).perform(MainActivityTest.clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(replaceText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(3000);
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
        }

    }
}
