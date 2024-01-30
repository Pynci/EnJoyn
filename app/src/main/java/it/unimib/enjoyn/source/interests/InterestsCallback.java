package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;

public interface InterestsCallback {

    void onSuccessCreateUsersInterest(CategoriesHolder categoriesHolder);
    void onFailureCreateUsersInterest(Exception e);

    void onSuccessGetInterestsFromLocal(List<Category> list);
    void onFailureGetInterestsFromLocal(Exception e);

    void onSuccessSaveOnLocal();
    void onFailureSaveOnLocal(Exception e);

    void onSuccessGetInterestsFromRemote(List<Category> categoryList);
    void onFailureGetInterestsFromRemote(String message);

    void onSuccessDeleteAllInterestsFromLocal();
    void onFailureDeleteAllInteretsFromLocal(Exception e);
}
