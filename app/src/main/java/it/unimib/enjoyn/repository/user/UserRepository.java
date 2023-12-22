package it.unimib.enjoyn.repository.user;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback{

    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository(UserRemoteDataSource userRemoteDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        userRemoteDataSource.setUserCallback(this);
    }

    @Override
    public void addUser(User user) {
        userRemoteDataSource.addUser(user);
    }

    @Override
    public void onAddFailure() {

    }

    @Override
    public void onAddSuccess() {

    }
}
