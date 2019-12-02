package com.playover;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.playover.broadcast_receivers.InternetBroadcastReceiver;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.UserViewModel;

import java.util.Set;

public class MessagingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MessagingActivity";
    static FragmentManager messagingFragmentManager;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private AuthUserViewModel authVm;
    private String recipientUIDs = new String();
    private String messagerUIDs;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String onCreate = "onCreate";
        String onCreateMsg = "In On Create";
        Log.i(onCreate, onCreateMsg);
        setContentView(R.layout.activity_messaging);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        // Code to discover Bundle keys. Keys found: "from" "msgSequence" "msgThreadId"
//        if(b != null) {
//            Set<String> bundleKeys = b.keySet();
//            for (String key : bundleKeys) {
//                Log.i(TAG, key);
//            }
//        }

        if (b != null){
            if (b.containsKey("recipientUids")) {
                recipientUIDs = b.getString("recipientUids");
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
        NavigationView navigationView = findViewById(R.id.nav_view_messaging);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        transaction = fragmentManager.beginTransaction();
        if (recipientUIDs.length() == 0) {
            //Log.i("messages", "null");
            MessagingThreads_Fragment newMessageThreadsFragment = new MessagingThreads_Fragment();
            transaction.replace(R.id.containerMessaging, newMessageThreadsFragment, "Message Threads");
            transaction.commit();
        } else {
            Log.i("messages", "else it is");

            Bundle bundle = new Bundle();
            bundle.putString("recipientUid", recipientUIDs);
            MessagingBubbles_Fragment newMessagingBubblesFragment = new MessagingBubbles_Fragment();
            newMessagingBubblesFragment.setArguments(bundle);
            transaction.replace(R.id.containerMessaging, newMessagingBubblesFragment, "Messaging Bubbles");
            transaction.commit();
        }
    }

    public String getRecipientUID() {
        return recipientUIDs;
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
            Intent displayNoService = new Intent(MessagingActivity.this, NoInternet.class);
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


}
