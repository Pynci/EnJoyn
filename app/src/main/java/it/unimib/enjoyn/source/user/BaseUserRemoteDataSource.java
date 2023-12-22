package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public abstract class BaseUserRemoteDataSource {

    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }

    public abstract void addUser(User user);
}
