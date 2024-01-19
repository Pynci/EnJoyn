package it.unimib.enjoyn.util;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.units.qual.C;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.category.CategoryRepository;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.repository.user.UserRepository;
import it.unimib.enjoyn.source.category.CategoryRemoteDataSource;
import it.unimib.enjoyn.source.user.AuthenticationDataSource;
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

    public EventsRoomDatabase getEventDao(Application application) { //istanza di news room database
        return EventsRoomDatabase.getDatabase(application);
    }

    public IUserRepository getUserRepository(boolean debugMode){

        UserRemoteDataSource userRemoteDataSource = new UserRemoteDataSource();
        AuthenticationDataSource authenticationDataSource = new AuthenticationDataSource();
        //TODO: aggiungere eventuale istanza locale (da passare anch'essa al repository)
        //TODO: singleton?

        return new UserRepository(userRemoteDataSource, authenticationDataSource);
    }

    public ICategoryRepository getcategoryRepository(boolean debugMode){

        CategoryRemoteDataSource categoryRemoteDataSource = new CategoryRemoteDataSource();
        return new CategoryRepository(categoryRemoteDataSource);
    }

//    public FirebaseAuth getFirebaseAuth(){
//        return FirebaseAuth.getInstance();
//    }
//
//    public FirebaseUser getFirebaseUser(){
//        return FirebaseAuth.getInstance().getCurrentUser();
//    }
}
