package com.waterworks.model;

import java.util.ArrayList;

public class SwimCompRegi2Item {
    String _StudentId, _StudentName;
    ArrayList<String> Event, AgeGroup, Distance, StrokeType, StudentID, tdid, ControlType, Text, Value;


    public SwimCompRegi2Item(String _StudentId, String _StudentName,
                             ArrayList<String> event, ArrayList<String> ageGroup,
                             ArrayList<String> distance, ArrayList<String> strokeType,
                             ArrayList<String> studentID, ArrayList<String> tdid,
                             ArrayList<String> controlType, ArrayList<String> text,
                             ArrayList<String> value) {
        super();
        this._StudentId = _StudentId;
        this._StudentName = _StudentName;
        Event = event;
        AgeGroup = ageGroup;
        Distance = distance;
        StrokeType = strokeType;
        StudentID = studentID;
        this.tdid = tdid;
        ControlType = controlType;
        Text = text;
        Value = value;
    }

    public String get_StudentId() {
        return _StudentId;
    }

    public void set_StudentName(String _StudentName) {
        this._StudentName = _StudentName;
    }

    public String get_StudentName() {
        return _StudentName;
    }

    public void set_StudentId(String _StudentId) {
        this._StudentId = _StudentId;
    }

    public ArrayList<String> getEvent() {
        return Event;
    }

    public void setEvent(ArrayList<String> event) {
        Event = event;
    }

    public ArrayList<String> getAgeGroup() {
        return AgeGroup;
    }

    public void setAgeGroup(ArrayList<String> ageGroup) {
        AgeGroup = ageGroup;
    }

    public ArrayList<String> getDistance() {
        return Distance;
    }

    public void setDistance(ArrayList<String> distance) {
        Distance = distance;
    }

    public ArrayList<String> getStrokeType() {
        return StrokeType;
    }

    public void setStrokeType(ArrayList<String> strokeType) {
        StrokeType = strokeType;
    }

    public ArrayList<String> getStudentID() {
        return StudentID;
    }

    public void setStudentID(ArrayList<String> studentID) {
        StudentID = studentID;
    }

    public ArrayList<String> getTdid() {
        return tdid;
    }

    public void setTdid(ArrayList<String> tdid) {
        this.tdid = tdid;
    }

    public ArrayList<String> getControlType() {
        return ControlType;
    }

    public void setControlType(ArrayList<String> controlType) {
        ControlType = controlType;
    }

    public ArrayList<String> getText() {
        return Text;
    }

    public void setText(ArrayList<String> text) {
        Text = text;
    }

    public ArrayList<String> getValue() {
        return Value;
    }

    public void setValue(ArrayList<String> value) {
        Value = value;
    }

}