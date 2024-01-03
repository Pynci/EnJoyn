package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;

public interface IUserRepository {

    MutableLiveData<Result> addUser(String email, String password, String username);

    MutableLiveData<Result> getUserByUsername(String username);
    MutableLiveData<Result> getUserByEmail(String email);
}
