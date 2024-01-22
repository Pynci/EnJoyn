package it.unimib.enjoyn.source.user;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private final ValueEventListener userListener;
    private String currentUserUID;
    boolean isFirstUserFetch;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);
        currentUserUID = "";
        isFirstUserFetch = true;

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    if(isFirstUserFetch){
                        user.setUid(currentUserUID);
                        isFirstUserFetch = false;
                        userCallback.onUserReady(user);
                    }
                    userCallback.onRemoteDatabaseSuccess(user);
                }
                else{
                    userCallback.onRemoteDatabaseFailure(new Exception("ERRORE (cambiare stringa)"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userCallback.onRemoteDatabaseSuccess(null);
            }
        };
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
                        userCallback.onRemoteDatabaseFailure(task.getException());
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
                        userCallback.onRemoteDatabaseFailure(task.getException());
                    }
                });
    }

    @Override
    public void getCurrentUser(String uid){
        //currentUserUID = uid;
        dbReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot queryResult = task.getResult();
                        final Iterator<DataSnapshot> iterator = queryResult.getChildren().iterator();
                        if(iterator.hasNext()){
                            User currentUser = iterator.next().getValue(User.class);
                            currentUser.setUid(uid);
                            userCallback.onRemoteUserFetchSuccess(currentUser);
                        }
                        else{
                            userCallback.onRemoteDatabaseFailure(task.getException());
                        }
                    }
                    else{
                        userCallback.onRemoteDatabaseFailure(task.getException());
                    }
                });
    }

//    @Override
//    public void clearCurrentUser(String uid){
//        //currentUserUID = "";
//        dbReference
//                .child(Constants.USERS_PATH)
//                .child(uid)
//                .removeEventListener(userListener);
//    }

    public void updateUser(String uid, Map<String, Object> updateMap){
        DatabaseReference userReference = dbReference
                .child(Constants.USERS_PATH)
                .child(uid);

        userReference.updateChildren(updateMap).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                userCallback.onRemoteDatabaseSuccess();
            else
                userCallback.onRemoteDatabaseFailure(task.getException());
        });
    }

    @Override
    public void updatePropic(Uri propic) {

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference userImageRef = storageRef.child("user_images/" + currentUserUID);

        UploadTask uploadTask = userImageRef.putFile(propic);

        uploadTask.addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                userCallback.onRemoteDatabaseSuccess();
            } else {
                userCallback.onRemoteDatabaseFailure(task.getException());
            }
        });
    }

    @Override
    public void updateNameAndSurname(String name, String surname) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", name);
        updateMap.put("surname", surname);
        updateUser(currentUserUID, updateMap);
    }

    @Override
    public void updateDescription(String description) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("description", description);
        updateUser(currentUserUID, updateMap);
    }

    @Override
    public void updateEmailVerificationStatus(Boolean status){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("emailVerified", status);
        updateUser(currentUserUID, updateMap);
    }

    @Override
    public void updateProfileConfigurationStatus(Boolean status){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("profileConfigured", status);
        updateUser(currentUserUID, updateMap);
    }

}