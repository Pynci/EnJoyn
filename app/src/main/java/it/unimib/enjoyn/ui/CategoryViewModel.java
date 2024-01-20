package it.unimib.enjoyn.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Iterator;
import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class CategoryViewModel extends ViewModel {

    private final ICategoryRepository categoryRepository;

    public CategoryViewModel() {
        categoryRepository = ServiceLocator.getInstance().getcategoryRepository(false);
    }

    public MutableLiveData<Result> getAllCategories() {
        return categoryRepository.readAllCategories();
    }

    public MutableLiveData<Result> getAllImages(List<Category> categoryList) {

        Iterator<Category> i = categoryList.iterator();
        Result.ResultList resultList = new Result.ResultList();

        while (i.hasNext()) {
            categoryRepository.readImageFromName(i.next().getNome()).observeForever(resultList::addResult);
        }

        return new MutableLiveData<>(resultList);
    }
}
