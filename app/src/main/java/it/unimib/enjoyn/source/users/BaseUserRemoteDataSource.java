package it.unimib.enjoyn.source.users;

import android.net.Uri;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public interface BaseUserRemoteDataSource {

    void createUser(User user, Callback callback);

    void getUserByUsername(String username, Callback callback);

    void getUserByEmail(String email, Callback callback);

    void getCurrentUser(String uid, Callback callback);

    void updatePropic(String uid, Uri propic, Callback callback);

    void updateNameAndSurname(String uid, String name, String surname, Callback callback);

    void updateDescription(String uid, String description, Callback callback);

    void updateEmailVerificationStatus(String uid, Boolean status, Callback callback);

    void updateProfileConfigurationStatus(String uid, Boolean status, Callback callback);

   void updateCategoriesSelectionStatus(String uid, Boolean status, Callback callback);

    void getPropicByUid(String userID, Callback callback);
}
