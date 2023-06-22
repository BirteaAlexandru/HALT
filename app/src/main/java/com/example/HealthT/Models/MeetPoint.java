package com.example.HealthT.Models;

import android.location.Location;

import java.util.Date;

public class MeetPoint {
    String meetPointId=null;
    String activity;
    int numberOfPersons;
    Date date;
    Location location;

    public MeetPoint(String activity, Location location, int numberOfPersons, int mYear, int mMonth, int mDay, int mHour, int mMinute){
        this.activity= activity;
        this.location= location;
        this.numberOfPersons= numberOfPersons;
        date = new Date(mYear, mMonth, mDay);
        date.setHours(mHour);
        date.setMinutes(mMinute);
    }

    public MeetPoint(String meetPointId, String activity, String hour, String minute)
    {
        this.meetPointId= meetPointId;
        this.activity= activity;
        this.date= new Date();
        this.date.setHours(Integer.parseInt(hour));
        this.date.setMinutes(Integer.parseInt(minute));
    }

    public String getMeetPointId() {return meetPointId;}

    public void setMeetPointId(String meetPointId) {this.meetPointId = meetPointId;}

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getNumberOfPersons() {return numberOfPersons;}

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {return location;}

    public void setLocation(Location location) {this.location = location;}
}
