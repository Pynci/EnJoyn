package it.unimib.enjoyn.source.users;

import android.net.Uri;

import it.unimib.enjoyn.model.User;

public interface UserCallback {
    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByEmailSuccess(User userByEmail);
    void onRemoteDatabaseFailure(Exception exception);
    void onLocalDatabaseFailure(Exception exception);
    void onRemoteUserUpdateSuccess();
    void onLocalUserUpdateSuccess(User user);
    void onGetUserByUsernameFailure(Exception exception);
    void onGetUserByEmailFailure(Exception exception);

    void onGetCurrentUserPropicSuccess(Uri uri);
    void onGetCurrentUserPropicFailure(Exception e);
}