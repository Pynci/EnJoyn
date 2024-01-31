package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.source.Callback;

public abstract class BaseAuthenticationDataSource {

    public abstract String getCurrentUserUID();

    public abstract void signUp(String email, String password, String username, Callback callback);
    public abstract void signIn(String email, String password, Callback callback);

    public abstract void refreshSession(Callback callback);

    public abstract void signOut(Callback callback);
    public abstract void sendEmailVerification(Callback callback);
    public abstract void sendResetPasswordEmail(String email, Callback callback);

    public abstract void checkEmailVerification(Callback callback);
}
