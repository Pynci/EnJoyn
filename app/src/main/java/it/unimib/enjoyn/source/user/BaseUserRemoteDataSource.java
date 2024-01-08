package it.unimib.enjoyn.source.user;

import android.net.Uri;

public abstract class BaseUserRemoteDataSource {

    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }

    public abstract void storeUser(String uid, String email, String username);

    public abstract void getUserByUsername(String username);

    public abstract void getUserByEmail(String email);

    public abstract void createPropic(Uri propic);

    public abstract void createUserNameAndSurname(String name, String surname);

    public abstract void createUserDescription(String description);
}
