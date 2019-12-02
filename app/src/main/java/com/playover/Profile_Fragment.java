package com.playover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.playover.models.Discount;
import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.BuddiesViewModel;
import com.playover.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;



public class Profile_Fragment extends Fragment {

    private Button editProfileBtn, sendMessageBtn, buddyMessageBtn, blockUserBtn, reportMisuseBtn;
    private ImageView dndImage;
    private ImageView profileImage, tempImage;
    private ImageButton starBtn;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private UserViewModel userVm;
    private AuthUserViewModel authVm;
    private BuddiesViewModel buddyVm;
    private String userUid;
    private TextView profileName, profileGroup, profilePosition, profileRelationshipText, profileInterest;
    public Boolean buddy = false;
    private Bundle b = new Bundle();

    public Profile_Fragment() {
        //Required empty constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        String onCreate = "onCreate";
        String onCreateMsg = "In On Create";
        Log.i(onCreate, onCreateMsg);
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfileBtn = rootView.findViewById(R.id.editBtn);
        sendMessageBtn = rootView.findViewById(R.id.ProfileMessageBtn);
        blockUserBtn = rootView.findViewById(R.id.blockUserButton);
        reportMisuseBtn = rootView.findViewById(R.id.reportMisuseButton);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        profileImage = rootView.findViewById(R.id.profileImage);
        profileName = rootView.findViewById(R.id.profileName);
        profileGroup = rootView.findViewById(R.id.profileGroup);
        profilePosition = rootView.findViewById(R.id.profilePosition);
        profileRelationshipText = rootView.findViewById(R.id.profileRelationshipText);
        profileInterest = rootView.findViewById(R.id.profileInterest);
        tempImage = rootView.findViewById(R.id.tempImage);
        dndImage = rootView.findViewById(R.id.dndImage);
        starBtn = rootView.findViewById(R.id.profileBuddyStar);
        //buddyMessageBtn = rootView.findViewById(R.id.buddyMessageBtn);

        userVm = new UserViewModel();
        authVm = new AuthUserViewModel();
        buddyVm = new BuddiesViewModel();

        if (this.getContext() instanceof ProfileActivity) {
            editProfileBtn.setVisibility(View.VISIBLE);
            if (authVm != null && userVm != null && authVm.getUser() != null) {
                userVm.getUser(authVm.getUser().getUid(),
                        (Person user) -> {
                            if (user != null) {
                                String list = "users list";
                                String list2 = "users List";
                                String list3 = "users List";
                                Log.i(list, user.toString());
                                Log.i(list2, user.toString());
                                Log.i(list3, user.toString());
                                Log.i("users list: ", user.toString());
                                if (!TextUtils.isEmpty(user.getFirstName()) && !TextUtils.isEmpty(user.getLastName())) {
                                    String setName = user.getFirstName() + " " + user.getLastName();
                                    profileName.setText(setName);
                                    b.putString(Constants.KEY_FNAME, user.getFirstName());
                                    b.putString(Constants.KEY_LNAME, user.getLastName());
                                } else {
                                    b.putString(Constants.KEY_FNAME, "");
                                    b.putString(Constants.KEY_LNAME, "");
                                }
                                if (!TextUtils.isEmpty(user.getGroup())) {
                                    profileGroup.setText(user.getGroup());
                                    b.putString(Constants.KEY_GROUP, user.getGroup());
                                } else {
                                    b.putString(Constants.KEY_GROUP, "");
                                }
                                if (!TextUtils.isEmpty(user.getPosition())) {
                                    profilePosition.setText(user.getPosition());
                                    b.putString(Constants.KEY_POSITION, user.getPosition());
                                } else {
                                    b.putString(Constants.KEY_POSITION, "");
                                }
                                if (!TextUtils.isEmpty(user.getRelationshipStatus())) {
                                    profileRelationshipText.setText(user.getRelationshipStatus());
                                    b.putString(Constants.KEY_RELATIONSHIP, user.getRelationshipStatus());
                                } else {
                                    b.putString(Constants.KEY_RELATIONSHIP, "");
                                }
                                if (!TextUtils.isEmpty(user.getInterests())) {
                                    profileInterest.setText(user.getInterests());
                                    b.putString(Constants.KEY_INTERESTS, user.getInterests());
                                } else {
                                    b.putString(Constants.KEY_INTERESTS, "");
                                }
                                if (!TextUtils.isEmpty(user.getImageUri())) {
                                    b.putString(Constants.KEY_IMAGEURI, user.getImageUri());
                                    tempImage.setVisibility(View.INVISIBLE);
                                    profileImage.setVisibility(View.VISIBLE);

                                    String dbImage = user.getImageUri();
                                    Picasso mPicasso = Picasso.get();
                                    mPicasso.load(dbImage)
                                            .resize(500, 500)
                                            .centerCrop()
                                            .into(profileImage);
                                } else {
                                    tempImage.setImageResource(R.drawable.profile_avatar_placeholder);
                                    tempImage.setVisibility(View.VISIBLE);
                                    profileImage.setVisibility(View.INVISIBLE);
                                    b.putString(Constants.KEY_IMAGEURI, "");
                                }
                                if (user.isDnd()) {
                                    dndImage.setVisibility(View.VISIBLE);
                                }

                                b.putString(Constants.KEY_DOB, user.getDob());

                            }
                        }
                );
            }

            editProfileBtn.setOnClickListener(v -> {
                transaction = fragmentManager.beginTransaction();
                EditProfile_Fragment newEditProfile = new EditProfile_Fragment();
                newEditProfile.setArguments(b);
                transaction.replace(R.id.containerProfile, newEditProfile, "EditProfile");
                transaction.addToBackStack("EditProfile");
                transaction.commit();
            });
        }

        if (this.getContext() instanceof CheckIn && getArguments() != null || this.getContext() instanceof Buddies_Activity && getArguments() != null) {
            sendMessageBtn.setVisibility(View.VISIBLE);
            blockUserBtn.setVisibility(View.VISIBLE);
            reportMisuseBtn.setVisibility(View.VISIBLE);
            starBtn.setVisibility(View.VISIBLE);

            sendMessageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent messagingIntent = new Intent(getActivity(), MessagingActivity.class);
                        messagingIntent.putExtra("recipientUids", getArguments().getString(Constants.KEY_BUDUID));
                        startActivity(messagingIntent);
                    }
                    catch (Exception e) {
                        // Log.v("Exception",e.getMessage());
                    }
                }
            });

            buddyVm.getBuddies(authVm.getUser().getUid(),
                    (ArrayList<Person> persons) -> {
                        for(Person person : persons) {
                            if (person.getuId().equals(getArguments().getString(Constants.KEY_BUDUID))) {
                                starBtn.setImageResource(R.drawable.ic_star_black_24dp);
                                buddy = true;
                            }
                        }
                    });

            blockUserBtn.setOnClickListener(v -> {
                Context context = this.getContext();
//                CharSequence text = "User Blocked";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//
//                toast.setGravity(50, 300, 900);
//                toast.show();
                buddyVm.putBuddy(authVm.getUser().getUid(), getArguments().getString(Constants.KEY_BUDUID), true,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.i("block buddy: ", "worked!");
                                    //display message to user
                                } else {
                                    Log.i("blcok buddy", "didn't work!");
                                    //display message to user
                                }
                            }
                        });
            });

            // Build out onClickListener for report Misuse button
            reportMisuseBtn.setOnClickListener(v-> {
                String selectedUserUid = getArguments().getString(Constants.KEY_BUDUID);
                String selectedUserName = getArguments().getString(Constants.KEY_BUDNAME);
                String reportersUserName = authVm.getUser().getUid();
                transaction = fragmentManager.beginTransaction();
                ReportMisuse_Fragment newMisuseReport = new ReportMisuse_Fragment();
                Bundle args = new Bundle();
                args.putString(Constants.KEY_BUDUID, selectedUserUid);
                args.putString(Constants.KEY_BUDNAME, selectedUserName);
                args.putString(Constants.KEY_UID, reportersUserName);
                newMisuseReport.setArguments(args);
                // we are not replacing the profile here!
                transaction.replace(R.id.containerCheckIn, newMisuseReport, "ReportMisuse");
                transaction.addToBackStack("ReportMisuse");
                transaction.commit();
            });

            starBtn.setOnClickListener(v -> {
                if (!buddy) {
                    buddy = true;
                    starBtn.setImageResource(R.drawable.ic_star_black_24dp);
                    buddyVm.putBuddy(authVm.getUser().getUid(), getArguments().getString(Constants.KEY_BUDUID), false,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("add buddy: ", "worked!");
                                        //display message to user
                                    } else {
                                        // Log.i("add buddy", "didn't work!");
                                        //display message to user
                                    }
                                }
                            });
                } else {
                    buddy = false;
                    starBtn.setImageResource(R.drawable.ic_star_border_black_24dp);
                    buddyVm.deleteBuddy(authVm.getUser().getUid(), getArguments().getString(Constants.KEY_BUDUID), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("delete buddy: ", "worked!");
                                //display message to user
                            } else {
                                //Log.i("delete buddy", "didn't work!");
                                //display message to user
                            }
                        }

                    });
                }
            });

            userUid = getArguments().getString(Constants.KEY_BUDUID);
            if (userVm != null) {
                userVm.getUser(userUid,
                        (Person user) -> {
                            if (user != null) {
                                Log.i("users list: ", user.toString());
                                if (!TextUtils.isEmpty(user.getFirstName()) && !TextUtils.isEmpty(user.getLastName())) {
                                    String setName = user.getFirstName() + " " + user.getLastName();
                                    profileName.setText(setName);
                                }
                                if (!TextUtils.isEmpty(user.getGroup())) {
                                    profileGroup.setText(user.getGroup());
                                }
                                if (!TextUtils.isEmpty(user.getPosition())) {
                                    profilePosition.setText(user.getPosition());
                                }
                                if (!TextUtils.isEmpty(user.getRelationshipStatus())) {
                                    profileRelationshipText.setText(user.getRelationshipStatus());
                                }
                                if (!TextUtils.isEmpty(user.getInterests())) {
                                    profileInterest.setText(user.getInterests());
                                }
                                if (!TextUtils.isEmpty(user.getImageUri())) {
                                    tempImage.setVisibility(View.INVISIBLE);
                                    profileImage.setVisibility(View.VISIBLE);

                                    String dbImage = user.getImageUri();
                                    Picasso mPicasso = Picasso.get();
                                    mPicasso.load(dbImage)
                                            .resize(500, 500)
                                            .centerCrop()
                                            .into(profileImage);
                                } else {
                                    tempImage.setImageResource(R.drawable.profile_avatar_placeholder);
                                    tempImage.setVisibility(View.VISIBLE);
                                    profileImage.setVisibility(View.INVISIBLE);
                                }
                                if (user.isDnd()) {
                                    dndImage.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                );
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        String onAttach = "onAttach";
        String attachMsg = "In On Attach";
        Log.i(onAttach, attachMsg);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        String onDetach = "onDetach";
        String detachMsg = "In On Detach";
        Log.i(onDetach, detachMsg);
        super.onDetach();
    }

}
