package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback{

    //Serve a contenere l'eccezione che si potrebbe verificare nel caso dell'aggiunta di un utente
    private final MutableLiveData<Exception> addResultException;
    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository(UserRemoteDataSource userRemoteDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        userRemoteDataSource.setUserCallback(this);
        addResultException = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Exception> addUser(User user) {
        userRemoteDataSource.addUser(user);
        return addResultException;
    }

    @Override
    public MutableLiveData<User> getUser(String email) {
        //Deve chiamare il getUser() del UserRemoteDataSource
        //Deve restituire il risultato contenuto nel mutable live data
        return null;
    }

    @Override
    public void onAddFailure(Exception exception) {
        addResultException.postValue(exception);
    }

    @Override
    public void onGetSuccess(User user) {
        //Deve popolare il mutable live data con il risultato
    }

    @Override
    public void onGetFailure() {

    }
}