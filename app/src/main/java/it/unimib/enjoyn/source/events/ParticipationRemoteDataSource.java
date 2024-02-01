package it.unimib.enjoyn.source.events;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.util.Constants;

public class ParticipationRemoteDataSource implements BaseParticipationRemoteDataSource {

    private final DatabaseReference dbReference;

    public ParticipationRemoteDataSource(){
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
    }

    @Override
    public void createParticipation(Event event, User user, Callback callback){
        dbReference
                .child(Constants.EVENTPARTICIPATIONS_PATH)
                .child(event.getEid())
                .child(user.getUid())
                .setValue(user.getUsername())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }else{
                        callback.onComplete(new Result.Error(Constants.PARTICIPATION_REMOTE_CREATION_ERROR));
                    }
                });
    }

    public void fetchEventParticipations(Event event){

    }
}
