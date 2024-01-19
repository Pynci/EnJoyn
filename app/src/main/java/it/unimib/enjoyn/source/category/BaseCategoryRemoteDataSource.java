package it.unimib.enjoyn.source.category;

public abstract class BaseCategoryRemoteDataSource {

    protected CategoryCallback categoryCallback;

    public void setCategoryCallback(CategoryCallback cb) {
        categoryCallback = cb;
    }

    public abstract void getAllCategories();
}
