package it.unimib.enjoyn.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.repository.interests.IInterestRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class CategoryViewModel extends ViewModel {

    private final ICategoryRepository categoryRepository;
    private final IInterestRepository interestRepository;
    private final CategoriesHolder categoriesHolder;

    public CategoryViewModel() {
        categoryRepository = ServiceLocator.getInstance().getCategoryRepository();
        interestRepository = ServiceLocator.getInstance().getInterestRepository();
        categoriesHolder = CategoriesHolder.getInstance();
    }

    public MutableLiveData<Result> getAllCategories() {
        return categoryRepository.readAllCategories();
    }

    public MutableLiveData<Result> getAllImages(List<Category> categoryList) {
        return categoryRepository.readAllImagesFromCategories(categoryList);
    }

    public MutableLiveData<Result> setUserInterests() {
        return interestRepository.createUserInterests(categoriesHolder);
    }
}
