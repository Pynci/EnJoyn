package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.database.InterestDao;
import it.unimib.enjoyn.model.Category;

public class InterestLocalDataSource extends BaseInterestLocalDataSource{

    private final InterestDao interestDao;

    public InterestLocalDataSource(LocalRoomDatabase localRoomDatabase) {

        interestDao = localRoomDatabase.categoryDao();
    }

    @Override
    public void getAllInterests() {

        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Category> categoryList = interestDao.getAll();

            if(categoryList.size() == 0) {
                interestsCallback.onFailureGetInterestsFromLocal(
                        new Exception("Errore nel recupero dei dati richiesti dal room"));
            }
            else{
                interestsCallback.onSuccessGetInterestsFromLocal(categoryList);
            }
        });
    }

    @Override
    public void storeInterests(List<Category> categoryList) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            interestDao.deleteInterests();
            long[] rowIds = interestDao.insertAll(categoryList);
            if(rowIds.length == 0) {
                interestsCallback.onFailureSaveOnLocal(
                        new Exception("Errore nel salvataggio dei dati"));
            }
            else{
                interestsCallback.onSuccessSaveOnLocal();
            }
        });
    }

    @Override
    public void deleteUserInterests() {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowDeleted = interestDao.deleteInterests();
            if(rowDeleted > 0) {
                interestsCallback.onSuccessDeleteAllInterestsFromLocal();
            }
            else {
                interestsCallback.onFailureDeleteAllInteretsFromLocal(
                        new Exception("Errore nella cancellazione dei dati dal DB locale"));
            }
        });
    }
}
