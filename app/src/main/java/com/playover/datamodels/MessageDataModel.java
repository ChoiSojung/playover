package com.playover.datamodels;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.playover.models.Message;
import com.playover.models.UserMessageThread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MessageDataModel {

    private DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;
    private String DBkey = new String();

    public MessageDataModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listeners = new HashMap<>();
    }

    //gets message thread by uid from the database
    public void getMessageThread(String uID, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        DBkey = getDBkey(uID);
        Query query = mDatabase.child(DBkey).orderByKey().equalTo(uID);
        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);
            }
        };
        query.addValueEventListener(messageListener);
        listeners.put(mDatabase, messageListener);
    }

    public void putMessageThread(UserMessageThread thread) {
        String uid = thread.getMessageThreadUID();
        DBkey = getDBkey(uid);

        try {
            DatabaseReference threadsRef = mDatabase.child(DBkey);
            Map<String, Object> threadMap = new HashMap<>();
            threadMap.put(uid, thread);
            threadsRef.updateChildren(threadMap);
            threadsRef.push();
        }
        catch (Exception ex)
        {
            Log.e("Error creating message thread: ", ex.getMessage());
        }
    }

    private String getDBkey(String uid){
        String key = new String();
        if(uid.substring(0,2).equals("G-")) {
            key = "groupMessageThreads";
        } else {
            key = "messageThreads";
        }
        return key;
    }

    //clears the listeners
    public void clear() {
        listeners.forEach(Query::removeEventListener);
    }



}
