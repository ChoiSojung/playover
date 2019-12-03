package com.playover;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.NotificationsViewModel;
import com.playover.viewmodels.UserViewModel;
import com.playover.R;

import java.util.Objects;

public class Settings_Fragment extends Fragment {

    private String TAG = "Settings_Fragment.java";
    private TextView changePasswordText;
    private AuthUserViewModel authVm;
    private UserViewModel userVm;
    private Switch dndSwitch;
    private LinearLayout changePassword;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private static Bundle b = new Bundle();


    public Settings_Fragment() {
        //Required empty constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        String inClass = "inClass";
        String inClassMsg = "InClass Settings Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreateView";
        String onCreateMsg = "On Create View";
        Log.i(onCreate, onCreateMsg);
        dndSwitch = rootView.findViewById(R.id.settingsDndSwitch);
        changePassword = rootView.findViewById(R.id.settingsChangePassword);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        authVm = new AuthUserViewModel();
        userVm = new UserViewModel();

        if (authVm != null && userVm != null && authVm.getUser() != null) {
            userVm.getUser(authVm.getUser().getUid(),
                    (Person user) -> {
                        if (user != null) {
                            if (user.isDnd()) {
                                dndSwitch.setChecked(user.isDnd());
                            }
                        }
                    });
        }

        dndSwitch.setOnClickListener(v -> dndStatus(dndSwitch.isChecked()));

        changePassword.setOnClickListener(v -> {
            transaction = fragmentManager.beginTransaction();
            ChangePasswordFragment newChangePassword = new ChangePasswordFragment();
            transaction.replace(R.id.containerSettings, newChangePassword, "ChangePassword");
            transaction.addToBackStack("ChangePassword");
            transaction.commit();
        });

        //This is strictly for development of the FCM messaging. Remove this prior to any release to users.
        TextView fcmIdFromFbInstance = rootView.findViewById(R.id.settings_fragment_fcmviafbinstance);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get FB Instance ID token
                        String token = task.getResult().getToken();

                        Log.d(TAG, token);
                        //TextView showToken = rootView.findViewById(R.id.settings_fragment_fcmviafbinstance);
                        fcmIdFromFbInstance.setText(token);
                    }
                });

        NotificationsViewModel notificationsVm = new NotificationsViewModel();
        TextView fcmIdFromRtDb = rootView.findViewById(R.id.settings_fragment_fcmviafbrtdb);
        if (authVm != null && notificationsVm != null && authVm.getUser() != null) {
            notificationsVm.getFCMinstanceID(authVm.getUser().getUid(),
                    (String FCMinstanceID) -> {
                        if (FCMinstanceID != null) {
                            fcmIdFromRtDb.setText(FCMinstanceID);
                        }
                    });
        }
        if(fcmIdFromRtDb.getText().equals(fcmIdFromFbInstance.getText())) {
            fcmIdFromRtDb.setTextColor(getResources().getColor(R.color.solidgreen, null));
            fcmIdFromFbInstance.setTextColor(getResources().getColor(R.color.solidgreen, null));
        } else {
            fcmIdFromRtDb.setTextColor(getResources().getColor(R.color.solidred, null));
            fcmIdFromFbInstance.setTextColor(getResources().getColor(R.color.solidred, null));
        }

        return rootView;
    }

    //updates dnd status in person object in the database
    public void dndStatus(boolean isChecked) {
        if (authVm != null && userVm != null) {
            userVm.updateDnd(authVm.getUser().getUid(), isChecked, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i("dndToggle", "switch toggle written to firebase");
                    }
                }
            });

        }
    }
}

