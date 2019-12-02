package com.playover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.playover.models.Person;
import com.playover.models.UserMessageThread;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.UserViewModel;
import com.playover.viewmodels.MessageViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessagingThreads_Fragment extends Fragment {

    protected List<String> messageThreads = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagingThreads_Fragment.ContentAdapter adapter;
    private UserViewModel userViewModel;
    private MessageViewModel messageViewModel;
    private AuthUserViewModel authVm;
    private UserMessageThread userMessageThread;
    private Context context;

    public MessagingThreads_Fragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String onCreate = "onCreate";
        String onCreateMsg = "In On Create";
        Log.i(onCreate, onCreateMsg);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_messagingthreads, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        userViewModel = new UserViewModel();
        authVm = new AuthUserViewModel();
        messageViewModel = new MessageViewModel();
        getMessageThreads();
        return v;
    }

    private void getMessageThreads() {
        userViewModel.getUser(authVm.getUser().getUid(),
                (Person user) -> {
                    if (user != null) {
                        if (user.getMessageThreads() != null) {
                            messageThreads = user.getMessageThreads();
                        } else {
                            Toast.makeText(getActivity(), "You have no messages.", Toast.LENGTH_LONG).show();
                        }
                    }
                    adapter = new MessagingThreads_Fragment.ContentAdapter(recyclerView.getContext(), messageThreads);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                    divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.thread_divider));
                    recyclerView.addItemDecoration(divider);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        messageThreads.clear();
        adapter.notifyDataSetChanged();
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        messageThreads.clear();
        getMessageThreads();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView thumbnail;
        public TextView name;
        public TextView gUids;
        public TextView uid;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.thread_list, parent, false));
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.list_name);
            gUids = itemView.findViewById(R.id.group_uids);
            uid = itemView.findViewById(R.id.uid);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                MessagingActivity.messagingFragmentManager.popBackStack();
                FragmentTransaction transaction = MessagingActivity.messagingFragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("threadUid", uid.getText().toString());
                bundle.putString("recipientUid", gUids.getText().toString());
                MessagingBubbles_Fragment newMessagingBubblesFragment = new MessagingBubbles_Fragment();
                newMessagingBubblesFragment.setArguments(bundle);
                transaction.replace(R.id.containerMessaging, newMessagingBubblesFragment).addToBackStack(null);
                transaction.commit();
            }
            catch (Exception e) {
                Log.v("Exception",e.getMessage());
            }
        }
    }


    public static class ContentAdapter extends RecyclerView.Adapter<MessagingThreads_Fragment.ViewHolder> {
        List<String> messageThreads;
        private int length;
        private Context c;

        public ContentAdapter(Context context, List<String> messageThreads) {
            c = context;
            this.messageThreads = messageThreads;
            length = messageThreads.size();
        }

        @Override
        public MessagingThreads_Fragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater l = LayoutInflater.from(parent.getContext());
            View v = l.inflate(R.layout.thread_list, parent, false);
            return new MessagingThreads_Fragment.ViewHolder(l, parent);
        }


        @Override
        public void onBindViewHolder(MessagingThreads_Fragment.ViewHolder holder, final int position) {
            String threadUid = messageThreads.get(position);
//            Log.i("ThreadUid: ", "this is " + threadUid);
            UserViewModel userViewModel = new UserViewModel();
            AuthUserViewModel authVm = new AuthUserViewModel();
            MessageViewModel messageViewModel = new MessageViewModel();
            String uid = authVm.getUser().getUid();
//            Log.i("MyUid", uid);
            String recipientUid = threadUid.replaceFirst(uid,"");
//            Log.i("RecipientUID: " , recipientUid);
            // check if thread is an one-on-one thread (mUid == uid) for backward compactibility
            String mUid = threadUid.replaceFirst(recipientUid, "");
//            Log.i("mUid", mUid);
            if (mUid.equals(uid)) {
                try {
                    userViewModel.getUser(recipientUid,
                            (Person user) -> {
                                if (user.getImageUri() != null) {
                                    String dbImage = user.getImageUri();
                                    Picasso mPicasso = Picasso.get();
                                    mPicasso.load(dbImage)
                                            .resize(30, 30)
                                            .centerCrop()
                                            .into(holder.thumbnail);
                                } else {
                                    holder.thumbnail.setImageResource(R.drawable.profile_avatar_placeholder);
                                }
                                String fullName = user.getFirstName() + " " + user.getLastName();
                                holder.name.setText(fullName);
                                holder.uid.setText(threadUid);
                                holder.gUids.setText(recipientUid);
                                /*Log.i("messageTtreadUid", "this is " + threadUid);
                                Log.i("messageTrecipientuid", "this is " + recipientUid);*/
                            });

                } catch (NullPointerException npe) {
                    Log.e("Message Thead Fragment Exception: ", npe.getMessage());
                }
            } else {
                messageViewModel.getMessages(threadUid,
                        (UserMessageThread thread) -> {
                            String groupName = thread.getMessageGroupName();
                            String groupIds = thread.getMessageGroupUIDs();
                            Log.i("groupMessageGroup", "this is " + groupIds);
                            holder.name.setText(groupName);
                            String reciptUid = groupIds.replaceFirst(uid,"");
                            reciptUid = reciptUid.replace(",,",",");
                            /*Log.i("groupMessageRecipt", "this is " + reciptUid);*/
                            holder.gUids.setText(reciptUid);
                            holder.uid.setText(threadUid);
                            holder.thumbnail.setImageResource(R.drawable.ic_group_messaging_48dp);
                        });
            }
            }



        @Override
        public int getItemCount() {
            return length;
        }

    }


}



