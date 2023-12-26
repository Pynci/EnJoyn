package it.unimib.enjoyn.util;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.repository.user.UserMockRepository;
import it.unimib.enjoyn.repository.user.UserRepository;
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

    //non sono sicuro serva davvero questo metodo -Pinci
    public DatabaseReference getDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    //non sono sicuro serva davvero questo metodo -Pinci
    public FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    public IUserRepository getUserRepository(boolean debugMode){

        UserRemoteDataSource userRemoteDataSource = new UserRemoteDataSource();
        //TODO: aggiungere eventuale istanza locale (da passare anch'essa al repository)

        if(debugMode){
            return new UserMockRepository();
        }
        else {
            return new UserRepository(userRemoteDataSource);
        }
    }
}
