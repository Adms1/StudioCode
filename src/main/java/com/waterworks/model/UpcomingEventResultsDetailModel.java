package com.waterworks.model;

public class UpcomingEventResultsDetailModel {
    private String Swimmer;
    private String StudentID;
    private String Age;
    private String placeno;
    private String MeetTime;
    private String TimeImprovement;

    public String getSwimmer() {
        return Swimmer;
    }

    public void setSwimmer(String swimmer) {
        Swimmer = swimmer;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPlaceno() {
        return placeno;
    }

    public void setPlaceno(String placeno) {
        this.placeno = placeno;
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

    public UpcomingEventResultsDetailModel() {

    }
}
