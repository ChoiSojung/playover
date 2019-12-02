package com.playover;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordReset_Fragment extends Fragment {
    private EditText email_reset;
    private AuthUserViewModel authVm;

    public PasswordReset_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String onCreate = "onCreate";
        String onCreateMsg = "In On Create";
        Log.i(onCreate, onCreateMsg);
        View rootView = inflater.inflate(R.layout.fragment_password_reset, container, false);
        authVm = new AuthUserViewModel();
        email_reset = rootView.findViewById(R.id.email_reset);
        Button btn_reset = rootView.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    final ProgressDialog sendEmailDialog = new ProgressDialog(getActivity());
                    sendEmailDialog.setTitle("Sending Reset Email...");
                    sendEmailDialog.show();
                    if (!TextUtils.isEmpty(email_reset.getText().toString().toLowerCase().trim()) && emailPattern(email_reset.getText().toString().trim())) {
                        authVm.sendResetEmail(email_reset.getText().toString().toLowerCase().trim(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendEmailDialog.dismiss();
                                    if(getActivity()!=null) {
                                        Toast.makeText(getActivity(), "Reset Email Sent!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (task.getException() != null) {
                                        sendEmailDialog.dismiss();
                                        if(getActivity()!=null) {
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        email_reset.setError("Please enter a valid email!");
                    }
                }
            }
        });
        return rootView;
    }

    //method to check for valid email string
    public boolean emailPattern(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
