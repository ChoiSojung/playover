package com.playover.viewmodels;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.playover.datamodels.AuthUserDataModel;

import static android.support.constraint.Constraints.TAG;

public class AuthUserViewModel {
    private AuthUserDataModel authUserDataModel;
    private FirebaseUser user;

    //constructor.  view model instantiates a data model
    public AuthUserViewModel() {
        authUserDataModel = new AuthUserDataModel();
        user = authUserDataModel.getAuthUser();
    }

    // vm gets current user with listener attached
    public FirebaseUser getUser() {
        user = authUserDataModel.getAuthUser();
        return user;

    }

    public String getUserEmail() {
        return user.getEmail();
    }

    //mvvm viewmodel create auth'd user
    public void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> activityCallBack) {
        authUserDataModel.createUserWithEmailAndPassword(email, password, activityCallBack);

    }

    //mvvm viewmodel delete auth user
    public void deleteAuthUser(FirebaseUser user, OnCompleteListener<Void> activityCallBack) {
        authUserDataModel.deleteAuthUser(user, activityCallBack);
    }

    //mvvm viewmodel sign out auth user
    public void signOutUser(FirebaseAuth.AuthStateListener activityCallBack) {
        authUserDataModel.signOutUser(activityCallBack);
    }

    //mvvm viewmodel send password reset email
    public void sendResetEmail(String email, OnCompleteListener<Void> activityCallBack) {
        authUserDataModel.sendResetEmail(email, activityCallBack);
    }

    //mvvm viewmodel log in user
    public void logInUser(String email, String password,OnCompleteListener<AuthResult> activityCallBack) {
        authUserDataModel.logInUser(email, password, activityCallBack);
    }

    //mvvm viewmodel update password
    public void updatePassword(String newPassword, OnCompleteListener<Void> activityCallBack) {
        authUserDataModel.upadatePassword(user, newPassword, activityCallBack);
    }

    public void reauthenticateUser (String email, String password, OnCompleteListener<Void> activityCallBack) {
        if(!password.trim().isEmpty()) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, password);
            authUserDataModel.reauthenticateUser(user, credential, activityCallBack);
        }
    }


    //mvvm viewmodel clear auth state viewmodels
    public void clear(){
        authUserDataModel.clear();
    }
}
