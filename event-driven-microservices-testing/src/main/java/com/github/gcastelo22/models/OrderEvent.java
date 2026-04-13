package com.github.gcastelo22.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Root object representing the event structure sent to the broker.
 */
public class OrderEvent {
  @JsonProperty("event_type")
  private String eventType;

  private OrderPayload payload;

  // Standard Getters and Setters
  public String getEventType() { return eventType; }
  public void setEventType(String eventType) { this.eventType = eventType; }

  public OrderPayload getPayload() { return payload; }
  public void setPayload(OrderPayload payload) { this.payload = payload; }
}