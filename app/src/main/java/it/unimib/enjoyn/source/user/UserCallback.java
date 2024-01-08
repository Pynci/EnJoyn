package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {

    void onStoreUserFailure(Exception exception);
    void onStoreUserSuccess();

    void onGetUserByUsernameSuccess(User userByUsername);
    void onGetUserByUsernameFailure(Exception exception);

    void onGetUserByEmailSuccess(User userByEmail);
    void onGetUserByEmailFailure(Exception exception);

    void onCreatePropicFailure(Exception exception);
    void onCreatePropicSuccess();

    void onCreateNameAndSurnameFailure(Exception exception);
    void onCreateNameAndSurnameSuccess();

    void onCreateDescriptionFailure(Exception exception);
    void onCreateDescriptionSuccess();
}