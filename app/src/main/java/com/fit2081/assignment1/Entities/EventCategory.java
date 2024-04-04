package com.fit2081.assignment1.Entities;

public class EventCategory {
    private String categoryID;
    private String categoryName;
    private int eventCount;
    private boolean isActive;

    public EventCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
