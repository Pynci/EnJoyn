package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback{

    private final MutableLiveData<Result> addResultException;
    private final MutableLiveData<Result> userByUsername;
    private final MutableLiveData<Result> userByEmail;
    private final UserRemoteDataSource userRemoteDataSource;

    public UserRepository(UserRemoteDataSource userRemoteDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        userRemoteDataSource.setUserCallback(this);
        addResultException = new MutableLiveData<>();
        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> addUser(String email, String password, String username) {
        userRemoteDataSource.addUser(email, password, username);
        return addResultException;
    }

    @Override
    public MutableLiveData<Result> getUserByUsername(String username){
        userRemoteDataSource.getUserByUsername(username);
        return userByUsername;
    }

    @Override
    public MutableLiveData<Result> getUserByEmail(String email) {
        userRemoteDataSource.getUserByEmail(email);
        return userByEmail;
    }


    @Override
    public void onAddUserSuccess(){
        addResultException.postValue(null);
    }

    @Override
    public void onAddUserFailure(Exception exception) {
        addResultException.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onGetUserByUsernameSuccess(User user){
        userByUsername.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onGetUserByUsernameFailure(Exception exception){
        userByUsername.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onGetUserByEmailSuccess(User user) {
        userByEmail.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onGetUserByEmailFailure(Exception exception) {
        userByEmail.postValue(new Result.Error(exception.getMessage()));
    }

}