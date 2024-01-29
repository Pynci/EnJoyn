package it.unimib.enjoyn.source.events;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Constants;

public class EventCreationRemoteDataSource extends BaseEventCreationRemoteDataSource {

    private final DatabaseReference dbReference;

    public EventCreationRemoteDataSource(){
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
    }

    @Override
    public void createEventCreation(Event event, User user){
        dbReference
                .child(Constants.EVENTCREATIONS_PATH)
                .child(user.getUid())
                .child(event.getEid())
                .setValue(event)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        eventCreationCallback.onRemoteEventCreationAdditionSuccess(event, user);
                    }else{
                        eventCreationCallback.onRemoteEventCreationAdditionFailure(task.getException());
                    }
                });
    }

}
