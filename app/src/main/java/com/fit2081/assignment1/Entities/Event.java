package com.fit2081.assignment1.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "eventID")
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

    public String getEventName() {
        return eventName;
    }

    public int getTicketAvailable() {
        return ticketAvailable;
    }

    public boolean isActive() {
        return isActive;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

}
