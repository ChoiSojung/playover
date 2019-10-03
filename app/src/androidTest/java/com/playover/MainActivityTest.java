package com.playover;

import android.Manifest;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;


public class MainActivityTest {

    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    //can open app and see the landing page
    @Test
    public void canSeeLanding() throws InterruptedException {
        Thread.sleep(5000);
         try {
             onView(withId(R.id.main_content)).check(matches(isDisplayed()));
             onView(withId(R.id.main_content)).perform(DrawerActions.open());
             onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
             Thread.sleep(5000);
             onView(withId(R.id.lblfeature_main))
                     .check(matches(withText("Features")));
         }
            //else perform test
        catch (Exception ex) {
                onView(withId(R.id.lblfeature_main))
                        .check(matches(withText("Features")));
            }

    }



    //can open app and see first name on profile
    @Test
    public void canSeeFirstName() throws InterruptedException {
        Thread.sleep(5000);
        //if test client is already signed in. sign out and perform test
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(replaceText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
            Thread.sleep(5000);
            onView(withText("Randall Hacker")).check(matches(isDisplayed()));
        }
        //else perform test
        catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(replaceText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
            Thread.sleep(5000);
            onView(withText("Randall Hacker")).check(matches(isDisplayed()));
        }
    }


    //can navigate to login fragment, login and go to check in
    @Test
    public void canGoToLogIn() throws InterruptedException {
        Thread.sleep(5000);
        //if test client is already signed in. sign out and perform test
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            //not valid
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(replaceText("mvie")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            //shouldn't exist
            onView(withId(R.id.email_login)).perform(replaceText("mviencek@msn.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            //valid
            onView(withId(R.id.email_login)).perform(replaceText("mviencek@hotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(10000);
            if (withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
            Espresso.pressBack();

        }
        //else perform test
        catch (Exception ex) {
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            //not valid
            onView(withId(R.id.email_login)).perform(replaceText("mvie")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            //shouldn't exist
            onView(withId(R.id.email_login)).perform(replaceText("mviencek@msn.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            //exists
            onView(withId(R.id.email_login)).perform(replaceText("mviencek@hotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(10000);
            if (withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
            Espresso.pressBack();
        }

    }

    @Test
    public void dataUseAndTerms() throws InterruptedException {
        Thread.sleep(5000);
        //if test client is already signed in. sign out and perform test
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.signup_button_main)).perform(click());
            onView(withId(R.id.termsSignUp)).perform(clickClickableSpan("data use policy"));
            Thread.sleep(2000);
            Espresso.pressBack();
            onView(withId(R.id.termsSignUp)).perform(clickClickableSpan("terms"));
            Thread.sleep(2000);
            Espresso.pressBack();

        } catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.signup_button_main)).perform(click());
            onView(withId(R.id.termsSignUp)).perform(clickClickableSpan("data use policy"));
            Thread.sleep(2000);
            Espresso.pressBack();
            onView(withId(R.id.termsSignUp)).perform(clickClickableSpan("terms"));
            Thread.sleep(2000);
            Espresso.pressBack();
        }

    }

    //helper method
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    //can click the reset password textview and send reset password email
    @Test
    public void canResetPassword() throws InterruptedException {
        //if test client is already signed in. sign out and perform test
        try {
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            Thread.sleep(2000);
            onView(withId(R.id.forget_main)).perform(clickClickableSpan("Reset"));
            Thread.sleep(5000);
            onView(withId(R.id.email_reset)).perform(replaceText("test@msn.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_reset)).perform(click());
            Thread.sleep(10000);
            onView(withId(R.id.email_reset)).perform(replaceText("mikeviencek@gmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_reset)).perform(click());
            Thread.sleep(10000);
            onView(withId(R.id.email_reset)).check(matches(isDisplayed()));
        }
        //else perform test
        catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            Thread.sleep(2000);
            onView(withId(R.id.forget_main)).perform(clickClickableSpan("Reset"));
            Thread.sleep(5000);
            onView(withId(R.id.email_reset)).perform(replaceText("test@msn.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_reset)).perform(click());
            Thread.sleep(10000);
            onView(withId(R.id.email_reset)).perform(replaceText("mikeviencek@gmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_reset)).perform(click());
            Thread.sleep(10000);
            onView(withId(R.id.email_reset)).check(matches(isDisplayed()));
        }

    }

    //can go through sign up and then request a new validation code on the validation fragment
    //can not test validation code part.  no way to get code emailed to user
    @Test
    public void signUpAndResendValidationCode() throws InterruptedException {
        //if test client is already signed in. sign out and perform test
        try {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.signup_button_main)).perform(click());
            Thread.sleep(5000);
            onView(isRoot()).perform(pressBack());
            onView(withId(R.id.lblfeature_main)).check(matches(isDisplayed()));
            onView(withId(R.id.signup_button_main)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.buttonSignUp)).perform(click());
            onView(withId(R.id.firstNameSignUp)).perform(replaceText("Developer")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.lastNameSignUp)).perform(replaceText("Test")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.positionSignUp)).perform(replaceText("Application Developer")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.emailSignUp)).perform(replaceText("mviencekhotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.passwordSignUp)).perform(replaceText("123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            onView(withId(R.id.emailSignUp)).perform(replaceText("mviencek@hotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.passwordSignUp)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.passwordSignUp)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.verify)).perform(click());
            onView(withId(R.id.verificationEdit)).perform(replaceText("1234")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.verificationEdit)).perform(replaceText("12345")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.verificationEdit)).perform(replaceText("12")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.verificationEdit)).perform(replaceText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.resendVerification)).perform(click());
            onView(isRoot()).perform(pressBack());
            Thread.sleep(8000);
            onView(withId(R.id.buttonSignUp))
                    .check(matches(isDisplayed()));
        }
        //else perform test
        catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.signup_button_main)).perform(click());
            Thread.sleep(5000);
            onView(isRoot()).perform(pressBack());
            onView(withId(R.id.lblfeature_main)).check(matches(isDisplayed()));
            onView(withId(R.id.signup_button_main)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.buttonSignUp)).perform(click());
            onView(withId(R.id.firstNameSignUp)).perform(replaceText("Developer")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.lastNameSignUp)).perform(replaceText("Test")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.positionSignUp)).perform(replaceText("Application Developer")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.emailSignUp)).perform(replaceText("mviencekhotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.passwordSignUp)).perform(replaceText("123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            onView(withId(R.id.emailSignUp)).perform(replaceText("mviencek@hotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.passwordSignUp)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.passwordSignUp)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verification_Password)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.buttonSignUp)).perform(click());
            Thread.sleep(5000);
            onView(withId(R.id.verify)).perform(click());
            onView(withId(R.id.verificationEdit)).perform(replaceText("1234")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.verificationEdit)).perform(replaceText("12345")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.verificationEdit)).perform(replaceText("12")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.verificationEdit)).perform(replaceText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.verify)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.resendVerification)).perform(click());
            onView(isRoot()).perform(pressBack());
            Thread.sleep(8000);
            onView(withId(R.id.buttonSignUp))
                    .check(matches(isDisplayed()));
        }
    }

    //static method for clicking on clickable spans in textviews
    public static ViewAction clickClickableSpan(final CharSequence textToClick) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.instanceOf(TextView.class);
            }

            @Override
            public String getDescription() {
                return "clicking on a ClickableSpan";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                SpannableString spannableString = (SpannableString) textView.getText();

                if (spannableString.length() == 0) {
                    // TextView is empty, nothing to do
                    throw new NoMatchingViewException.Builder()
                            .includeViewHierarchy(true)
                            .withRootView(textView)
                            .build();
                }

                // Get the links inside the TextView and check if we find textToClick
                ClickableSpan[] spans = spannableString.getSpans(0, spannableString.length(), ClickableSpan.class);
                if (spans.length > 0) {
                    ClickableSpan spanCandidate;
                    for (ClickableSpan span : spans) {
                        spanCandidate = span;
                        int start = spannableString.getSpanStart(spanCandidate);
                        int end = spannableString.getSpanEnd(spanCandidate);
                        CharSequence sequence = spannableString.subSequence(start, end);
                        if (textToClick.toString().equals(sequence.toString())) {
                            span.onClick(textView);
                            return;
                        }
                    }
                }

                // textToClick not found in TextView
                throw new NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(textView)
                        .build();

            }
        };
    }
}