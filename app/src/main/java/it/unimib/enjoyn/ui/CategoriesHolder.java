package it.unimib.enjoyn.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unimib.enjoyn.model.Category;

public class CategoriesHolder {

    List<Category> categoriesSelected;
    private static CategoriesHolder instance;

    private CategoriesHolder() {
        categoriesSelected = new ArrayList<>();
    }

    public static CategoriesHolder getInstance() {

        if(instance == null) {
            instance = new CategoriesHolder();
        }
        return instance;
    }

    public void addCategory(Category category) {
        categoriesSelected.add(category);
    }

    public void removeCategory(String nomeCategoria) {

        Iterator<Category> iterator = categoriesSelected.iterator();
        boolean isremoved = false;

        while (iterator.hasNext() && !isremoved) {
            if(iterator.next().getTipo().equals(nomeCategoria)) {
                iterator.remove();
                isremoved = true;
            }
        }
    }

    public List<Category> getCategories() {
        return categoriesSelected;
    }
}
