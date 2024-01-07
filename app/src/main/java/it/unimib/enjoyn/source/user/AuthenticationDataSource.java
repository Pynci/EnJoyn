package it.unimib.enjoyn.source.user;

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
                        authenticationCallback.onSignUpSuccess(fbUser.getUid(), email, username);

                    } else {
                        authenticationCallback.onSignUpFailure(task.getException());
                    }
                });
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        authenticationCallback.onSignInSuccess();
                    }
                    else{
                        authenticationCallback.onSignInFailure(task.getException());
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
                authenticationCallback.onEmailVerificationSendingSuccess();
            }
            else{
                authenticationCallback.onEmailVerificationSendingFailure(task.getException());
            }
        });
    }
}
