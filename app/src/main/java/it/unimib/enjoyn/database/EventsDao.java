package it.unimib.enjoyn.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.enjoyn.model.Event;

@Dao
public interface EventsDao {

    @Query("SELECT * FROM event ORDER BY date, time DESC, distance ASC")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE id = :id")
    Event getEvent(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertEventList(List<Event> eventList);

    @Insert
    void insertAll(Event... event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();

    @Delete
    void deleteAllWithoutQuery(Event... event);

    @Update
    void updateSingleFavoriteEvent(Event event);

    @Update
    int updateSingleTodoEvent(Event event);

    @Update
    void updateListFavoriteEvents(List<Event> events);


}
