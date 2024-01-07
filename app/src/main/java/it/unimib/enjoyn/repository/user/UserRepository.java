package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback{

    private final MutableLiveData<Result> updateNameAndSurnameResult;
    private final MutableLiveData<Result> createImageResult;
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
        createImageResult = new MutableLiveData<>();
        updateNameAndSurnameResult = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> createUser(String email, String password, String username) {
        userRemoteDataSource.createUser(email, password, username);
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
    public void onCreateUserSuccess(){
        addResultException.postValue(null);
    }

    @Override
    public void onCreateUserFailure(Exception exception) {
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


    /*
    TODO: Da testare e controllare che siano implementati correttamente
     */
    @Override
    public void onCreateUserPropicFailure(Exception exception) {
        createImageResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onCreateUserPropicSuccess() {
        createImageResult.postValue(null);
    }

    @Override
    public void onUpdateNameAndSurnameFailure(Exception exception) {
        updateNameAndSurnameResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onUpdateNameAndSurnameSuccess() {
        updateNameAndSurnameResult.postValue(null);
    }

    @Override
    public MutableLiveData<Result> createUserPropic(Uri uri) {
        userRemoteDataSource.createUserPropic(uri);
        return createImageResult;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateUserNameAndSurname(name, surname);
        return updateNameAndSurnameResult;
    }

}