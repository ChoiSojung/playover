package com.playover.viewmodels;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.playover.datamodels.HotelDataModel;
import com.playover.models.Hotel;
import com.playover.models.Person;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HotelViewModel {

    private HotelDataModel hotelDataModel;

    public HotelViewModel() {
        this.hotelDataModel = new HotelDataModel();
    }

    public void updateExistingHotelWithNewGuest(String userUid, String checkoutTime, Hotel existingHotel, OnCompleteListener<Void> viewModelCallBack)
    {
        this.hotelDataModel.updateExistingHotelWithNewGuest(userUid,checkoutTime ,existingHotel, viewModelCallBack);
    }

    public void findHotelCheckedInto(String guestUID, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback)
    {
        this.hotelDataModel.findHotelCheckedInto(guestUID, dataChangedCallback, dataErrorCallback);
    }

    public void removeGuest(String guestUID, String hotelUid)
    {
        this.hotelDataModel.removeGuest(guestUID, hotelUid);
    }

    public void putGuest(String userUId, String checkoutTime,Hotel hotel)
    {
        this.hotelDataModel.putGuest(userUId,checkoutTime,hotel);
    }

    public void getGuests(Hotel hotel, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback, OnCompleteListener<String> comp)
    {
        this.hotelDataModel.getGuests(hotel, dataChangedCallback, dataErrorCallback, comp);
    }

    public void getHotels(Consumer<ArrayList<Hotel>> responseCallback) {
        hotelDataModel.getHotels(
                (DataSnapshot dataSnapshot) -> {
                    ArrayList<Hotel> hotels = new ArrayList<>();
                    for (DataSnapshot hotelsSnapshot : dataSnapshot.getChildren()) {
                        Hotel h = hotelsSnapshot.getValue(Hotel.class);
                        hotels.add(h);
                    }
                    responseCallback.accept(hotels);
                },
                (databaseError -> System.out.println("Error reading Hotels: " + databaseError))
        );
    }

    public void findHotel( String uId, Consumer<DataSnapshot> dataChangedCallback, Consumer<DatabaseError> dataErrorCallback)
    {
        hotelDataModel.findHotel(uId, dataChangedCallback, dataErrorCallback);
    }

    public void getUser(String uId, Consumer<Person> responseCallback) {
        hotelDataModel.getUser(uId,
                (DataSnapshot dataSnapshot) -> {
                    Person user = new Person();
                    for (DataSnapshot matchesSnapshot : dataSnapshot.getChildren()) {
                        user = matchesSnapshot.getValue(Person.class);
                    }
                    responseCallback.accept(user);
                },
                (databaseError -> System.out.println("Error reading Matches: " + databaseError))
        );
    }

//    public HotelDataModel getHotelDataModel() {
//        return hotelDataModel;
//    }
//
//    public void setHotelDataModel(HotelDataModel hotelDataModel) {
//        this.hotelDataModel = hotelDataModel;
//    }

    public void clear()
    {
        this.hotelDataModel.clear();
    }
}