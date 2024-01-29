package it.unimib.enjoyn.source.events;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Constants;

public class EventParticipationRemoteDataSource extends BaseEventParticipationRemoteDataSource {

    private final DatabaseReference dbReference;

    public EventParticipationRemoteDataSource(){
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
    }

    @Override
    public void createEventParticipation(Event event, User user){
        dbReference
                .child(Constants.EVENTPARTICIPATIONS_PATH)
                .child(event.getEid())
                .child(user.getUid())
                .setValue(user)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        eventParticipationCallback.onRemoteEventParticipationAdditionSuccess(event, user);
                    }else{
                        eventParticipationCallback.onRemoteEventParticipationAdditionFailure(task.getException());
                    }
                });
    }
}
