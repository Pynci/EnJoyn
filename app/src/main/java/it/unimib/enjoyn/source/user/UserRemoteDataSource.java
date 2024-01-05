package it.unimib.enjoyn.source.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Constants;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    private final FirebaseAuth auth;
    private FirebaseUser fbUser;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void addUser(String email, String password, String username) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        fbUser = auth.getCurrentUser();
                        storeUser(email, username, fbUser);

                    } else {
                        userCallback.onAddUserFailure(task.getException());
                    }
                });
    }

    public void storeUser(String email, String username, FirebaseUser fbUser){

        Map<String, String> userMap = new HashMap<>();

        userMap.put("email", email);
        userMap.put("username", username);

        dbReference
                .child(Constants.PATH_FOR_USERS)
                .child(fbUser.getUid())
                .setValue(userMap)
                .addOnCompleteListener( result -> {
                    if(result.isSuccessful()){
                        userCallback.onAddUserSuccess();
                    }
                    else{
                        userCallback.onAddUserFailure(result.getException());
                    }
                });
    }

    // https://firebase.google.com/docs/auth/android/manage-users?hl=it#update_a_users_profile
    public void updateUser(){

    }

    public void getUserByUsername(String username){
        dbReference
                .child(Constants.PATH_FOR_USERS)
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

    public void getUserByEmail(String email){
        dbReference
                .child(Constants.PATH_FOR_USERS)
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
}