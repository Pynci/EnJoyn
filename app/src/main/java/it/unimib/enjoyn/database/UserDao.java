package it.unimib.enjoyn.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import it.unimib.enjoyn.model.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE uid = :uid")
    User getUser(String uid);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);
    @Update
    int updateUser(User user);
    @Delete
    int deleteUser(User user);

}