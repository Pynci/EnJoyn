package it.unimib.enjoyn.source.user;

public abstract class BaseAuthenticationDataSource {
    protected AuthenticationCallback authenticationCallback;

    public void setAuthenticationCallback(AuthenticationCallback authenticationCallback){
        this.authenticationCallback = authenticationCallback;
    }

    public abstract void signUp(String email, String password, String username);
    public abstract void signIn(String email, String password);

    public abstract String getCurrentUserEmail();

    public abstract String getCurrentUserUID();

    public abstract Boolean isCurrentUserEmailVerified();

    public abstract void sendEmailVerification();
    public abstract void sendResetPasswordEmail(String email);
}
