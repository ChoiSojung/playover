package com.playover;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
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

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MessagingBubbles_Fragment extends Fragment {

    private FragmentManager fragmentManager;
    //private DatabaseReference mDatabase;
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
    private String threadUid;
    private String groupUids;
    private String[] reciptUids;
    private String senderUID;
    private String myUID;
    private UserMessageThread userMessageThread;
    private String username;
    private Calendar calendar = Calendar.getInstance();
    private TimeZone timeZone = calendar.getTimeZone();

    public MessagingBubbles_Fragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String inClass = "inClass";
        String inClassMsg = "inClass MessagingBubbles Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create";
        Log.i(onCreate, onCreateMsg);
        final View rootView = inflater.inflate(R.layout.fragment_messagingbubbles, container, false);
        if (getActivity() != null) {
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        assert getArguments() != null;
        messageBubbles = new ArrayList<>();
        authVm = new AuthUserViewModel();
        userViewModel = new UserViewModel();
        listView = rootView.findViewById(R.id.list_msg);
        btnSend = rootView.findViewById(R.id.btn_chat_send);
        editText = rootView.findViewById(R.id.msg_type);
        textViewGroupName = rootView.findViewById(R.id.group_name);
        textViewUsers = rootView.findViewById(R.id.users);
        recipientUID = getArguments().getString("recipientUid");
        if (recipientUID.endsWith(",")){
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            recipientUID = recipientUID.substring(0, recipientUID.length()-1);
        }
        if (recipientUID.startsWith(",")){
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            recipientUID = recipientUID.substring(1, recipientUID.length());
        }
//        Log.i("recipientUID", "is " + recipientUID);
        senderUID = authVm.getUser().getUid();
        myUID = senderUID;
        if (getArguments().containsKey("threadUid")){
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            threadUid = getArguments().getString("threadUid");
//            Log.i ("aLREADYthreadUid",threadUid);
        } else {
            threadUid = generateMessageThreadUID(senderUID, recipientUID);
        }
        groupUids = senderUID + "," + recipientUID;
//        Log.i("group ids", groupUids);
        reciptUids = recipientUID.split(",");
        for (String i : reciptUids){
//            Log.i("reciptUids", "is " + i);
        }
        userViewModel.getUser(myUID,
                (Person user) -> {
                    username = user.getFirstName() + " " + user.getLastName();
                    textViewUsers.setText(
                            textViewUsers.getText().toString() + myUID + ":" + username + ",");
                });

        for(String uid : reciptUids) {
            userViewModel.getUser(uid,
                    (Person user) -> {
                        username = user.getFirstName() + " " + user.getLastName();
                        textViewUsers.setText(
                                textViewUsers.getText().toString() + uid + ":" + username + ",");
//                        Log.i("reciptUidName",  uid + ":" + username + ",");
                    });
        }

        //set ListView adapter first
        adapter = new MessageAdapter(getActivity(), R.layout.left_message_bubble, messageBubbles);
        listView.setAdapter(adapter);

        messageViewModel = new MessageViewModel();
        //mDatabase = FirebaseDatabase.getInstance().getReference();

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
//                        Log.i("onClickGroupName", "this is " + textViewGroupName.getText().toString());
                        userMessageThread = new UserMessageThread(
                                textViewGroupName.getText().toString()
                                , threadUid
                                , groupUids
                                , message);
                        userViewModel.getUser(myUID,
                                (Person user) -> {
                                    user.addThread(threadUid);
                                    try {
                                        userViewModel.addMessageThreadToUser(user);
                                    }catch(Exception e) {
                                        //Log.e("Exception occurred adding message thread to sender: ", e.getMessage());
                                    }
                                });

                        for(String uid : reciptUids) {
                            userViewModel.getUser(uid,
                                    (Person user) -> {
                                        user.addThread(threadUid);
                                        try {
                                            userViewModel.addMessageThreadToUser(user);
                                        } catch (Exception e) {
                                            //Log.e("Exception occurred adding message thread to recipient: ", e.getMessage());
                                        }
                                    });
                        }
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
                    HashMap<String, String> uidNameMap = new HashMap<>();
                    String[] uNPairs = textViewUsers.getText().toString().split(",");
//                    Log.i("textViewUsers", "is " + textViewUsers.getText().toString());
                    for (String pair : uNPairs){
                        String[] uidName = pair.split(":");
                        for(String i : uidName){
//                            Log.i("uidName", "is " + i);
                        }
                        uidNameMap.put(uidName[0], uidName[1]);
                    }
                    for (String i : uidNameMap.keySet()){
//                        Log.i("uidNameMap", i + " : " + uidNameMap.get(i));
                    }
                    if (thread != null) {
                        String enterIf = "EnterIf";
                        String enterIfMsg = "Entered If Statement";
                        Log.i(enterIf, enterIfMsg);
                        if (thread.getMessages() != null) {
                            messageBubbles.clear();
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            List<Message> messages = thread.getMessages();
                            // put group name in message window
                            if(reciptUids.length == 1){
                                textViewGroupName.setText(
                                        uidNameMap.get(myUID)
                                                + " and "
                                                + uidNameMap.get(reciptUids[0]));
                            } else {
                                textViewGroupName.setText(thread.getMessageGroupName());
                            }
                            for (Message message : messages) {
                                String UID = message.getMessageUID();
                                String userName = uidNameMap.get(message.getSenderUID());
//                                Log.i("bubbleUsername", "this is " + userName);

                                //parse Firebase Severvalue timeStamp use method found in https://exceptionshub.com/firebase-timestamp-to-date-and-time.html
                                /*Object ts = message.getTimestamp();*/
                                Long epochValue = message.getTimestampLong();
                                DateFormat format = new SimpleDateFormat("dd/MM HH:mm");
                                format.setTimeZone(timeZone);
                                Date epochDate = new Date(epochValue);
                                String timestamp= format.format(epochDate);

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
                        // generate new group name if is a new thread
                        if(reciptUids.length == 1){
                            textViewGroupName.setText(
                                    uidNameMap.get(myUID)
                                            + " and "
                                            + uidNameMap.get(reciptUids[0]));
                        } else {
                            RequestNewGroupName();
                        }
//                        Log.i("Messaging Bubble Activity: ", "No messages found in DB");
                    }
                    adapter.notifyDataSetChanged();
                }
        );
    }

    private String generateMessageThreadUID(String senderUID, String recipientUID) {
        String MTUID;
        reciptUids = recipientUID.split(",");
        if (reciptUids.length > 1){
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            // all group chat begin with G-, and is saved under key groupMessageThread
            MTUID = "G-" + getMD5(senderUID + "," + recipientUID);
        } else {
            // backward compatible threadId for one-on-one messaging
            int compare = senderUID.compareTo(recipientUID);
            if(compare < 0){
                MTUID = senderUID + recipientUID;
            } else {
                MTUID = recipientUID + senderUID;
            }
        }

//        Log.i("MessageThreadID:", MTUID);
        return MTUID;
    }

    // hash uids to get Message Thread UID using MD5 algorithm and standard algo.
    private static String getMD5(String input) {
        //sort all uids so the same group people will stay in the same group chat
        String[] arr = input.split(",");
//        Log.i("unsorted uids", "this " + input);
        input = "";
        Arrays.sort(arr);
        for (String id : arr){
//            Log.i("sorted", id);
            input += id;
        }
//        Log.i("sorted String", input);
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

    private void RequestNewGroupName(){
        Context context = getActivity();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AlertDialog);
        builder.setTitle("Enter Group Name: ");

        final EditText groupNameField = new EditText(context);
        groupNameField.setHint("It is fun to PlayOver");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    textViewGroupName.setText(getDefaultGroupName());
                    Toast.makeText(context, "Group must have a name"
                            , Toast.LENGTH_SHORT);
                } else {
                    textViewGroupName.setText(groupName);
//                    Log.i ("requestNewName", "this is " + textViewGroupName.getText().toString());
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textViewGroupName.setText(getDefaultGroupName());
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private String getDefaultGroupName(){
        DateFormat format = new SimpleDateFormat("dd/MM/yy");
        String currentDate = format.format(calendar.getTime());
        String output = "Group created on " + currentDate;
        return output;
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
