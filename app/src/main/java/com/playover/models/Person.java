package com.playover.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Person implements Parcelable {
    private String firstName;
    private String lastName;
    private String group;
    private String relationshipStatus;
    private String dob;
    private String position;
    private String emailAddress;
    private String uId;
    private String interests;
    private String imageUri;
    private boolean dnd;
    private List<String> messageThreads;

    //Added by fw
    private String hotelCheckedInto;

    public Person() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Person(String firstName, String lastName, String group, String position,
                  String dob, String relationshipStatus, String emailAddress, String uId, String interests,
                  String imageUri, boolean dnd, List<String> messageThreads, String hotelCheckedInto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.position = position;
        this.dob = dob;
        this.relationshipStatus = relationshipStatus;
        this.emailAddress = emailAddress;
        this.uId = uId;
        this.interests = interests;
        this.imageUri = imageUri;
        this.dnd = dnd;
        if (messageThreads == null) {
            this.messageThreads = new ArrayList<>();
        } else {
            this.messageThreads = messageThreads;
        }
        if (hotelCheckedInto != null) {
            this.hotelCheckedInto = hotelCheckedInto;
        }
    }

    public void addThread(String threadUid) {
        if (messageThreads == null) {
            messageThreads = new ArrayList<>();
        }
        if (!messageThreads.contains(threadUid)) {
            messageThreads.add(threadUid);
        }
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getuId() {
        return uId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGroup() {
        return group;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public String getDob() {
        return dob;
    }

    public String getPosition() {
        return position;
    }

    public String getInterests() {
        return interests;
    }

    public boolean isDnd() {
        return dnd;
    }

    public List<String> getMessageThreads() {
        return messageThreads;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setDnd(boolean dnd) {
        this.dnd = dnd;
    }

    public void setMessageThreads(List<String> messageThreads) {
        this.messageThreads = messageThreads;
    }

    @Override
    public String toString() {
        return "Person{" +
                "uId= '" + uId + '\'' +
                "firstName= '" + firstName + '\'' +
                ", lastName= '" + lastName + '\'' +
                ", group= '" + group + '\'' +
                ", relationshipStatus= '" + relationshipStatus + '\'' +
                ", dob= " + dob +
                ", position=' " + position + '\'' +
                ", emailAddress=' " + emailAddress + '\'' +
                ", interests=' " + interests + '\'' +
                ", imageUri=' " + imageUri + '\'' +
                ", dnd=' " + dnd + '\'' +
                '}';
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person();
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(group);
        dest.writeString(position);
        dest.writeSerializable(dob);
        dest.writeString(relationshipStatus);
        dest.writeString(emailAddress);
        dest.writeString(uId);
        dest.writeString(interests);
        dest.writeString(imageUri);
        dest.writeByte((byte) (dnd ? 1 : 0));
        dest.writeList(messageThreads);
        dest.writeString(hotelCheckedInto);
    }

    //Added by fw
    public String getHotelCheckedInto() {
        return hotelCheckedInto;
    }

    public void setHotelCheckedInto(String hotelCheckedInto) {
        this.hotelCheckedInto = hotelCheckedInto;
    }
}