package it.unimib.enjoyn.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.interests.IInterestRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class InterestsViewModel extends ViewModel {

    private final IInterestRepository interestRepository;
    private final CategoriesHolder categoriesHolder;

    public InterestsViewModel(Application application) {

        interestRepository = ServiceLocator.getInstance().getInterestRepository(application);
        categoriesHolder = CategoriesHolder.getInstance();
    }

    public MutableLiveData<Result> setUserInterests() {
        return interestRepository.createUserInterests(categoriesHolder);
    }

    public MutableLiveData<Result> getInterests() {
        return interestRepository.getUserInterests();
    }
}
