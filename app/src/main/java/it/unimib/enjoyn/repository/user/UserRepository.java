package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.interests.BaseInterestLocalDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestRemoteDataSource;
import it.unimib.enjoyn.source.users.BaseAuthenticationDataSource;
import it.unimib.enjoyn.source.users.BaseUserLocalDataSource;
import it.unimib.enjoyn.source.users.BaseUserRemoteDataSource;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;
import it.unimib.enjoyn.util.Constants;

public class UserRepository implements IUserRepository {

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

        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
        emailSent = new MutableLiveData<>();
        currentUserPropic = new MutableLiveData<>();

        createUserInterestsResult = new MutableLiveData<>();
        userInterests = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> getCurrentUser(){
        userLocalDataSource.getUser(authenticationDataSource.getCurrentUserUID(), currentUser::postValue);
        return currentUser;
    }


    // SIGNUP

    @Override
    public MutableLiveData<Result> signUp(String email, String password, String username) {
        authenticationDataSource.signUp(email, password, username, result -> {
            if(result.isSuccessful()){
                User user = ((Result.UserSuccess) result).getData();
                createRemoteUser(user);
            }
            else{
                currentUser.postValue(result);
            }
        });

        return currentUser;
    }

    private void createRemoteUser(User user){
        userRemoteDataSource.createUser(user, result -> {
            if(result.isSuccessful()){
                createLocalUser(user);
            }
            else{
                currentUser.postValue(result);
            }
        });
    }

    private void createLocalUser(User user){
        userLocalDataSource.insertUser(user, result -> {
            if(result.isSuccessful()){
                currentUser.postValue(new Result.UserSuccess(user));
            }
            else{
                currentUser.postValue(result);
            }
        });
    }

    @Override
    public MutableLiveData<Result> signIn(String email, String password){
        authenticationDataSource.signIn(email, password, result -> {
            if(result.isSuccessful()){
                User user = ((Result.UserSuccess) result).getData();
                getRemoteUser(user.getUid());
            }
            else{
                currentUser.postValue(result);
            }
        });
        return currentUser;
    }

    private void getRemoteUser(String uid){
        userRemoteDataSource.getCurrentUser(uid, result -> {
            if(result.isSuccessful()){
                User user = ((Result.UserSuccess) result).getData();
                createLocalUser(user);
            }
            else{
                currentUser.postValue(result);
            }
        });
    }

