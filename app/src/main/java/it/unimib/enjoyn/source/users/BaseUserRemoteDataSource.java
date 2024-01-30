package it.unimib.enjoyn.source.users;

import android.net.Uri;

import it.unimib.enjoyn.model.User;

public abstract class BaseUserRemoteDataSource {

    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback) {
        this.userCallback = userCallback;
    }

    public abstract void createUser(User user);

    public abstract void getUserByUsername(String username);

    public abstract void getUserByEmail(String email);

    public abstract void getCurrentUser(String uid);

    //public abstract void clearCurrentUser(String uid);

    public abstract void updatePropic(String uid, Uri propic);

    public abstract void updateNameAndSurname(String uid, String name, String surname);

    public abstract void updateDescription(String uid, String description);

    public abstract void updateEmailVerificationStatus(String uid, Boolean status);

    public abstract void updateProfileConfigurationStatus(String uid, Boolean status);

    public abstract void updateCategoriesSelectionStatus(String uid, Boolean status);
}