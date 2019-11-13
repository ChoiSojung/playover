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
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SingUpTest {
    @Rule
    public ActivityTestRule <MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant( Manifest.permission.ACCESS_COARSE_LOCATION);


    @Test
    public void SignUpAUser() throws InterruptedException {
        Thread.sleep(5000);
        try {
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.signup_button_main)).perform(click());
            onView(isRoot()).perform(pressBack());
            onView(withId(R.id.lblfeature_main)).check(matches(isDisplayed()));
            onView(withId(R.id.signup_button_main)).perform(click());
            //onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            //onView(withId(R.id.main_content)).perform(DrawerActions.open());
            //onView(withId(R.id.nav_view)).perform( NavigationViewActions.navigateTo(R.id.nav_sign_out));
            //onView(withId(R.id.signup_button_main)).perform(click());
            //onView(withId(R.id.wv_login)).check(matches(isCompletelyDisplayed()));
            //onView(withId(R.id.lblfeature_main)).check(matches(isDisplayed()));
            //onView(withId(R.id.signup_button_main)).perform( DrawerActions.open());
            //onView(withId(R.id.nav_view)).perform( NavigationViewActions.navigateTo(R.id.nav_sign_out));
            onView(withId(R.id.firstNameSignUp)).perform(replaceText("Play")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.lastNameSignUp)).perform(replaceText("over")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.positionSignUp)).perform(replaceText("piolt")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.emailSignUp)).perform(replaceText("playover@alaskaair.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.passwordSignUp)).perform(replaceText("test321")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("test321")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            Thread.sleep(5000);

        }
        catch (Exception ex) {
            onView(withId(R.id.signup_button_main)).perform(click());
            onView(isRoot()).perform(pressBack());
            onView(withId(R.id.lblfeature_main)).check(matches(isDisplayed()));
            onView(withId(R.id.signup_button_main)).perform(click());
           // onView(withId(R.id.signup_button_main)).perform(MainActivityTest.clickClickableSpan("Sign Up"));
            onView(withId(R.id.firstNameSignUp)).perform(replaceText("Play")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.lastNameSignUp)).perform(replaceText("over")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.positionSignUp)).perform(replaceText("piolt")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.emailSignUp)).perform(replaceText("playover@alaskaair.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.passwordSignUp)).perform(replaceText("test321")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("test321")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            Thread.sleep(5000);

        }
    }

}
