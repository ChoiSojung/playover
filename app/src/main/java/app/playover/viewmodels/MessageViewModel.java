package app.playover.viewmodels;

import com.google.firebase.database.DataSnapshot;
import app.playover.datamodels.MessageDataModel;
import app.playover.models.UserMessageThread;

import java.util.function.Consumer;

public class MessageViewModel {

    private MessageDataModel messageDataModel;

    public MessageViewModel() {
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
