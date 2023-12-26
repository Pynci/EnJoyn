package it.unimib.enjoyn.source.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Executor;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Costants;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;
    FirebaseAuth auth;
    private FirebaseUser fbUser;

    public UserRemoteDataSource() {
        dbReference = ServiceLocator.getInstance().getDatabaseReference();
        auth = ServiceLocator.getInstance().getFirebaseAuth();
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
                .child(user.getId())
                .setValue(user)
                .addOnCompleteListener( result -> {
                    if(!result.isSuccessful()){
                        userCallback.onAddFailure(result.getException());
                    }
                });
    }

    public void getUser(String email) {


        //Cerca l'utente nel DB sulla base dell'email

        //Se va a buon fine chiama il metodo onGetSuccess(User user)
        //Se non va a buon fine chiama il metodo onGetFailure
    }
}