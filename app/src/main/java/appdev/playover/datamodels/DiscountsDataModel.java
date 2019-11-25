package appdev.playover.datamodels;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import appdev.playover.models.Comment;
import appdev.playover.models.Discount;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DiscountsDataModel {
    private DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;

    public DiscountsDataModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listeners = new HashMap<>();
    }

    //creates new discount in firebase real time database
    public void createNewDiscount(Discount discount, OnCompleteListener<Void> viewModelCallBack) {
        //drill down to where the actual entry will be and push a new entry
        DatabaseReference discountRefPush = mDatabase.child("discounts").child(discount.getState()).child(discount.getCity()).push();
        //go to spot where discount will be added
        DatabaseReference discountRefSet = mDatabase.child("discounts").child(discount.getState()).child(discount.getCity());
        Map<String, Object> discEntry = new HashMap<>();
        //grab pushed key
        String pushedkey = discountRefPush.getKey();
        discount.setUId(pushedkey);
        //if there is a discount comment push on comments and create and create unique entry
        //assign comment to that entry
        if (discount.getComment() != null && pushedkey != null) {
            DatabaseReference commentRefPush = mDatabase.child("discounts").child(discount.getState()).child(discount.getCity())
                    .child(pushedkey).child("comments").push();

            discEntry.put(pushedkey + "/comments/" + commentRefPush.getKey() + "/commentDetail/", discount.getComment().getCommentDetail());
            discEntry.put(pushedkey + "/comments/" + commentRefPush.getKey() + "/commentPoster/", discount.getComment().getCommentPoster());
            discEntry.put(pushedkey + "/comments/" + commentRefPush.getKey() + "/commentDate/", discount.getComment().getCommentDate());
            discEntry.put(pushedkey + "/comments/" + commentRefPush.getKey() + "/posterUId/", discount.getComment().getPosterUId());
        }
        //set the fields
        discEntry.put(pushedkey + "/address/", discount.getAddress());
        discEntry.put(pushedkey + "/businessName/", discount.getBusinessName());
        discEntry.put(pushedkey + "/category/", discount.getCategory());
        discEntry.put(pushedkey + "/city/", discount.getCity());
        discEntry.put(pushedkey + "/details/", discount.getDetails());
        discEntry.put(pushedkey + "/phone/", discount.getPhone());
        discEntry.put(pushedkey + "/state/", discount.getState());
        discEntry.put(pushedkey + "/uid/", discount.getUId());
        discEntry.put(pushedkey + "/website/", discount.getWebsite());
        discEntry.put(pushedkey + "/posterUId/", discount.getPosterUId());
        discEntry.put(pushedkey + "/posterName/", discount.getPosterName());
        discEntry.put(pushedkey + "/lastUpdate/", discount.getLastUpdate());
        //update set reference
        discountRefSet.updateChildren(discEntry).addOnCompleteListener(viewModelCallBack);

    }

    //updates discount in firebase real time database
    public void updateDiscount(Discount discount, OnCompleteListener<Void> viewModelCallBack) {
        //go to spot where discount will be added
        DatabaseReference discountRefSet = mDatabase.child("discounts").child(discount.getState()).child(discount.getCity());
        Map<String, Object> discEntry = new HashMap<>();
        discEntry.put(discount.getUId() + "/address/", discount.getAddress());
        discEntry.put(discount.getUId() + "/businessName/", discount.getBusinessName());
        discEntry.put(discount.getUId() + "/category/", discount.getCategory());
        discEntry.put(discount.getUId() + "/city/", discount.getCity());
        discEntry.put(discount.getUId() + "/details/", discount.getDetails());
        discEntry.put(discount.getUId() + "/phone/", discount.getPhone());
        discEntry.put(discount.getUId() + "/state/", discount.getState());
        discEntry.put(discount.getUId() + "/uid/", discount.getUId());
        discEntry.put(discount.getUId() + "/website/", discount.getWebsite());
        discEntry.put(discount.getUId() + "/posterUId/", discount.getPosterUId());
        discEntry.put(discount.getUId() + "/posterName/", discount.getPosterName());
        discEntry.put(discount.getUId() + "/lastUpdate/", discount.getLastUpdate());
        //update set reference
        discountRefSet.updateChildren(discEntry).addOnCompleteListener(viewModelCallBack);
    }

    public void getDiscounts(String State, String City, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        //set query
        DatabaseReference allDiscounts = mDatabase.child("discounts").child(State).child(City);
        //listener that looks for a data change
        ValueEventListener discountsListener = new ValueEventListener() {
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
        allDiscounts.addValueEventListener(discountsListener);
        listeners.put(mDatabase, discountsListener);

    }

    //gets comments for selected discount
    public void getDiscountComments(String state, String city, String uId, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        //set reference
        Query comments = mDatabase.child("discounts").child(state).child(city).child(uId).child("comments").orderByKey();
        //listener that looks for a data change
        ValueEventListener commentListener = new ValueEventListener() {
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
        comments.addValueEventListener(commentListener);
        listeners.put(mDatabase, commentListener);
    }

    //delete discount datamodel
    public void deleteDiscount(String state, String city, String uId, OnCompleteListener<Void> activityCallBack) {
        DatabaseReference deleteRef = mDatabase.child("discounts").child(state).child(city).child(uId);
        deleteRef.removeValue().addOnCompleteListener(activityCallBack);
    }

    public void addComment(String state, String city, String uId, Comment comment, OnCompleteListener<Void> viewModelCallBack) {
        DatabaseReference addCommentPush = mDatabase.child("discounts").child(state).child(city).child(uId).child("comments").push();
        DatabaseReference commentRefSet = mDatabase.child("discounts").child(state).child(city).child(uId).child("comments");
        Map<String, Object> discComment = new HashMap<>();
        comment.setUId(addCommentPush.getKey());
        discComment.put(addCommentPush.getKey() + "/commentDetail/", comment.getCommentDetail());
        discComment.put(addCommentPush.getKey() + "/commentPoster/", comment.getCommentPoster());
        discComment.put(addCommentPush.getKey() + "/commentDate/", comment.getCommentDate());
        discComment.put(addCommentPush.getKey() + "/posterUId/", comment.getPosterUId());
        commentRefSet.updateChildren(discComment).addOnCompleteListener(viewModelCallBack);
    }

    //clears the listeners.
    public void clear() {
        listeners.forEach(Query::removeEventListener);
    }
}
