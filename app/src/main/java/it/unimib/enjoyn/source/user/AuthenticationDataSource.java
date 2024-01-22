package it.unimib.enjoyn.source.user;

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
    public String getCurrentUserUID(){
        fbUser = auth.getCurrentUser();
        if(fbUser != null){
            return fbUser.getUid();
        }
        else{
            return "";
        }
    }

    @Override
    public void signUp(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbUser = auth.getCurrentUser();
                        if(fbUser != null){
                            authenticationCallback.onSignUpSuccess(new User(fbUser.getUid(), username, email));
                        }
                        else{
                            authenticationCallback.onAuthFailure(task.getException());
                        }
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
                        if(fbUser != null){
                            authenticationCallback.onSignInSuccess(new User(fbUser.getUid(), email));
                        }
                        else{
                            authenticationCallback.onAuthFailure(task.getException());
                        }

                    }
                    else{
                        authenticationCallback.onAuthFailure(task.getException());
                    }
                });
    }

    @Override
    public void refreshSession(){
        fbUser = auth.getCurrentUser();
        if(fbUser != null){
            authenticationCallback.onAlreadySignedIn(fbUser.getUid());
        }
        else{
            authenticationCallback.onSignOutSuccess();
        }
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
    public void sendEmailVerification() {
        fbUser.sendEmailVerification().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                authenticationCallback.onEmailSendingSuccess();
            }
            else{
                authenticationCallback.onEmailSendingFailure(task.getException());
            }
        });
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        if (fbUser == null) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    authenticationCallback.onEmailSendingSuccess();
                }
                else{
                    authenticationCallback.onEmailSendingFailure(task.getException());
                }
            });
        }
        else {
            authenticationCallback.onEmailSendingFailure(new Exception("Impossibile modificare password " +
                    "se l'utente è ancora autenticato"));
        }
    }

    @Override
    public void checkEmailVerification(){
        fbUser
            .reload()
            .addOnSuccessListener(task -> authenticationCallback.onEmailCheckSuccess(fbUser.isEmailVerified()))
            .addOnFailureListener(e -> authenticationCallback.onEmailCheckFailure(e));
    }
}
