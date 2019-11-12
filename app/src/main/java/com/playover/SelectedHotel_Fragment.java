package com.playover;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.playover.models.Buddy;
import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.BuddiesViewModel;
import com.playover.viewmodels.HotelViewModel;
import com.playover.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

public class SelectedHotel_Fragment extends Fragment{

    TextView mTxtHotelName;
    TextView mTxtHotelAddress;
    TextView mTxtCheckoutSet;
    ExpandableListView mList;
    private String personToMessageUids = new String();
    private RecyclerView recyclerView;
    private static SelectedHotel_Fragment.ContentAdapter adapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private Button messageSelectedButton;
    // private Button messageAllButton;
    private Toolbar mTitle;
    static List<Person> mPeopleAlsoCheckedIn = new ArrayList<>();
    private HotelViewModel hotelVm = new HotelViewModel();
    private static HotelViewModel hotVm = new HotelViewModel();
    private static UserViewModel userVm = new UserViewModel();
    private static AuthUserViewModel authVm = new AuthUserViewModel();
    public static BuddiesViewModel budVm = new BuddiesViewModel();
    private long count;
    public static boolean isCheckedIn = false;
    private String hotelCheckedInto = null;
    private SearchView searchView;
    private static List<Person> filteredGuests;

    CharSequence query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        isCheckedIn = true;

        //enable search in the tool bar
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_selected_hotel, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_also_checked_in);
        mPeopleAlsoCheckedIn.clear();
        messageSelectedButton = v.findViewById(R.id.messageSelectedBtn);
        // messageAllButton = v.findViewById(R.id.messageAllBtn);
        try
        {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("HOTEL");
        }
        catch (Exception e)
        {
            Log.v("Couldn't find toolbar",e.getMessage());
        }

        messageSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentAdapter adapter = (ContentAdapter) recyclerView.getAdapter();
                    Iterator<String> messagingList = adapter.getMessagingList().iterator();

                    while(messagingList.hasNext()){
                        String mListTemp = messagingList.next();
                        personToMessageUids += mListTemp + ",";
                    }
                    // clean off the last comma from the message uids list
                    if(personToMessageUids.substring(personToMessageUids.length()-1).equals(",")) {
                        personToMessageUids = personToMessageUids.substring
                                (0, personToMessageUids.length() - 1);
                    }

                    Log.i("iPTMU", personToMessageUids);

                    // assuming there is one initially
                    if (personToMessageUids != null) {
                        Intent messagingIntent = new Intent(getActivity(), MessagingActivity.class);
                        messagingIntent.putExtra("recipientUids", personToMessageUids);
                        ContentAdapter.checkboxPosition = -1;
                        Log.i("messageThreadBundle: ", "create new group");
                        startActivity(messagingIntent);
                    }
                }
                catch (Exception e) {
                    Log.v("Exception",e.getMessage());
                }
            }
        });

