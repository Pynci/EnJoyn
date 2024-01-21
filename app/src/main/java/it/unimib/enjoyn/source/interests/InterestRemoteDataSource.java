package it.unimib.enjoyn.source.interests;

import com.google.firebase.database.FirebaseDatabase;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.ui.CategoriesHolder;

public class InterestRemoteDataSource extends BaseInterestRemoteDataSource {

    private final FirebaseDatabase firebaseDatabase;

    public InterestRemoteDataSource() {

        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void storeUserInterests(CategoriesHolder categoriesHolder, User user) {

        firebaseDatabase
                .getReference()
                .child("Interests")
                .child(user.getUid())
                .setValue(categoriesHolder.getCategories())
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        interestsCallback.onSuccessCreateUsers();
                    }
                    else{
                        interestsCallback.onFailureCreateUsers(task.getException());
                    }
                });
    }
}
