package com.playover;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SettingsDrawerTest {

    @Rule
    public ActivityTestRule<SettingsActivity> activityTestRule
            = new ActivityTestRule<>(SettingsActivity.class);


    @Test
    public void test_navigation_view() throws InterruptedException {
        Thread.sleep(2000);

        onView(withId(R.id.settings_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_settings)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        Thread.sleep(1000);

        onView(withId(R.id.settings_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_settings)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
        Thread.sleep(1000);

        onView(withId(R.id.profile_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        Thread.sleep(1000);

        onView(withId(R.id.settings_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_settings)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
        Thread.sleep(1000);

        onView(withId(R.id.main_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        Thread.sleep(1000);

        onView(withId(R.id.settings_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_settings)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
        Thread.sleep(1000);

        onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        Thread.sleep(1000);

        onView(withId(R.id.settings_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_settings)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
        Thread.sleep(1000);

        onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        Thread.sleep(1000);

        onView(withId(R.id.settings_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_settings)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));

    }
}
