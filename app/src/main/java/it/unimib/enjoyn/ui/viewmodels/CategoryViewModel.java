package it.unimib.enjoyn.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class CategoryViewModel extends ViewModel {

    private final ICategoryRepository categoryRepository;

    public CategoryViewModel() {
        categoryRepository = ServiceLocator.getInstance().getCategoryRepository();
    }

    public MutableLiveData<Result> getAllCategories() {
        return categoryRepository.readAllCategories();
    }

    public MutableLiveData<Result> getAllImages(List<Category> categoryList) {
        return categoryRepository.readAllImagesFromCategories(categoryList);
    }
}
