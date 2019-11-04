package com.playover;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.playover.broadcast_receivers.InternetBroadcastReceiver;
import com.playover.models.Hotel;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.HotelViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CheckIn extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    //A global fragment manager
    static FragmentManager mFragMgr;

    //location properties
    LocationManager locationManager;
    double longitudeNetwork, latitudeNetwork;

    //static ViewPager viewPager;
    private ProgressDialog mProgress;
    private AuthUserViewModel authVm;
    private HotelViewModel hotelVm;
    private static String hotelCheckedInto;
    private Hotel obj = null;
    String userId = null;

    private boolean firstTime;
    private long hotelCheckedIncount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHotelCheckedInto(null);
        firstTime = true;

        setContentView(R.layout.activity_check_in);

        mFragMgr = getSupportFragmentManager();
        authVm = new AuthUserViewModel();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawer = findViewById(R.id.main_content);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        try
        {
             authVm = new AuthUserViewModel();
             hotelVm = new HotelViewModel();
             userId = authVm.getUser().getUid();
             hotelVm.findHotelCheckedInto(userId, (DataSnapshot result) -> {

                 //Log.i("misuse", String.valueOf(result.getChildrenCount()));
                 hotelCheckedIncount = result.getChildrenCount();
                 Log.i("findhotelcheckedinto: ", userId + " checked in count: " + hotelCheckedIncount);
                if (hotelCheckedIncount > 0) {
                    DataSnapshot snapshot = result.getChildren().iterator().next();
                    setHotelCheckedInto(snapshot.getValue().toString());

                    Toast.makeText(getApplicationContext(),"Already checked-in, loading hotel",Toast.LENGTH_LONG).show();

                    try
                    {

                        hotelVm.findHotel(hotelCheckedInto, (DataSnapshot hotel) -> {
                            try
                            {
                                DataSnapshot snapshotHotel = hotel.getChildren().iterator().next();
                                obj = snapshotHotel.getValue(Hotel.class);

                                ListHotels_Fragment.mPlacesList.clear();
                                ListHotels_Fragment.mPlacesList.add(obj);

                                FragmentTransaction transaction = mFragMgr.beginTransaction();

                                Bundle args = new Bundle();
                                args.putString("hotel",obj.getName());
                                args.putString("address",obj.getAddress());
//                                args.putString("checkoutDay", "");
//                                args.putString("checkoutTime", "");
                                args.putShort("pos",(short)0);


                                //go to selected hotel
                                SelectedHotel_Fragment selectedHotel = new SelectedHotel_Fragment();
                                selectedHotel.setArguments(args);
                                transaction.replace(R.id.containerCheckIn, selectedHotel,"SelectedHotel");
                                transaction.addToBackStack("SelectedHotel");

                                transaction.commitAllowingStateLoss();

                            }
                            catch (Exception e)
                            {

                            }
                        }, null);
                    }
                    catch(Exception e)
                    {

                    }
                }
                else
                {
                    //location
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    //progress dialog
                    mProgress = new ProgressDialog(this);
                    mProgress.setTitle("Getting results ...");
                    mProgress.show();
                    checkLocation();

                    boolean fineLocationPermission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                    boolean coarseLocationPermission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

                    if (!fineLocationPermission || !coarseLocationPermission) {
                        ActivityCompat.requestPermissions(CheckIn.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        ActivityCompat.requestPermissions(CheckIn.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }

                    List<String> providers = locationManager.getProviders(true);
                    Location bestLocation = null;
                    for (String provider : providers) {
                        locationManager.requestLocationUpdates(provider, 10 * 1000, 100, locationListenerNetwork);

                        Location l = locationManager.getLastKnownLocation(provider);
                        if (l == null) {
                            continue;
                        }
                        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                            // Found best last known location: %s", l);
                            bestLocation = l;
                        }
                    }
                    try {
                        if (bestLocation != null) {
                            latitudeNetwork = bestLocation.getLatitude();
                            longitudeNetwork = bestLocation.getLongitude();
                            if(!isTestMode()) {
                                new FindHotelsAsync().execute(latitudeNetwork, longitudeNetwork);
                            }
                            else{
                                new FindHotelsAsync().execute(47.608013, -122.335167);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Device location not found yet, please wait or press the <back> button",Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Device location not found yet, please wait...", Toast.LENGTH_LONG).show();
                    }
                }
            },null);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Enables search filter menu option in the tool bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    private static boolean isTestMode() {
        boolean isTest;
        try {
            Class.forName("com.playover.CheckInTest");
            isTest = true;
        } catch (Exception ex) {
            isTest = false;
        }
        return isTest;
    }

    ///////////////
    //location
    ///////////////
    private boolean checkLocation() {
        if (!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.enable_location);
        dialog.setMessage(getString(R.string.location_message));
        dialog.setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent accessLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(accessLocationIntent);
            }
        });

        dialog.setNegativeButton(R.string.location_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing here ?
            }
        });
        dialog.show();
    }


    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isTestMode()) {
                        new FindHotelsAsync().execute(latitudeNetwork, longitudeNetwork);
                    }
                    else{
                        new FindHotelsAsync().execute(47.608013, -122.335167);
                    }
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //new FindHotelsAsync().execute(latitudeNetwork,longitudeNetwork);
                }
            });
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };


    ///////////////
    //Here API search
    ///////////////
    public class FindHotelsAsync extends AsyncTask<Double, Double, Void> {
        private String output;
        private Exception exc;

        @Override
        protected Void doInBackground(Double... doubles) {
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            exc = null;

            //The following line seems to be problematic
            //ListHotels_Fragment.mPlacesList.clear();

            BufferedReader reader = null;
            try {
                URL url = new URL("https://places.cit.api.here.com/places/v1/discover/explore?at=" + doubles[0] + "%2C" + doubles[1] + "&cat=accommodation&app_id=uik2W5pnjRgoEeInvGAa&app_code=DH4d-u-naeYlwOqUTF4V3A");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    // Get the server response
                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        // Append server response in string
                        sb.append(line);
                    }

                    this.setOutput(sb.toString());
                    JSONObject json;
                    try {
                        json = new JSONObject(this.getOutput());

                        JSONArray hotels = json.getJSONObject("results").getJSONArray("items");
                        for (int i = 0; i < hotels.length(); i++) {
                            ListHotels_Fragment.mPlacesList.add(
                                    new Hotel(hotels.getJSONObject(i).getString("title"),
                                              hotels.getJSONObject(i).getString("vicinity"),
                                              hotels.getJSONObject(i).getJSONArray("position").getString(0).replace(".","*"),
                                              hotels.getJSONObject(i).getJSONArray("position").getString(1).replace(".","*"),
                                              String.valueOf(hotels.getJSONObject(i).getInt("distance") * 0.000621371192).substring(0,4)
                                            ));
                        }

                        mProgress.dismiss();
                    } catch (JSONException je) {
                        Log.e("HERE exception message:", je.getMessage());
                    }

                } catch (Exception ex) {
                    Log.e("Here exception message:", ex.getMessage());
                    System.out.println(ex.getMessage());
                    exc = ex;
                } finally {
                    try {
                        urlConnection.disconnect();
                        reader.close();
                    } catch (Exception ex) {
                        Log.e("HERE exception message:", ex.getMessage());
                    }
                }

            } catch (MalformedURLException mue) {

            } catch (IOException ioex) {
                Log.e("HERE exception message:", ioex.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //Need to create recyclerview here(instead of in the onCreate method)
            // to allow the Async to finish before the UI thread
            if (android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            if (null == exc ) {
                //show listhostels fragment only if currently on the check-in activity
                if (firstTime) {
                    //dismiss progress bar
                    mProgress.dismiss();
                    firstTime = false;
                    //Add our new fragment to the backstack and view ?
                    FragmentTransaction trans = mFragMgr.beginTransaction();
                    trans.addToBackStack("ListHotels");
                    trans.replace(R.id.containerCheckIn, new ListHotels_Fragment(), "ListHotels");
                    trans.commitAllowingStateLoss();
                }
            } else {
                Toast.makeText(getApplicationContext(), "503: The service is unavailable", Toast.LENGTH_LONG).show();
            }
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //remove the location listeners
        if (null != locationManager) {
            locationManager.removeUpdates(locationListenerNetwork);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent newAct = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(newAct);
                break;
            case R.id.nav_buddies:
                Intent buddy = new Intent(getApplicationContext(), Buddies_Activity.class);
                startActivity(buddy);
                break;
            case R.id.nav_check_in:
                drawer.closeDrawer(GravityCompat.START);
                //handle special profile case
                Fragment cur = getSupportFragmentManager().findFragmentByTag("Profile");
                if(null != cur && cur.isVisible())
                {
                    getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.nav_discounts:
                Intent discounts = new Intent(getApplicationContext(), Discounts.class);
                startActivity(discounts);
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

    //check for network on resume
    //receiver listener on fires when there is a
    //change in network state.
    @Override
    public void onResume() {
        super.onResume();
        isNetworkAvailable();
    }

    //clear auth eventlisteners
    @Override
    public void onPause(){
        super.onPause();
        authVm.clear();
    }

    //check for networks
    public void isNetworkAvailable() {
        boolean networks = InternetBroadcastReceiver.isNetworkAvailable(getApplicationContext());
        if (!networks) {
            Intent displayNoService = new Intent(CheckIn.this, NoInternet.class);
            startActivity(displayNoService);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Fragment cur = getSupportFragmentManager().findFragmentByTag("Profile");
            //Only refresh activity if user is not checked in
            if( ! SelectedHotel_Fragment.isCheckedIn)
            {
                super.onBackPressed();
                this.recreate();
            }
            else if(null != cur && cur.isVisible())
            {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public static String getHotelCheckedInto() {
        return hotelCheckedInto;
    }

    public static void setHotelCheckedInto(String hotelCheckedInto) {
        CheckIn.hotelCheckedInto = hotelCheckedInto;
    }
}
