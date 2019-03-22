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

import static de.openknowledge.projects.todolist.websocket.application.todo.TodoResponseMessageType.CREATE_TODO_RESPONSE;
import static de.openknowledge.projects.todolist.websocket.application.todo.TodoResponseMessageType.GET_TODO_RESPONSE;
import static de.openknowledge.projects.todolist.websocket.application.todo.TodoResponseMessageType.GET_TODOS_RESPONSE;
import static de.openknowledge.projects.todolist.websocket.infrastructure.websocket.ErrorResponseMessageType.APPLICATION_ERROR;
import static de.openknowledge.projects.todolist.websocket.infrastructure.websocket.ErrorResponseMessageType.VALIDATION_ERROR;

import de.openknowledge.projects.todolist.websocket.infrastructure.domain.entity.EntityNotFoundException;
import de.openknowledge.projects.todolist.websocket.infrastructure.error.ApplicationErrorDTO;
import de.openknowledge.projects.todolist.websocket.infrastructure.validation.ValidationErrorDTO;
import de.openknowledge.projects.todolist.websocket.infrastructure.websocket.Message;
import de.openknowledge.projects.todolist.websocket.infrastructure.websocket.MessageEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * A websocket server endpoint for todo messages.
 */
@ServerEndpoint(value = "/todos", decoders = {TodoMessageDecoder.class}, encoders = {MessageEncoder.class})
public class TodoEndpoint {

  private static final Logger LOG = LoggerFactory.getLogger(TodoEndpoint.class);

  @Inject
  private TodoApplicationService service;

  @OnClose
  public void onClose(final Session session) {
    LOG.info("Close session with id {}", session.getId());
  }

  @OnMessage
  @ActivateRequestContext
  public void onMessage(final Message message, final Session session) throws IOException, EncodeException, EntityNotFoundException {
    LOG.info("Received {} message in session {}", message.getType(), session.getId());

    switch ((TodoRequestMessageType) message.getType()) {
      case CREATE_TODO: {
        TodoFullDTO todo = service.createTodo((NewTodo) message.getData());
        session.getBasicRemote().sendObject(new Message<>(CREATE_TODO_RESPONSE, todo));
        break;
      }
      case DELETE_TODO: {
        service.deleteTodo((TodoIdentifier) message.getData());
        break;
      }
      case GET_TODO: {
        TodoFullDTO todo = service.getTodo((TodoIdentifier) message.getData());
        session.getBasicRemote().sendObject(new Message<>(GET_TODO_RESPONSE, todo));
        break;
      }
      case GET_TODOS: {
        List<TodoListDTO> todos = service.getTodos();
        session.getBasicRemote().sendObject(new Message<>(GET_TODOS_RESPONSE, todos));
        break;
      }
      case UPDATE_TODO: {
        service.updateTodo((ModifiedTodo) message.getData());
        break;
      }
    }
  }

  @OnError
  public void onError(final Session session, final Throwable throwable) throws IOException, EncodeException {
    if (throwable instanceof ConstraintViolationException) {
      handleValidationError(session, (ConstraintViolationException) throwable);
    } else if (throwable instanceof EntityNotFoundException) {
      handleTodoNotFound(session, (EntityNotFoundException) throwable);
    } else {
      handleUnknownApplicationError(session, throwable);
    }
  }

  private void handleValidationError(final Session session, final ConstraintViolationException e) throws IOException, EncodeException {
    List<ValidationErrorDTO> errors = e.getConstraintViolations()
        .stream()
        .map(ValidationErrorDTO::new)
        .collect(Collectors.toList());
    session.getBasicRemote().sendObject(new Message(VALIDATION_ERROR, errors));
  }

  private void handleTodoNotFound(final Session session, final EntityNotFoundException e) throws IOException, EncodeException {
    LOG.warn("Todo with identifier {} not found", e.getIdentifier());
    ApplicationErrorDTO error = new ApplicationErrorDTO(() -> "TODO_NOT_FOUND", String.format("Todo with identifier %s not found", e.getIdentifier()));
    session.getBasicRemote().sendObject(new Message<>(APPLICATION_ERROR, error));
  }

  private void handleUnknownApplicationError(Session session, Throwable throwable) throws IOException, EncodeException {
    LOG.error(throwable.getMessage(), throwable.getCause());
    ApplicationErrorDTO error = new ApplicationErrorDTO(() -> "UNKNOWN", "An unknown error occurred");
    session.getBasicRemote().sendObject(new Message<>(APPLICATION_ERROR, error));
  }

  @OnOpen
  public void onOpen(final Session session) {
    LOG.info("Open session with id {}", session.getId());
  }
}
