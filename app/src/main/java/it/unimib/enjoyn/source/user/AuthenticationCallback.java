package it.unimib.enjoyn.source.user;

public interface AuthenticationCallback {

    void onAuthOperationSuccess();
    void onAuthOperationFailure(Exception exception);

    void onSignUpSuccess(String uid, String email, String username);

}
