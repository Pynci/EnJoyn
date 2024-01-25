package it.unimib.enjoyn.source.user;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Constants;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    private final FirebaseStorage firebaseStorage;
    boolean isFirstUserFetch;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);
        isFirstUserFetch = true;
    }

    @Override
    public void createUser(User user) {
        dbReference
                .child(Constants.USERS_PATH)
                .child(user.getUid())
                .setValue(user)
                .addOnCompleteListener( result -> {
                    if(result.isSuccessful()){
                        userCallback.onUserCreationSuccess(user);
                    }
                    else{
                        userCallback.onRemoteDatabaseFailure(result.getException());
                    }
                });
    }

    @Override
    public void getUserByUsername(String username){
        dbReference
                .child(Constants.USERS_PATH)
                .orderByChild("username")
                .equalTo(username)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        final Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            userCallback.onGetUserByUsernameSuccess(iterator.next().getValue(User.class));
                        }
                        else{
                            userCallback.onGetUserByUsernameSuccess(null);
                        }
                    }
                    else{
                        userCallback.onGetUserByUsernameFailure(task.getException());
                    }
                });
    }

    @Override
    public void getUserByEmail(String email){
        dbReference
                .child(Constants.USERS_PATH)
                .orderByChild("email")
                .equalTo(email)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        final Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            userCallback.onGetUserByEmailSuccess(iterator.next().getValue(User.class));
                        }
                        else{
                            userCallback.onGetUserByEmailSuccess(null);
                        }
                    }
                    else{
                        userCallback.onGetUserByEmailFailure(task.getException());
                    }
                });
    }

    @Override
    public void getCurrentUser(String uid){
        dbReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User currentUser = task.getResult().getValue(User.class);
                        userCallback.onRemoteUserFetchSuccess(currentUser);
                    }
                    else{
                        userCallback.onRemoteDatabaseFailure(task.getException());
                    }
                });
    }

    public void updateUser(String uid, Map<String, Object> updateMap){
        DatabaseReference userReference = dbReference
                .child(Constants.USERS_PATH)
                .child(uid);

        userReference.updateChildren(updateMap).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                userCallback.onRemoteUserUpdateSuccess();
            else
                userCallback.onRemoteDatabaseFailure(task.getException());
        });
    }

    @Override
    public void updatePropic(String uid, Uri propic) {
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference userImageRef = storageRef.child("user_images/" + uid);

        UploadTask uploadTask = userImageRef.putFile(propic);

        uploadTask.addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                userCallback.onRemoteUserUpdateSuccess();
            } else {
                userCallback.onRemoteDatabaseFailure(task.getException());
            }
        });
    }

    @Override
    public void updateNameAndSurname(String uid, String name, String surname) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", name);
        updateMap.put("surname", surname);
        updateUser(uid, updateMap);
    }

    @Override
    public void updateDescription(String uid, String description) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("description", description);
        updateUser(uid, updateMap);
    }

    @Override
    public void updateEmailVerificationStatus(String uid, Boolean status){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("emailVerified", status);
        updateUser(uid, updateMap);
    }

    @Override
    public void updateProfileConfigurationStatus(String uid, Boolean status){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("profileConfigured", status);
        updateUser(uid, updateMap);
    }

    @Override
    public void updateCategoriesSelectionStatus(String uid, Boolean status){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("categoriesSelectionDone", status);
        updateUser(uid, updateMap);
    }

    @Override
    public void getImageByUserId(String userId) {

        StorageReference imageref = firebaseStorage.getReference()
                .child("user_images")
                .child(userId);

        imageref
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    userCallback.onGetCurrentUserPropicSuccess(uri);
                })
                .addOnFailureListener(e -> {
                    userCallback.onGetCurrentUserPropicFailure(e);
                });
    }

}