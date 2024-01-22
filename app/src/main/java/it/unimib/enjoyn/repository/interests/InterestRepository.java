package it.unimib.enjoyn.repository.interests;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.source.interests.BaseInterestLocalDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestRemoteDataSource;
import it.unimib.enjoyn.source.interests.InterestsCallback;
import it.unimib.enjoyn.ui.CategoriesHolder;
import it.unimib.enjoyn.util.ServiceLocator;

public class InterestRepository implements IInterestRepository, InterestsCallback {

    private final IUserRepository userRepository;
    private final BaseInterestRemoteDataSource baseInterestDataSource;
    private final BaseInterestLocalDataSource baseInterestLocalDataSource;
    private final MutableLiveData<Result> createUserInterestsResult;

    public InterestRepository(BaseInterestRemoteDataSource baseInterestDataSource,
                              BaseInterestLocalDataSource baseInterestLocalDataSource) {
        userRepository = ServiceLocator.getInstance().getUserRepository();
        this.baseInterestDataSource = baseInterestDataSource;
        this.baseInterestLocalDataSource = baseInterestLocalDataSource;
        createUserInterestsResult = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> createUserInterests(CategoriesHolder categoriesHolder) {
        baseInterestDataSource.storeUserInterests(categoriesHolder, userRepository.getCurrentUser());
        return createUserInterestsResult;
    }

    @Override
    public void onSuccessCreateUsers() {
        createUserInterestsResult.postValue(new Result.Success());
    }

    @Override
    public void onFailureCreateUsers(Exception e) {
        createUserInterestsResult.postValue(new Result.Error(e.getMessage()));
    }

    @Override
    public void onSuccessGetInterestsFromLocal(List<Category> list) {

    }

    @Override
    public void onFailureGetInterestsFromLocal(Exception e) {

    }

    @Override
    public void onSuccessSaveOnLocal() {

    }

    @Override
    public void onFailureSaveOnLocal(Exception e) {

    }
}
