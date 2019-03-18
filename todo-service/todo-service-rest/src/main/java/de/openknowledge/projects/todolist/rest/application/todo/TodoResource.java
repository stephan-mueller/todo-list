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

import de.openknowledge.projects.todolist.rest.domain.todo.Todo;
import de.openknowledge.projects.todolist.rest.domain.todo.TodoRepository;
import de.openknowledge.projects.todolist.rest.infrastructure.domain.entity.EntityNotFoundException;
import de.openknowledge.projects.todolist.rest.infrastructure.error.ApplicationErrorDTO;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.infinispan.jmx.annotations.Parameter;
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

/**
 * A resource that provides access to the {@link Todo} entity.
 */
@Path("todos")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class TodoResource {

  private static final Logger LOG = LoggerFactory.getLogger(TodoResource.class);

  @Inject
  private TodoRepository repository;

  @POST
  @Transactional
  @Operation(description = "Create a new todo")
  @APIResponses({
      @APIResponse(responseCode = "201", description = "Todo created",
          content = @Content(schema = @Schema(implementation = TodoFullDTO.class))),
      @APIResponse(responseCode = "400", description = "Invalid request data",
          content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class)))})
  public Response createTodo(
      @RequestBody(description = "new todo", required = true, content = @Content(schema = @Schema(implementation = NewTodo.class))) @Valid final NewTodo newTodo) {
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

    return Response.status(Status.CREATED).entity(createdTodo).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  @Operation(description = "Delete a todo")
  @APIResponses({
      @APIResponse(responseCode = "204", description = "Todo deleted"),
      @APIResponse(responseCode = "404", description = "Todo with given id does not exist")})
  public Response deleteTodo(@Parameter(description = "todo identifier") @PathParam("id") @Min(1) @Max(10000) final Long todoId) {
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
  @Operation(description = "Find todo by id")
  @APIResponses({
      @APIResponse(responseCode = "200", description = "Successful retrieval of requested todo"),
      @APIResponse(responseCode = "404", description = "Todo with given id does not exist")})
  public Response getTodo(@Parameter(description = "todo identifier") @PathParam("id") @Min(1) @Max(10000) final Long todoId) {
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
  @Operation(description = "Find all todos")
  @APIResponses(@APIResponse(responseCode = "200", description = "Successful retrieval of todos",
      content = @Content(schema = @Schema(implementation = TodoListDTO.class))))
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
  @Operation(description = "Update a todo")
  @APIResponses({
      @APIResponse(responseCode = "204", description = "Todo updated"),
      @APIResponse(responseCode = "400", description = "Invalid request data",
          content = @Content(schema = @Schema(implementation = ApplicationErrorDTO.class))),
      @APIResponse(responseCode = "404", description = "Todo with given id does not exist")})
  public Response updateTodo(@Parameter(description = "todo identifier") @PathParam("id") @Min(1) @Max(10000) final Long todoId,
                             @RequestBody(description = "modified todo", required = true, content = @Content(schema = @Schema(implementation = ModifiedTodo.class))) @Valid final ModifiedTodo modifiedTodo) {
    LOG.info("Update todo with id {} ({})", todoId, modifiedTodo);

    try {
      Todo foundTodo = repository.find(todoId);

      foundTodo.updateTodo(modifiedTodo.getTitle(), modifiedTodo.getDescription(), modifiedTodo.getDueDate(), modifiedTodo.getDone());

      Todo updatedTodo = repository.update(foundTodo);

      LOG.info("Todo updated {}", updatedTodo);

      return Response.status(Status.NO_CONTENT).build();
    } catch (EntityNotFoundException e) {
      LOG.warn("Todo with id {} not found", todoId);
      return Response.status(Status.NOT_FOUND).build();
    }
  }
}
