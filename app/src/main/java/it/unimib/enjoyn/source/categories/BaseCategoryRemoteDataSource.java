package it.unimib.enjoyn.source.categories;

public abstract class BaseCategoryRemoteDataSource {

    protected CategoryCallback categoryCallback;

    public void setCategoryCallback(CategoryCallback cb) {
        categoryCallback = cb;
    }

    public abstract void getAllCategories();

    public abstract void getImageFromName(String name);
}
