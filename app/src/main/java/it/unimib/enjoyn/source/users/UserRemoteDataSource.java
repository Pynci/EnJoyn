package it.unimib.enjoyn.source.users;

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

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.util.Constants;

public class UserRemoteDataSource implements BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    private final FirebaseStorage firebaseStorage;
    boolean isFirstUserFetch;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);
        isFirstUserFetch = true;
    }

    @Override
    public void createUser(User user, Callback callback) {
        dbReference
                .child(Constants.USERS_PATH)
                .child(user.getUid())
                .setValue(user)
                .addOnCompleteListener( result -> {
                    if(result.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error("USER_REMOTE_CREATION_FAILED"));
                    }
                });
    }

    @Override
    public void getUserByUsername(String username, Callback callback){
        dbReference
                .child(Constants.USERS_PATH)
                .orderByChild("username")
                .equalTo(username)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        final Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            User user = iterator.next().getValue(User.class);
                            callback.onComplete(new Result.UserSuccess(user));
                        }
                        else{
                            callback.onComplete(new Result.UserSuccess(null));
                        }
                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.USER_REMOTE_FETCH_ERROR));
                    }
                });
    }

    @Override
    public void getUserByEmail(String email, Callback callback){
        dbReference
                .child(Constants.USERS_PATH)
                .orderByChild("email")
                .equalTo(email)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        final Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            User user = iterator.next().getValue(User.class);
                            callback.onComplete(new Result.UserSuccess(user));
                        }
                        else{
                            callback.onComplete(new Result.UserSuccess(null));
                        }
                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.USER_REMOTE_FETCH_ERROR));
                    }
                });
    }

    @Override
    public void getCurrentUser(String uid, Callback callback){
        dbReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User currentUser = task.getResult().getValue(User.class);
                        callback.onComplete(new Result.UserSuccess(currentUser));
                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.USER_REMOTE_FETCH_ERROR));
                    }
                });
    }

    public void updateUser(String uid, Map<String, Object> updateMap, Callback callback){
        DatabaseReference userReference = dbReference
                .child(Constants.USERS_PATH)
                .child(uid);

        userReference.updateChildren(updateMap).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                callback.onComplete(new Result.Success());
            else
                callback.onComplete(new Result.Error(Constants.USER_REMOTE_UPDATE_ERROR));
        });
    }

    @Override
    public void updatePropic(String uid, Uri propic, Callback callback) {
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference userImageRef = storageRef.child("user_images/" + uid);

        UploadTask uploadTask = userImageRef.putFile(propic);

        uploadTask.addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                callback.onComplete(new Result.Success());
            } else {
                callback.onComplete(new Result.Error(Constants.USER_REMOTE_UPDATE_ERROR));
            }
        });
    }

    @Override
    public void updateNameAndSurname(String uid, String name, String surname, Callback callback) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", name);
        updateMap.put("surname", surname);
        updateUser(uid, updateMap, callback);
    }

    @Override
    public void updateDescription(String uid, String description, Callback callback) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("description", description);
        updateUser(uid, updateMap, callback);
    }

    @Override
    public void updateEmailVerificationStatus(String uid, Boolean status, Callback callback){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("emailVerified", status);
        updateUser(uid, updateMap, callback);
    }

    @Override
    public void updateProfileConfigurationStatus(String uid, Boolean status, Callback callback){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("profileConfigured", status);
        updateUser(uid, updateMap, callback);
    }

    @Override
    public void updateCategoriesSelectionStatus(String uid, Boolean status, Callback callback){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("categoriesSelectionDone", status);
        updateUser(uid, updateMap, callback);
    }

    @Override
    public void getPropicByUid(String userId, Callback callback) {

        StorageReference imageref = firebaseStorage.getReference()
                .child("user_images")
                .child(userId);

        imageref
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    callback.onComplete(new Result.SingleImageReadFromRemote(uri));
                })
                .addOnFailureListener(e -> {
                    callback.onComplete(new Result.Error(Constants.USER_REMOTE_FETCH_ERROR));
                });
    }

}