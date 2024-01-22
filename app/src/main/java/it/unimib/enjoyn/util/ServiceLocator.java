package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.repository.category.CategoryRepository;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.repository.user.UserRepository;
import it.unimib.enjoyn.source.category.CategoryRemoteDataSource;
import it.unimib.enjoyn.source.user.AuthenticationDataSource;
import it.unimib.enjoyn.source.user.BaseUserLocalDataSource;
import it.unimib.enjoyn.source.user.BaseUserRemoteDataSource;
import it.unimib.enjoyn.source.user.UserLocalDataSource;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public LocalRoomDatabase getLocalDatabase(Application application) {
        return LocalRoomDatabase.getDatabase(application);
    }

    public IUserRepository getUserRepository(Application application){

        BaseUserLocalDataSource userLocalDataSource = new UserLocalDataSource(getLocalDatabase(application));
        BaseUserRemoteDataSource userRemoteDataSource = new UserRemoteDataSource();
        AuthenticationDataSource authenticationDataSource = new AuthenticationDataSource();

        return new UserRepository(userLocalDataSource, userRemoteDataSource, authenticationDataSource);
    }

    public ICategoryRepository getcategoryRepository(boolean debugMode){

        CategoryRemoteDataSource categoryRemoteDataSource = new CategoryRemoteDataSource();
        return new CategoryRepository(categoryRemoteDataSource);
    }

}
