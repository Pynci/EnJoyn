package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public abstract class BaseUserLocalDataSource {

    public abstract void getUser(String uid, Callback callback);
    public abstract void insertUser(User user, Callback callback);
    public abstract void updateUser(User user, Callback callback);
    public abstract void deleteUser(User user, Callback callback);
}
