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
package de.openknowledge.projects.todolist.microprofile.application.todo;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;

/**
 * Integration test class for the resource {@link TodoResource}.
 */
public class TodoResourceIT {

  private URI baseURI = UriBuilder.fromUri("http://localhost:9080/todo-service").build();

  @Test
  public void createTodoShouldReturn201() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean fridge\",\n"
              + "  \"description\": \"It's a mess\",\n"
              + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
              + "  \"done\": false\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.CREATED.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/Todo-schema.json"))
        .body("id", Matchers.notNullValue())
        .body("title", Matchers.equalTo("clean fridge"))
        .body("description", Matchers.equalTo("It's a mess"))
        .body("dueDate", Matchers.notNullValue())
        .body("done", Matchers.equalTo(false));
  }

  @Test
  public void createTodoShouldReturn400() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(2));
  }

  @Test
  public void deleteTodoShouldReturn204() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .delete(getSingleItemUri(4L))
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  public void deleteTodoShouldReturn404ForEntityNotFoundException() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .delete(getSingleItemUri(999L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void getTodoShouldReturn200() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.OK.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/Todo-schema.json"))
        .body("id", Matchers.equalTo(1))
        .body("title", Matchers.equalTo("clean fridge"))
        .body("description", Matchers.equalTo("It's a mess"))
        .body("dueDate", Matchers.notNullValue())
        .body("done", Matchers.equalTo(false));
  }

  @Test
  public void getTodoShouldReturn404ForEntityNotFoundException() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(getSingleItemUri(999L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void getTodosShouldReturn200() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.OK.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/Todos-schema.json"))
        .body("size()", Matchers.is(7));
  }

  @Test
  public void updateTodoShouldReturn204() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean bathroom\",\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(2L))
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  public void updateTodoShouldReturn400ForEntityNotFoundException() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(2));
  }

  @Test
  public void updateTodoShouldReturn404ForEntityNotFoundException() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean bathroom\",\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(999L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  private URI getListUri() {
    return UriBuilder.fromUri(baseURI).path("api").path("todos").build();
  }

  private URI getSingleItemUri(final Long todoId) {
    return UriBuilder.fromUri(getListUri()).path("{id}").build(todoId);
  }
}
