package it.unimib.enjoyn.source.user;

import it.unimib.enjoyn.database.UserDao;
import it.unimib.enjoyn.database.UserRoomDatabase;

public class UserLocalDataSource extends BaseUserLocalDataSource {

    private final UserDao userDao;

    public UserLocalDataSource(UserRoomDatabase userRoomDatabase){
        this.userDao = userRoomDatabase.userDao();
    }


    @Override
    public void getCurrentUser() {
        
    }

    @Override
    public void insertCurrentUser() {

    }

    @Override
    public void updateCurrentUser() {

    }

    @Override
    public void clearCurrentUser() {

    }
}
