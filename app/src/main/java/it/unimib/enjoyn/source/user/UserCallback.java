package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {
    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByEmailSuccess(User userByEmail);
    void onUserCreationSuccess(User user);
    void onRemoteDatabaseFailure(Exception exception);
    void onLocalDatabaseFailure(Exception exception);



    void onRemoteUserFetchSuccess(User user);
    void onRemoteUserUpdateSuccess();
    void onLocalUserDeletionSuccess();
    void onLocalUserUpdateSuccess(User user);
    void onLocalUserFetchSuccess(User user);
    void onLocalUserInsertionSuccess(User user);
}