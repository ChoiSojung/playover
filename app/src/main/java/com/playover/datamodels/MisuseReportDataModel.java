package com.playover.datamodels;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.playover.models.UserMessageThread;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MisuseReportDataModel {

    private DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;

//    //gets report thread by uid from the database
//    public void getMessageThread(String uID, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
//        Query query = mDatabase.child("messageThreads").orderByKey().equalTo(uID);
//        ValueEventListener messageListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                dataChangedCallback.accept(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("db error: ", databaseError.getMessage());
//                dataErrorCallback.accept(databaseError);
//            }
//        };
//        query.addValueEventListener(messageListener);
//        listeners.put(mDatabase, messageListener);
//    }

    public void putMessageThread(String misuseReport) {

        try {
            DatabaseReference threadsRef = mDatabase.child("misuseReports");
//            Map<String, Object> threadMap = new HashMap<>();
//            threadMap.put(uid, thread);
//            threadsRef.updateChildren(threadMap);
            //hreadsRef.
            //threadsRef.setValue("yooo");
            Log.i("misuse", "model");

        }
        catch (Exception ex)
        {
            Log.e("Error creating misuse report: ", ex.getMessage());
        }
    }

    //clears the listeners
    public void clear() {
        listeners.forEach(Query::removeEventListener);
    }

}
