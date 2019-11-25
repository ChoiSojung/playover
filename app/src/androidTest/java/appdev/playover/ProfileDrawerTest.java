package appdev.playover;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.playover.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class ProfileDrawerTest {

    @Rule
    public ActivityTestRule<ProfileActivity> activityTestRule
            = new ActivityTestRule<>(ProfileActivity.class);


    @Test
    public void test_navigation_view() throws InterruptedException {
        Thread.sleep(2000);

        onView(ViewMatchers.withId(R.id.profile_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
        Thread.sleep(1000);

        onView(withId(R.id.profile_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
        Thread.sleep(1000);

        onView(withId(R.id.main_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_check_in));
        Thread.sleep(1000);

        onView(withId(R.id.main_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));

    }

    @Test
    public void test_navigation_view_sign_out() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.profile_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_profile));
        Thread.sleep(1000);

        onView(withId(R.id.profile_content)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_profile)).perform(NavigationViewActions.navigateTo(R.id.nav_sign_out));
    }


}
