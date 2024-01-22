package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.model.Category;

public abstract class BaseInterestLocalDataSource {

    InterestsCallback interestsCallback;

    public abstract void getAllCategories();
    public abstract void setAllCategories(List<Category> categoryList);
}
