package it.unimib.enjoyn.source.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                        authenticationCallback.onSignUpSuccess(fbUser.getUid(), email, username);

                    } else {
                        authenticationCallback.onAuthOperationFailure(task.getException());
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        fbUser = auth.getCurrentUser();
                        authenticationCallback.onAuthOperationSuccess();
                    }
                    else{
                        authenticationCallback.onAuthOperationFailure(task.getException());
                    }
                });
    }

    @Override
    public String getCurrentUserEmail(){
        if(fbUser != null)
            return fbUser.getEmail();
        return null;
    }

    @Override
    public void sendEmailVerification() {
        fbUser.sendEmailVerification().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                authenticationCallback.onAuthOperationSuccess();
            }
            else{
                authenticationCallback.onAuthOperationFailure(task.getException());
            }
        });
    }

    @Override
    public void sendResetPasswordEmail(String email) {
        if (fbUser == null) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    authenticationCallback.onAuthOperationSuccess();
                }
                else{
                    authenticationCallback.onAuthOperationFailure(task.getException());
                }
            });
        }
        else {
            authenticationCallback.onAuthOperationFailure(new Exception("Impossibile modificare password " +
                    "se l'utente è ancora autenticato"));
        }
    }
}
