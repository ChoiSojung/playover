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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.UserViewModel;

import java.util.Objects;

import static android.support.constraint.Constraints.TAG;

public class ChangePasswordFragment extends Fragment {

    private EditText currentPasswordEdit, newPasswordEdit1, newPasswordEdit2;
    private Button changePw;
    private AuthUserViewModel authVm;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    public ChangePasswordFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_password_change, container, false);

        currentPasswordEdit = rootView.findViewById(R.id.currentPassword);
        newPasswordEdit1 = rootView.findViewById(R.id.newPassword1);
        newPasswordEdit2 = rootView.findViewById(R.id.newPassword2);
        changePw = rootView.findViewById(R.id.passwordChangeBtn);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        authVm = new AuthUserViewModel();
        Context mContext = getContext();

        changePw.setOnClickListener(v -> authVm.reauthenticateUser(authVm.getUserEmail(),
                currentPasswordEdit.getText().toString(), task -> {
                    if (task.isSuccessful()) {
                        if ((newPasswordEdit1.getText().toString().length() >= 6
                                && newPasswordEdit1.getText().toString().length() <= 10)
                                && (newPasswordEdit2.getText().toString().length() >= 6
                                && newPasswordEdit2.getText().toString().length() <= 10)
                                && (newPasswordEdit1.getText().toString().equals(newPasswordEdit2.getText().toString())
                                && !newPasswordEdit1.getText().toString().isEmpty()
                        )) {
                            authVm.updatePassword(newPasswordEdit1.getText().toString(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User password updated.");
                                    Toast.makeText(mContext, "Password Updated!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            newPasswordEdit1.setError("Try again.");
                            newPasswordEdit2.setError("Try again.");
                        }
                        Log.d(TAG, "User re-authenticated.");
                    } else {
                        currentPasswordEdit.setError("Re-enter correct password.");
                    }
                }));

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
