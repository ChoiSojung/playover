package com.playover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.BuddiesViewModel;
import com.playover.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Buddies_Fragment extends Fragment {
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Bundle bud;
    private Button allMessageButton;
    private Button messageButton;
    private TextView noBuddies;
    private ArrayList<Person> mDataset = new ArrayList<>();
    private SearchView searchView;
    private BuddiesViewModel budVm;
    private AuthUserViewModel authVm;
    private UserViewModel userVm;
    private ArrayList<Person> p = new ArrayList<>();
    private View.OnClickListener listener;
    private Context c;

    public Buddies_Fragment() {
        //Required empty constructor

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initDB();
        View v = inflater.inflate(R.layout.fragment_list_buddies, container, false);
        noBuddies = v.findViewById(R.id.noBuddies);
        noBuddies.setVisibility(View.VISIBLE);
        recyclerView = v.findViewById(R.id.buddies_recycleView);

        //left here for the next person in case they want to use it
    /*    allMessageButton = v.findViewById(R.id.message_all);
        allMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (msg.getExtras() != null ) {
                        msg.getExtras();
                        Intent profile = new Intent(getActivity(), MessagingActivity.class);
                        profile.putExtra("uid", msg.getDataString());
                        startActivity(profile);

                    }else{
                        Intent profile = new Intent(getActivity(), MessagingActivity.class);
                        startActivity(profile);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), " Something went wrong... Please try again", Toast.LENGTH_LONG).show();


                }
            }

            });*/

        messageButton = v.findViewById(R.id.message_sel);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buddyToMessageUids = new String();
                Intent message = new Intent(getActivity(), MessagingActivity.class);
                Buddies_Fragment.ContentAdapter adapter = (Buddies_Fragment.ContentAdapter) recyclerView.getAdapter();
                Iterator<String> messagingList = adapter.getMessagingList().iterator();

                while(messagingList.hasNext()){
                    String mListTemp = messagingList.next();
                    buddyToMessageUids += mListTemp + ",";
                }

                message.putExtra("recipientUids", buddyToMessageUids);
                startActivity(message);

            }
        });

        //left here for the next person in case they want to use it
        /*    searchView = v.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                budVm.firebaseSearch(query,
                        (ArrayList<Person> user) -> {
                            p = user;
                            ContentAdapter searching = new ContentAdapter(p);
                            layoutManager = new LinearLayoutManager((getContext()));
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(searching);
                        });
                return false;
            }

            //filter as you type -- not building for now
            @Override
            public boolean onQueryTextChange(String newText) {
                budVm.firebaseSearch(newText,
                        (ArrayList<Person> users) -> {


                return false;
            }
        });*/

        return v;
    }

    public void initDB() {
        budVm = new BuddiesViewModel();
        authVm = new AuthUserViewModel();
        budVm.getBuddies(authVm.getUser().getUid(),
                (ArrayList<Person> uId) -> {
                    p = uId;
                    ContentAdapter adapter = new ContentAdapter(p);
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    noBuddies();
                });
    }

    public void noBuddies() {
        if (p.size() == 0) {
            noBuddies.setVisibility(View.VISIBLE);
        } else {
            noBuddies.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView first, last, position;
        public TextView recipientUid;
        private Person mBud;
        private CheckBox checkbox;
        private ImageView thumbnail;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list_buddies, parent, false));
            checkbox = itemView.findViewById(R.id.checkbox);
            first = itemView.findViewById(R.id.list_first);
            last = itemView.findViewById(R.id.list_last);
            position = itemView.findViewById(R.id.list_position);
            recipientUid = itemView.findViewById(R.id.recipient_uid);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View v) {
            getAdapterPosition();
            Person person = p.get(getAdapterPosition());
            bud = new Bundle();
            bud.putString(Constants.KEY_BUDUID, person.getuId());
            fragmentManager = getFragmentManager();
            transaction = fragmentManager.beginTransaction();
            Profile_Fragment profile = new Profile_Fragment();
            profile.setArguments(bud);
            transaction.replace(R.id.containerBuddies, profile, "Buddy Profile");
            transaction.addToBackStack("Buddy Profile");
            transaction.commit();
        }
    }


    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private int LENGTH;
        private ArrayList<Person> mDataset;
        private List<String> messagingList = new ArrayList<>();
        public int checkboxPosition = -1;

        public ContentAdapter(ArrayList<Person> personArrayList) {
            this.mDataset = personArrayList;
            LENGTH = 0;
        }

        public List<String> getMessagingList() {
            return messagingList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater l = LayoutInflater.from(parent.getContext());
            View v = l.inflate(R.layout.item_list_buddies, parent, false);
            return new Buddies_Fragment.ViewHolder(l, parent);
        }

        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {

                holder.mBud = mDataset.get(position);
                String buddyUid = mDataset.get(position).getuId();
                if (holder.mBud.getImageUri() != null) {
                    String dbImage = holder.mBud.getImageUri();
                    Picasso mPicasso = Picasso.get();
                    mPicasso.load(dbImage)
                            .resize(30, 30)
                            .centerCrop()
                            .into(holder.thumbnail);
                } else {
                    holder.thumbnail.setImageResource(R.drawable.profile_avatar_placeholder);
                }
                holder.first.setText(String.format("%s", holder.mBud.getFirstName()));
                holder.last.setText(String.format("%s", holder.mBud.getLastName()));
                holder.position.setText(String.format("%s", holder.mBud.getPosition()));
                holder.recipientUid.setText(String.format("%s", holder.mBud.getuId()));
                if(checkboxPosition == position && !messagingList.contains(buddyUid)){
                    holder.checkbox.setChecked(true);
                    messagingList.add(buddyUid);
                }
                else if(checkboxPosition == position && messagingList.contains(buddyUid)){
                    holder.checkbox.setChecked(false);
                    messagingList.remove(buddyUid);
                }
                else if (messagingList.contains(buddyUid)){
                    holder.checkbox.setChecked(true);
                }
                else{
                    holder.checkbox.setChecked(false);
                    messagingList.remove(buddyUid);
                }
                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.checkbox.isChecked()) {
                            if (checkboxPosition != position) {
                                checkboxPosition = position;
                                notifyDataSetChanged();
                            } else {
                                checkboxPosition = -1;
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

            } catch (NullPointerException npe) {
                Log.e("onBindViewHolder Error: ", npe.getMessage());
            } catch (IndexOutOfBoundsException iobe) {

            }
        }

        @Override
        public int getItemCount() {
            if (mDataset != null) {
                LENGTH = mDataset.size();
            }
            return LENGTH;
        }
    }
}





