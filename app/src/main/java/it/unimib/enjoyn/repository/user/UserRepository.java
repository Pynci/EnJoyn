package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.AuthenticationCallback;
import it.unimib.enjoyn.source.user.BaseAuthenticationDataSource;
import it.unimib.enjoyn.source.user.BaseUserLocalDataSource;
import it.unimib.enjoyn.source.user.BaseUserRemoteDataSource;
import it.unimib.enjoyn.source.user.UserCallback;

public class UserRepository implements IUserRepository, UserCallback, AuthenticationCallback {

    private final BaseUserRemoteDataSource userRemoteDataSource;
    private final BaseAuthenticationDataSource authenticationDataSource;

    private final MutableLiveData<Result> userByUsername;
    private final MutableLiveData<Result> userByEmail;
    private final MutableLiveData<Result> emailVerified;
    private User currentUser;

    private final MutableLiveData<Result> resultFromRemoteDatabase;
    private final MutableLiveData<Result> resultFromAuth;

    public UserRepository(BaseUserLocalDataSource userLocalDataSource,
                          BaseUserRemoteDataSource userRemoteDataSource,
                          BaseAuthenticationDataSource authenticationDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);

        currentUser = null;
        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();

        resultFromRemoteDatabase = new MutableLiveData<>();
        resultFromAuth = new MutableLiveData<>();
        emailVerified = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> createUser(String email, String password, String username) {
        authenticationDataSource.signUp(email, password, username);
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
    public User getCurrentUser(){
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> updatePropic(Uri uri) {
        userRemoteDataSource.updatePropic(uri);
        return resultFromRemoteDatabase;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateNameAndSurname(name, surname);
        return resultFromRemoteDatabase;
    }

    @Override
    public MutableLiveData<Result> updateDescription(String description) {
        userRemoteDataSource.updateDescription(description);
        return resultFromRemoteDatabase;
    }

    @Override
    public MutableLiveData<Result> signIn(String email, String password){
        authenticationDataSource.signIn(email, password);
        return resultFromAuth;
    }

    @Override
    public MutableLiveData<Result> refreshSession(){
        authenticationDataSource.refreshSession();
        return resultFromAuth;
    }

    @Override
    public MutableLiveData<Result> signOut(){
        authenticationDataSource.signOut();
        return resultFromAuth;
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
    public MutableLiveData<Result> updateEmailVerificationStatus(){
        authenticationDataSource.checkEmailVerification();
        return emailVerified;
    }

    @Override
    public MutableLiveData<Result> updateProfileConfigurationStatus(){
        userRemoteDataSource.updateProfileConfigurationStatus(true);
        return resultFromRemoteDatabase;
    }




    // CALLBACK

    @Override
    public void onRemoteDatabaseSuccess(User user) {
        currentUser = user;
        resultFromRemoteDatabase.postValue(new Result.Success());
    }

    @Override
    public void onRemoteDatabaseSuccess() {
        resultFromRemoteDatabase.postValue(new Result.Success());
    }

    @Override
    public void onRemoteDatabaseFailure(Exception exception) {
        resultFromRemoteDatabase.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onUserReady(User user) {
        currentUser = user;
        resultFromAuth.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onAuthSuccess() {
        resultFromAuth.postValue(new Result.Success());
    }

    @Override
    public void onAuthFailure(Exception exception) {
        resultFromAuth.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onSignUpSuccess(User user) {
        userRemoteDataSource.storeUser(user);
    }

    @Override
    public void onUserCreationSuccess(User user) {
        userRemoteDataSource.setCurrentUser(user.getUid());
    }

    @Override
    public void onSignInSuccess(User user) {
        userRemoteDataSource.setCurrentUser(user.getUid());
    }

    @Override
    public void onSignOutSuccess() {
        resultFromAuth.postValue(new Result.Success());
        userRemoteDataSource.clearCurrentUser(currentUser.getUid());
        currentUser = null;
    }

    @Override
    public void onEmailCheckSuccess(Boolean status) {
        currentUser.setEmailVerified(status);
        userRemoteDataSource.updateEmailVerificationStatus(status);
        emailVerified.postValue(new Result.BooleanSuccess(status));
    }

    @Override
    public void onEmailCheckFailure(Exception exception) {
        emailVerified.postValue(new Result.Error(exception.getLocalizedMessage()));
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