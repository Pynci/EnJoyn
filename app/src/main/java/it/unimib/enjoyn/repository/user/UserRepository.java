package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.AuthenticationCallback;
import it.unimib.enjoyn.source.user.AuthenticationDataSource;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class UserRepository implements IUserRepository, UserCallback, AuthenticationCallback {

    private final UserRemoteDataSource userRemoteDataSource;
    private final AuthenticationDataSource authenticationDataSource;
    private final MutableLiveData<Result> updateNameAndSurnameResult;
    private final MutableLiveData<Result> createPropicResult;
    private final MutableLiveData<Result> createUserResult;
    private final MutableLiveData<Result> signInResult;
    private final MutableLiveData<Result> userByUsernameResult;
    private final MutableLiveData<Result> userByEmailResult;
    private final MutableLiveData<Result> emailVerificationSendingResult;

    public UserRepository(UserRemoteDataSource userRemoteDataSource, AuthenticationDataSource authenticationDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);

        createUserResult = new MutableLiveData<>();
        userByUsernameResult = new MutableLiveData<>();
        userByEmailResult = new MutableLiveData<>();
        createPropicResult = new MutableLiveData<>();
        updateNameAndSurnameResult = new MutableLiveData<>();
        emailVerificationSendingResult = new MutableLiveData<>();
        signInResult = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> createUser(String email, String password, String username) {
        authenticationDataSource.signUp(email, password, username);
        return createUserResult;
    }

    @Override
    public MutableLiveData<Result> signIn(String email, String password){
        authenticationDataSource.signIn(email, password);
        return signInResult;
    }

    @Override
    public MutableLiveData<Result> sendEmailVerification(){
        authenticationDataSource.sendEmailVerification();
        return emailVerificationSendingResult;
    }

    @Override
    public MutableLiveData<Result> getUserByUsername(String username){
        userRemoteDataSource.getUserByUsername(username);
        return userByUsernameResult;
    }

    @Override
    public MutableLiveData<Result> getUserByEmail(String email) {
        userRemoteDataSource.getUserByEmail(email);
        return userByEmailResult;
    }

    @Override
    public MutableLiveData<Result> getCurrentUser(){
        // TODO: risolvere bug qui (qualcosa di asincrono si smerda)
        userRemoteDataSource.getUserByEmail(authenticationDataSource.getCurrentUserEmail());
        return userByEmailResult;
    }


    /*
    La creazione di un nuovo utente è andata a buon fine se:
    - l'inserimento nel sistema di autenticazione ha avuto successo
    - l'inserimento nel database ha avuto successo
     */
    @Override
    public void onSignUpSuccess(String uid, String email, String username) {
        userRemoteDataSource.storeUser(uid, email, username);
    }

    @Override
    public void onSignUpFailure(Exception exception) {
        createUserResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onStoreUserSuccess(){
        createUserResult.postValue(new Result.Success());
    }

    @Override
    public void onStoreUserFailure(Exception exception) {
        createUserResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onSignInSuccess() {
        signInResult.postValue(new Result.Success());
    }

    @Override
    public void onSignInFailure(Exception exception) {
        signInResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onEmailVerificationSendingSuccess() {
        emailVerificationSendingResult.postValue(new Result.Success());
    }

    @Override
    public void onEmailVerificationSendingFailure(Exception exception) {
        emailVerificationSendingResult.postValue(new Result.Error(exception.getMessage()));
    }


    @Override
    public void onGetUserByUsernameSuccess(User user){
        userByUsernameResult.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onGetUserByUsernameFailure(Exception exception){
        userByUsernameResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onGetUserByEmailSuccess(User user) {
        userByEmailResult.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onGetUserByEmailFailure(Exception exception) {
        userByEmailResult.postValue(new Result.Error(exception.getMessage()));
    }


    /*
    TODO: Da testare e controllare che siano implementati correttamente
     */
    @Override
    public void onCreatePropicFailure(Exception exception) {
        createPropicResult.postValue(new Result.Error(exception.getMessage()));
    }

    @Override
    public void onCreatePropicSuccess() {
        createPropicResult.postValue(null);
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
    public MutableLiveData<Result> createPropic(Uri uri) {
        userRemoteDataSource.createPropic(uri);
        return createPropicResult;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateUserNameAndSurname(name, surname);
        return updateNameAndSurnameResult;
    }
}