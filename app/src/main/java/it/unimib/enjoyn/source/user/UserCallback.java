package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {

    void onCreateUserFailure(Exception exception);
    void onCreateUserSuccess();

    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByUsernameFailure(Exception exception);

    void onGetUserByEmailSuccess(User userByEmail);
    void onGetUserByEmailFailure(Exception exception);

    void onCreateUserPropicFailure(Exception exception);
    void onCreateUserPropicSuccess();

    void onUpdateNameAndSurnameFailure(Exception exception);
    void onUpdateNameAndSurnameSuccess();
}