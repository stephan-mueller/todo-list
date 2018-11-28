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

import de.openknowledge.projects.todolist.microprofile.domain.todo.Todo;
import de.openknowledge.projects.todolist.microprofile.domain.todo.TodoRepository;
import de.openknowledge.projects.todolist.microprofile.infrastructure.domain.entity.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A resource that provides access to the {@link de.openknowledge.projects.todolist.microprofile.domain.todo.Todo} entity.
 */
@Path("todos")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Api(description = "Todo operations", tags = "Todo")
public class TodoResource {

  private static final Logger LOG = LoggerFactory.getLogger(TodoResource.class);

  @Inject
  private TodoRepository repository;

  @POST
  @Transactional
  @ApiOperation(value = "Create a new todo")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Todo created", response = TodoFullDTO.class),
      @ApiResponse(code = 400, message = "Invalid request data")})
  public Response createTodo(@ApiParam(value = "new todo") @Valid final NewTodo newTodo) {
    LOG.info("Create todo {}", newTodo);

    Todo todo = Todo.newBuilder()
        .withTitle(newTodo.getTitle())
        .withDescription(newTodo.getDescription())
        .withDueDate(newTodo.getDueDate())
        .setDone(newTodo.isDone())
        .build();

    repository.create(todo);

    TodoFullDTO createdTodo = new TodoFullDTO(todo);

    LOG.info("Todo created {}", createdTodo);

    return Response.status(Status.CREATED).entity(createdTodo).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @ApiOperation(value = "Delete a todo")
  @ApiResponses({
      @ApiResponse(code = 204, message = "Todo deleted"),
      @ApiResponse(code = 404, message = "Todo with given id does not exist")})
  public Response deleteTodo(
      @ApiParam(value = "todo identifier") @PathParam("id") @Min(1) @Max(10000) final Long todoId) {
    LOG.info("Delete todo with id {}", todoId);

    try {
      Todo foundTodo = repository.find(todoId);

      repository.delete(foundTodo);

      LOG.info("Todo deleted");

      return Response.status(Status.NO_CONTENT).build();
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/{id}")
  @ApiOperation(value = "Find todo by id")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Successful retrieval of requested todo", response = TodoFullDTO.class),
      @ApiResponse(code = 404, message = "Todo with given id does not exist")})
  public Response getTodo(
      @ApiParam(value = "todo identifier") @PathParam("id") @Min(1) @Max(10000) final Long todoId) {
    LOG.info("Find todo with id {}", todoId);

    try {
      Todo foundTodo = repository.find(todoId);
      TodoFullDTO todo = new TodoFullDTO(foundTodo);

      return Response.status(Status.OK).entity(todo).build();
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @GET
  @ApiOperation(value = "Find all todos")
  @ApiResponses(@ApiResponse(code = 200, message = "Successful retrieval of todos", response = TodoFullDTO.class, responseContainer = "List"))
  public Response getTodos() {
    LOG.info("Find all todos");

    List<TodoListDTO> todos = repository.findAll()
        .stream()
        .map(TodoListDTO::new)
        .collect(Collectors.toList());

    LOG.info("Found {} todos", todos.size());

    if (todos.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.status(Status.OK)
        .entity(new GenericEntity<List<TodoListDTO>>(todos) {
        }).build();
  }

  @PUT
  @Path("/{id}")
  @Transactional
  @ApiOperation(value = "Update a todo")
  @ApiResponses({
      @ApiResponse(code = 204, message = "Todo updated"),
      @ApiResponse(code = 400, message = "Invalid request data"),
      @ApiResponse(code = 404, message = "Todo with given id does not exist")})
  public Response updateTodo(@ApiParam(value = "todo identifier") @PathParam("id") @Min(1) @Max(10000) final Long todoId,
                             @ApiParam(value = "modified todo") @Valid final ModifiedTodo modifiedTodo) {
    LOG.info("Update todo with id {} ({})", todoId, modifiedTodo);

    try {
      Todo foundTodo = repository.find(todoId);

      foundTodo.updateTodo(modifiedTodo.getTitle(), modifiedTodo.getDescription(), modifiedTodo.getDueDate(), modifiedTodo.isDone());

      Todo updatedTodo = repository.update(foundTodo);

      LOG.info("Todo updated {}", updatedTodo);

      return Response.status(Status.NO_CONTENT).build();
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }
}
