package it.unimib.enjoyn.source.events;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private List<Event> eventList;
    private boolean firstFetch;


    public EventRemoteDataSource(JSONParserUtil jsonParserUtil) {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance(Constants.STORAGE_PATH);
        this.jsonParserUtil = jsonParserUtil;
        eventList = new ArrayList<>();
        firstFetch = true;
    }

    @Override
    public void getEvent(String category) {

    }

    @Override
    public void fetchAllEvents(){

        SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
        String dataAttuale = formatoData.format(new Date());

        dbReference
                .child(Constants.EVENTS_PATH)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Event event = snapshot.getValue(Event.class);
                        event.setEid(snapshot.getKey());
                        if(event.getDate().compareTo(dataAttuale) >= 0){
                            eventCallback.onRemoteEventAdded(event);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Event event = snapshot.getValue(Event.class);
                        event.setEid(snapshot.getKey());
                        if(event.getDate().compareTo(dataAttuale) >= 0){
                            eventCallback.onRemoteEventChanged(event);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Event event = snapshot.getValue(Event.class);
                        event.setEid(snapshot.getKey());
                        if(event.getDate().compareTo(dataAttuale) >= 0){
                            eventCallback.onRemoteEventRemoved(event);
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // per ora niente
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        eventCallback.onRemoteEventFetchFailure(new Exception(error.getMessage()));
                    }
                });
    }

//    //Vecchio getEvent()
//    @Override
//    public void getEvent() {
//        EventsDatabaseResponse eventDBResponse = null;
//        try {
//            eventDBResponse = jsonParserUtil.parseJSONEventFileWithGSon("prova.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (eventDBResponse != null){
//            eventCallback.onSuccessFromRemote(eventDBResponse, System.currentTimeMillis());
//        } else{
//            //TODO onFailure
//        }
//    }

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

