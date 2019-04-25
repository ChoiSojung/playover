package com.playover;

import android.Manifest;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.playover.viewmodels.AuthUserViewModel;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.playover.MainActivityTest.clickClickableSpan;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class BuddiesTest {

    ;
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void canMakeBuddies() throws InterruptedException {
        Thread.sleep(5000);
        try {
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("devonm996r@hotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("123456")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);


            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(1000);
            // onView(withId(R.id.buddies_list)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
            Thread.sleep(3000);
            onView(withId(R.id.recycler_view))
                    .check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.recycler_view).atPosition(0))
                    .perform(click());
            onView(withId(R.id.profile)).check(matches(isDisplayed()));
            onView(withId(R.id.profileBuddyStar)).perform(click());
            onView(withId(R.id.nav_profile)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(1000);
            onView(withId(R.id.buddies_list)).check(matches(isDisplayed()));
            Thread.sleep(2000);
            onView(withRecyclerView(R.id.buddies_recycleView).atPosition(0)).check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.buddies_recycleView).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.profile)).check(matches(isDisplayed()));
            onView(withId(R.id.profileName)).check(matches(isDisplayed()));
            onView(withId(R.id.profileBuddyStar)).perform(click());
            onView(withId(R.id.profileBuddyStar)).perform(click());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(1000);
            onView(withId(R.id.buddies_list)).check(matches(isDisplayed()));
            onView(withId(R.id.noBuddies)).check(matches(withId(R.string.no_buddies)));
            onView(withId(R.id.nav_buddies)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);



        }

        //else logout and log error
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    //helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
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