//        messageAllButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try
//                {
//
//                    //Intent messaging = new Intent(getActivity(), Messaging_Bubbles_Activity.class);
//                    //startActivity(messaging);
//
//                }
//                catch (Exception e)
//                {
//                    Log.v("Exception",e.getMessage());
//                }
//            }
//        });

        mTxtHotelName = v.findViewById(R.id.txtHotelName);
        mTxtHotelAddress = v.findViewById(R.id.txtHotelAddress);
        mTxtCheckoutSet = v.findViewById(R.id.txtCheckoutSet);
        mTxtHotelName.setText(getArguments().get("hotel").toString());
        mTxtHotelAddress.setText(getArguments().get("address").toString());

        mTxtCheckoutSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = authVm.getUser().getUid();
                    hotelVm.findHotelCheckedInto(userId, (DataSnapshot hotel) ->
                    {
                        try
                        {
                            DataSnapshot snapshot = hotel.getChildren().iterator().next();
                            hotelCheckedInto = snapshot.getValue().toString();

                            //////////
                            //write db
                            //////////
                            hotelVm.removeGuest(userId, hotelCheckedInto);

                            CheckIn.setHotelCheckedInto(null);

                            hotelVm.clear();
                            authVm.clear();
                            userVm.clear();
                            //show the hotels recyclerview
                            isCheckedIn = false;

                            Fragment cur = getActivity().getSupportFragmentManager().findFragmentByTag("SelectedHotel");
                            if(null != cur && cur.isVisible())
                            {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }

                            getActivity().recreate();
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                    },null);
            }
        });



        userVm.clear();
        hotelVm.clear();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //read guests from db
        doRead(getArguments().getShort("pos"));

    }

    public boolean userIsBlocked(String userUid) {

        hotelVm.getUser(authVm.getUser().getUid(), (Person p) -> {

        } );
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        //inflate menu; adds items to the action bar if present
        inflater.inflate(R.menu.search_menu, menu);
        //associate searchable config with search view
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        setUpSearchView(searchItem);

        //old code
        //searchView = (SearchView) searchItem.getActionView();

        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//            @Override
//            public boolean onQueryTextSubmit(String query){
//                //Log.i("filter", mPeopleAlsoCheckedIn.toString());
//                List<Person> filteredPersons = getFilteredGuests(query);
//                //Log.i("filter", filteredGuests.toString());
//                updateRecyclerView(filteredPersons);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query){
//                List<Person> filteredPersons = getFilteredGuests(query);
//                updateRecyclerView(filteredPersons);
//                return true;
//            }
//
//        });
//

    }

    private void setUpSearchView(MenuItem searchView){

        SearchView searchMenuItem = (SearchView)searchView.getActionView();
        ImageView closeButton = (ImageView)searchMenuItem.findViewById(R.id.search_close_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Log.i("CloseButton sez: ", "Clicked!");
                                               searchMenuItem.setQuery("", false);

                                               //getFilteredGuests("");

                                               //searchMenuItem.setQuery("",false);
                                               //doRead(getArguments().getShort("pos"));
                                               searchMenuItem.clearFocus();
                                           }
                                       });




        RxSearchView.queryTextChanges(searchMenuItem)
                .doOnEach(notification->{
                    query = (CharSequence)notification.getValue();
                })
                .debounce(300,TimeUnit.MILLISECONDS)
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results->{
                    updateRecyclerView(getFilteredGuests(query.toString()));

                });
    }

    public List<Person> getFilteredGuests(String searchKeyword) {
        //filteredGuests.clear();
        if(searchKeyword.isEmpty()){
            doRead(getArguments().getShort("pos"));
        } else {
            //Log.i("filter", charSequence.toString());
            String filterPattern = searchKeyword.toLowerCase().trim();
            List<Person> filteredList = new ArrayList<>();
            //Log.i("filter", mPeopleAlsoCheckedIn.toString());
            for (Person p : mPeopleAlsoCheckedIn) {
                //Log.i("filter", p.toString());
                String email = p.getEmailAddress();
                //Log.i("filter", email);
                //email=email.substring(email.indexOf("@"+1, email.indexOf(".")));
                if(email.toLowerCase().contains(filterPattern)){
                    //Log.i("filter", p.toString());
                    filteredList.add(p);
                }
            }
            //Log.i("filter", filteredList.toString());
            filteredGuests = filteredList;

        }
        return filteredGuests;
    }

    public void refreshSearch(String s){
        updateRecyclerView(getFilteredGuests(s));
        Log.i("refresh", getFilteredGuests(s).toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.action_search){
            return true;
        }
        return getActivity().onOptionsItemSelected(item);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckBox checkbox;
        public TextView name;
        public TextView position;
        public ImageButton buddyStar;
        public TextView recipientUid;
        private ImageView thumbnail;
        public ImageView blockedIcon;
        public boolean buddy = false;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list_checked_in, parent, false));
            checkbox = itemView.findViewById(R.id.checkbox);
            name = itemView.findViewById(R.id.list_name);
            position = itemView.findViewById(R.id.list_position);
            buddyStar = itemView.findViewById(R.id.buddyStar);
            blockedIcon = itemView.findViewById(R.id.blockedPerson);
            recipientUid = itemView.findViewById(R.id.recipient_uid);
            thumbnail = itemView.findViewById(R.id.thumbnail);



            budVm.getBuddies(authVm.getUser().getUid(),
                    (ArrayList<Person> persons) -> {
                        //Log.i("misuse", persons.toString());



                        for(Person person : persons) {
                            //Log.i("buddy", String.valueOf("in for"));
                            if (person.getuId().equals(recipientUid.getText().toString())) {
                                buddyStar.setImageResource(R.drawable.ic_star_black_24dp);
                                buddy = true;
                                hotVm.getUser(authVm.getUser().getUid(), (Person curUser) -> {
                                    //Log.i("misuse", curUser.toString());
                                    HashMap<String, Buddy> buddies = curUser.getBuddies();
                                    if (buddies != null) {
                                        boolean isBuddy = buddies.containsKey(person.getuId());
                                        if (isBuddy) {
                                            boolean isBlocked = buddies.get(person.getuId()).getBlocked();

                                            Log.i("misuse", person.toString());
                                            Log.i("misuse", String.valueOf(isBlocked));
                                            if (isBlocked) {
                                                // show blocked image
                                                blockedIcon.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                    //Log.i("misuse", buddies.toString());
                                });
                            }
                        }
                    });


            //// start

//            if (p.getBuddies() == null) {
//                //Log.i("buddy", p.toString());
//                if ( ! authVm.getUser().getUid().equals(p.getuId())) {
//                    //Log.i("misuse", p.toString());
//                    guests.add(p);
//                    if (dS.getChildrenCount() == count) {
//
//                        updateRecyclerView(guests);
//                    }
//                }
//            } else {
//                HashMap<String, Buddy> buddies = p.getBuddies();
//
//                boolean isBuddy = buddies.containsKey(authVm.getUser().getUid());
//                //Log.i("buddy", String.valueOf(isBuddy));
//                //Log.i("buddy", String.valueOf(p));
//                boolean isBlocked = false;
//                //Log.i("buddy", "huh");
//                if (isBuddy) {
//                    Log.i("buddy", "huh");
//                    isBlocked = buddies.get(authVm.getUser().getUid()).getBlocked();
//                }
//                //Log.i("buddy", String.valueOf(isBlocked));
//                //Log.i("buddy", String.valueOf("crap"));
//                //Log.i("buddy", ));
//
//                if (!p.getuId().equals("null")) {
//                    //don't add own user to list
//                    if (!authVm.getUser().getUid().equals(p.getuId()) && !isBlocked) {
//
//                        guests.add(p);
//                    }
//                    if (dS.getChildrenCount() == count) {
//
//                        updateRecyclerView(guests);
//                    }
//                }
//            }

            //// stop

            buddyStar.setOnClickListener(v -> {

                if (!buddy) {
                    buddy = true;
                    buddyStar.setImageResource(R.drawable.ic_star_black_24dp);
                    budVm.putBuddy(authVm.getUser().getUid(), mPeopleAlsoCheckedIn.get(getAdapterPosition()).getuId(), false, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            } else {

                            }
                        }
                    });
                } else {
                    buddy = false;
                    buddyStar.setImageResource(R.drawable.ic_star_border_black_24dp);
                    budVm.deleteBuddy(authVm.getUser().getUid(), mPeopleAlsoCheckedIn.get(getAdapterPosition()).getuId(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Hotels onComplete: remove buddy", "successful");
                            } else {

                            }
                        }
                    });
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = CheckIn.mFragMgr.beginTransaction();
            Bundle args = new Bundle();
            args.putString(Constants.KEY_BUDUID, mPeopleAlsoCheckedIn.get(getAdapterPosition()).getuId());
            args.putString(Constants.KEY_BUDNAME, name.getText().toString().trim());

            //go to selected profile
            Profile_Fragment profile = new Profile_Fragment();
            profile.setArguments(args);
            transaction.replace(R.id.containerCheckIn, profile,"Profile");
            transaction.addToBackStack("Profile");
            transaction.commit();
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<SelectedHotel_Fragment.ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static int LENGTH;
        private final Context c;
        private List<Buddy> mBuddyList;
        private List<String> messagingList = new ArrayList<>();
        public static int checkboxPosition = -1;

        public ContentAdapter(Context context) {
            c = context;
        }

        @Override
        public SelectedHotel_Fragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater l = LayoutInflater.from(parent.getContext());
            View v = l.inflate(R.layout.item_list_checked_in, parent, false);
            return new SelectedHotel_Fragment.ViewHolder(l, parent);
        }

        @Override
        public void onBindViewHolder(SelectedHotel_Fragment.ViewHolder holder, final int position) {
            try {
                //Log.i("misuse", mPeopleAlsoCheckedIn.toString());
                String name = mPeopleAlsoCheckedIn.get(position).getFirstName() +
                        " " + mPeopleAlsoCheckedIn.get(position).getLastName();
                String occupation = mPeopleAlsoCheckedIn.get(position).getPosition();
                String personCheckInUid = mPeopleAlsoCheckedIn.get(position).getuId();

                if(! name.equalsIgnoreCase("null")) {
                    holder.name.setText(name);
                    holder.position.setText(occupation);
                    holder.recipientUid.setText(personCheckInUid);
                    if(checkboxPosition == position && !messagingList.contains(personCheckInUid)){
                        holder.checkbox.setChecked(true);
                        messagingList.add(personCheckInUid);
                    }
                    else if(checkboxPosition == position && messagingList.contains(personCheckInUid)){
                        holder.checkbox.setChecked(false);
                        messagingList.remove(personCheckInUid);
                    }
                    else if (messagingList.contains(personCheckInUid)){
                        holder.checkbox.setChecked(true);
                    }
                    else{
                        holder.checkbox.setChecked(false);
                        messagingList.remove(personCheckInUid);
                    }
                    holder.checkbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkboxPosition != position) {
                                checkboxPosition = position;
                                notifyDataSetChanged();
                            } else {
                                checkboxPosition = -1;
                                notifyDataSetChanged();
                            }
                        }
                    });

                    if (mPeopleAlsoCheckedIn.get(position).getImageUri() != null) {
                        String dbImage = mPeopleAlsoCheckedIn.get(position).getImageUri();
                        Picasso mPicasso = Picasso.get();
                        mPicasso.load(dbImage)
                                .resize(30, 30)
                                .centerCrop()
                                .into(holder.thumbnail);
                    } else {
                        holder.thumbnail.setImageResource(R.drawable.profile_avatar_placeholder);
                    }
                }

            } catch (NullPointerException npe) {

            } catch (IndexOutOfBoundsException iobe) {

            }
        }

        public interface ContentAdapterListener{
            void onPersonSelected(Person p);

    }

        public List<String> getMessagingList() {
            return messagingList;
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }

        public static void setLENGTH(int LENGTH) {
            ContentAdapter.LENGTH = LENGTH;
        }
    }

