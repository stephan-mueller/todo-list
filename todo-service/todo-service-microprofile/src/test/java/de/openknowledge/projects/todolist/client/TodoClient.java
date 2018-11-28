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

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.apache.commons.lang3.Validate.notNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Default implementation for the remote todo client.
 */
public class TodoClient {

  private static final Logger LOG = LoggerFactory.getLogger(TodoClient.class);

  private final String url;

  public TodoClient(final String url) {
    this.url = notNull(url, "url must not be null");
  }

  private Invocation.Builder newClient(final URI uri) {
    return ClientBuilder.newClient()
        .target(uri)
        .request(MediaType.APPLICATION_JSON);
  }

  private URI getBaseUri() {
    return UriBuilder.fromPath(url).path("api").path("todos").build();
  }

  public TodoFullResponse createTodo(final TodoBase newTodo) {
    LOG.debug("create new todo {}", newTodo);

    Response response = newClient(getBaseUri()).post(Entity.json(newTodo));

    if (BAD_REQUEST.getStatusCode() == response.getStatus()) {
      throw new IllegalArgumentException();
    }

    if (INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus()) {
      throw new IllegalStateException();
    }

    TodoFullResponse createdTodo = response.readEntity(TodoFullResponse.class);

    LOG.debug("Todo {} created", createdTodo);

    return createdTodo;
  }

  public void deleteTodo(final Long todoId) throws TodoNotFoundException {
    LOG.debug("Delete todo {}", todoId);

    URI uri = UriBuilder.fromUri(getBaseUri()).path(todoId.toString()).build();

    Response response = newClient(uri).delete();

    if (BAD_REQUEST.getStatusCode() == response.getStatus()) {
      throw new IllegalArgumentException();
    }

    if (NOT_FOUND.getStatusCode() == response.getStatus()) {
      throw new TodoNotFoundException(todoId);
    }

    if (INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus()) {
      throw new IllegalStateException();
    }

    LOG.debug("Todo {} deleted", todoId);
  }

  public TodoFullResponse getTodo(final Long todoId) throws TodoNotFoundException {
    LOG.debug("Get todo {}", todoId);

    URI uri = UriBuilder.fromUri(getBaseUri()).path(todoId.toString()).build();

    Response response = newClient(uri).get();

    if (BAD_REQUEST.getStatusCode() == response.getStatus()) {
      throw new IllegalArgumentException();
    }

    if (NOT_FOUND.getStatusCode() == response.getStatus()) {
      throw new TodoNotFoundException(todoId);
    }

    if (INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus()) {
      throw new IllegalStateException();
    }

    TodoFullResponse todo = response.readEntity(TodoFullResponse.class);

    LOG.debug("Todo {} received", todoId);

    return todo;
  }

  public List<TodoListResponse> getTodos() {
    LOG.debug("Get todos");

    Response response = newClient(getBaseUri()).get();

    if (INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus()) {
      throw new IllegalStateException();
    }

    List<TodoListResponse> todos = response.readEntity(new GenericType<List<TodoListResponse>>() {
    });

    LOG.debug("Todos {} received", todos.size());

    return todos;
  }

  public void updateTodo(final Long todoId, final TodoBase modifiedTodo) throws TodoNotFoundException {
    LOG.debug("Update todo {}", todoId);

    URI uri = UriBuilder.fromUri(getBaseUri()).path(todoId.toString()).build();

    Response response = newClient(uri).put(Entity.json(modifiedTodo));

    if (BAD_REQUEST.getStatusCode() == response.getStatus()) {
      throw new IllegalArgumentException();
    }

    if (NOT_FOUND.getStatusCode() == response.getStatus()) {
      throw new TodoNotFoundException(todoId);
    }

    if (INTERNAL_SERVER_ERROR.getStatusCode() == response.getStatus()) {
      throw new IllegalStateException();
    }

    LOG.debug("Todo {} updated", todoId);
  }
}
