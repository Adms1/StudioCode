package com.waterworks.model;

import java.util.ArrayList;
import java.util.HashMap;

public class PoolRecordsModel
{
   private String EventDescription;
   private ArrayList<HashMap<String, String>> eventDetails;

   public PoolRecordsModel() {
   }

   public String getEventDescription() {
      return EventDescription;
   }

   public void setEventDescription(String eventDescription) {
      EventDescription = eventDescription;
   }

   public ArrayList<HashMap<String, String>> getEventDetails() {
      return eventDetails;
   }

   public void setEventDetails(ArrayList<HashMap<String, String>> eventDetails) {
      this.eventDetails = eventDetails;
   }
}
