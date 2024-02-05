package it.unimib.enjoyn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.unimib.enjoyn.model.Category;

@Dao
public interface InterestDao {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Insert
    long[] insertAll(List<Category> categoryList);

    @Query("DELETE FROM category")
    int deleteInterests();
}
