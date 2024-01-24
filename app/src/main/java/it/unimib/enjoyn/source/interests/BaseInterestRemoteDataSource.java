package it.unimib.enjoyn.source.interests;

import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;

public abstract class BaseInterestRemoteDataSource {

    protected InterestsCallback interestsCallback;

    public void setInterestsCallback(InterestsCallback interestsCallback){
        this.interestsCallback = interestsCallback;
    }

    public abstract void storeUserInterests(CategoriesHolder categoriesHolder, String uid);
}
