package it.unimib.enjoyn.source.events;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

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
                .child(Constants.PARTICIPATIONS_PATH)
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

    public void fetchEventParticipations(Event event, Callback callback){
        dbReference
                .child(Constants.PARTICIPATIONS_PATH)
                .child(event.getEid())
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       Result.ResultList userList = new Result.ResultList();

                       DataSnapshot queryResult = task.getResult();
                       for (DataSnapshot dataSnapshot : queryResult.getChildren()) {
                           User user = dataSnapshot.getValue(User.class);
                           user.setUid(dataSnapshot.getKey());
                           userList.addResult(new Result.UserSuccess(user));
                       }
                       callback.onComplete(userList);
                   }
                   else{
                        callback.onComplete(new Result.Error(Constants.PARTICIPATION_REMOTE_FETCH_ERROR));
                   }
                });
    }
}
