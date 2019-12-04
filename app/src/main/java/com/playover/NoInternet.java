package com.playover;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.playover.broadcast_receivers.InternetBroadcastReceiver;

public class NoInternet extends AppCompatActivity {
    public TextView noInternetMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String inClass = "inClass";
        String inClassMsg = "inClass NoInternet";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create";
        Log.i(onCreate, onCreateMsg);
        setContentView(R.layout.activity_no_internet);
        noInternetMessage = findViewById(R.id.noInternetMessage);
        showAlert();
    }

    private void showAlert() {
        final AlertDialog.Builder noInternetDialog = new AlertDialog.Builder(this);
        noInternetDialog.setTitle(R.string.no_internet_title);
        noInternetDialog.setMessage(R.string.no_internet_message);
        noInternetDialog.setPositiveButton(R.string.no_internet_positive_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConnectionDialog();

            }
        });
        noInternetDialog.create();
        noInternetDialog.show();
    }

    @Override
    public void onBackPressed() {
        //if internet is enabled, allow backpress
        if (InternetBroadcastReceiver.isNetworkAvailable(this)) {
            super.onBackPressed();
        }
        //else close out the app on backpress
        else{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }

    public void checkConnectionDialog(){
        //if internet is enabled, allow backpress
        if (InternetBroadcastReceiver.isNetworkAvailable(this)) {
            onBackPressed();
        }
        else{
             showAlert();
        }
    }
}