    @Override
    public MutableLiveData<Result> refreshSession(){
        authenticationDataSource.refreshSession(result -> {
            if(result.isSuccessful()){
                getCurrentUser();
            }
            else{
                String errorMessage = ((Result.Error) result).getMessage();
                if(errorMessage.equals(Constants.USER_NOT_LOGGED_ERROR)){
                    currentUser.postValue(new Result.UserSuccess(null));
                }
                else{
                    currentUser.postValue(result);
                }
            }
        });
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> signOut(){
        authenticationDataSource.signOut(result -> {
            if(result.isSuccessful()){
                User user = ((Result.UserSuccess) currentUser.getValue()).getData();
                deleteLocalUser(user);
            }
            else{
                currentUser.postValue(result);
            }
        });
        return currentUser;
    }

    private void deleteLocalUser(User user){
        userLocalDataSource.deleteUser(user, result -> {
            if(result.isSuccessful()){
                deleteLocalInterests();
            }
            else{
             currentUser.postValue(result);
            }
        });
    }

    @Override
    public MutableLiveData<Result> getUserByUsername(String username){
        userRemoteDataSource.getUserByUsername(username, userByUsername::postValue);
        return userByUsername;
    }

    @Override
    public MutableLiveData<Result> getUserByEmail(String email) {
        userRemoteDataSource.getUserByEmail(email, userByEmail::postValue);
        return userByEmail;
    }

    @Override
    public MutableLiveData<Result> updatePropic(Uri uri) {
        userRemoteDataSource.updatePropic(authenticationDataSource.getCurrentUserUID(), uri,
                result -> {
                    currentUser.postValue(currentUser.getValue());  //notifica
                });
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> updateNameAndSurname(String name, String surname) {
        userRemoteDataSource.updateNameAndSurname(authenticationDataSource.getCurrentUserUID(), name, surname,
                result -> {
                    if(result.isSuccessful()){
                        User user = ((Result.UserSuccess) currentUser.getValue()).getData();
                        user.setName(name);
                        user.setSurname(surname);
                        updateLocalUser(user);
                    }
                    else{
                        currentUser.postValue(result);
                    }
                });
        return currentUser;
    }


    @Override
    public MutableLiveData<Result> updateDescription(String description) {
        userRemoteDataSource.updateDescription(authenticationDataSource.getCurrentUserUID(), description,
                result -> {
                    if(result.isSuccessful()){
                        User user = ((Result.UserSuccess) currentUser.getValue()).getData();
                        user.setDescription(description);
                        updateLocalUser(user);
                    }
                    else{
                        currentUser.postValue(result);
                    }

                });
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> refreshEmailVerificationStatus(){
        authenticationDataSource.checkEmailVerification(refreshResult -> {
            if(refreshResult.isSuccessful()){
                User user = ((Result.UserSuccess) currentUser.getValue()).getData();
                user.setEmailVerified(((Result.BooleanSuccess) refreshResult).getData());
                userRemoteDataSource.updateEmailVerificationStatus(authenticationDataSource.getCurrentUserUID(), user.getEmailVerified(),
                        result -> {
                            if(result.isSuccessful()){
                                updateLocalUser(user);
                            }
                            else{
                                currentUser.postValue(result);
                            }
                        });
            }
            else{
                currentUser.postValue(refreshResult);
            }
        });
        return currentUser;
    }



    @Override
    public MutableLiveData<Result> updateProfileConfigurationStatus(){
        userRemoteDataSource.updateProfileConfigurationStatus(authenticationDataSource.getCurrentUserUID(), true,
                result -> {
                    if(result.isSuccessful()){
                        User user = ((Result.UserSuccess) currentUser.getValue()).getData();
                        user.setProfileConfigured(true);
                        updateLocalUser(user);
                    }
                    else{
                        currentUser.postValue(result);
                    }
                });
        return currentUser;
    }

    @Override
    public MutableLiveData<Result> updateCategoriesSelectionStatus(){
        userRemoteDataSource.updateCategoriesSelectionStatus(authenticationDataSource.getCurrentUserUID(), true,
                result -> {
                    if(result.isSuccessful()){
                        User user = ((Result.UserSuccess) currentUser.getValue()).getData();
                        user.setCategoriesSelectionDone(true);
                        updateLocalUser(user);
                    }
                    else{
                        currentUser.postValue(result);
                    }
                });
        return currentUser;
    }

    private void updateLocalUser(User user){
        userLocalDataSource.updateUser(user, result -> {
            if(result.isSuccessful()){
                currentUser.postValue(new Result.UserSuccess(user));
            }
            else{
                currentUser.postValue(result);
            }
        });
    }

    @Override
    public MutableLiveData<Result> sendEmailVerification(){
        authenticationDataSource.sendEmailVerification(emailSent::postValue);
        return emailSent;
    }

    @Override
    public MutableLiveData<Result> sendResetPasswordEmail(String email) {
        authenticationDataSource.sendResetPasswordEmail(email, emailSent::postValue);
        return emailSent;
    }

    @Override
    public MutableLiveData<Result> getCurrentUserPropic() {
        userRemoteDataSource.getPropicByUid(authenticationDataSource.getCurrentUserUID(), currentUserPropic::postValue);
        return currentUserPropic;
    }


    //CRUD INTERESTS
    @Override
    public MutableLiveData<Result> createUserInterests(CategoriesHolder categoriesHolder) {
        interestRemoteDataSource.createUserInterests(categoriesHolder, authenticationDataSource.getCurrentUserUID(),
                result -> {
                    if(result.isSuccessful()){
                        createLocalInterests(categoriesHolder.getCategories());
                    }
                    else{
                        createUserInterestsResult.postValue(result);
                    }
                });
        return createUserInterestsResult;
    }

    private void createLocalInterests(List<Category> categoryList){
        interestLocalDataSource.insertInterests(categoryList, result -> {
            if(result.isSuccessful()){
                createUserInterestsResult.postValue(new Result.Success());
                CategoriesHolder.getInstance().refresh();
                getUserInterests();
            }
            else{
                createUserInterestsResult.postValue(result);
            }
        });
    }

    @Override
    public MutableLiveData<Result> getUserInterests() {
        interestLocalDataSource.getAllInterests(result -> {
            if(result.isSuccessful()){
                userInterests.postValue(result);
            }
            else{
                getRemoteInterests(authenticationDataSource.getCurrentUserUID());
            }
        });
        return userInterests;
    }

    private void getRemoteInterests(String uid){
        interestRemoteDataSource.getUserInterests(uid, result -> {
            if(result.isSuccessful()){
                List<Category> categoryList = ((Result.CategorySuccess) result).getCategoryList();
                createLocalInterests(categoryList);
            }
            else{
                userInterests.postValue(result);
            }
        });
    }

    private void deleteLocalInterests(){
        interestLocalDataSource.deleteUserInterests(result -> {
            if(result.isSuccessful()){
                currentUser.postValue(new Result.UserSuccess(null));
            }
            else{
                currentUser.postValue(result);
            }
        });
    }

}