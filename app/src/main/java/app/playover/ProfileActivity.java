package app.playover;


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
import app.playover.R;

import app.playover.broadcast_receivers.InternetBroadcastReceiver;
import app.playover.viewmodels.AuthUserViewModel;


public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private AuthUserViewModel authVm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String onCreate = "onCreate";
        String onCreateMsg = "In On Create";
        Log.i(onCreate, onCreateMsg);
        setContentView(R.layout.activity_profile);
        fragmentManager = getSupportFragmentManager();
        authVm = new AuthUserViewModel();
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        drawer = findViewById(R.id.profile_content);
        NavigationView navigationView = findViewById(R.id.nav_view_profile);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        transaction = fragmentManager.beginTransaction();
        Profile_Fragment newProfile = new Profile_Fragment();
        transaction.replace(R.id.containerProfile, newProfile, "Profile");
        transaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                drawer.closeDrawer(GravityCompat.START);
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

    //check for network on resume
    //receiver listener on fires when there is a
    //change in network state.
    @Override
    public void onRestart() {
        super.onRestart();
        isNetworkAvailable();
    }

    //clear auth eventlisteners
    @Override
    public void onPause(){
        super.onPause();
        authVm.clear();
    }

    //check for network on resume
    //receiver listener on fires when there is a
    //change in network state.
    @Override
    public void onResume() {
        super.onResume();
        isNetworkAvailable();
    }

    //check for networks
    public void isNetworkAvailable() {
        boolean networks = InternetBroadcastReceiver.isNetworkAvailable(getApplicationContext());
        if (!networks) {
            Intent displayNoService = new Intent(ProfileActivity.this, NoInternet.class);
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
