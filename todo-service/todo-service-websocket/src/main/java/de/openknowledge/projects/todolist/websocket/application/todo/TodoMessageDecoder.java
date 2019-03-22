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
package de.openknowledge.projects.todolist.websocket.application.todo;

import static de.openknowledge.projects.todolist.websocket.application.todo.TodoRequestMessageType.CREATE_TODO;
import static de.openknowledge.projects.todolist.websocket.application.todo.TodoRequestMessageType.DELETE_TODO;
import static de.openknowledge.projects.todolist.websocket.application.todo.TodoRequestMessageType.GET_TODO;
import static de.openknowledge.projects.todolist.websocket.application.todo.TodoRequestMessageType.GET_TODOS;
import static de.openknowledge.projects.todolist.websocket.application.todo.TodoRequestMessageType.UPDATE_TODO;

import de.openknowledge.projects.todolist.websocket.infrastructure.websocket.Message;
import de.openknowledge.projects.todolist.websocket.infrastructure.websocket.MessageEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decoder for incoming todo messages.
 */
public class TodoMessageDecoder implements Decoder.Text<Message> {

  private static final Logger LOG = LoggerFactory.getLogger(MessageEncoder.class);

  public void init(final EndpointConfig config) {
    LOG.trace("initialize ...");
  }

  @Override
  public Message decode(final String s) {
    JsonObject jsonObject = Json.createReader(new StringReader(s)).readObject();
    TodoRequestMessageType type = TodoRequestMessageType.valueOf(jsonObject.getString("type"));
    JsonObject data = jsonObject.getJsonObject("data");

    switch (type) {
      case CREATE_TODO:
        return getCreateTodoMessage(data.toString());
      case DELETE_TODO:
        return getDeleteTodoMessage(data.toString());
      case GET_TODO:
        return getGetTodoMessage(data.toString());
      case GET_TODOS:
        return getGetTodosMessage();
      case UPDATE_TODO:
        return getUpdateTodoMessage(data.toString());
      default:
        return getGetTodosMessage();
    }
  }

  private Message getCreateTodoMessage(final String data) {
    Jsonb jsonb = JsonbBuilder.create();
    NewTodo newTodo = jsonb.fromJson(data, NewTodo.class);
    return new Message<>(CREATE_TODO, newTodo);
  }

  private Message getDeleteTodoMessage(final String data) {
    Jsonb jsonb = JsonbBuilder.create();
    TodoIdentifier identifier = jsonb.fromJson(data, TodoIdentifier.class);
    return new Message<>(DELETE_TODO, identifier);
  }

  private Message getGetTodoMessage(final String data) {
    Jsonb jsonb = JsonbBuilder.create();
    TodoIdentifier identifier = jsonb.fromJson(data, TodoIdentifier.class);
    return new Message<>(GET_TODO, identifier);
  }

  private Message getGetTodosMessage() {
    return new Message<>(GET_TODOS);
  }

  private Message getUpdateTodoMessage(final String data) {
    Jsonb jsonb = JsonbBuilder.create();
    ModifiedTodo modifiedTodo = jsonb.fromJson(data, ModifiedTodo.class);
    return new Message<>(UPDATE_TODO, modifiedTodo);
  }

  @Override
  public boolean willDecode(final String s) {
    try {
      JsonObject jsonObject = Json.createReader(new StringReader(s)).readObject();
      String type = jsonObject.getString("type");
      return TodoRequestMessageType.contains(type);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      return false;
    }
  }

  public void destroy() {
    LOG.trace("destroy ...");
  }
}
