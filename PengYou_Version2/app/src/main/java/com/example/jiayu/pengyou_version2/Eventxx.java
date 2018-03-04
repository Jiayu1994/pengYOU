package com.example.jiayu.pengyou_version2;

/**
 * Created by Jiayu on 18/2/18.
 */

//Continue: Retrieving data from firebase database (Part 14)
public class Eventxx {

    //Without this, the app may crash
    public Eventxx(){

    }

    // These name must match firebase!
    public String title;
    public String specificEvent;
    public String time;
    public String date;
    public String location;
    public String numberParticipant;
    public String description;

    public Eventxx(String title, String specificEvent) {
        this.title = title;
        this.specificEvent=specificEvent;
        this.time = time;
        this.date = date;
        this.location = location;
        this.numberParticipant = numberParticipant;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecificEvent() {
        return "( " + specificEvent + " )";
    }
    public void setSpecificEvent(String specificEvent) {
        this.specificEvent = specificEvent;
    }

    public String getTimeEvent() {
        return "  " + time;
    }

    public String getDateEvent() {
        return "Date & Time : " + date + ",";
    }

    public String getLocationEvent() {
        return location;
    }

    public String getParticipantEvent() {
        return "Participants Required : " + numberParticipant;
    }

    public String getDescriptionEvent(){
        return "Description : " + description;
    }
} //Pause: Retrieving data from firebase database (Part 14)

