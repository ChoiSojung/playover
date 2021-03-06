package com.playover;

import android.os.Bundle;
import android.os.Parcel;

import com.playover.models.Message;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

//@RunWith(JUnit4.class)
public class MessageUnitTest {

    private Message underTest;
    private String testContent = "test message";
    private String testSenderUid = "11223344";
    private String testRecipientUid = "55667788"; //deprecated, only use for reading old threads
    private Object testTimestamp = (long) 1574655832;
    private String testMessageUid = "99001122";

    @After
    public void tearDown() {
        underTest = null;
    }

    @Test
    public void testConstructor() {
        underTest = new Message(testContent, testSenderUid, testRecipientUid, testTimestamp);
        assertEquals(testContent, underTest.getContent());
        assertEquals(testRecipientUid, underTest.getRecipientUID());
        assertEquals(testSenderUid, underTest.getSenderUID());
        assertEquals(testTimestamp, underTest.getTimestamp());
    }

    @Test
    public void testSettersAndGetters() {
        underTest = new Message();
        underTest.setContent(testContent);
        underTest.setRecipientUID(testRecipientUid);
        underTest.setSenderUID(testSenderUid);
        underTest.setTimestamp(testTimestamp);
        underTest.setMessageUID(testMessageUid);
        String actualContent = underTest.getContent();
        String actualRecipientUid = underTest.getRecipientUID();
        String actualSenderUid = underTest.getSenderUID();
        Object actualTimestamp = underTest.getTimestamp();
        String actualMessageUid = underTest.getMessageUID();
        assertEquals(testContent, actualContent);
        assertEquals(testRecipientUid, actualRecipientUid);
        assertEquals(testSenderUid, actualSenderUid);
        assertEquals(testTimestamp, actualTimestamp);
        assertEquals(testMessageUid, actualMessageUid);
    }

    @Test
    public void testGetTimestampLong(){
        underTest = new Message();
        underTest.setTimestamp(testTimestamp);
        Object actualTimestamp = underTest.getTimestamp();
        Long epochValue = (long) actualTimestamp;
        Long timestampLong = underTest.getTimestampLong();
        assertEquals(epochValue, timestampLong);
    }

    @Test
    public void testToMap(){
        underTest = new Message();
        underTest.setContent(testContent);
        underTest.setRecipientUID(testRecipientUid);
        underTest.setSenderUID(testSenderUid);
        underTest.setTimestamp(testTimestamp);
        underTest.setMessageUID(testMessageUid);
        Object actualContent = underTest.getContent();
        Object actualRecipientUid = underTest.getRecipientUID();
        Object actualSenderUid = underTest.getSenderUID();
        Object actualTimestamp = underTest.getTimestamp();
        Object actualMessageUid = underTest.getMessageUID();
        Map<String, Object> result = new HashMap<>();
        result = underTest.toMap();
        assertEquals(result.get("messageUID"), actualMessageUid);
        assertEquals(result.get("content"), actualContent);
        assertEquals(result.get("senderUID"), actualSenderUid);
        assertEquals(result.get("recipientUID"), actualRecipientUid);
        assertEquals(result.get("timestamp"), actualTimestamp);
    }


/*    @Test
    public void testMessageIsParcelable(){
        Message message = new Message(testContent, testSenderUid, testRecipientUid, testTimestamp);

        Parcel parcel = Parcel.obtain();
        message.writeToParcel(parcel, message.describeContents());
        parcel.setDataPosition(0);

        Message createdFromParcel = Message.CREATOR.createFromParcel(parcel);
        assertEquals(createdFromParcel.getContent(), testContent);
        assertEquals(createdFromParcel.getSenderUID(), testSenderUid);
        assertEquals(createdFromParcel.getRecipientUID(), testRecipientUid);
        assertEquals(createdFromParcel.getTimestamp(), testTimestamp);

    }*/
}
