package com.playover;

import android.Manifest;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
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

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.playover.MainActivityTest.withRecyclerView;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringEndsWith.endsWith;

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
            onView(allOf(withClassName(endsWith("EditText"))))
                    .perform(replaceText(""));
            Thread.sleep(3000);
            onView(allOf(withClassName(endsWith("EditText"))))
                    .perform(replaceText("Espresso Comment"));
            onView(withText("Add")).perform(scrollTo(), click());
            Thread.sleep(2000);
            onView(withId(R.id.displayBName)).check(matches(isDisplayed()));
            Thread.sleep(1000);
            Espresso.pressBack();
            Thread.sleep(1000);
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText("Espresso Test Business")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.address)).perform(replaceText("123 Espresso Test Address")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.city)).perform(replaceText("Seattle")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.states)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(47).perform(click());
            onView(withId(R.id.phone)).perform(replaceText("206-867-5309")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.website)).perform(replaceText("www.github.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.discount_details)).perform(replaceText("Shop Here And Get Great Discounts!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.first_comment)).perform(replaceText("Great Customer Service!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.category)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(1).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(10000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(1000);
            Espresso.pressBack();
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.editDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText("Espresso Test Edit Business")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.address)).perform(replaceText("123 Espresso Test Edit Address")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.city)).perform(replaceText("Seattle")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.states)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(47).perform(click());
            onView(withId(R.id.phone)).perform(replaceText("425-420-9999")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.website)).perform(replaceText("www.editurl.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.discount_details)).perform(replaceText("Shop Here And Get Ok Discounts!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.category)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(2).perform(click());
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.displayPhone)).check(matches(withText("425-420-9999")));
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
            onView(allOf(withClassName(endsWith("EditText"))))
                    .perform(replaceText(""));
            Thread.sleep(3000);
            onView(allOf(withClassName(endsWith("EditText"))))
                    .perform(replaceText("Espresso Comment"));
            onView(withText("Add")).perform(scrollTo(), click());
            Thread.sleep(2000);
            onView(withId(R.id.displayBName)).check(matches(isDisplayed()));
            Thread.sleep(1000);
            Espresso.pressBack();
            Thread.sleep(1000);
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText("Espresso Test Business")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.address)).perform(replaceText("123 Espresso Test Address")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.city)).perform(replaceText("Seattle")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.states)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(47).perform(click());
            onView(withId(R.id.phone)).perform(replaceText("206-867-5309")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.website)).perform(replaceText("www.github.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.discount_details)).perform(replaceText("Shop Here And Get Great Discounts!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.first_comment)).perform(replaceText("Great Customer Service!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.category)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(1).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(10000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(1000);
            Espresso.pressBack();
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.editDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText("Espresso Test Edit Business")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.address)).perform(replaceText("123 Espresso Test Edit Address")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.city)).perform(replaceText("Seattle")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.states)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(47).perform(click());
            onView(withId(R.id.phone)).perform(replaceText("425-420-9999")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.website)).perform(replaceText("www.editurl.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.discount_details)).perform(replaceText("Shop Here And Get Ok Discounts!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.category)).perform(click());
            onData(allOf(Is.is(CoreMatchers.instanceOf(String.class)))).atPosition(2).perform(click());
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.displayPhone)).check(matches(withText("425-420-9999")));
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
