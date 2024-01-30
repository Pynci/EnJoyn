package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.model.Category;

public abstract class BaseInterestLocalDataSource {

    InterestsCallback interestsCallback;

    public void setInterestsCallback(InterestsCallback interestsCallback){
        this.interestsCallback = interestsCallback;
    }

    public abstract void getAllInterests();
    public abstract void storeInterests(List<Category> categoryList);
    public abstract void deleteUserInterests();
}
