package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {
    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByEmailSuccess(User userByEmail);
    void onSuccessFromRemote();
    void onFailureFromRemote(Exception exception);
}