package com.playover;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class SelectedHotelTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void testCheckOut() throws Exception {
        Thread.sleep(5000);
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);
            onView(withId(R.id.email_login)).perform(replaceText("hotelTest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.recycler_view_hotels))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withText("Confirm")).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            onView(withId(R.id.txtCheckoutSet)).perform(click());
        } catch (Exception ex) {
            onView(withId(R.id.lblogin_main)).perform(MainActivityTest.clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(replaceText("hotelTest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.recycler_view_hotels))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withText("Confirm")).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            onView(withId(R.id.txtCheckoutSet)).perform(click());
            }
        }
    }