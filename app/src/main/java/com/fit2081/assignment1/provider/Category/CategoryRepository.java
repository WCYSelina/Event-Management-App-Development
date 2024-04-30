package com.fit2081.assignment1.provider.Category;
import android.app.Application;
import androidx.lifecycle.LiveData;

import com.fit2081.assignment1.Entities.EventCategory;
import java.util.List;

public class CategoryRepository {

    // private class variable to hold reference to DAO
    private CategoryDAO categoryDAO;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allCategoriesLiveData;

    // constructor to initialise the repository class
    CategoryRepository(Application application) {
        // get reference/instance of the database
        CategoryDatabase db = CategoryDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        categoryDAO = db.categoryDAO();

        // once the class is initialised get all the items in the form of LiveData
        allCategoriesLiveData = categoryDAO.getAllItems();
    }

    /**
     * Repository method to get all cards
     * @return LiveData of type List<Item>
     */
    LiveData<List<EventCategory>> getAllCategories() {
        return allCategoriesLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param category object containing details of new Item to be inserted
     */
    void insert(EventCategory category) {
        // Executes the database operation to insert the item in a background thread.
        CategoryDatabase.databaseWriteExecutor.execute(() -> categoryDAO.addItem(category));
    }

    void insert(List<EventCategory> categories) {
        for (EventCategory category: categories) {
            CategoryDatabase.databaseWriteExecutor.execute(() -> categoryDAO.addItem(category));
        }
    }
    void deleteAll() {
        // Executes the database operation to insert the item in a background thread.
        CategoryDatabase.databaseWriteExecutor.execute(() -> categoryDAO.deleteAllCategories());
    }

    LiveData<Boolean> findByCategoryId(String categoryID) {
        return categoryDAO.findByCategoryID(categoryID);
    }
    void increamentByCategoryID(String categoryID) {
        CategoryDatabase.databaseWriteExecutor.execute(() -> categoryDAO.increamentByCategoryID(categoryID));
    }
}
