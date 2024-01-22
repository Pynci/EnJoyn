package it.unimib.enjoyn.source.category;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.util.Constants;

public class CategoryRemoteDataSource extends BaseCategoryRemoteDataSource{

    private final DatabaseReference databaseReference;
    private final FirebaseStorage firebaseStorage;

    public CategoryRemoteDataSource() {
        databaseReference = FirebaseDatabase.getInstance(Constants.DATABASE_PATH).getReference();
        firebaseStorage = FirebaseStorage.getInstance();
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

                            Category temp = dataSnapshot.getValue(Category.class);
                            temp.setId(Integer.parseInt(dataSnapshot.getKey()));
                            allCategories.add(temp);

                        }

                        categoryCallback.onSuccessGetAllCategories(allCategories);
                    }
                    else {
                        categoryCallback.onFailureGetAllCategories(task.getException().getMessage());
                    }
        });
    }

    @Override
    public void getImageFromName(String name) {

        StorageReference imageref = firebaseStorage.getReference()
                .child("categories")
                .child(name.toLowerCase()+".jpg");

        imageref
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    categoryCallback.onSuccessGetImageFromName(uri);
                })
                .addOnFailureListener(e -> {
                    categoryCallback.onFailureGetImageFromName(e);
                });
    }
}
