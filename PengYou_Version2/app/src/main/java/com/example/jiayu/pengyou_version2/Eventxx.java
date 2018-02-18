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

    public Eventxx(String title, String specificEvent) {
        this.title = title;
        this.specificEvent=specificEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecificEvent() {
        return specificEvent;
    }

    public void setSpecificEvent(String specificEvent) {
        this.specificEvent = specificEvent;
    }
} //Pause: Retrieving data from firebase database (Part 14)
