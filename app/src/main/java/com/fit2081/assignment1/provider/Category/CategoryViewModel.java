package com.fit2081.assignment1.provider.Category;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.assignment1.Entities.EventCategory;

import java.util.List;

/**
 * ViewModel class is used for pre-processing the data,
 * before passing it to the controllers (Activity or Fragments). ViewModel class should not hold
 * direct reference to database. ViewModel class relies on repository class, hence the database is
 * accessed using the Repository class.
 */
public class CategoryViewModel extends AndroidViewModel {
    // reference to CardRepository
    private CategoryRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allCategoryLiveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new CategoryRepository(application);

        // get all items by calling method defined in repository class
        allCategoryLiveData = repository.getAllCategories();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<EventCategory>> getAllCategory() {
        return allCategoryLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param category object containing details of new Item to be inserted
     */
    public void insert(EventCategory category) {
        repository.insert(category);
    }
    public void insert(List<EventCategory> category) {
        repository.insert(category);
    }
    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<Boolean> findByCategoryID(String categoryID) {
        return repository.findByCategoryId(categoryID);
    }

    public void increamentByCategoryID(String categoryID) {
        repository.increamentByCategoryID(categoryID);
    }

}