//    private void prepareListData() {
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<String>>();
//
//        // Adding child data
//        listDataHeader.add("STATUS: CHECKED IN");
//
//        // Adding child data
//        List<String> options = new ArrayList<>();
//        //options.add("Checked-in with Custom message");
//        //options.add("Checked-in, but Do not disturb");
//        options.add("Check-out");
//
//        listDataChild.put(listDataHeader.get(0), options); // Header, Child data
//    }


    private void doRead(short positionInHotelsList) {
        List<Person> guests = new ArrayList<>();

        try

        {
            hotelVm.getGuests(ListHotels_Fragment.mPlacesList.get((int) positionInHotelsList), (DataSnapshot dS) -> {
                guests.clear();
                count = 0;
                for (DataSnapshot x : dS.getChildren()) {
                    //Log.i("misuse", x.toString());
                    hotelVm.getUser(x.getKey(), (Person p) ->
                    {
                        //Log.i("misuse", String.valueOf("in for"));
                        //Log.i("misuse", p.toString());
                        count++;
                        try
                        {
                            //Log.i("buddy", p.getBuddies().toString());
                            // get buddies, if the authVm.getUser().getUid() matches and blocked is true, don't add
                            //Log.i("misuse", p.toString());
                            if (p.getBuddies() == null) {
                                //Log.i("buddy", p.toString());
                                if ( ! authVm.getUser().getUid().equals(p.getuId())) {
                                    //Log.i("misuse", p.toString());
                                    guests.add(p);
                                    if (dS.getChildrenCount() == count) {

                                        updateRecyclerView(guests);
                                    }
                                }
                            } else {
                                HashMap<String, Buddy> buddies = p.getBuddies();

                                boolean isBuddy = buddies.containsKey(authVm.getUser().getUid());
                                //Log.i("buddy", String.valueOf(isBuddy));
                                //Log.i("buddy", String.valueOf(p));
                                boolean isBlocked = false;
                                //Log.i("buddy", "huh");
                                if (isBuddy) {
                                    Log.i("buddy", "huh");
                                    isBlocked = buddies.get(authVm.getUser().getUid()).getBlocked();
                                }
                                //Log.i("buddy", String.valueOf(isBlocked));
                                //Log.i("buddy", String.valueOf("crap"));
                                //Log.i("buddy", ));

                                if (!p.getuId().equals("null")) {
                                    //don't add own user to list
                                    if (!authVm.getUser().getUid().equals(p.getuId()) && !isBlocked) {

                                        guests.add(p);
                                    }
                                    if (dS.getChildrenCount() == count) {

                                        updateRecyclerView(guests);
                                    }
                                }
                            }
                        }
                        catch (NullPointerException npe)
                        {

                        }
                    });
                }
            }, null, null);
        }
        catch (Exception e)
        {

        }
    }

    public void updateRecyclerView(List<Person> people) {
        SelectedHotel_Fragment.mPeopleAlsoCheckedIn.clear();
        SelectedHotel_Fragment.mPeopleAlsoCheckedIn.addAll(people);
        adapter = new SelectedHotel_Fragment.ContentAdapter(recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setLENGTH(mPeopleAlsoCheckedIn.size());
//        Log.i("filter", mPeopleAlsoCheckedIn.toString());
    }

    private String NewGroupName(String groupName){ return(groupName);}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ContentAdapter.checkboxPosition = -1;
        personToMessageUids = "";
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ListHotels_Fragment.mPlacesList.clear();
        ContentAdapter.checkboxPosition = -1;
        personToMessageUids = "";
    }
}