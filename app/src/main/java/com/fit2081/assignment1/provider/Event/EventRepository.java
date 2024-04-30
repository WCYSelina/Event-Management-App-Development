package com.fit2081.assignment1.provider.Event;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.assignment1.Entities.Event;

import java.util.List;

public class EventRepository {
    // private class variable to hold reference to DAO
    private EventDAO eventDAO;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;

    // constructor to initialise the repository class
    EventRepository(Application application) {
        // get reference/instance of the database
        EventDatabase db = EventDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        eventDAO = db.eventDAO();

        // once the class is initialised get all the items in the form of LiveData
        allEventsLiveData = eventDAO.getAllItems();
    }

    /**
     * Repository method to get all cards
     * @return LiveData of type List<Item>
     */
    LiveData<List<Event>> getAllCategories() {
        return allEventsLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param event object containing details of new Item to be inserted
     */
    void insert(Event event) {
        // Executes the database operation to insert the item in a background thread.
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.addItem(event));
    }

    void deleteAll() {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteAll());
    }

    void deleteByEventID(String eventID) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteByEventID(eventID));
    }
}
