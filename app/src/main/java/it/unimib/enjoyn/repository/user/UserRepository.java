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

    private final BaseUserLocalDataSource userLocalDataSource;
    private final BaseUserRemoteDataSource userRemoteDataSource;
    private final BaseAuthenticationDataSource authenticationDataSource;

    private final MutableLiveData<Result> userByUsername;
    private final MutableLiveData<Result> userByEmail;
    private final MutableLiveData<Result> currentUser;
    private final MutableLiveData<Result> emailSent;

    public UserRepository(BaseUserLocalDataSource userLocalDataSource,
                          BaseUserRemoteDataSource userRemoteDataSource,
                          BaseAuthenticationDataSource authenticationDataSource){
        this.userLocalDataSource = userLocalDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        userLocalDataSource.setUserCallback(this);
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);

        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
        emailSent = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> getCurrentUser(){
        userLocalDataSource.getUser(authenticationDataSource.getCurrentUserUID());
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> signUp(String email, String password, String username) {
        authenticationDataSource.signUp(email, password, username);
        return currentUser;
    }


    @Override
    public MutableLiveData<Result> signIn(String email, String password){
        authenticationDataSource.signIn(email, password);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> refreshSession(){
        authenticationDataSource.refreshSession();
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> signOut(){
        authenticationDataSource.signOut();
        return currentUser;
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
    public MutableLiveData<Result> updatePropic(Uri uri) {
        userRemoteDataSource.updatePropic(authenticationDataSource.getCurrentUserUID(), uri);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateNameAndSurname(authenticationDataSource.getCurrentUserUID(), name, surname);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> updateDescription(String description) {
        userRemoteDataSource.updateDescription(authenticationDataSource.getCurrentUserUID(), description);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> sendEmailVerification(){
        authenticationDataSource.sendEmailVerification();
        return emailSent;
    }

    @Override
    public MutableLiveData<Result> sendResetPasswordEmail(String email) {
        authenticationDataSource.sendResetPasswordEmail(email);
        return emailSent;
    }

    @Override
    public MutableLiveData<Result> updateEmailVerificationStatus(){
        authenticationDataSource.checkEmailVerification();
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> updateProfileConfigurationStatus(){
        userRemoteDataSource.updateProfileConfigurationStatus(authenticationDataSource.getCurrentUserUID(), true);
        return currentUser;
    }



    // CALLBACK

    @Override
    public void onRemoteDatabaseFailure(Exception exception) {
        currentUser.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onLocalDatabaseFailure(Exception exception) {
        currentUser.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onRemoteUserFetchSuccess(User user) {
        userLocalDataSource.insertUser(user);
    }

    @Override
    public void onRemoteUserUpdateSuccess() {
        userRemoteDataSource.getCurrentUser(authenticationDataSource.getCurrentUserUID());
    }

    @Override
    public void onLocalUserDeletionSuccess() {
        currentUser.postValue(new Result.UserSuccess(null));
    }

    @Override
    public void onLocalUserUpdateSuccess(User user) {
        currentUser.postValue(new Result.UserSuccess(user));
    }

    @Override
    public void onLocalUserFetchSuccess(User user) {
        currentUser.postValue(new Result.UserSuccess(user));
    }

    @Override
    public void onLocalUserInsertionSuccess(User user) {
        currentUser.postValue(new Result.UserSuccess(user));
    }

    @Override
    public void onAuthFailure(Exception exception) {
        currentUser.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onSignUpSuccess(User user) {
        userRemoteDataSource.createUser(user);
    }

    @Override
    public void onUserCreationSuccess(User user) {
        currentUser.postValue(new Result.UserSuccess(user));
    }

    @Override
    public void onSignInSuccess(User user) {
        userRemoteDataSource.getCurrentUser(user.getUid());
    }

    @Override
    public void onSignOutSuccess() {
        User user = ((Result.UserSuccess) currentUser.getValue()).getData();
        if(user != null){
            userLocalDataSource.deleteUser(((Result.UserSuccess) currentUser.getValue())
                    .getData());
        }
    }

    @Override
    public void onEmailCheckSuccess(Boolean status) {
        userRemoteDataSource.updateEmailVerificationStatus(authenticationDataSource.getCurrentUserUID(), status);
    }

    @Override
    public void onEmailCheckFailure(Exception exception) {
        currentUser.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onAlreadySignedIn(String uid) {
        userLocalDataSource.getUser(uid);
    }

    @Override
    public void onEmailSendingSuccess() {
        emailSent.postValue(new Result.Success());
    }

    @Override
    public void onEmailSendingFailure(Exception exception) {
        emailSent.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onGetUserByUsernameSuccess(User user){
        userByUsername.postValue(new Result.UserSuccess(user));
    }

    @Override
    public void onGetUserByEmailSuccess(User user) {
        userByEmail.postValue(new Result.UserSuccess(user));
    }

}