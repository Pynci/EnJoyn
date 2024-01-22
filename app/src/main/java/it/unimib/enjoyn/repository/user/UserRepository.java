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
    private final MutableLiveData<Result> emailVerified;
    private User currentUserSincronoDelVaffanculo;
    private final MutableLiveData<Result> currentUser;

    private final MutableLiveData<Result> resultFromRemoteDatabase;
    private final MutableLiveData<Result> resultFromAuth;

    public UserRepository(BaseUserLocalDataSource userLocalDataSource,
                          BaseUserRemoteDataSource userRemoteDataSource,
                          BaseAuthenticationDataSource authenticationDataSource){
        this.userLocalDataSource = userLocalDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        userLocalDataSource.setUserCallback(this);
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);

        currentUserSincronoDelVaffanculo = null;
        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();

        resultFromRemoteDatabase = new MutableLiveData<>();
        resultFromAuth = new MutableLiveData<>();
        emailVerified = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
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

    public MutableLiveData<Result> getCurrentUser(){
        // TODO: prendere l'utente dal database locale Room
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
    public User getCurrentUserSincronoDelVaffanculo(){
        return currentUserSincronoDelVaffanculo;
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
        currentUserSincronoDelVaffanculo = user;
        resultFromRemoteDatabase.postValue(new Result.Success());
    }

    @Override
    public void onRemoteDatabaseSuccess() {
        resultFromRemoteDatabase.postValue(new Result.Success());
    }

    @Override
    public void onRemoteDatabaseFailure(Exception exception) {
        currentUser.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onLocalDatabaseFailure(Exception exception) {
        currentUser.postValue(new Result.Error(exception.getLocalizedMessage()));
    }





    @Override
    public void onUserReady(User user) {
        currentUserSincronoDelVaffanculo = user;
        resultFromAuth.postValue(new Result.UserSuccess(user));
    }

    @Override
    public void onRemoteUserFetchSuccess(User user) {
        userLocalDataSource.insertUser(user);
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
    public void onAuthSuccess() {
        resultFromAuth.postValue(new Result.Success());
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
        userLocalDataSource.deleteUser(((Result.UserSuccess) currentUser.getValue())
                .getData());
        currentUserSincronoDelVaffanculo = null;
    }

    @Override
    public void onEmailCheckSuccess(Boolean status) {
        currentUserSincronoDelVaffanculo.setEmailVerified(status);
        userRemoteDataSource.updateEmailVerificationStatus(status);
        emailVerified.postValue(new Result.BooleanSuccess(status));
    }

    @Override
    public void onEmailCheckFailure(Exception exception) {
        emailVerified.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onAlreadySignedIn(String uid) {
        userLocalDataSource.getUser(uid);
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