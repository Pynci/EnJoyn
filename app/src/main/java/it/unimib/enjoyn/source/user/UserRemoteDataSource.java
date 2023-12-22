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

                    /*
                    TODO: Controllare se l'operazione va a buon fine o meno
                     */

                    if(result.isSuccessful()){

                    }
                    else{

                    }

                });
    }
}
