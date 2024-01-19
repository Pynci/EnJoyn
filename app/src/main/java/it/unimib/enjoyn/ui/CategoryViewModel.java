package it.unimib.enjoyn.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class CategoryViewModel extends ViewModel {

    private final ICategoryRepository categoryRepository;

    public CategoryViewModel() {
        categoryRepository = ServiceLocator.getInstance().getcategoryRepository(false);
    }

    public MutableLiveData<Result> getAllNews() {
        return categoryRepository.readAllCategories();
    }
}
