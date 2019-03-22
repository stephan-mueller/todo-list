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

import de.openknowledge.projects.todolist.websocket.domain.todo.Todo;
import de.openknowledge.projects.todolist.websocket.domain.todo.TodoRepository;
import de.openknowledge.projects.todolist.websocket.infrastructure.domain.entity.EntityNotFoundException;
import de.openknowledge.projects.todolist.websocket.infrastructure.stereotypes.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;

/**
 * Application service that provides access to the {@link Todo} entity.
 */
@Service
@ValidateOnExecution
public class TodoApplicationService {

  private static final Logger LOG = LoggerFactory.getLogger(TodoApplicationService.class);

  @Inject
  private TodoRepository repository;

  @Transactional
  public TodoFullDTO createTodo(@Valid final NewTodo newTodo) {
    LOG.info("Create todo {}", newTodo);

    Todo todo = Todo.newBuilder()
        .withTitle(newTodo.getTitle())
        .withDescription(newTodo.getDescription())
        .withDueDate(newTodo.getDueDate())
        .setDone(newTodo.getDone())
        .build();

    repository.create(todo);

    TodoFullDTO createdTodo = new TodoFullDTO(todo);

    LOG.info("Todo created {}", createdTodo);

    return createdTodo;
  }

  @Transactional
  public void deleteTodo(@Valid final TodoIdentifier identifier) throws EntityNotFoundException {
    LOG.info("Delete todo with identifier {}", identifier);

    Todo foundTodo = repository.find(identifier.getId());

    repository.delete(foundTodo);

    LOG.info("Todo deleted");
  }

  public TodoFullDTO getTodo(@Valid final TodoIdentifier identifier) throws EntityNotFoundException {
    LOG.info("Find todo with identifier {}", identifier);

    Todo foundTodo = repository.find(identifier.getId());
    TodoFullDTO todo = new TodoFullDTO(foundTodo);

    LOG.info("Found todo {}", foundTodo);

    return todo;
  }

  public List<TodoListDTO> getTodos() {
    LOG.info("Find all todos");

    List<TodoListDTO> todos = repository.findAll()
        .stream()
        .map(TodoListDTO::new)
        .collect(Collectors.toList());

    LOG.info("Found {} todos", todos.size());

    return todos;
  }

  @Transactional
  public void updateTodo(@Valid final ModifiedTodo modifiedTodo) throws EntityNotFoundException {
    LOG.info("Update todo with id {} ({})", modifiedTodo.getId(), modifiedTodo);

    Todo foundTodo = repository.find(modifiedTodo.getId());

    foundTodo.updateTodo(modifiedTodo.getTitle(), modifiedTodo.getDescription(), modifiedTodo.getDueDate(), modifiedTodo.getDone());

    Todo updatedTodo = repository.update(foundTodo);

    LOG.info("Todo updated {}", updatedTodo);
  }
}
