package com.waterworks.model;

import java.util.ArrayList;

public class UpcomingEventStudListModel {
    private String StudentName;
    private ArrayList<Events> eventsList;

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public ArrayList<Events> getEventsList() {
        return eventsList;
    }

    public void setEventsList(ArrayList<Events> eventsList) {
        this.eventsList = eventsList;
    }

    public UpcomingEventStudListModel() {
    }

    public class Events {
        private String EventNumber, Description, StrockDescription;

        public String getEventNumber() {
            return EventNumber;
        }

        public void setEventNumber(String eventNumber) {
            EventNumber = eventNumber;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getStrockDescription() {
            return StrockDescription;
        }

        public void setStrockDescription(String strockDescription) {
            StrockDescription = strockDescription;
        }

        public Events() {
        }

    }

}
