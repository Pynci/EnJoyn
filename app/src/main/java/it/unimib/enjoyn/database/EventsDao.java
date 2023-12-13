package it.unimib.enjoyn.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.enjoyn.Event;

public interface EventsDao {

    @Query("SELECT * FROM event ORDER BY date, time DESC")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE id = :id")
    Event getEvent(long id);

    @Query("SELECT * FROM event WHERE isFavorite = 1 ORDER BY date, time DESC")
    List<Event> getFavoriteEvents();

    @Query("SELECT * FROM event WHERE isTODO = 1 ORDER BY date, time DESC")
    List<Event> getTodoEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertNewsList(List<Event> eventList);

    @Insert
    void insertAll(Event... event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event")
    void deleteAll();

    @Query("DELETE FROM event WHERE isFavorite = 0")
    void deleteNotFavoriteNews();

    @Delete
    void deleteAllWithoutQuery(Event... event);

    @Update
    void updateSingleFavoriteNews(Event event);

    @Update
    void updateListFavoriteNews(List<Event> events);
}
