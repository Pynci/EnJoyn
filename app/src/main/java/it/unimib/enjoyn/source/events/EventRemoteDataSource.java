package it.unimib.enjoyn.source.events;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.Constants;
import it.unimib.enjoyn.util.JSONParserUtil;

public class EventRemoteDataSource extends BaseEventRemoteDataSource{
    private final JSONParserUtil jsonParserUtil;
    private EventsDatabaseResponse eventsDatabaseResponse;
    private final DatabaseReference dbReference;
    private final FirebaseStorage firebaseStorage;


    public EventRemoteDataSource(JSONParserUtil jsonParserUtil) {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getEvent(String category) {

    }

    @Override
    public void getEvent() {
        EventsDatabaseResponse eventDBResponse = null;
        try {
            eventDBResponse = jsonParserUtil.parseJSONEventFileWithGSon("prova.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (eventDBResponse != null){
            eventCallback.onSuccessFromRemote(eventDBResponse, System.currentTimeMillis());
        } else{
            //TODO onFailure
        }
    }

    @Override
    public void createEvent(Event event, User user){
        String key = dbReference.child(Constants.EVENTS_PATH).push().getKey();

        dbReference
                .child(Constants.EVENTS_PATH)
                .child(key)
                .setValue(event)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        event.setEid(key);
                        eventCallback.onRemoteEventAdditionSuccess(event, user);
                    }
                    else{
                        eventCallback.onRemoteEventAdditionFailure(task.getException());
                    }
                });
    }

    @Override
    public void updateEvent(String key, Map<String, Object> updateMap){
        DatabaseReference eventReference = dbReference
                .child(Constants.EVENTS_PATH)
                .child(key);

        eventReference
                .updateChildren(updateMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //eventCallback.onSuccessFromRemote();
                    }
                    else{
                        //eventCallback.onRemoteDatabaseFailure(task.getException());
                    }
                });
    }

    }

