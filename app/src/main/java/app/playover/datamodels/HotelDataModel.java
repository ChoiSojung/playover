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
import app.playover.models.Hotel;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HotelDataModel {

    private DatabaseReference mDatabase;
    private HashMap<DatabaseReference, ValueEventListener> listeners;

    public HotelDataModel() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.listeners = new HashMap<>();
    }

    public void getHotels(Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback)
    {
        DatabaseReference hotelsRef = mDatabase.child("hotels");
        //listener that looks for a data change
        ValueEventListener hotelListener = new ValueEventListener() {
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
        hotelsRef.addValueEventListener(hotelListener);
        listeners.put(hotelsRef, hotelListener);
    }

    public void removeGuest(String guestUID, String hotelUid)
    {
        try
        {
            DatabaseReference hotelGuestsRef = mDatabase.child("hotels").child(hotelUid).child("guestUids");
            hotelGuestsRef.child(guestUID).removeValue();

            //Also update the user to delete the hotel
            DatabaseReference userRef = mDatabase.child("users").child(guestUID);
            userRef.child("hotelCheckedInto").removeValue();
        }
        catch (Exception e)
        {

        }
    }

    public void putGuest(String uId, String checkoutTime, Hotel hotel)
    {
        try
        {
            DatabaseReference hotelsRef = mDatabase.child("hotels").child(hotel.getName() + hotel.getLat() + hotel.getLon());
            hotelsRef.setValue(hotel.getName() + hotel.getLat() + hotel.getLon());
            DatabaseReference hotelNameRef = mDatabase.child("hotels").child(hotel.getName() + hotel.getLat() + hotel.getLon()).child("name");
            hotelNameRef.setValue(hotel.getName());
            DatabaseReference hotelAddrRef = mDatabase.child("hotels").child(hotel.getName() + hotel.getLat() + hotel.getLon()).child("address");
            hotelAddrRef.setValue(hotel.getAddress());
            DatabaseReference hotelLatRef = mDatabase.child("hotels").child(hotel.getName() + hotel.getLat() + hotel.getLon()).child("lat");
            hotelLatRef.setValue(hotel.getLat());
            DatabaseReference hotelLonRef = mDatabase.child("hotels").child(hotel.getName() + hotel.getLat() + hotel.getLon()).child("lon");
            hotelLonRef.setValue(hotel.getLon());
            DatabaseReference hotelGuestsRef = mDatabase.child("hotels").child(hotel.getName() + hotel.getLat() + hotel.getLon()).child("guestUids");
            hotelGuestsRef.child(uId).setValue(checkoutTime);

            //Also update the user to store the hotel
            DatabaseReference userRefSet = mDatabase.child("users").child(uId);
            Map<String, Object> hotelEntry = new HashMap<>();
            Object obj2;
            obj2 = hotel.getName() + hotel.getLat() + hotel.getLon();
            hotelEntry.put("hotelCheckedInto", obj2);
            userRefSet.updateChildren(hotelEntry);
        }
        catch (Exception ex)
        {
            Log.e("put guest error: ", ex.getMessage());
        }
    }

    public void getGuests(Hotel hotel, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback, OnCompleteListener<String> comp)
    {

        DatabaseReference hotelsRef = mDatabase.child("hotels")
                            .child(hotel.getName() + hotel.getLat() + hotel.getLon())
                            .child("guestUids");

        hotelsRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);
            }
        });
    }

    public void findHotelCheckedInto(String guestUID, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback)
    {
        DatabaseReference userRef = mDatabase.child("users").child(guestUID);
        Query findHotel = userRef.orderByKey().equalTo("hotelCheckedInto");

        findHotel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);
            }
        });
    }

    public void updateExistingHotelWithNewGuest(String userUid, String checkoutTime, Hotel existingHotel, OnCompleteListener<Void> viewModelCallBack)
    {
        try
        {
            DatabaseReference hotelGuestsRefSet = mDatabase.child("hotels")
                                                .child(existingHotel.getName() +
                                                        existingHotel.getLat() +
                                                        existingHotel.getLon())
                                                .child("guestUids");

            Map<String, Object> guestEntry = new HashMap<>();
            Object obj;
            obj = checkoutTime;
            guestEntry.put(userUid, obj);
            hotelGuestsRefSet.updateChildren(guestEntry).addOnCompleteListener(viewModelCallBack);

            //Also update the user to store the hotel
            DatabaseReference userRefSet = mDatabase.child("users").child(userUid);
            Map<String, Object> hotelEntry = new HashMap<>();
            Object obj2;
            obj2 = existingHotel.getName() + existingHotel.getLat() + existingHotel.getLon();
            hotelEntry.put("hotelCheckedInto", obj2);
            userRefSet.updateChildren(hotelEntry).addOnCompleteListener(viewModelCallBack);

        }
        catch (Exception e)
        {
            Log.e("update guests error: ", e.getMessage());
        }
    }

    public void findHotel( String uId, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback)
    {
        DatabaseReference hotelsRef = mDatabase.child("hotels");
        Query findHotel = hotelsRef.orderByKey().equalTo(uId);

        findHotel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);
            }
        });
    }

    //custom get user method
    public void getUser(String uID, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback) {
        //set query
        Query query = mDatabase.child("users").orderByKey().equalTo(uID);
        //listener that looks for a data change
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
                dataChangedCallback.accept(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db error: ", databaseError.getMessage());
                dataErrorCallback.accept(databaseError);

            }
        });
    }

    //clears the listeners
    public void clear() {
        listeners.forEach(Query::removeEventListener);
    }
}
