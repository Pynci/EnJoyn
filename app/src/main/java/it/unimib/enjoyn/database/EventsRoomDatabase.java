package it.unimib.enjoyn.database;

import static it.unimib.enjoyn.util.Costants.EVENTS_DATABASE_NAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.enjoyn.Event;
import it.unimib.enjoyn.util.StringConverter;

@Database(entities = {Event.class}, version = 1)

@TypeConverters({StringConverter.class})
public abstract class EventsRoomDatabase extends RoomDatabase {
    @TypeConverters({StringConverter.class})
    public abstract EventsDao eventDao();

    private static volatile EventsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EventsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventsRoomDatabase.class, EVENTS_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}