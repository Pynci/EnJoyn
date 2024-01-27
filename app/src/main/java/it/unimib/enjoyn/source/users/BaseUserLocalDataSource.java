package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.model.User;

public abstract class BaseUserLocalDataSource {
    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback){
        this.userCallback = userCallback;
    }

    public abstract void getUser(String uid);
    public abstract void insertUser(User user);
    public abstract void updateUser(User user);
    public abstract void deleteUser(User user);
}
