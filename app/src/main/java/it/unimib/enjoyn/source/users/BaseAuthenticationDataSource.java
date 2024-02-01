package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.source.Callback;

public interface BaseAuthenticationDataSource {

    String getCurrentUserUID();

    void signUp(String email, String password, String username, Callback callback);
    void signIn(String email, String password, Callback callback);

    void refreshSession(Callback callback);

    void signOut(Callback callback);
    void sendEmailVerification(Callback callback);
    void sendResetPasswordEmail(String email, Callback callback);

    void checkEmailVerification(Callback callback);
}
