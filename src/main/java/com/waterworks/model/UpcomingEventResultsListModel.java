package com.waterworks.model;

import java.util.ArrayList;

public class UpcomingEventResultsListModel {
    private String EventNumber;
    private String MeetTime;
    private String TimeImprovement;
    private String Distance;
    private String strokedescription;
    private String placeno;

    public String getEventNumber() {
        return EventNumber;
    }

    public void setEventNumber(String eventNumber) {
        EventNumber = eventNumber;
    }

    public String getMeetTime() {
        return MeetTime;
    }

    public void setMeetTime(String meetTime) {
        MeetTime = meetTime;
    }

    public String getTimeImprovement() {
        return TimeImprovement;
    }

    public void setTimeImprovement(String timeImprovement) {
        TimeImprovement = timeImprovement;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getStrokedescription() {
        return strokedescription;
    }

    public void setStrokedescription(String strokedescription) {
        this.strokedescription = strokedescription;
    }

    public String getPlaceno() {
        return placeno;
    }

    public void setPlaceno(String placeno) {
        this.placeno = placeno;
    }

    public UpcomingEventResultsListModel() {

    }
}
