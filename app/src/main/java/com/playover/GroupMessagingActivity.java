package com.playover;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.playover.broadcast_receivers.InternetBroadcastReceiver;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.UserViewModel;

public class GroupMessagingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static FragmentManager messagingFragmentManager;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private AuthUserViewModel authVm;
    private String recipientUID;
    private String myUID;
    private UserViewModel userViewModel;
    private Button CreateGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("messages", "fuck this");
        setContentView(R.layout.activity_group_messaging);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null){
            if (b.containsKey("recipientUid")) {
                recipientUID = b.getString("recipientUid");
            }
        }
        fragmentManager = getSupportFragmentManager();
        messagingFragmentManager = getSupportFragmentManager();
        authVm = new AuthUserViewModel();
        Toolbar toolbar = findViewById(R.id.toolbar_messaging);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawer = findViewById(R.id.messaging_content);
        NavigationView navigationView = findViewById(R.id.nav_view_group_messaging);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        transaction = fragmentManager.beginTransaction();
        if (recipientUID == null) {
            //Log.i("messages", "null");
            GroupMessagingThreads_Fragment newGroupMessageThreadsFragment = new GroupMessagingThreads_Fragment();
            transaction.replace(R.id.containerGroupMessaging, newGroupMessageThreadsFragment, "Message Threads");
            transaction.commit();
        } else {
            Log.i("messages", "else it is");
            Bundle bundle = new Bundle();
            bundle.putString("recipientUid", recipientUID);
            MessagingBubbles_Fragment newMessagingBubblesFragment = new MessagingBubbles_Fragment();
            newMessagingBubblesFragment.setArguments(bundle);
            transaction.replace(R.id.containerGroupMessaging, newMessagingBubblesFragment, "Messaging Bubbles");
            transaction.commit();
        }


        CreateGroupButton = (Button) findViewById(R.id.create_group);

        CreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateGroup();
                Log.i(":==>", "Create New Group");
            }
        });
    }

    public String getRecipientUID() {
        return recipientUID;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent newProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(newProfile);
                break;
            case R.id.nav_check_in:
                Intent newAct = new Intent(getApplicationContext(), CheckIn.class);
                startActivity(newAct);
                break;
            case R.id.nav_sign_out:
                authVm.signOutUser(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(authVm.getUser()== null) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case R.id.nav_buddies:
                Intent buddy = new Intent(getApplicationContext(), Buddies_Activity.class);
                startActivity(buddy);
                break;
            case R.id.nav_discounts:
                Intent discounts = new Intent(getApplicationContext(), Discounts.class);
                startActivity(discounts);
                break;
            case R.id.nav_settings:
                Intent newSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(newSettings);
                break;
            case R.id.nav_messaging:
                Intent messaging = new Intent(getApplicationContext(), MessagingActivity.class);
                startActivity(messaging);
                break;
            case R.id.nav_group_messaging:
                Intent groupMessaging = new Intent(getApplicationContext(), GroupMessagingActivity.class);
                startActivity(groupMessaging);
                break;
        }
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        isNetworkAvailable();
    }

    @Override
    public void onPause(){
        super.onPause();
        authVm.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        isNetworkAvailable();
    }

    public void isNetworkAvailable() {
        boolean networks = InternetBroadcastReceiver.isNetworkAvailable(getApplicationContext());
        if (!networks) {
            Intent displayNoService = new Intent(GroupMessagingActivity.this, NoInternet.class);
            startActivity(displayNoService);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void CreateGroup() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(GroupMessagingActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");

        final EditText groupNameField = new EditText(GroupMessagingActivity.this);
        //groupNameField.setHint("playover everything");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName)){
                    Toast.makeText(GroupMessagingActivity.this, "Group must have a name"
                            , Toast.LENGTH_SHORT);
                } else {
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(String groupName){
        Toast.makeText(GroupMessagingActivity.this, "Group " + groupName + " will be created below"
                , Toast.LENGTH_SHORT);
    }
}
