package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.repository.category.CategoryRepository;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.repository.interests.IInterestRepository;
import it.unimib.enjoyn.repository.interests.InterestRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.repository.user.UserRepository;
import it.unimib.enjoyn.source.category.CategoryRemoteDataSource;
import it.unimib.enjoyn.source.interests.InterestLocalDataSource;
import it.unimib.enjoyn.source.interests.InterestRemoteDataSource;
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

    public IUserRepository getUserRepository(){

        UserRemoteDataSource userRemoteDataSource = new UserRemoteDataSource();
        AuthenticationDataSource authenticationDataSource = new AuthenticationDataSource();
        //TODO: aggiungere eventuale istanza locale (da passare anch'essa al repository)
        //TODO: singleton?

        return new UserRepository(userRemoteDataSource, authenticationDataSource);
    }

    public ICategoryRepository getCategoryRepository(){

        CategoryRemoteDataSource categoryRemoteDataSource = new CategoryRemoteDataSource();
        return new CategoryRepository(categoryRemoteDataSource);
    }

    public EventsRoomDatabase getRoomDatabase(Application application) {
        return EventsRoomDatabase.getDatabase(application);
    }

    public IInterestRepository getInterestRepository(Application application) {
        InterestRemoteDataSource interestDataSource = new InterestRemoteDataSource();
        InterestLocalDataSource interestLocalDataSource = new InterestLocalDataSource(getRoomDatabase(application));
        return new InterestRepository(interestDataSource, interestLocalDataSource);
    }

//    public FirebaseAuth getFirebaseAuth(){
//        return FirebaseAuth.getInstance();
//    }
//
//    public FirebaseUser getFirebaseUser(){
//        return FirebaseAuth.getInstance().getCurrentUser();
//    }
}
