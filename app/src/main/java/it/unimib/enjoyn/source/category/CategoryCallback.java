package it.unimib.enjoyn.source.category;

import java.util.List;
import it.unimib.enjoyn.model.Category;

public interface CategoryCallback {

    void onSuccessGetAllCategories(List<Category> list);
    void onFailureGetAllCategories(String error);

}
