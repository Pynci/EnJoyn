package it.unimib.enjoyn.source.interests;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.Callback;
import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;
import it.unimib.enjoyn.util.Constants;

public class InterestRemoteDataSource implements BaseInterestRemoteDataSource {

    private final FirebaseDatabase firebaseDatabase;

    public InterestRemoteDataSource() {

        firebaseDatabase = FirebaseDatabase.getInstance(Constants.DATABASE_PATH);
    }

    @Override
    public void createUserInterests(CategoriesHolder categoriesHolder, String uid, Callback callback) {

        firebaseDatabase
                .getReference()
                .child("Interests")
                .child(uid)
                .setValue(categoriesHolder.getCategories())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onComplete(new Result.Success());
                    }
                    else{
                        callback.onComplete(new Result.Error(Constants.INTEREST_REMOTE_CREATION_ERROR));
                    }
                });
    }

    @Override
    public void getUserInterests(String uid, Callback callback) {
        firebaseDatabase
                .getReference()
                .child(Constants.INTERESTS_PATH)
                .child(uid)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        DataSnapshot snapshot = task.getResult();
                        List<Category> allCategories = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            Category temp = dataSnapshot.getValue(Category.class);
                            temp.setId(Integer.parseInt(dataSnapshot.getKey()));
                            allCategories.add(temp);

                        }
                        callback.onComplete(new Result.CategorySuccess(allCategories));
                    } else {
                        callback.onComplete(new Result.Error(Constants.INTEREST_REMOTE_FETCH_ERROR));
                    }
                });
    }
}
