package app.playover;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import app.playover.R;

import app.playover.viewmodels.AuthUserViewModel;
import app.playover.viewmodels.NotificationsViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIn_Fragment extends Fragment {

    private String TAG = "LogIn_Fragment";
    private TextView sign_in;
    private TextView forget_main;
    private EditText email_login;
    private EditText password_login;
    private AuthUserViewModel authVm;
    private NotificationsViewModel notifyVM;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private String FCMinstanceID;

    public LogIn_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String inClass = "inClass";
        String inClassMsg = "inClass LogIn Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create";
        Log.i(onCreate, onCreateMsg);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String inClass = "inClass";
        String inClassMsg = "inClass Login Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create View";
        Log.i(onCreate, onCreateMsg);
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        authVm = new AuthUserViewModel();
        notifyVM = new NotificationsViewModel();
        if (getActivity() != null) {
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        sign_in = rootView.findViewById(R.id.sign_in);
        forget_main = rootView.findViewById(R.id.forget_main);
        //set spannable string to log in text
        SpannableString resetPwd = new SpannableString(forget_main.getText().toString().trim());
        //declare log in clickable event listener
        ClickableSpan clickableReset = new ClickableSpan() {
            //go to terms fragment add to backstack
            @Override
            public void onClick(@NonNull View widget) {
                transaction = fragmentManager.beginTransaction();
                PasswordReset_Fragment pwdResetUpNew = new PasswordReset_Fragment();
                transaction.replace(R.id.fragments_main, pwdResetUpNew, "Reset");
                transaction.addToBackStack("Reset");
                transaction.commit();
            }
        };

        //set clickable log in start and end index
        resetPwd.setSpan(clickableReset, resetPwd.length() - 5, resetPwd.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //set link color
        if (getContext() != null) {
            String enterIf = "EnterIf";
            String enterIfMsg = "Entered If Statement";
            Log.i(enterIf, enterIfMsg);
            resetPwd.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.link_color)),
                    resetPwd.length() - 5, resetPwd.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //set reset main text
        forget_main.setText(resetPwd);
        //set movement method for textview
        forget_main.setMovementMethod(LinkMovementMethod.getInstance());
        email_login = rootView.findViewById(R.id.email_login);
        password_login = rootView.findViewById(R.id.password_login);
        Button btn_login = rootView.findViewById(R.id.btn_login);
        //log in button clicked
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email_login.getText().toString().trim()) &&
                        emailPattern(email_login.getText().toString().trim()) &&
                        password_login.getText().toString().trim().length() >= 6 &&
                        password_login.getText().toString().trim().length() <= 10) {
                    if (getActivity() != null) {
                        final ProgressDialog logInDialog = new ProgressDialog(getActivity());
                        logInDialog.setTitle("Logging In...");
                        logInDialog.show();
                        final Activity parentActivity = getActivity();
                        authVm.logInUser(email_login.getText().toString().trim().toLowerCase(),
                                password_login.getText().toString().trim(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // added by QRP, on login set notification channels and store FB instance ID
                                            String uId = new AuthUserViewModel().getUser().getUid();
                                            //String FCMInstanceID;
                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                            if (!task.isSuccessful()) {
                                                                //Log.w(TAG, "getInstanceId failed", task.getException());
                                                                return;
                                                            }

                                                            // Get new Instance ID token
                                                            FCMinstanceID = task.getResult().getToken();
                                                            notifyVM.setFCMinstanceID(uId, FCMinstanceID, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.i(TAG, "Successfully set FCMinstanceID " + FCMinstanceID);
                                                                    } else {
                                                                        if (task.getException() != null)
                                                                            Log.d(TAG, "Failed to set FCMinstanceID: " + task.getException());
                                                                    }
                                                                }
                                                            });

                                                            Log.d(TAG, FCMinstanceID);
                                                        }
                                                    });
                                            notifyVM.createMyNotificationChannels(getActivity());
                                            logInDialog.dismiss();
                                            Intent intent = new Intent(parentActivity, CheckIn.class);
                                            parentActivity.startActivity(intent);
                                        } else {
                                            if (task.getException() != null) {
                                                logInDialog.dismiss();
                                                if (getActivity() != null) {
                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                                Log.e("LogIn error: ", task.getException().getMessage());
                                            }
                                        }
                                    }
                                });
                    }

                } else {
                    if (TextUtils.isEmpty(email_login.getText().toString().trim()) || !emailPattern(email_login.getText().toString().trim())) {
                        email_login.setError("Please enter a valid email address!");
                    }
                    if (password_login.getText().toString().trim().length() < 6 ||
                            password_login.getText().toString().trim().length() > 10) {
                        password_login.setError("Passwords must be between 6 and 10 characters!");
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
