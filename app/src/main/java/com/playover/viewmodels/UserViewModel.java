package com.playover.viewmodels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.playover.datamodels.UserDataModel;
import com.playover.models.Person;

import java.util.function.Consumer;

public class UserViewModel {

    private UserDataModel userDataModel;

    //constructor.  view model instantiates a data model
    public UserViewModel() {
        userDataModel = new UserDataModel();
    }

    //creates user in firebase realtime database
    public void createUser(String uId, Person person, OnCompleteListener<Void> activityCallBack)  {
        userDataModel.createUser(uId, person, activityCallBack);

    }

    //updates user in firebase realtime database
    public void updateUser(String uId, Person person, OnCompleteListener<Void> activityCallBack)  {
        userDataModel.updateUserInfo(uId, person, activityCallBack);

    }

    //update the image uri for user
    public void updateUserImage(String uId, String URI, OnCompleteListener<Void>activityCallBack){
        userDataModel.uploadImageURI(uId, URI, activityCallBack);
    }

    //update do not disturb status for user
    public void updateDnd(String uId, boolean dnd, OnCompleteListener<Void>activityCallBack) {
        userDataModel.updateDnd(uId, dnd, activityCallBack);
    }

    public void addMessageThreadToUser(Person person)  {
        userDataModel.addMessageThreadToUser(person);
    }


    //grabs user from firebase
    public void getUser(String uId, Consumer<Person> responseCallback) {
        userDataModel.getUser(uId,
                (DataSnapshot dataSnapshot) -> {
                    Person user = new Person();
                    for (DataSnapshot matchesSnapshot : dataSnapshot.getChildren()) {
                        user = matchesSnapshot.getValue(Person.class);
                    }
                    responseCallback.accept(user);
                },
                (databaseError -> System.out.println("Error getting user: " + databaseError))
        );
    }

    //clears the data model
    public void clear() {
        userDataModel.clear();
    }
}