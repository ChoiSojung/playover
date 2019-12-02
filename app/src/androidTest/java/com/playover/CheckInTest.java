package com.playover;
import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import com.playover.viewmodels.AuthUserViewModel;
import org.junit.Rule;
import org.junit.Test;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class CheckInTest {

    @Rule
    public ActivityTestRule<CheckIn> activityTestRule = new ActivityTestRule<>(CheckIn.class, true, false);
    @Rule
    public GrantPermissionRule locationFineRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public GrantPermissionRule locationCourseRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);
    private static AuthUserViewModel authUserViewModel = new AuthUserViewModel();

    private boolean checkForUser() {
        return (authUserViewModel.getUser() != null);
    }

//    @Test
//    public void canCheckInOut() throws InterruptedException {
//        if (checkForUser()) {
//            activityTestRule.launchActivity(new Intent());
//            Thread.sleep(4000);
//            onView(withId(R.id.txtCheckoutSet)).perform(click());
//            Thread.sleep(3000);
//            onView(withId(R.id.recycler_view_hotels))
//                    .check(matches(isDisplayed()));
//            onView(withRecyclerView(R.id.recycler_view_hotels).atPosition(0))
//                    .perform(click());
//            Thread.sleep(2000);
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("Today")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            Thread.sleep(1000);
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("Tomorrow")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            Thread.sleep(1000);
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("3 Days")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("4 Days")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            Thread.sleep(1000);
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("5 Days")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            Thread.sleep(1000);
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("6 Days")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            Thread.sleep(1000);
//            onView(withId(R.id.sprCheckout)).perform(click());
//            onData(allOf(is(instanceOf(String.class)),
//                    is("7 Days")))
//                    .inRoot(isPlatformPopup())
//                    .perform(click());
//            Thread.sleep(1000);
//            onView(withId(R.id.tprCheckOut)).perform(PickerActions.setTime(12, 00));
//            Thread.sleep(1000);
//            onView(withId(R.id.btnCheckInConfirm)).perform(click());
//            Thread.sleep(3000);
//            onView(withRecyclerView(R.id.recycler_view_also_checked_in).atPosition(0)).perform(click());
//            Thread.sleep(3000);
//            Espresso.pressBack();
//            Thread.sleep(10000);
//            onView(withRecyclerView(R.id.recycler_view_also_checked_in).atPositionOnView(0, R.id.checkbox))
//                    .perform(click());
//            onView(withId(R.id.messageSelectedBtn)).perform(click());
//            Thread.sleep(2000);
//            Espresso.pressBack();
//        }
//    }
    @Test
    public void testDrawer() throws InterruptedException {
        if (checkForUser()) {
            activityTestRule.launchActivity(new Intent());
            Thread.sleep(2000);
            onView(withId(R.id.main_content)).perform(DrawerActions.open());
            onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_discounts));
            Thread.sleep(2000);
            Espresso.pressBack();
        }
    }
    //helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}