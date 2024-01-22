package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {
    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByEmailSuccess(User userByEmail);
    void onUserCreationSuccess(User user);
    void onRemoteDatabaseSuccess(User user);
    void onRemoteDatabaseSuccess();
    void onRemoteDatabaseFailure(Exception exception);
    void onLocalDatabaseFailure(Exception exception);
    void onUserReady(User user);



    void onRemoteUserFetchSuccess(User user);
    void onLocalUserDeletionSuccess();
    void onLocalUserUpdateSuccess(User user);
    void onLocalUserFetchSuccess(User user);

    void onLocalUserInsertionSuccess(User user);
}