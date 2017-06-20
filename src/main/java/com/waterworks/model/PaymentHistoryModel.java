package com.waterworks.model;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentHistoryModel
{
   private String sitename;
   private String invoicedate;
   private String PaymentType;
   private String TotalAmount;
   private String AmountPaid;

   private ArrayList<HashMap<String, String>> Description;

   public PaymentHistoryModel() {
   }

   public String getSitename() {
      return sitename;
   }

   public void setSitename(String sitename) {
      this.sitename = sitename;
   }

   public String getInvoicedate() {
      return invoicedate;
   }

   public void setInvoicedate(String invoicedate) {
      this.invoicedate = invoicedate;
   }

   public String getPaymentType() {
      return PaymentType;
   }

   public void setPaymentType(String paymentType) {
      PaymentType = paymentType;
   }

   public String getTotalAmount() {
      return TotalAmount;
   }

   public void setTotalAmount(String totalAmount) {
      TotalAmount = totalAmount;
   }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        AmountPaid = amountPaid;
    }

    public ArrayList<HashMap<String, String>> getDescription() {
      return Description;
   }

   public void setDescription(ArrayList<HashMap<String, String>> description) {
      Description = description;
   }
}
