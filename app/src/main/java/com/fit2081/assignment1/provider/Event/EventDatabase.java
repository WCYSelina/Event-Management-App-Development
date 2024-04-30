package com.fit2081.assignment1.provider.Event;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fit2081.assignment1.Entities.Event;
import com.fit2081.assignment1.Entities.EventCategory;
import com.fit2081.assignment1.provider.Category.CategoryDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {
    // database name, this is important as data is contained inside a file named "card_database"
    public static final String EVENT_DATABASE = "event_database";

    // reference to DAO, here RoomDatabase
    // class will implement this interface
    public abstract EventDAO eventDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile EventDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    // ExecutorService is a JDK API that simplifies running tasks in asynchronous mode.
    // Generally speaking, ExecutorService automatically provides a pool of threads and an API
    // for assigning tasks to it.
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Since this class is an absract class, to get the database reference we would need
     * to implement a way to get reference to the database.
     *
     * @param context Application of Activity Context
     * @return a reference to the database for read and write operation
     */
    static EventDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EventDatabase.class, EVENT_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
