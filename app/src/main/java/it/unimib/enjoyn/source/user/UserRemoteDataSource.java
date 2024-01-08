package it.unimib.enjoyn.source.user;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Constants;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    private final FirebaseStorage firebaseStorage;
    private final FirebaseAuth auth;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void storeUser(String uid, String email, String username) {
        Map<String, String> userMap = new HashMap<>();

        userMap.put("email", email);
        userMap.put("username", username);

        dbReference
                .child(Constants.USERS_PATH)
                .child(uid)
                .setValue(userMap)
                .addOnCompleteListener( result -> {
                    if(result.isSuccessful()){
                        userCallback.onStoreUserSuccess();
                    }
                    else{
                        userCallback.onStoreUserFailure(result.getException());
                    }
                });
    }

    // https://firebase.google.com/docs/auth/android/manage-users?hl=it#update_a_users_profile
    public void updateUser(){

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

    /*
    TODO: Da testare e controllare che sia stato implementato correttamente
     */
    @Override
    public void createPropic(Uri propic) {

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference userImageRef = storageRef.child("user_images/" + auth.getUid());

        UploadTask uploadTask = userImageRef.putFile(propic);

        uploadTask.addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {
                userCallback.onCreatePropicFailure(task.getException());
            }
            else{
                userCallback.onCreatePropicSuccess();
            }
        });
    }

    @Override
    public void createUserNameAndSurname(String name, String surname) {

        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("nome", name);
        updateMap.put("cognome", surname);

        DatabaseReference userReference = dbReference
                .child(Constants.USERS_PATH)
                .child(auth.getUid());

        userReference.updateChildren(updateMap);
    }

    @Override
    public void createUserDescription(String description) {

        Map<String, Object> updateMap = new HashMap<>();

        updateMap.put("descrizione", description);

        DatabaseReference userReference = dbReference
                .child(Constants.USERS_PATH)
                .child(auth.getUid());

        userReference.updateChildren(updateMap);
    }

}