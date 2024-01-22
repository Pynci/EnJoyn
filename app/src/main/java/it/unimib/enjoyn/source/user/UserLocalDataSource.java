package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.database.UserDao;
import it.unimib.enjoyn.database.UserRoomDatabase;
import it.unimib.enjoyn.model.User;

public class UserLocalDataSource extends BaseUserLocalDataSource {

    private final UserDao userDao;

    public UserLocalDataSource(UserRoomDatabase userRoomDatabase){
        this.userDao = userRoomDatabase.userDao();
    }

    @Override
    public void getUser(String uid) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDao.getUser(uid);
            if(user != null){
                userCallback.onLocalUserFetchSuccess(user);
            }
            else{
                userCallback.onLocalDatabaseFailure(new Exception("ERRORE QUERY LOCALE"));
            }
        });
    }

    @Override
    public void insertUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = 0;
            rowId = userDao.insertUser(user);
            if(rowId != 0){
                userCallback.onLocalUserInsertionSuccess(user);
            }
            else{
                userCallback.onLocalDatabaseFailure(new Exception("ERRORE INSERIMENTO LOCALE"));
            }
        });
    }

    @Override
    public void updateUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowsUpdated = userDao.updateUser(user);
            if(rowsUpdated == 1){
                userCallback.onLocalUserUpdateSuccess(user);
            }
            else{
                userCallback.onLocalDatabaseFailure(new Exception("ERRORE AGGIORNAMENTO LOCALE"));
            }
        });
    }

    @Override
    public void deleteUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowsDeleted = userDao.deleteUser(user);
            if(rowsDeleted == 1){
                userCallback.onLocalUserDeletionSuccess();
            }
            else{
                userCallback.onLocalDatabaseFailure(new Exception("ERRORE CANCELLAZIONE LOCALE"));
            }
        });
    }
}
