package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.source.Callback;

public abstract class BaseAuthenticationDataSource {
    protected AuthenticationCallback authenticationCallback;

    public void setAuthenticationCallback(AuthenticationCallback authenticationCallback){
        this.authenticationCallback = authenticationCallback;
    }

    public abstract String getCurrentUserUID();

    public abstract void signUp(String email, String password, String username, Callback callback);
    public abstract void signIn(String email, String password, Callback callback);

    public abstract void refreshSession(Callback callback);

    public abstract void signOut(Callback callback);
    public abstract void sendEmailVerification();
    public abstract void sendResetPasswordEmail(String email);

    public abstract void checkEmailVerification();
}
