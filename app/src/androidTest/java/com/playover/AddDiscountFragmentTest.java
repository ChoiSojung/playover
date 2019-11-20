package com.playover;

import android.Manifest;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.playover.MainActivityTest.withRecyclerView;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

public class AddDiscountFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);



    @Test
    public void addNewDiscountTest() throws Exception {
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
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.business_name)).perform(replaceText("Amber India"));
            onView(withId(R.id.address)).perform(replaceText("4926 El Camino Real"));
            onView(withId(R.id.city)).perform(replaceText("(650) 968-7511"));
            onView(withId(R.id.phone)).perform(replaceText("Amber India"));
            onView(withId(R.id.website)).perform(replaceText("https://www.amber-india.com/"));
            onView(withId(R.id.discount_details)).perform(replaceText("Buy one get one free"));
            onView(withId(R.id.first_comment)).perform(replaceText("This is legit Amber Indiana!"));
            onView(withId(R.id.states)).perform(click());
            onData(anything()).atPosition(5).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.category)).perform(click());
            onData(anything()).atPosition(1).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(3000);



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
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.business_name)).perform(replaceText("Amber India"));
            onView(withId(R.id.address)).perform(replaceText("4926 El Camino Real"));
            onView(withId(R.id.city)).perform(replaceText("(650) 968-7511"));
            onView(withId(R.id.phone)).perform(replaceText("Amber India"));
            onView(withId(R.id.website)).perform(replaceText("https://www.amber-india.com/"));
            onView(withId(R.id.discount_details)).perform(replaceText("Buy one get one free"));
            onView(withId(R.id.first_comment)).perform(replaceText("This is legit Amber Indiana!"));
            onView(withId(R.id.states)).perform(click());
            onData(anything()).atPosition(5).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.category)).perform(click());
            onData(anything()).atPosition(1).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(3000);

        }
    }

    @Test
    public void missingFieldsThrowsToastErrorTest() throws Exception {
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
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(1000);
            onView(withText("Please fill in the required fields!")).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.business_name)).check(matches(hasErrorText("Business Name Required!")));
            onView(withId(R.id.city)).check(matches(hasErrorText("City is required!")));
            //onView(withId(R.id.states)).check(matches(withId(R.string.required)));
            onView(withId(R.id.discount_details)).check(matches(hasErrorText("Please enter discount details!")));
            //onView(withId(R.id.category)).check(matches(withId(R.string.required)));
            onView(withId(R.id.address)).check(matches(hasErrorText("Address is a required field!")));
            Thread.sleep(1000);

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
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(1000);
            onView(withText("Please fill in the required fields!")).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.business_name))
                    .check(matches(hasErrorText("Business Name Required!")));
            Thread.sleep(1000);
            onView(withId(R.id.business_name)).check(matches(hasErrorText("Business Name Required!")));
            onView(withId(R.id.city)).check(matches(hasErrorText("City is required!")));
            //onView(withId(R.id.states)).check(matches(withId(R.string.required)));
            onView(withId(R.id.discount_details)).check(matches(hasErrorText("Please enter discount details!")));
            //onView(withId(R.id.category)).check(matches(withId(R.string.required)));
            onView(withId(R.id.address)).check(matches(hasErrorText("Address is a required field!")));
            Thread.sleep(1000);
        }
    }

    @Test
    public void editExistingDiscountTest() throws Exception {
        try {
            Thread.sleep(5000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(typeText("nnTestDiscount@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(3000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.editDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText(""));
            onView(withId(R.id.address)).perform(replaceText(""));
            onView(withId(R.id.city)).perform(replaceText(""));
            onView(withId(R.id.phone)).perform(replaceText(""));
            onView(withId(R.id.website)).perform(replaceText(""));
            onView(withId(R.id.discount_details)).perform(replaceText(""));
            onView(withId(R.id.states)).perform(click());
            onData(anything()).atPosition(0).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.category)).perform(click());
            onData(anything()).atPosition(0).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(3000);
        } catch (Exception ex) {
            Thread.sleep(5000);
            onView(withId(R.id.lblogin_main)).perform(click());
            onView(withId(R.id.email_login)).perform(typeText("nnTestDiscount@fake.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(typeText("Passw0rd!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(8000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(3000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.editDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText(""));
            onView(withId(R.id.address)).perform(replaceText(""));
            onView(withId(R.id.city)).perform(replaceText(""));
            onView(withId(R.id.phone)).perform(replaceText(""));
            onView(withId(R.id.website)).perform(replaceText(""));
            onView(withId(R.id.discount_details)).perform(replaceText(""));
            onView(withId(R.id.states)).perform(click());
            onData(anything()).atPosition(0).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.category)).perform(click());
            onData(anything()).atPosition(0).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(3000);
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
