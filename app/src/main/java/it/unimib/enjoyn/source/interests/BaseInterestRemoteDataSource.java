package it.unimib.enjoyn.source.interests;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.ui.CategoriesHolder;

public abstract class BaseInterestRemoteDataSource {

    protected InterestsCallback interestsCallback;

    public abstract void storeUserInterests(CategoriesHolder categoriesHolder, User user);
}
