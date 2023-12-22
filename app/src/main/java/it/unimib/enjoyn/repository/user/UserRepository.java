package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback{

    //Si deve istanziare un oggetto di tipo mutable live data

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
    public MutableLiveData<User> getUser(String email) {
        //Deve chiamare il getUser() del UserRemoteDataSource
        //Deve restituire il risultato contenuto nel mutable live data
        return null;
    }

    @Override
    public void onAddFailure() {

    }

    @Override
    public void onAddSuccess() {

    }

    @Override
    public void onGetSuccess(User user) {
        //Deve popolare il mutable live data con il risultato
    }

    @Override
    public void onGetFailure() {

    }
}