package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;

public class UserMockRepository implements IUserRepository{

    @Override
    public MutableLiveData<Exception> addUser(User user) {
        return null;
    }

    @Override
    public MutableLiveData<User> getUser(String email) {
        return null;
    }
}
