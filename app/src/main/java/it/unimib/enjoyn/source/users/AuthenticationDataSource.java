package it.unimib.enjoyn.source.users;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.util.Constants;

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
    public void signUp(String email, String password, String username, Callback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbUser = auth.getCurrentUser();
                        if(fbUser != null){
                            callback.onComplete(new Result.UserSuccess(new User(fbUser.getUid(), username, email)));
                        }
                        else{
                            callback.onComplete(new Result.Error(Constants.USER_ALREADY_LOGGED_ERROR));
                        }
                    } else {
                        callback.onComplete(new Result.Error(Constants.SIGNUP_ERROR));
                    }
                });
    }

    @Override
    public void signIn(String email, String password, Callback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        fbUser = auth.getCurrentUser();
                        if(fbUser != null){
                            callback.onComplete(new Result.UserSuccess(new User(fbUser.getUid(), email)));
                        }
                        else{
                            callback.onComplete(new Result.Error(Constants.USER_ALREADY_LOGGED_ERROR));
                        }

                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.SIGNIN_ERROR));
                    }
                });
    }

    @Override
    public void refreshSession(Callback callback){
        fbUser = auth.getCurrentUser();
        if(fbUser != null){
            fbUser
                    .reload()
                    .addOnSuccessListener(task -> callback.onComplete(new Result.Success()))
                    .addOnFailureListener(e -> callback.onComplete(new Result.Error(Constants.SESSION_REFRESH_ERROR)));
        }
        else{
            callback.onComplete(new Result.Error(Constants.USER_NOT_LOGGED_ERROR));
        }
    }

    @Override
    public void signOut(Callback callback){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    callback.onComplete(new Result.Success());
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
        auth.signOut();
    }

    @Override
    public void sendEmailVerification(Callback callback) {
        fbUser.sendEmailVerification().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(Constants.EMAIL_SENDING_ERROR));
            }
        });
    }

    @Override
    public void sendResetPasswordEmail(String email, Callback callback) {
        if (fbUser == null) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onComplete(new Result.Success());
                }
                else{
                    callback.onComplete(new Result.Error(Constants.EMAIL_SENDING_ERROR));
                }
            });
        }
        else {
            callback.onComplete(new Result.Error(Constants.USER_ALREADY_LOGGED_ERROR));
        }
    }

    @Override
    public void checkEmailVerification(Callback callback){
        fbUser
            .reload()
            .addOnSuccessListener(task -> callback.onComplete(new Result.BooleanSuccess(fbUser.isEmailVerified())))
            .addOnFailureListener(e -> callback.onComplete(new Result.Error(Constants.SESSION_REFRESH_ERROR)));
    }
}
