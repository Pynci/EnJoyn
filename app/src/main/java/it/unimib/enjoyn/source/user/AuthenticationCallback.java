package it.unimib.enjoyn.source.user;

public interface AuthenticationCallback {
    void onSignUpSuccess(String uid, String email, String username);
    void onSignUpFailure(Exception exception);
    void onSignInSuccess();
    void onSignInFailure(Exception exception);
    void onEmailVerificationSendingSuccess();
    void onEmailVerificationSendingFailure(Exception exception);
}
