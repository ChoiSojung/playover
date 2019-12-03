package app.playover;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import app.playover.viewmodels.AuthUserViewModel;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_settings);
        fragmentManager = getSupportFragmentManager();
        authVm = new AuthUserViewModel();
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawer = findViewById(R.id.settings_content);
        NavigationView navigationView = findViewById(R.id.nav_view_settings);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        transaction = fragmentManager.beginTransaction();
        Settings_Fragment newSettings = new Settings_Fragment();
        transaction.replace(R.id.containerSettings, newSettings, "settings");
        transaction.commit();

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
                        if (authVm.getUser() == null) {
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
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_messaging:
                Intent messaging = new Intent(getApplicationContext(), MessagingActivity.class);
                startActivity(messaging);
                break;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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
