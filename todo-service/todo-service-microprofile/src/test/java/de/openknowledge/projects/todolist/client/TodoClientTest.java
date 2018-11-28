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
package de.openknowledge.projects.todolist.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

/**
 * Test class for  the client {@link TodoClient}
 */
@Ignore
public class TodoClientTest {

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().dynamicPort());

  private TodoClient client;

  @Before
  public void setUp() {
    String url = String.format("http://localhost:%s", wireMockRule.port());
    client = new TodoClient(url);
  }

  @Test
  public void createTodoShouldReturnStatusCode201AndTodoResponse() {
    stubFor(post(urlEqualTo("/api/todos"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"title\": \"clean fridge\",\n"
                                         + "  \"description\": \"It's a mess\",\n"
                                         + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                         + "  \"done\": false\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(CREATED.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                                .withBody("{"
                                          + "\"id\":\"1000\","
                                          + "\"title\": \"clean fridge\",\n"
                                          + "\"description\": \"It's a mess\",\n"
                                          + "\"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                          + "\"done\": false\n"
                                          + "}")));

    TodoFullResponse todo = client.createTodo(TestTodos.newTodo());
    assertThat(todo).isNotNull();
  }

  @Test
  public void createTodoShouldReturnStatusCode400() {
    stubFor(post(urlEqualTo("/api/todos"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"done\": false\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(BAD_REQUEST.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> client.createTodo(TestTodos.invalidTodo()))
        .withNoCause();
  }

  @Test
  public void createTodoShouldReturnStatusCode5xx() {
    stubFor(post(urlEqualTo("/api/todos"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"title\": \"clean fridge\",\n"
                                         + "  \"description\": \"It's a mess\",\n"
                                         + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                         + "  \"done\": false\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(INTERNAL_SERVER_ERROR.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalStateException()
        .isThrownBy(() -> client.createTodo(TestTodos.newTodo()))
        .withNoCause();
  }

  @Test
  public void deleteTodoShouldReturnStatusCode204() throws Exception {
    stubFor(delete(urlEqualTo("/api/todos/1"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(NO_CONTENT.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    client.deleteTodo(1L);
  }

  @Test
  public void deleteTodoShouldReturnStatusCode404() {
    stubFor(delete(urlEqualTo("/api/todos/999"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(NOT_FOUND.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatThrownBy(() -> client.deleteTodo(999L))
        .isInstanceOf(TodoNotFoundException.class)
        .withFailMessage("Todo 999 was not found");
  }

  @Test
  public void deleteTodoShouldReturnStatusCode5xx() {
    stubFor(delete(urlEqualTo("/api/todos/1"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(INTERNAL_SERVER_ERROR.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalStateException()
        .isThrownBy(() -> client.deleteTodo(1L))
        .withNoCause();
  }

  @Test
  public void getTodoShouldReturnStatusCode200AndTodoResponse() throws Exception {
    stubFor(get(urlEqualTo("/api/todos/1"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(OK.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                                .withBody("{"
                                          + "\"id\":\"1000\","
                                          + "\"title\": \"clean fridge\",\n"
                                          + "\"description\": \"It's a mess\",\n"
                                          + "\"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                          + "\"done\": false\n"
                                          + "}")));

    TodoFullResponse todo = client.getTodo(1L);
    assertThat(todo).isNotNull();
  }

  @Test
  public void getTodoShouldReturnStatusCode404() {
    stubFor(get(urlEqualTo("/api/todos/999"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(NOT_FOUND.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatThrownBy(() -> client.getTodo(999L))
        .isInstanceOf(TodoNotFoundException.class)
        .withFailMessage("Todo 999 was not found");
  }

  @Test
  public void getTodoShouldReturnStatusCode5xx() {
    stubFor(get(urlEqualTo("/api/todos/1"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(INTERNAL_SERVER_ERROR.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalStateException()
        .isThrownBy(() -> client.getTodo(1L))
        .withNoCause();
  }

  @Test
  public void getTodosShouldReturnStatusCode200AndListOfTodoResponses() {
    stubFor(get(urlEqualTo("/api/todos"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(OK.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                                .withBody("{"
                                          + "\"id\":\"1000\","
                                          + "\"title\": \"clean fridge\",\n"
                                          + "\"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                          + "\"done\": false\n"
                                          + "}")));

    List<TodoListResponse> todos = client.getTodos();
    assertThat(todos).hasSize(1);
  }

  @Test
  public void getTodosShouldReturnStatusCode5xx() {
    stubFor(get(urlEqualTo("/api/todos"))
                .withHeader(ACCEPT, equalTo(APPLICATION_JSON))
                .willReturn(aResponse()
                                .withStatus(INTERNAL_SERVER_ERROR.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalStateException()
        .isThrownBy(() -> client.getTodos())
        .withNoCause();
  }

  @Test
  public void updateTodoShouldReturnStatusCode204() throws Exception {
    stubFor(put(urlEqualTo("/api/todos/1"))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"title\": \"clean fridge\",\n"
                                         + "  \"description\": \"\",\n"
                                         + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                         + "  \"done\": true\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(NO_CONTENT.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    client.updateTodo(1L, TestTodos.modifiedTodo());
  }

  @Test
  public void updateTodoShouldReturnStatusCode400() {
    stubFor(put(urlEqualTo("/api/todos/1"))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"done\": true\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(NOT_FOUND.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> client.updateTodo(1L, TestTodos.invalidTodo()))
        .withNoCause();
  }

  @Test
  public void updateTodoShouldReturnStatusCode404() {
    stubFor(put(urlEqualTo("/api/todos/999"))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"title\": \"clean fridge\",\n"
                                         + "  \"description\": \"\",\n"
                                         + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                         + "  \"done\": true\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(NOT_FOUND.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatThrownBy(() -> client.updateTodo(999L, TestTodos.modifiedTodo()))
        .isInstanceOf(TodoNotFoundException.class)
        .withFailMessage("Todo 999 was not found");
  }

  @Test
  public void updateTodoShouldReturnStatusCode5xx() {
    stubFor(put(urlEqualTo("/api/todos/1"))
                .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
                .withRequestBody(equalTo("{\n"
                                         + "  \"title\": \"clean fridge\",\n"
                                         + "  \"description\": \"\",\n"
                                         + "  \"dueDate\": \"2018-01-01T12:34:56Z\",\n"
                                         + "  \"done\": true\n"
                                         + "}"))
                .willReturn(aResponse()
                                .withStatus(INTERNAL_SERVER_ERROR.getStatusCode())
                                .withHeader(CONTENT_TYPE, APPLICATION_JSON)));

    assertThatIllegalStateException()
        .isThrownBy(() -> client.updateTodo(1L, TestTodos.modifiedTodo()))
        .withNoCause();
  }
}
