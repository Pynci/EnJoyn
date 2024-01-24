package it.unimib.enjoyn.source.interests;

import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.ui.viewmodels.CategoriesHolder;
import it.unimib.enjoyn.util.Constants;

public class InterestRemoteDataSource extends BaseInterestRemoteDataSource {

    private final FirebaseDatabase firebaseDatabase;

    public InterestRemoteDataSource() {

        firebaseDatabase = FirebaseDatabase.getInstance(Constants.DATABASE_PATH);
    }

    @Override
    public void storeUserInterests(CategoriesHolder categoriesHolder, String uid) {

        firebaseDatabase
                .getReference()
                .child("Interests")
                .child(uid)
                .setValue(categoriesHolder.getCategories())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        interestsCallback.onSuccessCreateUsersInterest();
                    }
                    else{
                        interestsCallback.onFailureCreateUsersInterest(task.getException());
                    }
                });
    }
}
