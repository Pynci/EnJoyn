package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface AuthenticationCallback {

    void onAuthSuccess();
    void onAuthFailure(Exception exception);
    void onSignUpSuccess(User user);
    void onSignInSuccess(User user);
    void onSignOutSuccess();

}
