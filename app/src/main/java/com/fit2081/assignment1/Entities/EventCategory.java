package com.fit2081.assignment1.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "eventCategories")
public class EventCategory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    private String categoryID;
    private String categoryName;
    private int eventCount;
    private boolean isActive;

    public EventCategory(String categoryID, String categoryName, int eventCount, boolean isActive) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.eventCount = eventCount;
        this.isActive = isActive;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void resetEventCount() {
        this.eventCount = 0;
    }

    public void addEventCount() {
        this.eventCount += 1;
    }
    public void eventCountDecrement() { this.eventCount -= 1; }
    public boolean isActive() {
        return isActive;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}


}
