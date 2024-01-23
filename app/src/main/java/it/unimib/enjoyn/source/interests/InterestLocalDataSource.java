package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.database.CategoryDao;
import it.unimib.enjoyn.model.Category;

public class InterestLocalDataSource extends BaseInterestLocalDataSource{

    private final CategoryDao categoryDao;

    public InterestLocalDataSource(LocalRoomDatabase localRoomDatabase) {

        categoryDao = localRoomDatabase.categoryDao();
    }

    @Override
    public void getAllCategories() {

        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Category> categoryList = categoryDao.getAll();

            if(categoryList == null) {
                interestsCallback.onFailureGetInterestsFromLocal(
                        new Exception("Errore nel recupero dei dati richiesti dal room"));
            }
            else{
                interestsCallback.onSuccessGetInterestsFromLocal(categoryList);
            }
        });
    }

    @Override
    public void setAllCategories(List<Category> categoryList) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {

            long[] rowIds = categoryDao.insertAll(categoryList);
            if(rowIds.length == 0) {
                interestsCallback.onFailureSaveOnLocal(
                        new Exception("Errore nel salvataggio dei dati"));
            }
            else{
                interestsCallback.onSuccessSaveOnLocal();
            }
        });
    }
}
