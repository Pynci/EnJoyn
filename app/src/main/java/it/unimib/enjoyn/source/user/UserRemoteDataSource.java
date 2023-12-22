package it.unimib.enjoyn.source.user;

import com.google.firebase.database.DatabaseReference;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserRemoteDataSource implements BaseUserRemoteDataSource{

    private final DatabaseReference dbReference;

    public UserRemoteDataSource() {
        dbReference = ServiceLocator.getInstance().getDatanaseReference();
    }

    @Override
    public void addUser(User user) {

        //user.setId(dbReference.child("Users"));
    }
}
