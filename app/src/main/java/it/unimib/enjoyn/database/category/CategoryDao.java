package it.unimib.enjoyn.database.category;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.unimib.enjoyn.model.Category;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Insert
    int insertAll(List<Category> categoryList);
}
