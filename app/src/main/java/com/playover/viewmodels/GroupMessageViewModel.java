package com.playover.viewmodels;

import com.google.firebase.database.DataSnapshot;
import com.playover.datamodels.MessageDataModel;
import com.playover.models.UserMessageThread;

import java.util.function.Consumer;

public class GroupMessageViewModel {

    private MessageDataModel messageDataModel;

    public GroupMessageViewModel() {
        messageDataModel = new MessageDataModel();
    }

    public void putMessage(UserMessageThread thread)  {
        messageDataModel.putMessageThread(thread);
    }

    public void getMessages(String uId, Consumer<UserMessageThread> responseCallback) {
        messageDataModel.getMessageThread(uId,
                (DataSnapshot dataSnapshot) -> {
                    if (dataSnapshot.exists()) {
                        UserMessageThread thread = new UserMessageThread();
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                            thread = messageSnapshot.getValue(UserMessageThread.class);
                        }
                        responseCallback.accept(thread);
                    } else {
                        responseCallback.accept(null);
                    }
                },
                (databaseError -> System.out.println("Error reading message thread: " + databaseError))
        );
    }

    public void clear() {
    }

}
