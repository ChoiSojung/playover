package appdev.playover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import appdev.playover.models.Hotel;
import appdev.playover.models.Person;
import appdev.playover.viewmodels.AuthUserViewModel;
import appdev.playover.viewmodels.HotelViewModel;
import appdev.playover.viewmodels.UserViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.function.Consumer;

import static junit.framework.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.playover", appContext.getPackageName());
    }

    //testing constants
    @Test
    public void testConstants() {
        assertEquals(Constants.KEY_EMAIL, "email");
        assertEquals(Constants.KEY_FNAME, "firstName");
        assertEquals(Constants.KEY_LNAME, "lastName");
        assertEquals(Constants.KEY_GROUP, "group");
        assertEquals(Constants.KEY_POSITION, "position");
        assertEquals(Constants.KEY_INTERESTS, "interests");
        assertEquals(Constants.KEY_IMG, "imgUri");
        assertEquals(Constants.KEY_PASSWORD, "password");
        assertEquals(Constants.KEY_CODE, "validation_code");
        assertEquals(Constants.DUMMY, "DUMMY TEXT");
    }

    //unit test setting ByteArray and throwing exception
    @Test
    public void testByteArray() {
        byte[] myBytes = "test byte array".getBytes();
        ByteArrayDataSource testSource = new ByteArrayDataSource(myBytes);
        testSource.setType("test type");
        try {
            testSource.getInputStream();
        } catch (Exception ex) {

        }
    }

    //unit test to manually create a hotel and changing values
    @Test
    public void testCreatingHotel() {
        String hotelName = "Hotel California";
        String hotelAddress = "123 Such A Lovely Place, Los Angeles, California, 90005";
        Hotel h = new Hotel(hotelName, hotelAddress, "0", "0", "0");
        assertEquals(h.getName(), "Hotel California");
        assertEquals(h.getAddress(), "123 Such A Lovely Place, Los Angeles, California, 90005");
        h.setName("changed name");
        assertEquals(h.getName(), "changed name");
        h.setAddress("123 Main St., Any Town, Any State, 00000");
        assertEquals(h.getAddress(), "123 Main St., Any Town, Any State, 00000");
    }

    //unit test for creating a new user and deleting the newly created user.
    @Test
    public void createNewAuthUserAndDeleteUser() {
        AuthUserViewModel authVm = new AuthUserViewModel();
        authVm.createUserWithEmailAndPassword("testemail@testemail.com", "test123", new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    authVm.deleteAuthUser(authVm.getUser(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful())
                                    if(task.getException()!=null)
                                        Log.e("siginreset error:", task.getException().getMessage());
                            }

                    });
                }
            }
        });


    }

    //unit test for logging in user and logging out.
    @Test
    public void logInLogOutForTests() {
        AuthUserViewModel authVm = new AuthUserViewModel();
        authVm.logInUser("mikeviencek@gmail.com", "test123", new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authVm.signOutUser(new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            authVm.clear();
                        }
                    });
                }
            }
        });
    }

    //unit test to sign in, send reset email, and sign out
    @Test
    public void signInAndSendResetEmailAndLogOut() {
        AuthUserViewModel authVm = new AuthUserViewModel();
        authVm.logInUser("mikeviencek@gmail.com", "test123", new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authVm.sendResetEmail("mikeviencek@gmail.com", new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful())
                                if(task.getException()!=null)
                                    Log.e("siginreset error:", task.getException().getMessage());

                        }
                    });
                }

            }
        });
    }
    @Test
    public void createUserInDB(){
        UserViewModel userVm = new UserViewModel();
        Person testPerson = new Person("Test First Name", "Test Last Name", "Test Group",
                "Test Position", "12-31-2000", "Single", "Testing Email Address","TestUID","Testing Code =)","",false, null, "", null);
        userVm.createUser("TestUID", testPerson, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    if(task.getException()!= null)
                        Log.e("create user error: ", task.getException().getMessage());
                }
            }
        });
    }

    @Test
    public void testHotelDM() {
        HotelViewModel testHotelVm = new HotelViewModel();
        Hotel testHotel = new Hotel("fake hotel", "666 test drive",
                "48*0", "50*0", "5");
        testHotel.setLat("33*0");
        assertEquals(testHotel.getLat(), "33*0");
        testHotel.setLon("33*0");
        assertEquals(testHotel.getLon(), "33*0");
        testHotel.setDistance("5");
        assertEquals(testHotel.getDistance(), "5 mi");
        testHotelVm.getHotels(new Consumer<ArrayList<Hotel>>() {
            @Override
            public void accept(ArrayList<Hotel> hotels) {
                if(hotels != null){
                    Log.i("there are hotels", hotels.toString());
                } else {
                    Log.i("hotels are empty", "nothing here");
                }
            }
        });
        testHotelVm.putGuest("testUid", "12:00", testHotel);
    }

}
