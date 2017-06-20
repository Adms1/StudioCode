package com.waterworks.model;

import java.util.ArrayList;

public class UpcomingAllEventStudListModel {
    private String StartTime;
    private String EventDescription;
    private ArrayList<Events> eventsList;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public ArrayList<Events> getEventsList() {
        return eventsList;
    }

    public void setEventsList(ArrayList<Events> eventsList) {
        this.eventsList = eventsList;
    }

    public UpcomingAllEventStudListModel() {
    }

    public class Events {
        private String EventNumber, Gender, Age, Distance, StrokeDesc;

        public String getEventNumber() {
            return EventNumber;
        }

        public void setEventNumber(String eventNumber) {
            EventNumber = eventNumber;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String age) {
            Age = age;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String distance) {
            Distance = distance;
        }

        public String getStrokeDesc() {
            return StrokeDesc;
        }

        public void setStrokeDesc(String strokeDesc) {
            StrokeDesc = strokeDesc;
        }

        public Events() {
        }

    }

}
