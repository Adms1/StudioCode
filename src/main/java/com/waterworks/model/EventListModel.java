package com.waterworks.model;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListModel
{
   private String eventName;
   private ArrayList<HashMap<String, String>> eventDetails;

   public EventListModel() {
   }

   public String getEventName() {
      return eventName;
   }

   public void setEventName(String eventName) {
      this.eventName = eventName;
   }

   public ArrayList<HashMap<String, String>> getEventDetails() {
      return eventDetails;
   }

   public void setEventDetails(ArrayList<HashMap<String, String>> eventDetails) {
      this.eventDetails = eventDetails;
   }
}
