package it.unimib.enjoyn.source.interests;

import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;

public abstract class BaseInterestRemoteDataSource {

    public abstract void createUserInterests(CategoriesHolder categoriesHolder, String uid, Callback callback);
    public abstract void getUserInterests(String uid, Callback callback);
}
