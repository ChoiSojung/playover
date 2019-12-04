package com.playover;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.playover.models.Discount;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.DiscountsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Discounts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private AuthUserViewModel authVm;
    private LocationManager locationManager;
    private Double longitude;
    private Double latitude;
    private String cityName = "";
    private String stateName = "";
    private List<Address> addresses;
    private ListDiscount_Fragment listDiscounts;
    private DiscountsViewModel discVm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String inClass = "inClass";
        String inClassMsg = "inClass Discounts";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create View";
        Log.i(onCreate, onCreateMsg);
        listDiscounts = new ListDiscount_Fragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.containerDiscounts, listDiscounts, "ListDiscounts");
        transaction.commit();
        discVm = new DiscountsViewModel();
        authVm = new AuthUserViewModel();
        setContentView(R.layout.activity_discounts);
        Toolbar toolbar = findViewById(R.id.toolbar_discounts);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
        }
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
        }

        drawer = findViewById(R.id.discounts_content);
        NavigationView navigationView = findViewById(R.id.nav_view_discount);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        locationManager = (LocationManager) Objects.requireNonNull(getApplicationContext()).getSystemService((Context.LOCATION_SERVICE));
        boolean fineLocationPermission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        checkLocation();
        if (!fineLocationPermission || !coarseLocationPermission) {
            ActivityCompat.requestPermissions(Discounts.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(Discounts.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //every 60 secconds or 2000 meters
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 60000, 2000, locationListenerNetwork);
        if (location != null) {
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() == 1) {
                    cityName = addresses.get(0).getLocality().trim().toUpperCase();
                    stateName = addresses.get(0).getAdminArea().trim();
                    if (!isTestMode()) {
                        ProgressDialog discDialog = new ProgressDialog(this);
                        discDialog.setTitle("Getting Discounts...");
                        discDialog.show();
                        discVm.getDiscounts(stateName, cityName,
                                (ArrayList<Discount> discounts) -> {
                                    discDialog.dismiss();
                                    if (discounts != null && discounts.size() > 0) {
                                        listDiscounts.updateData(discounts, cityName, stateName);
                                    }
                                }

                        );
                    } else {
                        ProgressDialog discDialog = new ProgressDialog(this);
                        discDialog.setTitle("Getting Discounts...");
                        discDialog.show();
                        discVm.getDiscounts("Washington", "SEATTLE",
                                (ArrayList<Discount> discounts) -> {
                                    discDialog.dismiss();
                                    if (discounts != null && discounts.size() > 0) {
                                        listDiscounts.updateData(discounts, "SEATTLE", "Washington");
                                    }
                                }

                        );
                    }
                } else {
                    Log.e("location error: ", "Unable to get location from geocoder!");
                    String locatedAt = getString(R.string.get_city_state_error);
                    Toast.makeText(Discounts.this, locatedAt, Toast.LENGTH_SHORT).show();

                }
            } catch (Exception ex) {
                Log.e("location error: ", ex.getMessage());
                String locatedAt = getString(R.string.get_city_state_error);
                Toast.makeText(Discounts.this, locatedAt, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Discounts.this, "Unable to get location!", Toast.LENGTH_SHORT).show();
        }

    }

    private static boolean isTestMode() {
        boolean isTest;
        try {
            Class.forName("com.playover.DiscountsTest");
            isTest = true;
        } catch (Exception ex) {
            isTest = false;
        }
        return isTest;
    }

    private boolean checkLocation() {
        if (!isLocationEnabled()) {
                Toast.makeText(Discounts.this, "Please enable device location!", Toast.LENGTH_SHORT).show();

        }
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() == 1) {
                    cityName = addresses.get(0).getLocality().trim().toUpperCase();
                    stateName = addresses.get(0).getAdminArea().trim();
                }
                DiscountsViewModel discVm = new DiscountsViewModel();
                if (!isTestMode()) {
                    discVm.getDiscounts(stateName, cityName,
                            (ArrayList<Discount> discounts) -> {
                                if (discounts != null && discounts.size() > 0) {
                                    listDiscounts.updateData(discounts, cityName, stateName);

                                } else {
                                    listDiscounts.setNoDiscounts();
                                }

                            }
                    );
                } else {
                    discVm.getDiscounts("Washington", "SEATTLE",
                            (ArrayList<Discount> discounts) -> {
                                if (discounts != null && discounts.size() > 0) {
                                    listDiscounts.updateData(discounts, "SEATTLE", "Washington");

                                } else {
                                    listDiscounts.setNoDiscounts();
                                }

                            }
                    );
                }
            } catch (Exception ex) {
                //unable to use geocoder or nothing is returned
                Log.e("location error: ", ex.getMessage());
                String locatedAt = getString(R.string.get_city_state_error);
                Toast.makeText(Discounts.this, locatedAt, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };

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
            case R.id.nav_discounts:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_buddies:
                Intent buddy = new Intent(getApplicationContext(), Buddies_Activity.class);
                startActivity(buddy);
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStack();
            if (!isTestMode()) {
                discVm.getDiscounts(stateName, cityName,
                        (ArrayList<Discount> discounts) -> {
                            if (discounts != null && discounts.size() > 0) {
                                listDiscounts.updateData(discounts, cityName, stateName);
                                listDiscounts.setNoDiscounts();
                            }

                        });
            } else {
                discVm.getDiscounts("Washington", "SEATTLE",
                        (ArrayList<Discount> discounts) -> {
                            if (discounts != null && discounts.size() > 0) {
                                listDiscounts.updateData(discounts, "SEATTLE", "Washington");
                                listDiscounts.setNoDiscounts();
                            }

                        });
            }

        } else

        {
            super.onBackPressed();
        }
    }

}
