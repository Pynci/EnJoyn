package it.unimib.enjoyn.repository.category;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.category.BaseCategoryRemoteDataSource;
import it.unimib.enjoyn.source.category.CategoryCallback;
import it.unimib.enjoyn.source.category.CategoryRemoteDataSource;

public class CategoryRepository implements ICategoryRepository, CategoryCallback {

    private final BaseCategoryRemoteDataSource categoryRemoteDataSource;
    private final MutableLiveData<Result> allCategoriesResult;

    public CategoryRepository(CategoryRemoteDataSource categoryRemoteDataSource) {

        this.categoryRemoteDataSource = categoryRemoteDataSource;
        categoryRemoteDataSource.setCategoryCallback(this);

        allCategoriesResult = new MutableLiveData<>();
    }

    //Metodi CRUD
    @Override
    public MutableLiveData<Result> readAllCategories() {
        categoryRemoteDataSource.getAllCategories();
        return allCategoriesResult;
    }

    //Callback
    @Override
    public void onSuccessGetAllCategories(List<Category> list) {
        allCategoriesResult.postValue(new Result.CategoryResponseSuccess(list));
    }

    @Override
    public void onFailureGetAllCategories(String error) {
        allCategoriesResult.postValue(new Result.Error(error));
    }
}
