package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.database.UserDao;
import it.unimib.enjoyn.database.UserRoomDatabase;
import it.unimib.enjoyn.model.User;

public class UserLocalDataSource extends BaseUserLocalDataSource {

    private final UserDao userDao;
    private String currentUserUID;

    public UserLocalDataSource(UserRoomDatabase userRoomDatabase){
        this.userDao = userRoomDatabase.userDao();
    }


    public void setCurrentUser(){

    }

    @Override
    public void getCurrentUser() {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            User user = userDao.getUser(currentUserUID);
            // da fare callback
        });
    }

    @Override
    public void insertCurrentUser(User user) {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUser(user);
            // da fare callback
        });
    }

    @Override
    public void updateCurrentUser(User user) {

    }

    @Override
    public void clearCurrentUser() {
        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
            //userDao.deleteUser(User user)
        });
    }
}
