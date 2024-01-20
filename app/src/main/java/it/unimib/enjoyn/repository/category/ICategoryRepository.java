package it.unimib.enjoyn.repository.category;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;

public interface ICategoryRepository {

    MutableLiveData<Result> readAllCategories();
    MutableLiveData<Result> readImageFromName(String name);
}
