package app.playover;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import app.playover.R;

import app.playover.models.Person;
import app.playover.viewmodels.AuthUserViewModel;
import app.playover.viewmodels.UserViewModel;

import java.util.Random;

public class Verification_Fragment extends Fragment {

    private TextView verification_message;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String password;
    private String position;
    private int validationCode = 0;
    private TextView family;
    private TextView didntReceive;
    private TextView resendVerification;
    private EditText verificationEdit;
    private Button verify;
    private AuthUserViewModel authVm = new AuthUserViewModel();
    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private int verifyCount = 0;


    public Verification_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String inClass = "inClass";
        String inClassMsg = "InClass Verification Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create";
        Log.i(onCreate, onCreateMsg);
    }

    @Override
    //todo: correctly format page and add boarders
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String inClass = "inClass";
        String inClassMsg = "InClass Verification Fragment";
        Log.i(inClass, inClassMsg);
        String onCreate = "OnCreate";
        String onCreateMsg = "On Create";
        Log.i(onCreate, onCreateMsg);
        View rootView = inflater.inflate(R.layout.fragment_verification, container, false);
        verification_message = rootView.findViewById(R.id.verification_message);
        family = rootView.findViewById(R.id.family);
        verificationEdit = rootView.findViewById(R.id.verificationEdit);
        didntReceive = rootView.findViewById(R.id.didntReceive);
        resendVerification = rootView.findViewById(R.id.resendVerification);
        if (getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        //resend verification email if clicked
        resendVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailAddress != null) {
                    sendEmail();
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Validation Code Sent!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        verificationEdit = rootView.findViewById(R.id.verificationEdit);
        verify = rootView.findViewById(R.id.verify);
        //verify button - will create user and send to edit profile once database is set up
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //only allow 3 wrong validation code entries before resetting and sending a new verification email.
                if (verifyCount <= 2) {
                    if (verificationEdit.getText().toString().trim().length() != 0 &&
                            Integer.valueOf(verificationEdit.getText().toString()) == validationCode) {
                        if (getActivity() != null) {
                            final Activity parentActivity = getActivity();
                            ProgressDialog createUserDialog = new ProgressDialog(parentActivity);
                            createUserDialog.setTitle("Creating User...");
                            createUserDialog.show();
                            authVm.createUserWithEmailAndPassword(emailAddress, password, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        UserViewModel userVm = new UserViewModel();
                                        Person newUser = new Person(firstName, lastName, null, position, null,
                                                null, authVm.getUser().getEmail(), authVm.getUser().getUid(), null, null, false, null, null, null);
                                        Log.i("new user: ", newUser.toString());
                                        userVm.createUser(authVm.getUser().getUid(), newUser, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    createUserDialog.dismiss();
                                                    Intent intent = new Intent(parentActivity, ProfileActivity.class);
                                                    parentActivity.startActivity(intent);
                                                } else {
                                                    if(getActivity()!=null && task.getException()!=null) {
                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    //delete user if create fails.  display message and pop
                                                    authVm.deleteAuthUser(authVm.getUser(), new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                fragmentManager.popBackStack();
                                                                if(getActivity()!=null) {
                                                                    Toast.makeText(getActivity(),
                                                                            "Unable to create account.  Please try again later.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }

                                            }
                                        });
                                    } else {
                                        if (task.getException() != null) {
                                            if (getActivity() != null) {
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Verification Code Does Not Match!", Toast.LENGTH_SHORT).show();
                        }
                        verifyCount += 1;
                    }
                } else {
                    if (emailAddress != null) {
                        sendEmail();
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "Wrong code inputted too many times.\nNew Code Sent!", Toast.LENGTH_SHORT).show();
                        }
                        verifyCount = 0;
                    }
                }
            }
        });
        //grab bundle from sign up fragment
        Bundle b = this.getArguments();
        if (b != null) {
            if (b.getString(Constants.KEY_EMAIL) != null) {
                //sign up email address
                emailAddress = b.getString(Constants.KEY_EMAIL);
            }
            if (b.getInt(Constants.KEY_CODE) != 0) {
                //validation code from sign up fragment
                validationCode = b.getInt(Constants.KEY_CODE);
            }
            if (b.getString(Constants.KEY_FNAME) != null) {
                firstName = b.getString(Constants.KEY_FNAME);

            }
            if (b.getString(Constants.KEY_LNAME) != null) {
                lastName = b.getString(Constants.KEY_LNAME);

            }
            if (b.getString(Constants.KEY_POSITION) != null) {
                position = b.getString(Constants.KEY_POSITION);

            }
            if (b.getString(Constants.KEY_EMAIL) != null) {
                password = b.getString(Constants.KEY_PASSWORD);
            }
            // display message based on intent send from sign up
            if (!TextUtils.isEmpty(emailAddress) && validationCode != 0 && !TextUtils.isEmpty(firstName)
                    && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(position)) {
                String displayMessage = "Please verify your email by entering the code we've sent to " + emailAddress;
                verification_message.setText(displayMessage);
            }
        }

        return rootView;
    }

    //send email method.  spins up a new thread
    public void sendEmail() {
        Random rand = new Random();
        validationCode = rand.nextInt(999999);
        //validationCode = 0;
        if (validationCode != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //throw new IllegalAccessException("Testing Errors");
                        SendMail sender = new SendMail();
                        sender.MailingMessage("Playover Email Verification Code", "Thank you for joining Playover!\nHere is the requested verification code:  " + validationCode,
                                "playover.mobile@gmail.com", emailAddress);
                    } catch (Exception e) {
                        validationCode = 0;
                        Log.e("send mail error", e.getMessage(), e);
                    }
                }

            }).start();
        }
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
