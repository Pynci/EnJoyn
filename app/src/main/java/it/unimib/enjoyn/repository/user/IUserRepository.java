package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;

public interface IUserRepository {

    public abstract void addUser(User user);

    public abstract MutableLiveData<User> getUser(String email);
}
