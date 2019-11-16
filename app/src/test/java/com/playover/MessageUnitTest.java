package com.playover;

import com.playover.models.Message;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageUnitTest {

    private Message underTest;
    private String testContent = "test message";
    private String testSenderUid = "11223344";
    private String testRecipientUid = "55667788"; //deprecated
    private Object testTimestamp = "1543803194831";
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

}
