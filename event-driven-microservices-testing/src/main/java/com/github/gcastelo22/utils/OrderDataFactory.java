package com.github.gcastelo22.utils;

import com.github.gcastelo22.models.OrderEvent;
import com.github.gcastelo22.models.OrderPayload;
import java.util.Random;
import java.util.UUID;

/**
 * Factory class to generate random data for Order events.
 * Helps ensuring test independence by avoiding data collisions.
 */
public class OrderDataFactory {

  private static final Random RANDOM = new Random();

  /**
   * Creates an OrderEvent with a random ID and price.
   * @return A populated OrderEvent object
   */
  public static OrderEvent createValidOrder() {
    // Generate a random ID (e.g., ORD-7b2a)
    String randomId = "ORD-" + UUID.randomUUID().toString().substring(0, 4);

    // Generate a random amount between 10.0 and 500.0
    double randomAmount = 10 + (490 * RANDOM.nextDouble());
    // Round to 2 decimal places
    randomAmount = Math.round(randomAmount * 100.0) / 100.0;

    OrderPayload payload = new OrderPayload();
    payload.setOrderId(randomId);
    payload.setTotalAmount(randomAmount);
    payload.setCurrency("EUR");

    OrderEvent event = new OrderEvent();
    event.setEventType("ORDER_CREATED");
    event.setPayload(payload);

    return event;
  }

  /**
   * Creates an invalid OrderEvent (missing mandatory Order ID) to test system resilience.
   * @return An OrderEvent object with a null ID
   */
  public static OrderEvent createInvalidOrder() {
    OrderEvent event = createValidOrder();
    event.getPayload().setOrderId(null); // Explicitly breaking the contract
    return event;
  }
}