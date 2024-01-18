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

    private final MutableLiveData<Result> userByUsernameResult;
    private final MutableLiveData<Result> userByEmailResult;

    private final MutableLiveData<Result> resultFromRemote;
    private final MutableLiveData<Result> resultFromAuth;

    public UserRepository(UserRemoteDataSource userRemoteDataSource, AuthenticationDataSource authenticationDataSource){
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);

        userByUsernameResult = new MutableLiveData<>();
        userByEmailResult = new MutableLiveData<>();

        resultFromRemote = new MutableLiveData<>();
        resultFromAuth = new MutableLiveData<>();
    }


    /*
    Operazioni di manipolazione dei dati (create, get, read, update)
     */
    @Override
    public MutableLiveData<Result> createUser(String email, String password, String username) {
        authenticationDataSource.signUp(email, password, username);
        return resultFromRemote;
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
    public MutableLiveData<Result> createPropic(Uri uri) {
        userRemoteDataSource.createPropic(authenticationDataSource.getCurrentUserUID(), uri);
        return resultFromRemote;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateNameAndSurname(authenticationDataSource.getCurrentUserUID(), name, surname);
        return resultFromRemote;
    }

    @Override
    public MutableLiveData<Result> updateDescription(String description) {
        userRemoteDataSource.updateDescription(authenticationDataSource.getCurrentUserUID(), description);
        return resultFromRemote;
    }

    /*
    Operazioni di manipolazione dell'utente di firebase
     */

    @Override
    public MutableLiveData<Result> signIn(String email, String password){
        authenticationDataSource.signIn(email, password);
        return resultFromAuth;
    }

    @Override
    public MutableLiveData<Result> sendEmailVerification(){
        authenticationDataSource.sendEmailVerification();
        return resultFromAuth;
    }

    @Override
    public MutableLiveData<Result> getCurrentUser(){
        userRemoteDataSource.getUserByEmail(authenticationDataSource.getCurrentUserEmail());
        return userByEmailResult;
    }

    /*
    Callback
     */

    @Override
    public void onSuccessFromRemote() {
        resultFromRemote.postValue(new Result.Success());
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        resultFromRemote.postValue(new Result.Error(exception.getMessage()));
    }


    @Override
    public void onAuthOperationSuccess() {
        resultFromAuth.postValue(new Result.Success());
    }

    @Override
    public void onAuthOperationFailure(Exception exception) {
        resultFromAuth.postValue(new Result.Error(exception.getMessage()));
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

    /*
    TODO: smazzarsi e compattare le callback sottostanti (se possibile)
    Le tue annotazioni + le callback
     */

    @Override
    public void onGetUserByUsernameSuccess(User user){
        userByUsernameResult.postValue(new Result.UserResponseSuccess(user));
    }

    @Override
    public void onGetUserByEmailSuccess(User user) {
        userByEmailResult.postValue(new Result.UserResponseSuccess(user));
    }

}