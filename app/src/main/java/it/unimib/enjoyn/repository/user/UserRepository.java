package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.interests.BaseInterestLocalDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestRemoteDataSource;
import it.unimib.enjoyn.source.interests.InterestsCallback;
import it.unimib.enjoyn.source.user.AuthenticationCallback;
import it.unimib.enjoyn.source.user.BaseAuthenticationDataSource;
import it.unimib.enjoyn.source.user.BaseUserLocalDataSource;
import it.unimib.enjoyn.source.user.BaseUserRemoteDataSource;
import it.unimib.enjoyn.source.user.UserCallback;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;

public class UserRepository implements IUserRepository, UserCallback, AuthenticationCallback, InterestsCallback {

    private final BaseUserLocalDataSource userLocalDataSource;
    private final BaseUserRemoteDataSource userRemoteDataSource;
    private final BaseAuthenticationDataSource authenticationDataSource;
    private final BaseInterestRemoteDataSource interestRemoteDataSource;
    private final BaseInterestLocalDataSource interestLocalDataSource;

    private final MutableLiveData<Result> userByUsername;
    private final MutableLiveData<Result> userByEmail;
    private final MutableLiveData<Result> currentUser;
    private final MutableLiveData<Result> emailSent;
    private final MutableLiveData<Result> currentUserPropic;

    private final MutableLiveData<Result> createUserInterestsResult;
    private final MutableLiveData<Result> userInterests;
    private final MutableLiveData<Result> deleteInterestsResult;

    public UserRepository(BaseUserLocalDataSource userLocalDataSource,
                          BaseUserRemoteDataSource userRemoteDataSource,
                          BaseAuthenticationDataSource authenticationDataSource,
                          BaseInterestRemoteDataSource interestRemoteDataSource,
                          BaseInterestLocalDataSource interestLocalDataSource){

        this.userLocalDataSource = userLocalDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        this.interestLocalDataSource = interestLocalDataSource;
        this.interestRemoteDataSource = interestRemoteDataSource;

        userLocalDataSource.setUserCallback(this);
        userRemoteDataSource.setUserCallback(this);
        authenticationDataSource.setAuthenticationCallback(this);
        interestRemoteDataSource.setInterestsCallback(this);
        interestLocalDataSource.setInterestsCallback(this);

        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
        emailSent = new MutableLiveData<>();
        currentUserPropic = new MutableLiveData<>();

        createUserInterestsResult = new MutableLiveData<>();
        userInterests = new MutableLiveData<>();
        deleteInterestsResult = new MutableLiveData<>();
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

    @Override
    public MutableLiveData<Result> updateCategoriesSelectionStatus(){
        userRemoteDataSource.updateCategoriesSelectionStatus(authenticationDataSource.getCurrentUserUID(), true);
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> getCurrentUserPropic() {
        userRemoteDataSource.getImageByUserId(authenticationDataSource.getCurrentUserUID());
        return currentUserPropic;
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
        interestLocalDataSource.deleteUserInterests();
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
    public void onGetUserByUsernameFailure(Exception exception) {
        userByUsername.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onGetUserByEmailFailure(Exception exception) {
        userByEmail.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onGetCurrentUserPropicSuccess(Uri uri) {
        currentUserPropic.postValue(new Result.SingleImageReadFromRemote(uri));
    }

    @Override
    public void onGetCurrentUserPropicFailure(Exception e) {
        currentUserPropic.postValue(new Result.Error(e.getMessage()));
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
        userRemoteDataSource.getCurrentUser(user.getUid());
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
    public void onNotLoggedYet() {
        currentUser.postValue(new Result.UserSuccess(null));
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

    //CRUD
    @Override
    public MutableLiveData<Result> createUserInterests(CategoriesHolder categoriesHolder) {
        interestRemoteDataSource.storeUserInterests(categoriesHolder, authenticationDataSource.getCurrentUserUID());
        interestLocalDataSource.storeInterests(categoriesHolder.getCategories());
        return createUserInterestsResult;
    }

    @Override
    public MutableLiveData<Result> getUserInterests() {
        interestLocalDataSource.getAllCategories();
        return userInterests;
    }

    //CALLBACK
    @Override
    public void onSuccessCreateUsersInterest() {
        createUserInterestsResult.postValue(new Result.Success());
    }

    @Override
    public void onFailureCreateUsersInterest(Exception e) {
        createUserInterestsResult.postValue(new Result.Error(e.getMessage()));
    }

    @Override
    public void onSuccessGetInterestsFromLocal(List<Category> list) {
        userInterests.postValue(new Result.CategorySuccess(list));
    }

    @Override
    public void onFailureGetInterestsFromLocal(Exception e) {
        interestRemoteDataSource.getUserInterests(authenticationDataSource.getCurrentUserUID());
    }

    @Override
    public void onSuccessSaveOnLocal() {
        //Al momento non serve fare nulla
    }

    @Override
    public void onFailureSaveOnLocal(Exception e) {
        //Al momento non serve fare nulla
    }

    @Override
    public void onSuccessGetInterestsFromRemote(List<Category> categoryList) {
        interestLocalDataSource.storeInterests(categoryList);
        userInterests.postValue(new Result.CategorySuccess(categoryList));
    }

    @Override
    public void onFailureGetInterestsFromRemote(String message) {
        userInterests.postValue(new Result.Error(message));
    }

    @Override
    public void onSuccessDeleteAllInterestsFromLocal() {
        currentUser.postValue(new Result.UserSuccess(null));
    }

    @Override
    public void onFailureDeleteAllInteretsFromLocal(Exception e) {
        currentUser.postValue(new Result.Error(e.getMessage()));
    }

}