package com.playover.models;

import java.util.ArrayList;
import java.util.List;

public class UserMessageThread {

    private String messageThreadUID;
    private String messageGroupUIDs;
    private String messageGroupName;
    private List<Message> messages;

    public UserMessageThread() {
    }

    public UserMessageThread(
            String messageGroupName, String messageThreadUID
            , String messageGroupUIDs, Message message) {
        this.messageGroupName = messageGroupName;
        this.messageThreadUID = messageThreadUID;
        this.messageGroupUIDs = messageGroupUIDs;
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    public UserMessageThread(
            String messageGroupName, String messageThreadUID
            , String messageGroupUIDs, List<Message> messages) {
        this.messageGroupName = messageGroupName;
        this.messageGroupUIDs = messageGroupUIDs;
        this.messageThreadUID = messageThreadUID;
        this.messages = messages;
    }

    public String getMessageGroupName() { return messageGroupName; }

    public void setMessageGroupName(String messageGroupName) {
        this.messageGroupName = messageGroupName;
    }

    public String getMessageThreadUID() {
        return messageThreadUID;
    }

    public void setMessageThreadUID(String messageThreadUID) {
        this.messageThreadUID = messageThreadUID;
    }

    public String getMessageGroupUIDs() {
        return messageGroupUIDs;
    }

    public void setMessageGroupUIDs(String messageGroupUIDs){
        this.messageGroupUIDs = messageGroupUIDs;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Thread: ");
        sb.append(messageGroupName);
        sb.append("\n");
        sb.append(messageThreadUID);
        sb.append("\n");
        sb.append(messageGroupUIDs);
        sb.append("\n");
        for (Message message : messages) {
            sb.append("Message: ");
            sb.append(message.getContent());
            sb.append("\n");
        }
        return sb.toString();
    }
}
