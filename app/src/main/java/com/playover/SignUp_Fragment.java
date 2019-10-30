package com.playover;

import android.content.Context;
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

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp_Fragment extends Fragment {

    //member variables
    private int validationCode = 0;
    private EditText firstNameSignUp;
    private EditText lastNameSignUp;
    private EditText positionSignUp;
    private EditText emailSignUp;
    private EditText passwordSignUp;
    private TextView lblsignup;
    private TextView termsSignUp;
    private Button buttonSignUp;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private EditText verification_Password;
    private String tag = "vcode";


    public SignUp_Fragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        firstNameSignUp = rootView.findViewById(R.id.firstNameSignUp);
        lastNameSignUp = rootView.findViewById(R.id.lastNameSignUp);
        positionSignUp = rootView.findViewById(R.id.positionSignUp);
        emailSignUp = rootView.findViewById(R.id.emailSignUp);
        passwordSignUp = rootView.findViewById(R.id.passwordSignUp);
        verification_Password = rootView.findViewById(R.id.verification_Password);
        lblsignup = rootView.findViewById(R.id.lblsignup);

        termsSignUp = rootView.findViewById(R.id.termsSignUp);
        //set spannable string to terms text
        SpannableString termsSpan = new SpannableString(termsSignUp.getText().toString().trim());
        //declare terms clickable event listener
        ClickableSpan clickableTerms = new ClickableSpan() {
            //go to terms fragment add to backstack
            @Override
            public void onClick(@NonNull View widget) {
                if (getActivity() != null) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                    transaction = fragmentManager.beginTransaction();
                    Terms_Fragment terms = new Terms_Fragment();
                    transaction.replace(R.id.fragments_main, terms, "Terms");
                    transaction.addToBackStack("Terms");
                    transaction.commit();
                }

            }

        };
        //declare data use clickable span
        ClickableSpan clickableDataUse = new ClickableSpan() {
            //go to data use fragment.  add to backstack
            @Override
            public void onClick(@NonNull View widget) {
                if (getActivity() != null) {
                    transaction = fragmentManager.beginTransaction();
                    fragmentManager = getActivity().getSupportFragmentManager();
                    DataUse_Fragment dataUse = new DataUse_Fragment();
                    transaction.replace(R.id.fragments_main, dataUse, "DataUse");
                    transaction.addToBackStack("DataUse");
                    transaction.commit();

                }

            }
        };
        //set clickable terms start and end index
        termsSpan.setSpan(clickableTerms, termsSpan.length() - 27, termsSpan.length() - 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //set link color
        if(getContext() != null) {
            termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.link_color)),
                    termsSpan.length() - 27, termsSpan.length() - 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //set clickable terms start and end index
        termsSpan.setSpan(clickableDataUse, termsSpan.length() - 16, termsSpan.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //set link color
        if(getContext()!=null) {
            termsSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.link_color)),
                    termsSpan.length() - 16, termsSpan.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //set tesmsSignUp test
        termsSignUp.setText(termsSpan);
        //set movement method for textview
        termsSignUp.setMovementMethod(LinkMovementMethod.getInstance());

        buttonSignUp = rootView.findViewById(R.id.buttonSignUp);
        if (getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            //sign up click event.  all fields require.  position is an edit text for now until we get more direction
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(firstNameSignUp.getText().toString().trim())) {
                        firstNameSignUp.setError("First name is required!");
                    }
                    if (TextUtils.isEmpty(lastNameSignUp.getText().toString().trim())) {
                        lastNameSignUp.setError("Last name is required!");
                    }
                    if (TextUtils.isEmpty(positionSignUp.getText().toString().trim())) {
                        positionSignUp.setError("Position is required!");
                    }
                    if (TextUtils.isEmpty(emailSignUp.getText().toString().trim())) {
                        emailSignUp.setError("Email is required!");
                    }
                    //passwords must be between 6 and 10 characters
                    if (passwordSignUp.getText().toString().length() < 6 || passwordSignUp.getText().toString().length() > 10) {
                        passwordSignUp.setError("Passwords must be between 6 and 10 characters!");
                    }
                    if (passwordSignUp.getText().toString().compareTo(verification_Password.getText().toString()) != 0) {
                        passwordSignUp.setError("Passwords do not match!");
                        verification_Password.setError("Passwords do not match!");
                    }
                    //checking email pattern.  can adjust regex later based on needs
                    if (!emailPattern(emailSignUp.getText().toString().trim())) {
                        emailSignUp.setError("Please enter a valid email!");
                    }
                    //values cant be empty.  passwords must be betweeen 6 and 10 characters and password and verification_Pssword must match
                    if (passwordSignUp.getText().toString().length() >= 6
                            && passwordSignUp.getText().toString().length() <= 10
                            && !TextUtils.isEmpty(emailSignUp.getText().toString().trim())
                            && !TextUtils.isEmpty(positionSignUp.getText().toString().trim())
                            && !TextUtils.isEmpty(lastNameSignUp.getText().toString().trim())
                            && !TextUtils.isEmpty(firstNameSignUp.getText().toString().trim())
                            && emailPattern(emailSignUp.getText().toString().trim())
                            && passwordSignUp.getText().toString().compareTo(verification_Password.getText().toString()) == 0) {
                        sendEmail();
                        if (validationCode != 0) {
                            transaction = fragmentManager.beginTransaction();
                            Verification_Fragment Verification = new Verification_Fragment();
                            Bundle b = new Bundle();
                            Log.i(tag,String.valueOf(validationCode));
                            b.putInt(Constants.KEY_CODE, validationCode);
                            b.putString(Constants.KEY_EMAIL, emailSignUp.getText().toString().toLowerCase().trim());
                            b.putString(Constants.KEY_Employee_EMAIL, emailSignUp.getText().toString().toLowerCase().trim());
                            b.putString(Constants.KEY_FNAME, firstNameSignUp.getText().toString().trim());
                            b.putString(Constants.KEY_LNAME, lastNameSignUp.getText().toString().trim());
                            b.putString(Constants.KEY_POSITION, positionSignUp.getText().toString().trim());
                            b.putString(Constants.KEY_PASSWORD, passwordSignUp.getText().toString().trim());
                            Verification.setArguments(b);
                            transaction.replace(R.id.fragments_main, Verification, "Verification");
                            transaction.addToBackStack("Verification");
                            transaction.commit();
                        }
                    }
                }
            });
        }
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

    //send email method.  spins up a new thread.
    public void sendEmail() {
        Random rand = new Random();
        validationCode = rand.nextInt(999999);
        if (validationCode != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                   /* FirebaseAuth auth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword (emailSignUp.getText ().toString (),
                            passwordSignUp.getText ().toString ())
                            .addOnCompleteListener (new OnCompleteListener <AuthResult> () {
                                @Override
                                public void onComplete(@NonNull Task <AuthResult> task) {
                                    if(task.isSuccessful (){ firebaseAuth.getCurrentUser ().sendEmailVerification ().addOnCompleteListener ( new OnCompleteListener <Void> () {
                                        @Override
                                        public void onComplete(@NonNull Task <Void> task) {
                                            if(task.isSuccessful()){
                                                emailSignUp.setText("");
                                                passwordSignUp.setText ( "" );

                                            }
                                        }
                                    });
                                    }
                                } else {
                                    Toast.makeText (  )
                                }
                            }*/
                    try {
                        SendMail sender = new SendMail();
                        sender.MailingMessage("PlayOver Email Verification Code", "Here is the requested validation code:  " + validationCode,
                                "playover.mobile@gmail.com", emailSignUp.getText().toString().trim());
                    } catch (Exception e) {
                        validationCode = 0;
                        Log.e("send mail error", e.getMessage());
                    }
                }

            }).start();
        }
    }
}
