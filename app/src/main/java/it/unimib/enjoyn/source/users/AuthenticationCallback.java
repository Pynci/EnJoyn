package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.model.User;

public interface AuthenticationCallback {

    void onAuthFailure(Exception exception);
    void onSignUpSuccess(User user);
    void onSignInSuccess(User user);
    void onSignOutSuccess();
    void onEmailCheckSuccess(Boolean status);
    void onEmailCheckFailure(Exception exception);
    void onAlreadySignedIn(String uid);
    void onEmailSendingSuccess();
    void onEmailSendingFailure(Exception exception);
    void onNotLoggedYet();
}
