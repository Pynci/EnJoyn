package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.AuthenticationCallback;
import it.unimib.enjoyn.source.user.AuthenticationDataSource;
import it.unimib.enjoyn.source.user.BaseAuthenticationDataSource;
import it.unimib.enjoyn.source.user.BaseUserRemoteDataSource;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;
import it.unimib.enjoyn.util.Errors;

public class UserRepository implements IUserRepository, UserCallback, AuthenticationCallback {

    private final BaseUserRemoteDataSource userRemoteDataSource;
    private final BaseAuthenticationDataSource authenticationDataSource;

    private final MutableLiveData<Result> userByUsername;
    private final MutableLiveData<Result> userByEmail;
    private User currentUser;

    private final MutableLiveData<Result> resultFromRemoteDatabase;
    private final MutableLiveData<Result> resultFromAuth;

    public UserRepository(UserRemoteDataSource userRemoteDataSource, AuthenticationDataSource authenticationDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);

        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();

        resultFromRemoteDatabase = new MutableLiveData<>();
        resultFromAuth = new MutableLiveData<>();
    }


    /*
    Operazioni di manipolazione dei dati (create, get, read, update)
     */
    @Override
    public MutableLiveData<Result> createUser(String email, String password, String username) {
        authenticationDataSource.signUp(email, password, username);
        return resultFromRemoteDatabase;
    }

    @Override
    public MutableLiveData<Result> createPropic(Uri uri) {
        userRemoteDataSource.updatePropic(currentUser.getUid(), uri);
        return resultFromRemoteDatabase;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateNameAndSurname(currentUser.getUid(), name, surname);
        return resultFromRemoteDatabase;
    }

    @Override
    public MutableLiveData<Result> updateDescription(String description) {
        userRemoteDataSource.updateDescription(currentUser.getUid(), description);
        return resultFromRemoteDatabase;
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
    public MutableLiveData<Result> getCurrentUser(){
        userRemoteDataSource.getUserByEmail(authenticationDataSource.getCurrentUserEmail());
        return userByEmail;
    }

    public User getCurrentUserTAH(){
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> signIn(String email, String password){
        authenticationDataSource.signIn(email, password);
        return resultFromAuth;
    }

    @Override
    public void signOut(){
        authenticationDataSource.signOut();
    }

    @Override
    public MutableLiveData<Result> sendEmailVerification(){
        authenticationDataSource.sendEmailVerification();
        return resultFromAuth;
    }

    @Override
    public MutableLiveData<Result> sendResetPasswordEmail(String email) {
        authenticationDataSource.sendResetPasswordEmail(email);
        return resultFromAuth;
    }

    @Override
    public Result isCurrentUserEmailVerified(){
        Boolean value  = authenticationDataSource.isCurrentUserEmailVerified();
        if(value != null){
            return new Result.BooleanSuccess(value);
        }
        else
            return new Result.Error(Errors.EMAILVERIFICATION_CHECK_FAILURE);
    }

    /*
    Callback
     */

    @Override
    public void onRemoteDatabaseSuccess(User user) {
        resultFromRemoteDatabase.postValue(new Result.Success());
        currentUser = user;
    }

    @Override
    public void onRemoteDatabaseSuccess() {
        resultFromRemoteDatabase.postValue(new Result.Success());
    }

    @Override
    public void onRemoteDatabaseFailure(Exception exception) {
        resultFromRemoteDatabase.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onAuthSuccess() {
        resultFromAuth.postValue(new Result.Success());
    }

    @Override
    public void onAuthFailure(Exception exception) {
        resultFromAuth.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onSignUpSuccess(User user) {
        userRemoteDataSource.storeUser(user);
    }

    @Override
    public void onAccountCreationSuccess(User user) {
        resultFromAuth.postValue(new Result.Success());
        userRemoteDataSource.getUser(user.getUid());
    }

    @Override
    public void onSignInSuccess(User user) {
        resultFromAuth.postValue(new Result.Success());
        userRemoteDataSource.getUser(user.getUid());
    }

    @Override
    public void onSignOutSuccess() {
        userRemoteDataSource.stopGettingUser(currentUser.getUid());
        currentUser = null;
    }

    @Override
    public void onGetUserByUsernameSuccess(User user){
        userByUsername.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onGetUserByEmailSuccess(User user) {
        userByEmail.postValue(new Result.UserResponseSuccess(user));
    }

}