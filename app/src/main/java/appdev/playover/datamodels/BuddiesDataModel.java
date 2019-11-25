package appdev.playover.datamodels;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BuddiesDataModel {

    private DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;


    public BuddiesDataModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        listeners = new HashMap<>();
    }

    //gets buddies info by uId from the database
    public void getBuddies(String uId, Consumer<ArrayList<String>> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {

        //set query
        Query query = mDatabase.child(uId).child("buddies").orderByKey();

        //listener that looks for a data change
        ValueEventListener buddyListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> buddyUids = new ArrayList<String>();

                for (DataSnapshot buddies : dataSnapshot.getChildren()) {
                    buddyUids.add(buddies.getKey());
                }
                dataChangedCallback.accept(buddyUids);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);

            }
        };
        query.addValueEventListener(buddyListener);
        listeners.put(mDatabase, buddyListener);
    }

    //use the same function to create a buddy or update a buddy to blocked
    public void putBuddy(String authUId, String buddyUId, Boolean blocked, OnCompleteListener<Void> activityCallBack) {
        DatabaseReference buddiesSetReference = mDatabase.child(authUId);
        Map<String, Object> buddy = new HashMap<>();
        //false for now until someone blocks them
        buddy.put("buddies/" + buddyUId + "/blocked/", blocked);
        buddy.put("buddies/" + buddyUId + "/uid/", buddyUId);
        buddiesSetReference.updateChildren(buddy).addOnCompleteListener(activityCallBack);
    }

    //deletes buddy from user
    public void deleteBuddy(String authUId, String buddyUId, OnCompleteListener<Void> activityCallBack) {
        DatabaseReference deleteRef = mDatabase.child(authUId).child("buddies").child(buddyUId);
        deleteRef.removeValue().addOnCompleteListener(activityCallBack);
    }

    //left here for the next person
   /* public void firebaseSearch(String searchText, Consumer<ArrayList<String>> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {

        ///TODO: Change query to use mike's params
        //set query
        Query firebaseSearchQuery = mDatabase.orderByChild("firstName").startAt(searchText).endAt(searchText + "\uf8ff");

        //listener that looks for a data change
        ValueEventListener userListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> searchResults = new ArrayList<>();

                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    searchResults.add(users.getKey());
                }
                dataChangedCallback.accept(searchResults);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);

            }
        };
        firebaseSearchQuery.addValueEventListener(userListener);
        listeners.put(mDatabase, userListener);

    }*/

    //left here for the next person.
    /*public void isBuddy(String user, String buddy, Consumer<ArrayList<Boolean>> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        //set query to check for buddies child
        Query isBuddyQuery = mDatabase.child(user).child("buddies").orderByValue();

        //set listener
        ValueEventListener buddyListenter = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Boolean> buddyResults = new ArrayList<>();

                for (DataSnapshot buddies : dataSnapshot.getChildren()) {
                    buddyResults.add();

                }
                dataChangedCallback.accept(buddyResults);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("isBuddy DB error ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);
            }
        };

        isBuddyQuery.addValueEventListener(buddyListenter);
        listeners.put(mDatabase, buddyListenter);
    }
    */


    //clears the listeners
    public void clear() {
        listeners.forEach(Query::removeEventListener);
    }


}
