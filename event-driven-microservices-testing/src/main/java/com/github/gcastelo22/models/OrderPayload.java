package com.github.gcastelo22.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Inner payload containing specific order details.
 */
public class OrderPayload {
  @JsonProperty("order_id")
  private String orderId;

  @JsonProperty("total_amount")
  private double totalAmount;

  private String currency;

  // Standard Getters and Setters
  public String getOrderId() { return orderId; }
  public void setOrderId(String orderId) { this.orderId = orderId; }

  public double getTotalAmount() { return totalAmount; }
  public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
}