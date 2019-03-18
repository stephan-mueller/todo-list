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
package de.openknowledge.projects.todolist.graphql.application.todo;

import de.openknowledge.projects.todolist.graphql.domain.todo.Todo;
import de.openknowledge.projects.todolist.graphql.domain.todo.TodoRepository;
import de.openknowledge.projects.todolist.graphql.infrastructure.domain.entity.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

/**
 * GraphQL API Schema hat provides access to the {@link Todo} entity.
 */
@RequestScoped
public class TodoGraphQLApi {

  private static final Logger LOG = LoggerFactory.getLogger(TodoGraphQLApi.class);

  @Inject
  private TodoRepository repository;

  @Transactional
  @GraphQLMutation(name = "createTodo")
  public TodoFullDTO createTodo(@GraphQLArgument(name = "newTodo") @NotNull @Valid final NewTodo newTodo) {
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
  @GraphQLMutation(name = "deleteTodo")
  public boolean deleteTodo(@GraphQLArgument(name = "todoId") final Long todoId) {
    LOG.info("Delete todo with id {}", todoId);

    try {
      Todo foundTodo = repository.find(todoId);

      repository.delete(foundTodo);

      LOG.info("Todo deleted");

      return true;
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return false;
    }
  }

  @GraphQLQuery(name = "todo")
  public TodoFullDTO getTodo(@GraphQLArgument(name = "todoId") final Long todoId) {
    LOG.info("Find todo with id {}", todoId);

    try {
      Todo foundTodo = repository.find(todoId);
      return new TodoFullDTO(foundTodo);
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return null;
    }
  }

  @GraphQLQuery(name = "todos")
  public List<TodoFullDTO> getTodos() {
    LOG.info("Find all todos");

    List<TodoFullDTO> todos = repository.findAll()
        .stream()
        .map(TodoFullDTO::new)
        .collect(Collectors.toList());

    LOG.info("Found {} todos", todos.size());

    return todos;
  }

  @Transactional
  @GraphQLMutation(name = "updateTodo")
  public boolean updateTodo(@GraphQLArgument(name = "todoId") final Long todoId,
                            @GraphQLArgument(name = "modifiedTodo") @NotNull @Valid final ModifiedTodo modifiedTodo) {
    LOG.info("Update todo with id {} ({})", todoId, modifiedTodo);

    try {
      Todo foundTodo = repository.find(todoId);

      foundTodo.updateTodo(modifiedTodo.getTitle(), modifiedTodo.getDescription(), modifiedTodo.getDueDate(), modifiedTodo.getDone());

      Todo updatedTodo = repository.update(foundTodo);

      LOG.info("Todo updated {}", updatedTodo);

      return true;
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return false;
    }
  }
}
