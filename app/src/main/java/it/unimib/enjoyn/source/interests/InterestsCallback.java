package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.model.Category;

public interface InterestsCallback {

    void onSuccessCreateUsers();
    void onFailureCreateUsers(Exception e);

    void onSuccessGetInterestsFromLocal(List<Category> list);
    void onFailureGetInterestsFromLocal(Exception e);

    void onSuccessSaveOnLocal();
    void onFailureSaveOnLocal(Exception e);
}
