package appdev.playover.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Hotel implements Parcelable{
    private String name;
    private String address;
    private String lat;
    private String lon;
    private Map<String,String> guestUids;

    //only for personal location
    private String distance;

    public Hotel()
    {
        //required for db
    }

    public Hotel(String name, String address, String latitude, String longitude, String dist) {
        this.name = name;
        this.address = address;
        this.lat = latitude;
        this.lon = longitude;
        this.distance = dist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address.replace("<br/>","\n");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Object getGuestUids() {
        return guestUids;
    }

    public void setGuestUids(Map<String,String> guestUids) {
        this.guestUids = guestUids;
    }

    public String getDistance() {
        return distance + " mi";
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", guestUids='" + guestUids + '\'' +
                '}';
    }

    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel source) {
            return new Hotel();
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getAddress());
        dest.writeString(getLat());
        dest.writeString(getLon());
        dest.writeString(getGuestUids().toString());
    }
}