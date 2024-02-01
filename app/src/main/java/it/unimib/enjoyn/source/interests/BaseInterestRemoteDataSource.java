package it.unimib.enjoyn.source.interests;

import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;

public interface BaseInterestRemoteDataSource {

    void createUserInterests(CategoriesHolder categoriesHolder, String uid, Callback callback);
    void getUserInterests(String uid, Callback callback);
}
