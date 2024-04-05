package com.fit2081.assignment1.Entities;

public class EventCategory {
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
