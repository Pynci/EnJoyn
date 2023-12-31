package it.unimib.enjoyn.source.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Costants;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    private final FirebaseAuth auth;
    private FirebaseUser fbUser;

    public UserRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance().getReference(Costants.DATABASE_PATH);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void addUser(User user) {

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        fbUser = auth.getCurrentUser();
                        storeUser(user, fbUser);

                    } else {
                        userCallback.onAddFailure(task.getException());
                    }
                });
    }

    public void storeUser(User user, FirebaseUser fbUser){
        user.setId(fbUser.getUid());
        dbReference
                .child(Costants.PATH_FOR_USERS)
                .child(fbUser.getUid())
                .setValue(user)
                .addOnCompleteListener( result -> {
                    if(!result.isSuccessful()){
                        userCallback.onAddFailure(result.getException());
                    }
                });
    }

    public void getUser(String email) {


        dbReference
                .child(Costants.PATH_FOR_USERS);

        //Se va a buon fine chiama il metodo onGetSuccess(User user)
        //Se non va a buon fine chiama il metodo onGetFailure
    }
}