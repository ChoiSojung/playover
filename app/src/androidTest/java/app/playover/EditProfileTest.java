package app.playover;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class EditProfileTest {

    @Rule
    public ActivityTestRule<ProfileActivity> activityTestRule
            = new ActivityTestRule<>(ProfileActivity.class);

//    @Test
//    public void testOnSubmitName() {
//        onView(withId(R.id.editFirstName))
//                .perform(typeText("Clint"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.editLastName))
//                .perform(typeText("Shaw"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.editGroup))
//                .perform(typeText("Super Sonics"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.editPosition))
//                .perform(typeText("Baller"));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.editInterests)).perform(typeText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
//                "Vestibulum eleifend odio volutpat nibh ultricies mattis. " +
//                "Sed lobortis mauris ac turpis egestas, ut consequat ligula hendrerit. " +
//                "Nullam tempus neque nec neque lacinia venenatis sit amet ornare dolor. " +
//                "Praesent suscipit convallis orci sit amet fermentum. Mauris porta enim vitae congue ultricies. " +
//                "Suspendisse elementum eleifend auctor. Sed commodo ante nec placerat aliquam."));
//
//        Espresso.closeSoftKeyboard();
//
//        onView(withId(R.id.submitBtn))
//                .perform(scrollTo(), click());
//    }


}
