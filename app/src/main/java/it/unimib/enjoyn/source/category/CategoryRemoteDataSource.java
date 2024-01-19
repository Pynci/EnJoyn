package it.unimib.enjoyn.source.category;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.util.Constants;

public class CategoryRemoteDataSource extends BaseCategoryRemoteDataSource{

    private final DatabaseReference databaseReference;

    public CategoryRemoteDataSource() {
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
    }

    @Override
    public void getAllCategories() {

        databaseReference
                .child(Constants.CATEGORIES_PATH)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        DataSnapshot snapshot = task.getResult();
                        List<Category> allCategories = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            allCategories.add(dataSnapshot.getValue(Category.class));
                        }

                        categoryCallback.onSuccessGetAllCategories(allCategories);
                    }
                    else {
                        categoryCallback.onFailureGetAllCategories(task.getException().getMessage());
                    }
        });
    }
}
