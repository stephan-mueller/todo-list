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
package de.openknowledge.projects.todolist.rest.infrastructure.web.cors;

import de.openknowledge.projects.todolist.rest.test.IntegrationTestUtil;

import org.hamcrest.Matchers;
import org.jboss.resteasy.spi.CorsHeaders;
import org.junit.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import io.restassured.RestAssured;

/**
 * Integration test for the CORS filter {@link CustomCorsFilter}.
 */
public class CustomCorsFilterIT {

  private static String uri = IntegrationTestUtil.getBaseURI();

  @Test
  public void checkCorsHeader() {
    RestAssured.given()
        .header(CorsHeaders.ORIGIN, "localhost:8080")
        .when()
        .options(UriBuilder.fromUri(uri).path("api").build())
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept")
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, Matchers.notNullValue())
        .header(CorsHeaders.ACCESS_CONTROL_MAX_AGE, Matchers.notNullValue());
  }
}