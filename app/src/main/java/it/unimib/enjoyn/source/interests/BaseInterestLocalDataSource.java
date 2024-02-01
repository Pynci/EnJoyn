package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.source.Callback;

public abstract class BaseInterestLocalDataSource {

    public abstract void getAllInterests(Callback callback);
    public abstract void insertInterests(List<Category> categoryList, Callback callback);
    public abstract void deleteUserInterests(Callback callback);
}
