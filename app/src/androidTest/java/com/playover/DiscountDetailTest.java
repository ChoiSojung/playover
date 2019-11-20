package com.playover;

import android.Manifest;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.playover.MainActivityTest.withRecyclerView;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class DiscountDetailTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    @Test
    public void canGetToAddCommentWindow() throws Exception {
        try {
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("lladddiscounttest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(3000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.addComment)).perform(click());
            Thread.sleep(2000);
            UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject uiObject = mDevice.findObject(new UiSelector().text("Comment..."));
            if (uiObject.exists())
            {
                mDevice.pressBack();
            }

        } catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(click());
            onView(withId(R.id.email_login)).perform(typeText("lladddiscounttest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(3000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.addComment)).perform(click());
            Thread.sleep(2000);
            UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            UiObject uiObject = mDevice.findObject(new UiSelector().text("Comment..."));
            if (uiObject.exists())
            {
                mDevice.pressBack();
            }
        }
    }


    //static method for clicking on clickable spans in textviews
    public static ViewAction clickClickableSpan(final CharSequence textToClick) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return instanceOf(TextView.class);
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
