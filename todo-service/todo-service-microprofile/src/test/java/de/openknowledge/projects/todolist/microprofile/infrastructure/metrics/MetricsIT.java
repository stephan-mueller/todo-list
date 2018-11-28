/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
package de.openknowledge.projects.todolist.microprofile.infrastructure.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Ignore
public class MetricsIT {

  private static String httpPort;
  private static String httpsPort;
  private static String baseHttpUrl;
  private static String baseHttpsUrl;

  private List<String> metrics;
  private Client client;

  private final String INVENTORY_HOSTS = "inventory/systems";
  private final String INVENTORY_HOSTNAME = "inventory/systems/localhost";
  private final String METRICS_APPLICATION = "metrics/application";

  @BeforeClass
  public static void oneTimeSetup() {
    httpPort = System.getProperty("liberty.test.port");
    httpsPort = System.getProperty("liberty.https.port");
    baseHttpUrl = "http://localhost:" + httpPort + "/";
    baseHttpsUrl = "https://localhost:" + httpsPort + "/";
  }

  @Before
  public void setup() {
    client = ClientBuilder.newClient();
    client.register(JsrJsonpProvider.class);
  }

  @After
  public void teardown() {
    client.close();
  }

  @Test
  public void testSuite() {
    this.testPropertiesRequestTimeMetric();
    this.testInventoryAccessCountMetric();
    this.testInventorySizeGaugeMetric();
  }

  public void testPropertiesRequestTimeMetric() {
    connectToEndpoint(baseHttpUrl + INVENTORY_HOSTNAME);
    metrics = getMetrics();
    for (String metric : metrics) {
      if (metric.startsWith(
          "application:inventory_properties_request_time_rate_per_second")) {
        float seconds = Float.parseFloat(metric.split(" ")[1]);
        assertTrue(4 > seconds);
      }
    }
  }

  public void testInventoryAccessCountMetric() {
    connectToEndpoint(baseHttpUrl + INVENTORY_HOSTS);
    metrics = getMetrics();
    for (String metric : metrics) {
      if (metric.startsWith("application:inventory_access_count")) {
        assertTrue(
            1 == Character.getNumericValue(metric.charAt(metric.length() - 1)));
      }
    }
  }

  public void testInventorySizeGaugeMetric() {
    metrics = getMetrics();
    for (String metric : metrics) {
      if (metric.startsWith("application:inventory_size_guage")) {
        assertTrue(
            1 == Character.getNumericValue(metric.charAt(metric.length() - 1)));
      }
    }

  }

  public void connectToEndpoint(String url) {
    Response response = this.getResponse(url);
    this.assertResponse(url, response);
    response.close();
  }

  private List<String> getMetrics() {
    String usernameAndPassword = "admin" + ":" + "admin";
    String authorizationHeaderValue = "Basic " + Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
    Response metricsResponse = client.target(baseHttpsUrl + METRICS_APPLICATION)
        .request(MediaType.TEXT_PLAIN)
        .header("Authorization", authorizationHeaderValue)
        .get();

    BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) metricsResponse.getEntity()));
    List<String> result = new ArrayList<String>();
    try {
      String input;
      while ((input = br.readLine()) != null) {
        result.add(input);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }

    metricsResponse.close();
    return result;
  }

  private Response getResponse(String url) {
    return client.target(url).request().get();
  }

  private void assertResponse(String url, Response response) {
    assertEquals("Incorrect response code from " + url, 200, response.getStatus());
  }
}
