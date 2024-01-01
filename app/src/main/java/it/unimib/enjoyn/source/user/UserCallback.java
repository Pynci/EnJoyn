package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public interface UserCallback {

    public void onAddFailure(Exception exception);

    void onAddSuccess();

    public void onGetSuccess(User user);

    public void onGetFailure();
}