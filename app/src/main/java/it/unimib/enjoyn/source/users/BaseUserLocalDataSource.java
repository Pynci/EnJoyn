package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public interface BaseUserLocalDataSource {

    void getUser(String uid, Callback callback);
    void insertUser(User user, Callback callback);
    void updateUser(User user, Callback callback);
    void deleteUser(User user, Callback callback);
}
