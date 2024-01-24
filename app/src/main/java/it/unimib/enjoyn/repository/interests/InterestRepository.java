package it.unimib.enjoyn.repository.interests;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.source.interests.BaseInterestLocalDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestRemoteDataSource;
import it.unimib.enjoyn.source.interests.InterestsCallback;
import it.unimib.enjoyn.source.user.BaseAuthenticationDataSource;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;
import it.unimib.enjoyn.util.ServiceLocator;

public class InterestRepository implements IInterestRepository, InterestsCallback {

    private final IUserRepository userRepository;
    private final BaseAuthenticationDataSource authenticationDataSource;
    private final BaseInterestRemoteDataSource interestRemoteDataSource;
    private final BaseInterestLocalDataSource interestLocalDataSource;
    private final MutableLiveData<Result> createUserInterestsResult;
    private final MutableLiveData<Result> getCreateUserInterestsResult;

    public InterestRepository(Application application, BaseInterestRemoteDataSource interestRemoteDataSource,
                              BaseInterestLocalDataSource interestLocalDataSource,
                              BaseAuthenticationDataSource authenticationDataSource) {
        userRepository = ServiceLocator.getInstance().getUserRepository(application);
        this.authenticationDataSource = authenticationDataSource;
        this.interestRemoteDataSource = interestRemoteDataSource;
        this.interestLocalDataSource = interestLocalDataSource;

        interestRemoteDataSource.setInterestsCallback(this);
        interestLocalDataSource.setInterestsCallback(this);
        createUserInterestsResult = new MutableLiveData<>();
        getCreateUserInterestsResult = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> createUserInterests(CategoriesHolder categoriesHolder) {
        interestRemoteDataSource.storeUserInterests(categoriesHolder, authenticationDataSource.getCurrentUserUID());
        interestLocalDataSource.storeInterests(categoriesHolder.getCategories());
        return createUserInterestsResult;
    }

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
        getCreateUserInterestsResult.postValue(new Result.CategorySuccess(list));
    }

    @Override
    public void onFailureGetInterestsFromLocal(Exception e) {
        getCreateUserInterestsResult.postValue(new Result.Error(e.getMessage()));
    }

    @Override
    public void onSuccessSaveOnLocal() {
        //Al momento non serve fare nulla
    }

    @Override
    public void onFailureSaveOnLocal(Exception e) {
        //Al momento non serve fare nulla
    }
}
