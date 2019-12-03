package com.playover;

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
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Landing_Fragment extends Fragment {

    //member variables
    private TextView lblsocialize_main;
    private Button signup_button_main;
    private TextView lblogin_main;
    private TextView lblfeature_main;
    private TextView lblcheckin_main;
    private TextView main_login;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    public Landing_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
        if (getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
            lblsocialize_main = rootView.findViewById(R.id.lblsocialize_main);
            lblogin_main = rootView.findViewById(R.id.lblogin_main);
            lblfeature_main = rootView.findViewById(R.id.lblfeature_main);
            lblcheckin_main = rootView.findViewById(R.id.lblcheckin_main);
            signup_button_main = rootView.findViewById(R.id.signup_button_main);
            //set spannable string to log in text
            SpannableString logIn = new SpannableString(lblogin_main.getText().toString().trim());
            //declare log in clickable event listener
            ClickableSpan clickableSignUp = new ClickableSpan() {
                //go to terms fragment add to backstack
                @Override
                public void onClick(@NonNull View widget) {
                        transaction = fragmentManager.beginTransaction();
                        LogIn_Fragment logInNew = new LogIn_Fragment();
                        transaction.replace(R.id.fragments_main, logInNew, "LogIn");
                        transaction.addToBackStack("LogIn");
                        transaction.commit();
                }
            };

            //set clickable log in start and end index
            logIn.setSpan(clickableSignUp, logIn.length() - 7, logIn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //set link color
            if(getContext() != null) {
                logIn.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.link_color)),
                        logIn.length() - 7, logIn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //set lbllogin text
            lblogin_main.setText(logIn);
            //set movement method for textview
            lblogin_main.setMovementMethod(LinkMovementMethod.getInstance());

            //click event for sign up button
            signup_button_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transaction = fragmentManager.beginTransaction();
                    SignUp_Fragment signUpNew = new SignUp_Fragment();
                    transaction.replace(R.id.fragments_main, signUpNew, "SignUp");
                    transaction.addToBackStack("SignUp");
                    transaction.commit();
                }
            });
        }
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
}
