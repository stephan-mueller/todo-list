/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
package de.openknowledge.projects.todolist.microprofile.infrastructure.health;

import static org.junit.Assert.assertEquals;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@Ignore
public class HealthIT {

  private JsonArray servicesStates;

  private static HashMap<String, String> dataWhenServicesUP;

  static {
    dataWhenServicesUP = new HashMap<>();

    dataWhenServicesUP.put("System", "UP");
  }

  @Test
  public void testIfServicesAreUp() {
    servicesStates = connectToHealthEnpoint(200);
    checkStates(dataWhenServicesUP, servicesStates);
  }

  private void checkStates(HashMap<String, String> testData, JsonArray servStates) {
    testData.forEach((service, expectedState) -> assertEquals("The state of " + service + " service is not matching.", expectedState, getActualState(service, servStates)));
  }

  private static String port;
  private static String baseUrl;
  private final static String HEALTH_ENDPOINT = "health";
  public static final String INV_MAINTENANCE_FALSE = "io_openliberty_guides_inventory_inMaintenance\":false";
  public static final String INV_MAINTENANCE_TRUE = "io_openliberty_guides_inventory_inMaintenance\":true";

  static {
    port = "9080"; //System.getProperty("liberty.test.port");
    baseUrl = "http://localhost:" + port + "/";
  }

  public static JsonArray connectToHealthEnpoint(int expectedResponseCode) {
    String healthURL = baseUrl + HEALTH_ENDPOINT;
    Client client = ClientBuilder.newClient().register(JsrJsonpProvider.class);
    Response response = client.target(healthURL).request().get();
    assertEquals("Response code is not matching " + healthURL, expectedResponseCode, response.getStatus());
    JsonArray servicesStates = response.readEntity(JsonObject.class).getJsonArray("checks");
    response.close();
    client.close();
    return servicesStates;
  }

  public static String getActualState(String service, JsonArray servicesStates) {
    String state = "";
    for (Object obj : servicesStates) {
      if (obj instanceof JsonObject) {
        if (service.equals(((JsonObject) obj).getString("name"))) {
          state = ((JsonObject) obj).getString("state");
        }
      }
    }
    return state;
  }
}
