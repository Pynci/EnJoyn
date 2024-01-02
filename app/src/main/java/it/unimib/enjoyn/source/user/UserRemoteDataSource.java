package it.unimib.enjoyn.source.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import it.unimib.enjoyn.util.Costants;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    private final FirebaseAuth auth;
    private FirebaseUser fbUser;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Costants.DATABASE_PATH).getReference();
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
                        userCallback.onAddFailure(task.getException());
                    }
                });
    }

    public void storeUser(String email, String username, FirebaseUser fbUser){

        Map<String, String> userMap = new HashMap<>();

        userMap.put("email", email);
        userMap.put("username", username);

        dbReference
                .child(Costants.PATH_FOR_USERS)
                .child(fbUser.getUid())
                .setValue(userMap)
                .addOnCompleteListener( result -> {
                    if(!result.isSuccessful()){
                        userCallback.onAddFailure(result.getException());
                    }
                    else{
                        userCallback.onAddSuccess();
                    }
                });
    }

    // https://firebase.google.com/docs/auth/android/manage-users?hl=it#update_a_users_profile
    public void updateUser(){

    }

    public void getUser(String email) {
        dbReference
                .child(Costants.PATH_FOR_USERS);

        //Se va a buon fine chiama il metodo onGetSuccess(User user)
        //Se non va a buon fine chiama il metodo onGetFailure
    }
}