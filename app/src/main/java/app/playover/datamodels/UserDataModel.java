package app.playover.datamodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import app.playover.models.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserDataModel {

    private final DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;

    //constructor
    public UserDataModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listeners = new HashMap<>();
    }

    //gets user by uid from the database
    public void getUser(String uID, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        //set query
        Query query = mDatabase.child("users").orderByKey().equalTo(uID);
        //listener that looks for a data change
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);

            }
        };
        query.addValueEventListener(userListener);
        listeners.put(mDatabase, userListener);
    }

    //creates user in firebase real time database
    public void createUser(String uId, Person person, OnCompleteListener<Void> viewModelCallBack) {
        DatabaseReference usersRef = mDatabase.child("users");
        Map<String, Object> user = new HashMap<>();
        user.put(uId, person);
        usersRef.updateChildren(user).addOnCompleteListener(viewModelCallBack);
    }

    public void uploadImageURI(String uId, String uri, OnCompleteListener<Void> viewModelCallBack) {
        DatabaseReference usersRef = mDatabase.child("users");
        Map<String, Object> user = new HashMap<>();
        user.put(uId + "/imageUri/", uri);
        usersRef.updateChildren(user).addOnCompleteListener(viewModelCallBack);
    }

    public void updateDnd(String uId, boolean dnd, OnCompleteListener<Void> viewModelCallBack) {
        DatabaseReference usersRef = mDatabase.child("users");
        Map<String, Object> user = new HashMap<>();
        user.put(uId + "/dnd/", dnd);
        usersRef.updateChildren(user).addOnCompleteListener(viewModelCallBack);
    }

    public void addMessageThreadToUser(Person person) {
        String uid = person.getuId();
        try {
            DatabaseReference threadsRef = mDatabase.child("users");
            Map<String, Object> userMap = new HashMap<>();
            userMap.put(uid + "/messageThreads/", person.getMessageThreads());
            threadsRef.updateChildren(userMap);
        } catch (Exception ex) {
            Log.e("Error adding message thread to user: ", ex.getMessage());
        }
    }


    //updates edit profile fields ONLY in firebase real time database
    //other wise it will overwrite the user entry every time if we add fields later
    public void updateUserInfo(String uId, Person person, OnCompleteListener<Void> viewModelCallBack) {
        DatabaseReference usersRef = mDatabase.child("users");
        Map<String, Object> user = new HashMap<>();
        user.put(uId + "/firstName/", person.getFirstName());
        user.put(uId + "/lastName/", person.getLastName());
        user.put(uId + "/position/", person.getPosition());
        user.put(uId + "/dob/", person.getDob());
        user.put(uId + "/group/", person.getGroup());
        user.put(uId + "/interests/", person.getInterests());
        user.put(uId + "/relationshipStatus/", person.getRelationshipStatus());
        usersRef.updateChildren(user).addOnCompleteListener(viewModelCallBack);
    }

    //updates the firebase instance ID used for messaging & notifications
    public void updateFBinstanceID(String uId, String FCMinstanceID, OnCompleteListener<Void> viewModelCallBack) {
        DatabaseReference usersRef = mDatabase.child("users");
        Map<String, Object> user = new HashMap<>();
        user.put(uId + "/FCMinstanceID/", FCMinstanceID);
        usersRef.updateChildren(user).addOnCompleteListener(viewModelCallBack);
    }

    //clears the listeners.
    public void clear() {
        listeners.forEach(Query::removeEventListener);
    }

}
