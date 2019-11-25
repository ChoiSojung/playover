package appdev.playover;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
