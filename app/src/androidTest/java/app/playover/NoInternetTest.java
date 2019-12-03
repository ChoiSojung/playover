package app.playover;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.playover.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class NoInternetTest {

    @Rule
    public ActivityTestRule<NoInternet> activityTestRule = new ActivityTestRule<>(NoInternet.class);

    //checking if  dialog is displayed, click message is displayed
    @Test
    public void testActivityBuild() throws InterruptedException
    {
        Thread.sleep(4000);
        onView(ViewMatchers.withText(R.string.no_internet_title))
                .check(matches(isDisplayed()));
        onView(withText(R.string.no_internet_positive_btn))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

    }

    @Test
    public void testBackButton() throws InterruptedException
    {
        Thread.sleep(4000);
        onView(withText(R.string.no_internet_title))
                .check(matches(isDisplayed()));
        Espresso.pressBack();

    }
}
