package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.source.Callback;

public interface BaseInterestLocalDataSource {

    void getAllInterests(Callback callback);
    void insertInterests(List<Category> categoryList, Callback callback);
    void deleteUserInterests(Callback callback);
}
