package com.playover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playover.models.Message;
import com.playover.models.MessageBubble;
import com.playover.models.Person;
import com.playover.models.UserMessageThread;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.MessageViewModel;
import com.playover.viewmodels.UserViewModel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagingBubbles_Fragment extends Fragment {

    private FragmentManager fragmentManager;
    private DatabaseReference mDatabase;
    private MessageViewModel messageViewModel;
    private AuthUserViewModel authVm;
    private String recipientUID;
    private ListView listView;
    private View btnSend;
    private EditText editText;
    boolean myMessage;
    private List<MessageBubble> messageBubbles;
    private ArrayAdapter<MessageBubble> adapter;
    private TextView textViewGroupName;
    private TextView textViewUsers;
    private UserViewModel userViewModel;
    private String groupName;
    private String threadUid;
    private String groupUids;
    private String[] reciptUids;
    private String senderUID;
    private String myUID;
    private UserMessageThread userMessageThread;
    private String username;

    public MessagingBubbles_Fragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_messagingbubbles, container, false);
        if (getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        assert getArguments() != null;
        recipientUID = getArguments().getString("recipientUid");
        messageBubbles = new ArrayList<>();
        authVm = new AuthUserViewModel();
        userViewModel = new UserViewModel();
        listView = rootView.findViewById(R.id.list_msg);
        btnSend = rootView.findViewById(R.id.btn_chat_send);
        editText = rootView.findViewById(R.id.msg_type);
        textViewGroupName = rootView.findViewById(R.id.group_name);
        textViewUsers = rootView.findViewById(R.id.users);
        senderUID = authVm.getUser().getUid();
        myUID = senderUID;
        threadUid = generateMessageThreadUID(senderUID, recipientUID);
        groupUids = generateGroupUIDs(senderUID, recipientUID);
        reciptUids = recipientUID.split(",");
        if (reciptUids.length > 1){
            groupName = getArguments().getString("groupName");
        } else {
            groupName = "1-1 Group";
        }
        Log.i("group ids", groupUids);
        userViewModel.getUser(myUID,
                (Person user) -> {
                    textViewGroupName.setText(groupName);
                    username = user.getFirstName() + " " + user.getLastName();
                    textViewUsers.setText(textViewUsers.getText().toString() + myUID + ":" + username + ",");
                    user.addThread(threadUid);
                    try {
                        userViewModel.addMessageThreadToUser(user);
                    }catch(Exception e) {
                        Log.e("Exception occurred adding message thread to sender: ", e.getMessage());
                    }
                });

        for(String uid : reciptUids) {
            userViewModel.getUser(uid,
                    (Person user) -> {
                        username = user.getFirstName() + " " + user.getLastName();
                        textViewUsers.setText(textViewUsers.getText().toString() + uid + ":" + username + ",");
                        user.addThread(threadUid);
                        try {
                            userViewModel.addMessageThreadToUser(user);
                        } catch (Exception e) {
                            Log.e("Exception occurred adding message thread to recipient: ", e.getMessage());
                        }
                    });
        }

        //set ListView adapter first
        adapter = new MessageAdapter(getActivity(), R.layout.left_message_bubble, messageBubbles);
        listView.setAdapter(adapter);

        messageViewModel = new MessageViewModel();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getMessages();

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input a message", Toast.LENGTH_SHORT).show();
                } else {
                    messageBubbles.clear();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    //add message to list
                    String content = editText.getText().toString();
                    Message message = new Message();
                    message.setContent(content);
                    message.setSenderUID(senderUID);
                    message.setRecipientUID(recipientUID);
                    message.setTimestamp(message.generateTimestamp());
                    editText.setText("");
                    if (userMessageThread == null) {
                        userMessageThread = new UserMessageThread(groupName, threadUid, groupUids, message);
                    } else {
                        userMessageThread.addMessage(message);
                    }
                    messageViewModel.putMessage(userMessageThread);
                }
            }
        });
    return rootView;
    }

    private void getMessages() {
        messageViewModel.getMessages(threadUid,
                (UserMessageThread thread) -> {
                    if (thread != null) {
                        if (thread.getMessages() != null) {
                            messageBubbles.clear();
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            List<Message> messages = thread.getMessages();
                            HashMap<String, String> uidNameMap = new HashMap<>();
                            String[] uNPairs = textViewUsers.getText().toString().split(",");
                            for (String pair : uNPairs){
                                String[] uidName = pair.split(":");
                                uidNameMap.put(uidName[0], uidName[1]);
                            }
                            for (Message message : messages) {
                                String UID = message.getMessageUID();
                                String userName = uidNameMap.get(message.getSenderUID());
                                //Object ts = message.getTimestamp();
                                String timestamp = " time";
                                String content = message.getContent();
                                if (message.getSenderUID().equals(myUID)) {
                                    myMessage = true;
                                } else {
                                    myMessage = false;
                                }
                                MessageBubble messageBubble =
                                        new MessageBubble(userName, timestamp, content, myMessage);
                                messageBubbles.add(messageBubble);
                            }
                            userMessageThread = thread;
                        }
                    } else {
                        Log.i("Messaging Bubble Activity: ", "No messages found in DB");
                    }
                    adapter.notifyDataSetChanged();
                }
        );
    }

    // hash uids to get Message Thread UID using MD5 algorithm and standard algo.
    private String generateMessageThreadUID(String uid1, String uid2) {
        String hashedMTUID = getMD5(uid1+uid2);
        Log.i("Message Thread ID:", hashedMTUID);
        return hashedMTUID;
    }

    private static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger num = new BigInteger(1, messageDigest);

            String hashtext = num.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateGroupUIDs(String senderUID, String recipientUID) {
        return senderUID + "," + recipientUID;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
