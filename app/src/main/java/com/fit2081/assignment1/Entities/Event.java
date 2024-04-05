package com.fit2081.assignment1.Entities;

public class Event {
    private String eventID;
    private String categoryID;
    private String eventName;
    private int ticketAvailable;
    private boolean isActive;

    public Event(String eventID, String categoryID, String eventName, int ticketAvailable, boolean isActive) {
        this.eventID = eventID;
        this.categoryID = categoryID;
        this.eventName = eventName;
        this.ticketAvailable = ticketAvailable;
        this.isActive = isActive;
    }

    public String getEventID() {
        return eventID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getTicketAvailable() {
        return ticketAvailable;
    }

    public void setTicketAvailable(int ticketAvailable) {
        this.ticketAvailable = ticketAvailable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
