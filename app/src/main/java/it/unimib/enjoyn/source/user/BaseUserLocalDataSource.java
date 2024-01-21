package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.model.User;

public abstract class BaseUserLocalDataSource {
    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback){
        this.userCallback = userCallback;
    }

    public abstract void getCurrentUser();
    public abstract void insertCurrentUser(User user);
    public abstract void updateCurrentUser(User user);
    public abstract void clearCurrentUser();
}
