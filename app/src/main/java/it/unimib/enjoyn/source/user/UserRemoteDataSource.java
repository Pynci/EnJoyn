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
    private ValueEventListener userListener;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
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
    public void storeUser(User user) {
        dbReference
                .child(Constants.USERS_PATH)
                .child(user.getUid())
                .setValue(user)
                .addOnCompleteListener( result -> {
                    if(result.isSuccessful()){
                        userCallback.onAccountCreationSuccess(user);
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
    public void getUser(String uid){
        dbReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .addValueEventListener(userListener);
    }

    @Override
    public void stopGettingUser(String uid){
        dbReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .removeEventListener(userListener);
    }

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
    public void updatePropic(String uid, Uri propic) {

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference userImageRef = storageRef.child("user_images/" + uid);

        UploadTask uploadTask = userImageRef.putFile(propic);

        uploadTask.addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                userCallback.onRemoteDatabaseFailure(task.getException());
            }
            else{
                userCallback.onRemoteDatabaseSuccess();
            }
        });
    }

    @Override
    public void updateNameAndSurname(String uid, String name, String surname) {

        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("nome", name);
        updateMap.put("cognome", surname);

        updateUser(uid, updateMap);
    }

    @Override
    public void updateDescription(String uid, String description) {

        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("descrizione", description);

        updateUser(uid, updateMap);
    }

}