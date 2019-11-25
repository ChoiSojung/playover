package appdev.playover.datamodels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;

public class AuthUserDataModel {
    private FirebaseAuth firebaseAuth;
    //arraylist of firebase auth state listeners
    private ArrayList<FirebaseAuth.AuthStateListener> authListeners;
    //constructor
    public AuthUserDataModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        authListeners = new ArrayList<FirebaseAuth.AuthStateListener>();
    }
    public FirebaseUser getAuthUser() {
        return firebaseAuth.getCurrentUser();
    }

    //mvvm datamodel create auth'd user
    public void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> viewModelCallback) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(viewModelCallback);

    }

    //mvvm datamodel delete auth'd user
    public void deleteAuthUser(FirebaseUser user, OnCompleteListener<Void> viewModelCallBack) {
        user.delete().addOnCompleteListener(viewModelCallBack);
    }

    //mvvm datamodel sign out user
    public void signOutUser(FirebaseAuth.AuthStateListener viewModelCallBack) {
        firebaseAuth.addAuthStateListener(viewModelCallBack);
        authListeners.add(viewModelCallBack);
        firebaseAuth.signOut();
    }

    //mvvm datamodel send password reset email
    public void sendResetEmail(String email, OnCompleteListener<Void> viewModelCallBack) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(viewModelCallBack);
    }

    //mvvm datamodel log in user
    public void logInUser(String email, String password, OnCompleteListener<AuthResult> viewModelCallBack) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(viewModelCallBack);

    }

    //mvvm datamodel that updates password for user
    public void upadatePassword(FirebaseUser user, String newPassword, OnCompleteListener<Void> viewModelCallBack) {
        user.updatePassword(newPassword).addOnCompleteListener(viewModelCallBack);
    }

    public void reauthenticateUser(FirebaseUser user, AuthCredential credential, OnCompleteListener<Void> viewModelCallback) {
        user.reauthenticate(credential).addOnCompleteListener(viewModelCallback);
    }

    //mvvm datamodel clear authstate listeners on pause
    public void clear(){
        if(authListeners != null) {
            for (FirebaseAuth.AuthStateListener listener : authListeners) {
                firebaseAuth.removeAuthStateListener(listener);
            }
            authListeners.clear();
        }
    }
}
