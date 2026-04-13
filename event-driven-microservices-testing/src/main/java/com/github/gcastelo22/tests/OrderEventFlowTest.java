package com.github.gcastelo22.tests;

import com.github.gcastelo22.core.BaseTest;
import com.github.gcastelo22.models.OrderEvent;
import com.github.gcastelo22.utils.OrderDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.awaitility.Awaitility;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.json.JSONArray;

public class OrderEventFlowTest extends BaseTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testOrderProcessingFlow_Success() throws Throwable {
    OrderEvent event = OrderDataFactory.createValidOrder();
    String orderId = event.getPayload().getOrderId();
    String orderPayloadJson = objectMapper.writeValueAsString(event);

    LOG.info("STEP 1: PRODUCING EVENT [" + orderId + "]...");
    callUpstash("orders-service-v1", orderPayloadJson);

    LOG.info("STEP 2: WAITING FOR DELIVERY...");
    Awaitility.await()
        .atMost(10, TimeUnit.SECONDS)
        .until(() -> isMessageReceived(orderId));

    LOG.info("STEP 3: VALIDATING JSON SCHEMA CONTRACT...");
    String fullResponse = checkConsumerReceivedMessage();

    JSONObject jsonResponse = new JSONObject(fullResponse);
    JSONArray requests = jsonResponse.getJSONArray("data");
    String lastRequestBody = requests.getJSONObject(0).getString("content");

    validateJsonSchema(lastRequestBody, "/schemas/order-event-schema.json");

    LOG.info("TEST PASSED: Event delivered and contract respected.");
  }

  @Test
  public void testOrderProcessingFlow_NegativeScenario() throws Throwable {
    OrderEvent invalidEvent = OrderDataFactory.createInvalidOrder();
    String orderPayloadJson = objectMapper.writeValueAsString(invalidEvent);

    LOG.info("STEP 1: PRODUCING INVALID EVENT...");
    callUpstash("orders-service-v1", orderPayloadJson);

    LOG.info("STEP 2: CONFIRMING NON-DELIVERY...");
    Thread.sleep(5000);

    String receivedMessages = checkConsumerReceivedMessage();
    Assert.assertFalse("Consumer history should not contain null ID",
        receivedMessages.contains("\"order_id\":null"));

    LOG.info("NEGATIVE TEST PASSED.");
  }
}