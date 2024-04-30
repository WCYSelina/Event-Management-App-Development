package com.fit2081.assignment1.provider.Category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.fit2081.assignment1.Entities.EventCategory;

import java.util.List;

// Indicates that this interface is a Data Access Object (DAO),
// used for interacting with the database.
@Dao
public interface CategoryDAO {
    // Specifies a database query to retrieve all items from the "items" table. (referenced A.3.4)
    @Query("select * from eventCategories")
    LiveData<List<EventCategory>> getAllItems(); // Returns a LiveData object containing a list of Item objects.

    // Indicates that this method is used to insert data into the database.
    @Insert
    void addItem(EventCategory item); // Method signature for inserting an Item object into the database.

    @Query("DELETE FROM eventCategories WHERE id = :id")
    void deleteByID(int id);

    @Transaction
    default void deleteAndInsert(int id, EventCategory item){
        deleteByID(id);
        addItem(item);
    }
}
