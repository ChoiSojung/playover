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
import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.UserViewModel;

import java.util.Objects;

public class Settings_Fragment extends Fragment {

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
                    } else {
                        if (task.getException() != null)
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}

