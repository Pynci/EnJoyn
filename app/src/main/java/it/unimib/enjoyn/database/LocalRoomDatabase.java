package it.unimib.enjoyn.database;

import static it.unimib.enjoyn.util.Constants.EVENTS_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.StringConverter;

@Database(entities = {Event.class, User.class, Category.class}, version = 1)

@TypeConverters({StringConverter.class})
public abstract class LocalRoomDatabase extends RoomDatabase {
    @TypeConverters({StringConverter.class})
    public abstract EventsDao eventDao();
    @TypeConverters({StringConverter.class})
    public abstract UserDao userDao();
    @TypeConverters({StringConverter.class})
    public abstract CategoryDao categoryDao();

    private static volatile LocalRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LocalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalRoomDatabase.class, EVENTS_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
