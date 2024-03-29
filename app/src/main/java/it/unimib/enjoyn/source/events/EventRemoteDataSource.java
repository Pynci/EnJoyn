package it.unimib.enjoyn.source.events;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.util.Constants;

public class EventRemoteDataSource implements BaseEventRemoteDataSource{
    private final DatabaseReference dbReference;


    public EventRemoteDataSource() {
        dbReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
    }

    @Override
    public void fetchAllEvents(Callback addedCallback,
                               Callback changedCallback,
                               Callback removedCallback,
                               Callback cancelledCallback){

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
                            addedCallback.onComplete(new Result.SingleEventSuccess(event));
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Event event = snapshot.getValue(Event.class);
                        event.setEid(snapshot.getKey());
                        if(event.getDate().compareTo(dataAttuale) >= 0){
                            changedCallback.onComplete(new Result.SingleEventSuccess(event));
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Event event = snapshot.getValue(Event.class);
                        event.setEid(snapshot.getKey());
                        if(event.getDate().compareTo(dataAttuale) >= 0){
                            removedCallback.onComplete(new Result.SingleEventSuccess(event));
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // per ora niente, in caso aggiungere una callback
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(Constants.EVENT_REMOTE_FETCH_ERROR));
                    }
                });
    }

    @Override
    public void fetchSingleEvent(Event event,
                                 Callback dataChangeCallback,
                                 Callback cancelledCallback){
        dbReference
                .child(Constants.EVENTS_PATH)
                .child(event.getEid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Event event = snapshot.getValue(Event.class);
                        if(event != null){
                            event.setEid(snapshot.getKey());
                        }
                        dataChangeCallback.onComplete(new Result.SingleEventSuccess(event));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cancelledCallback.onComplete(new Result.Error(Constants.EVENT_REMOTE_FETCH_ERROR));
                    }
                });
    }

    @Override
    public void createEvent(Event event, User user, Callback callback){
        String key = dbReference.child(Constants.EVENTS_PATH).push().getKey();

        dbReference
                .child(Constants.EVENTS_PATH)
                .child(key)
                .setValue(event)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        event.setEid(key);
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.EVENT_REMOTE_CREATION_ERROR));
                    }
                });
    }

    @Override
    public void updateEvent(String key, Map<String, Object> updateMap, Callback callback){
        DatabaseReference eventReference = dbReference
                .child(Constants.EVENTS_PATH)
                .child(key);

        eventReference
                .updateChildren(updateMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.EVENT_REMOTE_UPDATE_ERROR));
                    }
                });
    }

    }

