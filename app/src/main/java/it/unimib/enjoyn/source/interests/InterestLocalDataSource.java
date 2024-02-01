package it.unimib.enjoyn.source.interests;

import java.util.List;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.database.InterestDao;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.util.Constants;

public class InterestLocalDataSource extends BaseInterestLocalDataSource{

    private final InterestDao interestDao;

    public InterestLocalDataSource(LocalRoomDatabase localRoomDatabase) {

        interestDao = localRoomDatabase.interestDao();
    }

    @Override
    public void getAllInterests(Callback callback) {

        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Category> categoryList = interestDao.getAll();

            if(categoryList.size() == 0) {
                callback.onComplete(new Result.Error(Constants.INTEREST_LOCAL_FETCH_ERROR));
            }
            else{
                callback.onComplete(new Result.CategorySuccess(categoryList));
            }
        });
    }

    @Override
    public void insertInterests(List<Category> categoryList, Callback callback) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            interestDao.deleteInterests();
            long[] rowIds = interestDao.insertAll(categoryList);
            if(rowIds.length == 0) {
                callback.onComplete(new Result.Error(Constants.INTEREST_LOCAL_CREATION_ERROR));
            }
            else{
                callback.onComplete(new Result.Success());
            }
        });
    }

    @Override
    public void deleteUserInterests(Callback callback) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowDeleted = interestDao.deleteInterests();
            if(rowDeleted > 0) {
                callback.onComplete(new Result.Success());
            }
            else {
                callback.onComplete(new Result.Error(Constants.INTEREST_LOCAL_DELETION_ERROR));
            }
        });
    }
}
