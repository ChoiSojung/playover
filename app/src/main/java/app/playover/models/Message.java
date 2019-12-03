package app.playover.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Message implements Parcelable {

    private String messageUID;
    private String content;
    private String senderUID;
    private String recipientUID;
    private Object timestamp;

    public Message() {

    }

    public Message(String content, String senderUID, String recipientUID, Object timestamp) {
        this.content = content;
        this.senderUID = senderUID;
        this.recipientUID = recipientUID;
        this.timestamp = timestamp;
    }

    public Message(Parcel in) {
        content = in.readString();
        senderUID = in.readString();
        recipientUID = in.readString();
        timestamp = in.readLong();
    }

    @Exclude
    public long getTimestampLong(){
        return (long)timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("messageUID", messageUID);
        result.put("content", content);
        result.put("senderUID", senderUID);
        result.put("recipientUID", recipientUID);
        result.put("timestamp", timestamp);
        return result;
    }

    public String getMessageUID() {
        return messageUID;
    }

    public void setMessageUID(String messageUID) {
        this.messageUID = messageUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getRecipientUID() {
        return recipientUID;
    }

    public void setRecipientUID(String recipientUID) {
        this.recipientUID = recipientUID;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public Object generateTimestamp() {
        return ServerValue.TIMESTAMP;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message();
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageUID);
        dest.writeString(content);
        dest.writeString(senderUID);
        dest.writeString(recipientUID);
        dest.writeValue(timestamp);
    }
}
