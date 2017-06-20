package com.waterworks.model;

public class SchedulingFilterInstArrayModel
{
   private String instructorID;
   private String instructorName;
   private String instructorGender;
   private String instructorPhoto;
   private boolean isSelected;

   public SchedulingFilterInstArrayModel()   {}

   public String getInstructorID() {
      return instructorID;
   }

   public void setInstructorID(String instructorID) {
      this.instructorID = instructorID;
   }

   public String getInstructorName() {
      return instructorName;
   }

   public void setInstructorName(String instructorName) {
      this.instructorName = instructorName;
   }

   public String getInstructorGender() {
      return instructorGender;
   }

   public void setInstructorGender(String instructorGender) {
      this.instructorGender = instructorGender;
   }

   public String getInstructorPhoto() {
      return instructorPhoto;
   }

   public void setInstructorPhoto(String instructorPhoto) {
      this.instructorPhoto = instructorPhoto;
   }

   public boolean isSelected() {
      return isSelected;
   }

   public void setIsSelected(boolean isSelected) {
      this.isSelected = isSelected;
   }
}
