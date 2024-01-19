package it.unimib.enjoyn.source.category;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

        DatabaseReference categoriesReference = databaseReference.child("Categories");

        categoriesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Category> allCategories = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Category childValue = childSnapshot.getValue(Category.class);
                    allCategories.add(childValue);
                }

                categoryCallback.onSuccessGetAllCategories(allCategories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryCallback.onFailureGetAllCategories(error.getMessage());
            }
        });
    }
}
