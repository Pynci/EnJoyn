package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback{

    private final MutableLiveData<Exception> addResultException;
    private final MutableLiveData<Exception> userByUsernameException;
    private final MutableLiveData<Exception> userByEmailException;
    private final MutableLiveData<User> userByUsername;
    private final MutableLiveData<User> userByEmail;
    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository(UserRemoteDataSource userRemoteDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        userRemoteDataSource.setUserCallback(this);
        addResultException = new MutableLiveData<>();
        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();
        userByUsernameException = new MutableLiveData<>();
        userByEmailException = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Exception> addUser(String email, String password, String username) {
        userRemoteDataSource.addUser(email, password, username);
        return addResultException;
    }

    @Override
    public MutableLiveData<User> getUserByUsername(String username){
        userRemoteDataSource.getUserByUsername(username);
        return userByUsername;
    }

    @Override
    public MutableLiveData<User> getUserByEmail(String email) {
        userRemoteDataSource.getUserByEmail(email);
        return userByEmail;
    }

    @Override
    public void onAddFailure(Exception exception) {
        addResultException.postValue(exception);
    }

    @Override
    public void onAddSuccess(){
        addResultException.postValue(null);
    }

    @Override
    public void onGetUserByUsernameSuccess(User user){
        userByUsername.postValue(user);
        userByUsernameException.postValue(null);
    }

    @Override
    public void onGetUserByUsernameFailure(Exception exception){
        userByUsername.postValue(null);
        userByUsernameException.postValue(exception);
    }

    @Override
    public void onGetUserByEmailSuccess(User user) {
        userByEmail.postValue(user);
        userByUsernameException.postValue(null);
    }

    @Override
    public void onGetUserByEmailFailure(Exception exception) {
        userByEmail.postValue(null);
        userByEmailException.postValue(exception);
    }

}