package com.playover;


import android.Manifest;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.playover.MainActivityTest.withRecyclerView;
import static com.playover.ProfileTest.clickClickableSpan;

@RunWith(AndroidJUnit4.class)
public class BuddyTest2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void buddyRecycleViewer() throws InterruptedException {
        Thread.sleep(5000);
        try {
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("devonm@northseattle.edu")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(5000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPosition(0)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.profileBuddyStar)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.profileBuddyStar)).perform(click());
            Thread.sleep(2000);
            Espresso.pressBack();
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPositionOnView(0, R.id.checkbox))
                    .perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.message_sel)).perform(click());
            Thread.sleep(2000);
            Espresso.pressBack();

        } catch (Exception ex) {
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("devonm@northseattle.edu")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(5000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPosition(0)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.profileBuddyStar)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.profileBuddyStar)).perform(click());
            Thread.sleep(2000);
            Espresso.pressBack();
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPositionOnView(0, R.id.checkbox))
                    .perform(click());
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPositionOnView(0, R.id.checkbox))
                    .perform(click());
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPositionOnView(1, R.id.checkbox))
                    .perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.message_sel)).perform(click());
            Thread.sleep(2000);
            Espresso.pressBack();
        }

    }

    @Test
    public void testDrawerBuddies() throws InterruptedException {
        try {
            Thread.sleep(3000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(3000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("devonm@northseattle.edu")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            Espresso.pressBack();
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(3000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
            Thread.sleep(3000);
            onView(withId(R.id.profile_content)).check(matches(isDisplayed()));
            onView(withId(R.id.profile_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
            Thread.sleep(3000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
        } catch (Exception ex) {
            Thread.sleep(3000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("devonm@northseattle.edu")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            Espresso.pressBack();
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(3000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
            Thread.sleep(3000);
            onView(withId(R.id.profile_content)).check(matches(isDisplayed()));
            onView(withId(R.id.profile_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
            Thread.sleep(3000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(3000);
            onView(withId(R.id.buddies_content)).check(matches(isDisplayed()));
            onView(withId(R.id.buddies_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_buddies)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));

        }
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
