package it.unimib.enjoyn.repository.category;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.category.BaseCategoryRemoteDataSource;
import it.unimib.enjoyn.source.category.CategoryCallback;
import it.unimib.enjoyn.source.category.CategoryRemoteDataSource;

public class CategoryRepository implements ICategoryRepository, CategoryCallback {

    private final BaseCategoryRemoteDataSource categoryRemoteDataSource;
    private final MutableLiveData<Result> allCategoriesResult;
    private final MutableLiveData<Result> readImageFromNameResult;
    private final List<Uri> imagesUri;
    private int expectedImagesUriSize;

    public CategoryRepository(CategoryRemoteDataSource categoryRemoteDataSource) {

        this.categoryRemoteDataSource = categoryRemoteDataSource;
        categoryRemoteDataSource.setCategoryCallback(this);
        imagesUri = new ArrayList<>();

        allCategoriesResult = new MutableLiveData<>();
        readImageFromNameResult = new MutableLiveData<>();
    }

    //Metodi CRUD
    @Override
    public MutableLiveData<Result> readAllCategories() {
        categoryRemoteDataSource.getAllCategories();
        return allCategoriesResult;
    }

    @Override
    public MutableLiveData<Result> readAllImagesFromCategories(List<Category> categoryList) {
        expectedImagesUriSize = categoryList.size();
        for (Category category : categoryList) {
            readImageFromName(category.getNome());
        }

        return readImageFromNameResult;
    }

    public void readImageFromName(String name) {
        categoryRemoteDataSource.getImageFromName(name);
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

    @Override
    public void onSuccessGetImageFromName(Uri uri) {
        imagesUri.add(uri);

        if(imagesUri.size() == expectedImagesUriSize) {
            readImageFromNameResult.postValue(new Result.ImagesReadFromRemote(imagesUri));
        }
    }

    @Override
    public void onFailureGetImageFromName(Exception e) {
        readImageFromNameResult.postValue(new Result.Error(e.getMessage()));
    }
}
