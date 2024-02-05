package it.unimib.enjoyn.source.categories;

import android.net.Uri;

import java.util.List;
import it.unimib.enjoyn.model.Category;

public interface CategoryCallback {

    void onSuccessGetAllCategories(List<Category> list);
    void onFailureGetAllCategories(String error);

    void onSuccessGetImageFromName(Uri uri);
    void onFailureGetImageFromName(Exception e);
}
