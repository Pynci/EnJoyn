package it.unimib.enjoyn.source.user;

import com.google.firebase.database.DatabaseReference;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Costants;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserRemoteDataSource extends BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;

    public UserRemoteDataSource() {
        dbReference = ServiceLocator.getInstance().getDatabaseReference();
    }

    @Override
    public void addUser(User user) {

        user.setId(dbReference.push().getKey());

        dbReference
                .child(Costants.PATH_FOR_USERS)
                .child(user.getId())
                .setValue(user)
                .addOnCompleteListener( result -> {

                    if(result.isSuccessful()){
                        userCallback.onAddSuccess();
                    }
                    else{
                        userCallback.onAddFailure();
                    }
                });
    }

    public void getUser(String email) {

        //Cerca l'utente nel DB sulla base dell'email

        //Se va a buon fine chiama il metodo onGetSuccess(User user)
        //Se non va a buon fine chiama il metodo onGetFailure
    }
}