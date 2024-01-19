package it.unimib.enjoyn.source.user;

import android.net.Uri;

import it.unimib.enjoyn.model.User;

public abstract class BaseUserRemoteDataSource {

    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }

    public abstract void storeUser(User user);

    public abstract void getUserByUsername(String username);

    public abstract void getUserByEmail(String email);

    public abstract void getUser(String uid);

    public abstract void stopGettingUser(String uid);

    public abstract void createPropic(String uid, Uri propic);

    public abstract void updateNameAndSurname(String uid, String name, String surname);

    public abstract void updateDescription(String uid, String description);
}
