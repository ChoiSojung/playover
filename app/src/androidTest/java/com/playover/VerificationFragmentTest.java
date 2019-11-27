package com.playover;

import android.Manifest;
import android.support.test.espresso.Espresso;
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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class VerificationFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);


    @Test
    public void testVerificationCodeFailed() throws InterruptedException {
        Thread.sleep(5000);
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);
            verificationCodeTest();
        }
        catch (Exception ex) {
            verificationCodeTest();
        }

    }

    private void verificationCodeTest() throws InterruptedException {
        onView(withId(R.id.signup_button_main)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.firstNameSignUp)).perform(typeText("testFirst"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.lastNameSignUp)).perform(typeText("testLast"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.positionSignUp)).perform(typeText("testPosition"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.emailSignUp)).perform(typeText("test@testemail.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordSignUp)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.verification_Password)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.verificationEdit)).perform(typeText("987654321"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.verify)).perform(click());
        onView(withText("Verification Code Does Not Match!"))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
}
