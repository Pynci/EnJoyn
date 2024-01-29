package it.unimib.enjoyn.ui.viewmodels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unimib.enjoyn.model.Category;

public class CategoriesHolder {

    private List<Category> categoriesSelected;
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
            if(iterator.next().getNome().equals(nomeCategoria)) {
                iterator.remove();
                isremoved = true;
            }
        }
    }

    public List<Category> getCategories() {
        return categoriesSelected;
    }

    public void refresh() {
        categoriesSelected.clear();
    }
}
