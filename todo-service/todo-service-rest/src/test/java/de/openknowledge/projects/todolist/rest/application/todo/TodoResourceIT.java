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
package de.openknowledge.projects.todolist.rest.application.todo;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.core.util.EntityManagerProvider;

import de.openknowledge.projects.todolist.rest.test.IntegrationTestUtil;

import org.hamcrest.Matchers;
import org.junit.Rule;
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

  private String baseURI = IntegrationTestUtil.getBaseURI();

  private String token;

  @Rule
  public EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

  @Rule
  public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> entityManagerProvider.connection());

  @Test
  @DataSet(strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create-expected.yml", ignoreCols = "CUS_ID")
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
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForEmptyRequestBody() {
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
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForMissingDueDate() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean fridge\",\n"
              + "  \"description\": \"It's a mess\",\n"
              + "  \"done\": false\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForMissingDone() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean fridge\",\n"
              + "  \"description\": \"It's a mess\",\n"
              + "  \"dueDate\": \"2018-01-01T12:34:56Z\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForMissingTitle() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"description\": \"It's a mess\",\n"
              + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
              + "  \"done\": false\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForTooLargeDescription() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean fridge\",\n"
              + "  \"description\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus\",\n"
              + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
              + "  \"done\": false\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForTooLongTitle() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula e\",\n"
              + "  \"description\": \"It's a mess\",\n"
              + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
              + "  \"done\": false\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }
  @Test
  @DataSet(value = "datasets/todos-create.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create.yml")
  public void createTodoShouldReturn400ForTooShortTitle() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"\",\n"
              + "  \"description\": \"It's a mess\",\n"
              + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
              + "  \"done\": false\n"
              + "}")
        .when()
        .post(getListUri())
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-delete.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-delete.yml")
  public void deleteTodoShouldReturn204() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .delete(getSingleItemUri(4L))
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/todos-delete.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-delete.yml")
  public void deleteTodoShouldReturn404ForEntityNotFoundException() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .delete(getSingleItemUri(999L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/todos.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
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
  @DataSet(value = "datasets/todos.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
  public void getTodoShouldReturn404ForEntityNotFoundException() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .when()
        .get(getSingleItemUri(999L))
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode());
  }

  @Test
  @DataSet(value = "datasets/todos.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
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
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update-expected.yml")
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
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForEmptyRequestBody() {
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
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForMissingDueDate() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean bathroom\",\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForMissingDone() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean bathroom\",\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\"\n"
              + "}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForMissingTitle() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForTooLongDescription() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"clean bathroom\",\n"
              + "  \"description\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForTooLongTitle() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula e\",\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
  public void updateTodoShouldReturn400ForTooShortTitle() {
    RestAssured.given()
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body("{\n"
              + "  \"title\": \"\",\n"
              + "  \"description\": \"It's really dirty :(\",\n"
              + "  \"dueDate\": \"2018-01-02T10:30:00Z\",\n"
              + "  \"done\": true\n"
              + "}")
        .when()
        .put(getSingleItemUri(1L))
        .then()
        .contentType(MediaType.APPLICATION_JSON)
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("json/schema/ErrorResponses-schema.json"))
        .body("size()", Matchers.is(1));
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update.yml")
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
