package com.playover;
import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.playover.CheckInTest.withRecyclerView;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.*;

import com.playover.viewmodels.AuthUserViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CheckInDialogTest {
    private static AuthUserViewModel authUserViewModel = new AuthUserViewModel();
    private CheckIn checkInActivity = mock(CheckIn.class);
    Intent testIntent = new Intent();
    public final ActivityTestRule<CheckIn> myActivityTestRule = new ActivityTestRule<>(CheckIn.class, true, false);

    @Before
    public void setup() {
        myActivityTestRule.launchActivity(testIntent);
    }

    @Test
    public void canCheckInOut() throws InterruptedException {
        //mockito main activity as work-around for location enabler dialog
        MainActivity activity = mock(MainActivity.class);
        doNothing().when(activity).requestPermissions();

        if (checkForUser()) {
            myActivityTestRule.launchActivity(testIntent);
            try {
                onView(withId(R.id.txtCheckoutSet)).perform(click());
                onView(withId(R.id.txtCheckoutSet)).check(matches(not(isEnabled())));

            } catch (NoMatchingViewException e) {

            }

            //check in by clicking on recyclerview item
            onView(withId(R.id.recycler_view_hotels))
                    .check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.recycler_view_hotels).atPosition(0))
                    .perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.txtCheckIn)).check(matches(allOf(withText("CHECK-IN TO:"), isDisplayed())));
            onView(withId(R.id.btnCheckInConfirm)).perform(click());
            onView(withId(R.id.btnCheckInConfirm)).check(matches(not(isEnabled())));

        }
    }


    //helper
    public void getListHotelsFragment(){
        ListHotels_Fragment mFragment = new ListHotels_Fragment();
        myActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.containerCheckIn, mFragment).commit();
    }

    //helper
    private boolean checkForUser() {
        return (authUserViewModel.getUser() != null);
    }


}