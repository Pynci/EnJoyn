package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.model.User;

public interface AuthenticationCallback {

    void onEmailCheckSuccess(Boolean status);
    void onEmailCheckFailure(Exception exception);
    void onEmailSendingSuccess();
    void onEmailSendingFailure(Exception exception);
}
