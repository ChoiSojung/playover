package com.playover;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.playover.MainActivityTest.withRecyclerView;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import com.playover.viewmodels.AuthUserViewModel;

public class DiscountsTest {
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);
    @Rule
    public ActivityTestRule<Discounts> activityTestRule = new ActivityTestRule<>(Discounts.class, true, false);
    private static AuthUserViewModel authUserViewModel = new AuthUserViewModel();

    private boolean checkForUser() {
        return (authUserViewModel.getUser() != null);
    }

    //can navigate to add discount fragment, create discount and press button
    @Test
    public void canGoToDiscountsAndCreate() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.addDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText("Espresso Test Business")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.address)).perform(replaceText("123 Espresso Test Address")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.city)).perform(replaceText("Seattle")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.states)).perform(click());
            onData(allOf(is(instanceOf(String.class)))).atPosition(47).perform(click());
            onView(withId(R.id.phone)).perform(replaceText("206-867-5309")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.website)).perform(replaceText("www.github.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.discount_details)).perform(replaceText("Shop Here And Get Great Discounts!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.first_comment)).perform(replaceText("Great Customer Service!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.category)).perform(click());
            onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(10000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(1000);
            Espresso.pressBack();
        }
    }

    //test editing an existing discount
    @Test
    public void editExistingDiscount()throws InterruptedException{
        //forcing activity to rebuild each time
        if (checkForUser()) {
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.editDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.business_name)).perform(replaceText("Espresso Test Edit Business")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.address)).perform(replaceText("123 Espresso Test Edit Address")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.city)).perform(replaceText("Seattle")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.states)).perform(click());
            onData(allOf(is(instanceOf(String.class)))).atPosition(47).perform(click());
            onView(withId(R.id.phone)).perform(replaceText("425-420-9999")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.website)).perform(replaceText("www.editurl.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.discount_details)).perform(replaceText("Shop Here And Get Ok Discounts!")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.category)).perform(click());
            onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());
            onView(withId(R.id.saveDiscount)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.displayPhone)).check(matches(withText("425-420-9999")));
        }
    }

    //test going to buddies
    @Test
    public void canNavigateDrawerToBuddies() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            Espresso.pressBack();
            Thread.sleep(2000);
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_buddies));
            Thread.sleep(2000);
            Espresso.pressBack();
        }
    }

    //test going to messages
//    @Test
//    public void canNavigateDrawerToMessages() throws InterruptedException {
//        if (checkForUser()) {
//            //forcing activity to rebuild each time
//            activityTestRule.launchActivity(new Intent());
//            Thread.sleep(5000);
//            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
//            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
//            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_messaging));
//            Thread.sleep(2000);
//            Espresso.pressBack();
//        }
//    }

    //test adding a comment to an existing discount
    @Test
    public void canAddComment() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
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
        }
    }

    @Test
    public void canNavigateDrawerToSettings() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
            Thread.sleep(2000);
            Espresso.pressBack();
        }
    }


    // test deleting an existing discount
    @Test
    public void canDeleteDiscount() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.deleteDiscount)).perform(click());
            Thread.sleep(5000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
            Thread.sleep(1000);
            Espresso.pressBack();
        }
    }

    //test going to profile
    @Test
    public void canNavigateDrawerToProfile() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
            Thread.sleep(2000);
            onView(withId(R.id.profile_content)).check(matches(isDisplayed()));
        }
    }

    //test going to check in
    @Test
    public void canNavigateDrawerToCheckIn() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
            Thread.sleep(2000);
            onView(withId(R.id.main_content)).check(matches(isDisplayed()));
        }
    }

    //test signing out
    @Test
    public void drawerSignOutAndSignBackIn() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
            Thread.sleep(2000);
            onView(withId(R.id.lblogin_main)).perform(MainActivityTest.clickClickableSpan("Sign In"));
            onView(withId(R.id.email_login)).perform(replaceText("mviencek@hotmail.com")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.password_login)).perform(replaceText("test123")).perform(ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            Thread.sleep(5000);
            if (MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0).matches(isDisplayed())) {
                onView(MainActivityTest.withRecyclerView(R.id.recycler_view).atPosition(0))
                        .check(matches(isDisplayed()));
            }
        }
    }

    //test going to buddies
    @Test
    public void canNavigateDrawerDiscounts() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            Espresso.pressBack();
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
//            onView(withId(R.id.discounts_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view_discount)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(2000);
            onView(withId(R.id.discounts_content)).check(matches(isDisplayed()));
        }
    }

    //can navigate to add discount fragment, create discount and press button
    @Test
    public void canNavigateDiscountTypes() throws InterruptedException {
        if (checkForUser()) {
            //forcing activity to rebuild each time
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(5000);
            onView(withId(R.id.all_discounts)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.food_discounts)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.fun_discounts)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.hotel_car_discounts)).perform(click());
            Thread.sleep(1000);
            onView(withId(R.id.all_discounts)).perform(click());
            Thread.sleep(1000);
            onView(withRecyclerView(R.id.recycler_view_discounts).atPosition(0)).perform(click());
        }
    }

}
