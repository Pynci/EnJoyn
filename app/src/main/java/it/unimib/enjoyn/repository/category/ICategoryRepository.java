package it.unimib.enjoyn.repository.category;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;

public interface ICategoryRepository {

    MutableLiveData<Result> readAllCategories();
    MutableLiveData<Result> readAllImagesFromCategories(List<Category> categoryList);
}
