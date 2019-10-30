package com.playover;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.playover.broadcast_receivers.InternetBroadcastReceiver;
import com.playover.viewmodels.AuthUserViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //member variables
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private final static int REQUEST_CODE_PERMISSIONS = 1;
    private AuthUserViewModel authVm;
    private BroadcastReceiver internetReceiver;
    static boolean shouldToast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check for permissions on log in
        requestPermissions();
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (InternetBroadcastReceiver.isNetworkAvailable(this)) {
            authVm = new AuthUserViewModel();
        }

        Landing_Fragment landing = new Landing_Fragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragments_main, landing, "Landing");
        transaction.commit();

        //attach receiver after activity has been built
        attachInternetActivity();
        if (InternetBroadcastReceiver.isNetworkAvailable(this)) {
            if (authVm.getUser() != null) {
                //go to check in
                Intent intent = new Intent(MainActivity.this, CheckIn.class);
                startActivity(intent);
            }
        }
    }


    //checking/requesting required permissions on app load
    private void requestPermissions() {
        final List<String> requiredAppPermissions = new ArrayList<>();
        requiredAppPermissions.add(Manifest.permission.INTERNET);
        requiredAppPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        requiredAppPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        requiredAppPermissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        requiredAppPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        for (int i = 0; i < requiredAppPermissions.size() - 1; i++) {
            if (ActivityCompat.checkSelfPermission(this, requiredAppPermissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{requiredAppPermissions.get(i)}, REQUEST_CODE_PERMISSIONS);
            }
        }

    }

    //check for internet connectivity(mobile and wifi)
    public void attachInternetActivity() {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        internetReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (InternetBroadcastReceiver.isNetworkAvailable(context)) {
                    if (shouldToast) {
                        //toast directions if connection restored
                        Toast.makeText(MainActivity.this, "Network Connection Restored!", Toast.LENGTH_SHORT).show();
                        shouldToast = false;
                    }

                } else {
                    //send user to page notifying them internet is required for app to work
                    shouldToast = true;
                    Intent displayNoService = new Intent(MainActivity.this, NoInternet.class);
                    startActivity(displayNoService);
                }
            }
        };
        //register receiver in activity
        registerReceiver(internetReceiver, intentFilter);
    }

    //unregister receiver on activity destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetReceiver);
    }

    //check permissions on launch.  prompt user if required permissions not granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    String[] permission = permissions[i].split("\\.");
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //Not interested in displaying Manifest.permission or empty strings to user
                        if (permission.length == 3 && !permission[2].isEmpty()) {
                            //if user declines to enable permission and clicks ignore
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                                    permissions[i])) {
                                Toast.makeText(MainActivity.this, permission[2] + " is required. Please go to settings and turn on!", Toast.LENGTH_LONG).show();

                            } else {
                                    Toast.makeText(MainActivity.this, permission[2] + " is required.  Please go to settings and turn on!", Toast.LENGTH_LONG).show();

                            }
                        }
                        //if string split was unsuccessful or they change how its returned on the next api release
                        else {
                            Toast.makeText(MainActivity.this, "That permission is required for App to work. Please go to settings and turn on!", Toast.LENGTH_LONG).show();

                        }
                    }
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // in case they pause the application and are logged in.
    //check for network on resume
    //receiver listener on fires when there is a
    //change in network state.
    @Override
    protected void onResume() {
        super.onResume();
        //if user not null
        if (InternetBroadcastReceiver.isNetworkAvailable(this)) {
            if (authVm != null) {
                if (authVm.getUser() != null) {
                    Intent intent = new Intent(MainActivity.this, CheckIn.class);
                    startActivity(intent);

                }
            } else {
                authVm = new AuthUserViewModel();
            }
        } else {
            goToNoInternet();
        }
    }

    // in case they pause the application and are logged in.
    //check for network on resume
    //receiver listener on fires when there is a
    //change in network state.
    // in case they restart the application and are logged in.
    @Override
    protected void onRestart() {
        super.onRestart();
        //if user not null
        if (InternetBroadcastReceiver.isNetworkAvailable(this)) {
            if (authVm != null) {
                if (authVm.getUser() != null) {
                    Intent intent = new Intent(MainActivity.this, CheckIn.class);
                    startActivity(intent);

                } else {
                    authVm = new AuthUserViewModel();
                }
            }
        } else {
            goToNoInternet();
        }
    }


    //intent to NoInternet
    public void goToNoInternet() {
        Intent displayNoService = new Intent(MainActivity.this, NoInternet.class);
        startActivity(displayNoService);
    }

    //adjust backstack and visibility of fragments.
    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStack();

        } else {
            super.onBackPressed();
        }
    }
}
