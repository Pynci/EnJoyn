package it.unimib.enjoyn.repository.user;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository extends IUserRepository{

    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository(UserRemoteDataSource userRemoteDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
    }

    @Override
    public void addUser(User user) {
        userRemoteDataSource.addUser(user);
    }
}
