/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.projects.todolist.rest.infrastructure.microprofiles.metrics;

import de.openknowledge.projects.todolist.rest.test.IntegrationTestUtil;

import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import io.restassured.RestAssured;

/**
 * Integration test for the MP-Metrics API.
 */
public class MetricsIT {

  private String uri = IntegrationTestUtil.getMetricsURI();

  @Test
  public void getApplicationMetrics() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(UriBuilder.fromUri(uri).path("application").build())
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());
  }

  @Test
  public void getBaseMetrics() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(UriBuilder.fromUri(uri).path("base").build())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Response.Status.OK.getStatusCode());
  }

  @Test
  public void getVendorMetrics() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(UriBuilder.fromUri(uri).path("vendor").build())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Response.Status.OK.getStatusCode());
  }
}