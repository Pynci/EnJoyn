package it.unimib.enjoyn.source.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.enjoyn.model.User;

public class AuthenticationDataSource extends BaseAuthenticationDataSource{

    private final FirebaseAuth auth;
    private FirebaseUser fbUser;

    public AuthenticationDataSource(){
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbUser = auth.getCurrentUser();
                        authenticationCallback.onSignUpSuccess(new User(fbUser.getUid(), email, username));

                    } else {
                        authenticationCallback.onAuthFailure(task.getException());
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        fbUser = auth.getCurrentUser();
                        authenticationCallback.onSignInSuccess(new User(fbUser.getUid(), email));
                    }
                    else{
                        authenticationCallback.onAuthFailure(task.getException());
                    }
                });
    }

    @Override
    public void signOut(){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    authenticationCallback.onSignOutSuccess();
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
        auth.signOut();
    }

    @Override
    public String getCurrentUserEmail(){
        if(fbUser != null)
            return fbUser.getEmail();
        return null;
    }

    @Override
    public String getCurrentUserUID(){
        if(fbUser != null)
            return fbUser.getUid();
        return null;
    }

    @Override
    public Boolean isCurrentUserEmailVerified(){
        if(fbUser != null)
            return fbUser.isEmailVerified();
        return null;
    }

    @Override
    public void sendEmailVerification() {
        fbUser.sendEmailVerification().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                authenticationCallback.onAuthSuccess();
            }
            else{
                authenticationCallback.onAuthFailure(task.getException());
            }
        });
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        if (fbUser == null) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    authenticationCallback.onAuthSuccess();
                }
                else{
                    authenticationCallback.onAuthFailure(task.getException());
                }
            });
        }
        else {
            authenticationCallback.onAuthFailure(new Exception("Impossibile modificare password " +
                    "se l'utente è ancora autenticato"));
        }
    }
}
