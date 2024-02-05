package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.database.UserDao;
import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.util.Constants;

public class UserLocalDataSource implements BaseUserLocalDataSource {

    private final UserDao userDao;

    public UserLocalDataSource(LocalRoomDatabase localRoomDatabase){
        this.userDao = localRoomDatabase.userDao();
    }

    @Override
    public void getUser(String uid, Callback callback) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDao.getUser(uid);
            if(user != null){
                callback.onComplete(new Result.UserSuccess(user));
            }
            else{
                callback.onComplete(new Result.Error(Constants.USER_LOCAL_FETCH_ERROR));
            }
        });
    }

    @Override
    public void insertUser(User user, Callback callback) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = userDao.insertUser(user);
            if(rowId != 0){
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(Constants.USER_LOCAL_CREATION_ERROR));
            }
        });
    }

    @Override
    public void updateUser(User user, Callback callback) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowsUpdated = userDao.updateUser(user);
            if(rowsUpdated == 1){
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(Constants.USER_LOCAL_UPDATE_ERROR));
            }
        });
    }

    @Override
    public void deleteUser(User user, Callback callback) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowsDeleted = userDao.deleteUser(user);
            if(rowsDeleted == 1){
                callback.onComplete(new Result.Success());
            }
            else{
                callback.onComplete(new Result.Error(Constants.USER_LOCAL_DELETION_ERROR));
            }
        });
    }
}
