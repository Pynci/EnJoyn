package it.unimib.enjoyn.source.users;

import it.unimib.enjoyn.database.UserDao;
import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.model.User;

public class UserLocalDataSource extends BaseUserLocalDataSource {

    private final UserDao userDao;

    public UserLocalDataSource(LocalRoomDatabase localRoomDatabase){
        this.userDao = localRoomDatabase.userDao();
    }

    @Override
    public void getUser(String uid) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
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
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            long rowId = userDao.insertUser(user);
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
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
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
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
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
