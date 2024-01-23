package it.unimib.enjoyn.repository.interests;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.CategoriesHolder;

public interface IInterestRepository {

    MutableLiveData<Result> createUserInterests(CategoriesHolder categoriesHolder);
}
