package it.unimib.enjoyn.source.users;

import android.net.Uri;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public abstract class BaseUserRemoteDataSource {

    public abstract void createUser(User user, Callback callback);

    public abstract void getUserByUsername(String username, Callback callback);

    public abstract void getUserByEmail(String email, Callback callback);

    public abstract void getCurrentUser(String uid, Callback callback);

    //public abstract void clearCurrentUser(String uid);

    public abstract void updatePropic(String uid, Uri propic, Callback callback);

    public abstract void updateNameAndSurname(String uid, String name, String surname, Callback callback);

    public abstract void updateDescription(String uid, String description, Callback callback);

    public abstract void updateEmailVerificationStatus(String uid, Boolean status, Callback callback);

    public abstract void updateProfileConfigurationStatus(String uid, Boolean status, Callback callback);

    public abstract void updateCategoriesSelectionStatus(String uid, Boolean status, Callback callback);

    public abstract void getPropicByUid(String userID, Callback callback);
}
