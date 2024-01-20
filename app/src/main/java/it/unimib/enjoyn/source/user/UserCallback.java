package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {
    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByEmailSuccess(User userByEmail);
    void onUserCreationSuccess(User user);
    void onRemoteDatabaseSuccess(User user);
    void onRemoteDatabaseSuccess();
    void onRemoteDatabaseFailure(Exception exception);
}