package com.github.gcastelo22.core;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;

public class BaseTest {

  protected final String QSTASH_TOKEN = System.getenv("QSTASH_TOKEN");
  protected final String QSTASH_URL = "https://qstash-eu-central-1.upstash.io/v2/publish/";
  protected final String WEBHOOK_API_URL = "https://webhook.site/token/" + System.getenv("WEBHOOK_ID") + "/requests?sorting=newest";

  public static final Logger LOG = Logger.getLogger(BaseTest.class.getName());

  public String callUpstash(String topicName, String payload) throws Exception {
    Client client = Client.create(new DefaultClientConfig());
    WebResource webResource = client.resource(UriBuilder.fromUri(QSTASH_URL + topicName).build());

    ClientResponse response = webResource
        .header("Authorization", "Bearer " + QSTASH_TOKEN)
        .type("application/json")
        .post(ClientResponse.class, payload);

    String entity = response.getEntity(String.class);
    LOG.info("QStash Response Status: " + response.getStatus());

    if (response.getStatus() != 201 && response.getStatus() != 200) {
      throw new RuntimeException("HTTP Error: " + response.getStatus() + " - " + entity);
    }
    return entity;
  }

  public String checkConsumerReceivedMessage() throws Exception {
    Client client = Client.create(new DefaultClientConfig());
    WebResource webResource = client.resource(UriBuilder.fromUri(WEBHOOK_API_URL).build());

    ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
    return response.getEntity(String.class);
  }

  public boolean isMessageReceived(String orderId) {
    try {
      String content = checkConsumerReceivedMessage();
      return content.contains(orderId);
    } catch (Exception e) {
      LOG.warning("Could not check consumer: " + e.getMessage());
      return false;
    }
  }

  public void validateJsonSchema(String jsonBody, String schemaPath) {
    try (InputStream inputStream = getClass().getResourceAsStream(schemaPath)) {
      if (inputStream == null) {
        throw new RuntimeException("Schema file not found at: " + schemaPath);
      }
      JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
      Schema schema = SchemaLoader.load(rawSchema);
      schema.validate(new JSONObject(jsonBody));
    } catch (Exception e) {
      throw new RuntimeException("Contract Violation: " + e.getMessage());
    }
  }
}