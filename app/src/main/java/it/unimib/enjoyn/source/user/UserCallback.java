package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {

    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByUsernameFailure(Exception exception);

    void onGetUserByEmailSuccess(User userByEmail);
    void onGetUserByEmailFailure(Exception exception);

    void onSuccessFormRemote();
    void onFailureFromRemote(Exception exception);
}