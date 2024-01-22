package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.database.category.CategoryDao;
import it.unimib.enjoyn.model.Category;

public class InterestLocalDataSource extends BaseInterestLocalDataSource{

    private final CategoryDao categoryDao;

    public InterestLocalDataSource(EventsRoomDatabase eventsRoomDatabase) {

        categoryDao = eventsRoomDatabase.categoryDao();
    }

    @Override
    public void getAllCategories() {

        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
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
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {

            int numberOfRowAdded = categoryDao.insertAll(categoryList);
            if(numberOfRowAdded == 0) {
                interestsCallback.onFailureSaveOnLocal(
                        new Exception("Errore nel salvataggio dei dati"));
            }
            else{
                interestsCallback.onSuccessSaveOnLocal();
            }
        });
    }
}
