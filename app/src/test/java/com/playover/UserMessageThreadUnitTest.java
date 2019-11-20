package com.playover;

import com.playover.models.UserMessageThread;
import com.playover.models.Message;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserMessageThreadUnitTest {

    private UserMessageThread underTest;
    private List<Message> testMessageList = new ArrayList<>();
    private String testMessageThreadUID = "123456";
    private String testMessageGroupName = "test Group Name";
    private String testMessageGroupUIDS = "firstUid,secondUid,thridUid";
    private Message testMessage = new Message("test message content", "987654", "112233", "1543803194831");

    @After
    public void tearDown() {
        underTest = null;
        testMessageList.clear();
    }

    @Test
    public void testSecondConstructor()  {
        testMessageList.add(testMessage);
        underTest = new UserMessageThread(testMessageGroupName, testMessageThreadUID, testMessageGroupUIDS, testMessage);
        assertEquals("123456", underTest.getMessageThreadUID());
        assertEquals(testMessageList, underTest.getMessages());
    }

    @Test
    public void testThirdConstructor()  {
        testMessageList.add(testMessage);
        underTest = new UserMessageThread(testMessageGroupName, testMessageThreadUID, testMessageGroupUIDS, testMessage);
        assertEquals("123456", underTest.getMessageThreadUID());
        assertEquals(testMessageList, underTest.getMessages());
    }

    @Test
    public void testGettersAndSetters() {
        underTest = new UserMessageThread();
        testMessageList.add(testMessage);
        underTest.setMessages(testMessageList);
        underTest.setMessageThreadUID(testMessageThreadUID);
        underTest.setMessageGroupName(testMessageGroupName);
        underTest.setMessageGroupUIDs(testMessageGroupUIDS);
        String actualMessageThreadUid = underTest.getMessageThreadUID();
        String actualMessageGroupName = underTest.getMessageGroupName();
        String actualMessageGroupUIDs = underTest.getMessageGroupUIDs();
        List<Message> actualMessageList = underTest.getMessages();
        assertEquals(testMessageList, actualMessageList);
        assertEquals(testMessageThreadUID, actualMessageThreadUid);
        assertEquals(testMessageGroupName,actualMessageGroupName);
        assertEquals(testMessageGroupUIDS,actualMessageGroupUIDs);
    }

    @Test
    public void testAddMessageListNotNull() {
        underTest = new UserMessageThread();
        testMessageList.add(testMessage);
        List<Message> actualMessageList = new ArrayList<>();
        underTest.setMessages(actualMessageList);
        underTest.addMessage(testMessage);
        assertEquals(testMessageList, underTest.getMessages());
    }

    @Test
    public void testAddMessageListNull() {
        underTest = new UserMessageThread();
        testMessageList.add(testMessage);
        List<Message> actualMessageList = null;
        underTest.setMessages(actualMessageList);
        assertEquals(null, underTest.getMessages());
        underTest.addMessage(testMessage);
        assertEquals(testMessageList, underTest.getMessages());
    }

    @Test
    public void testSerializer(){
        underTest = new UserMessageThread(testMessageGroupName, testMessageThreadUID, testMessageGroupUIDS, testMessage);
        String expectResult =
                "Thread: "+testMessageGroupName+"\n"+testMessageThreadUID+"\n"+testMessageGroupUIDS
                        +"\n"+"Message: "+testMessage.getContent()+"\n";
        assertEquals(expectResult, underTest.toString());
    }

}
