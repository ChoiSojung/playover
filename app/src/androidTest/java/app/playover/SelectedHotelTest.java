package app.playover;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.playover.R;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
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

//    @Test
//    public void testCheckOut() throws Exception {
//        Thread.sleep(5000);
//        try {
//            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
//            onView(withId(R.id.main_content)).perform(DrawerActions.open());
//            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
//            Thread.sleep(2000);
//            onView(withId(R.id.email_login)).perform(replaceText("hotelTest@fake.com")).perform(ViewActions.closeSoftKeyboard());
//            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
//            onView(withId(R.id.btn_login)).perform(click());
//            Thread.sleep(5000);
//            onView(withId(R.id.recycler_view_hotels))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//            onView(withText("Confirm")).perform(click());
//            Thread.sleep(5000);
//            onView(withId(R.id.recycler_view_also_checked_in))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
//            onView(withId(R.id.recycler_view_also_checked_in))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
//            onView(withId(R.id.txtCheckoutSet)).perform(click());
//        } catch (Exception ex) {
//            onView(withId(R.id.lblogin_main)).perform(MainActivityTest.clickClickableSpan("Sign In"));
//            onView(withId(R.id.email_login)).perform(replaceText("hotelTest@fake.com")).perform(ViewActions.closeSoftKeyboard());
//            onView(withId(R.id.password_login)).perform(replaceText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
//            onView(withId(R.id.btn_login)).perform(click());
//            Thread.sleep(5000);
//            onView(withId(R.id.recycler_view_hotels))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//            onView(withText("Confirm")).perform(click());
//            Thread.sleep(5000);
//            onView(withId(R.id.recycler_view_also_checked_in))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
//            onView(withId(R.id.recycler_view_also_checked_in))
//                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
//            onView(withId(R.id.txtCheckoutSet)).perform(click());
//            }
//        }

    @Test
    public void selectedHotelTest() throws Exception {
        try {
            Thread.sleep(5000);
            onView(ViewMatchers.withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);


            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.checkbox)));
            Thread.sleep(3000);
            onView(withId(R.id.messageSelectedBtn)).perform(click());
            Thread.sleep(3000);
            Espresso.pressBack();
            Thread.sleep(2000);
            Espresso.pressBack();
            Thread.sleep(2000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            Thread.sleep(1000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            Thread.sleep(1000);
            onView(withId(R.id.action_search)).perform(click());
            Thread.sleep(1000);

        } catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("rhtest@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.checkbox)));
            Thread.sleep(3000);
            onView(withId(R.id.messageSelectedBtn)).perform(click());
            Thread.sleep(3000);
            Espresso.pressBack();
            Thread.sleep(2000);
            Espresso.pressBack();
            Thread.sleep(2000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            Thread.sleep(1000);
            onView(withId(R.id.recycler_view_also_checked_in))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,TestUtils.clickChildViewWithId(R.id.buddyStar)));
            Thread.sleep(1000);
            onView(withId(R.id.action_search)).perform(click());
            Thread.sleep(1000);

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