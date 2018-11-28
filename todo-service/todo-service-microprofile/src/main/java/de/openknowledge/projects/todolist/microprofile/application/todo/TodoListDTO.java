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

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.microprofile.domain.todo.Todo;
import de.openknowledge.projects.todolist.microprofile.infrastructure.domain.value.AbstractValueObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.json.bind.annotation.JsonbDateFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * A DTO that represents a {@link Todo} in a list.
 */
public class TodoListDTO implements Serializable {

  @ApiModelProperty
  private Long id;

  @ApiModelProperty
  private String title;

  @ApiModelProperty
  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime dueDate;

  @ApiModelProperty
  private boolean done;

  public TodoListDTO() {
    super();
  }

  public TodoListDTO(final Todo todo) {
    this();
    notNull(todo, "todo must not be null");
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.dueDate = todo.getDueDate();
    this.done = todo.isDone();
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public boolean isDone() {
    return done;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TodoListDTO that = (TodoListDTO) o;
    return done == that.done &&
           Objects.equals(id, that.id) &&
           Objects.equals(title, that.title) &&
           Objects.equals(dueDate, that.dueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, dueDate, done);
  }

  @Override
  public String toString() {
    return "TodoListDTO{" +
           "id=" + id +
           ", title='" + title + '\'' +
           ", dueDate=" + dueDate +
           ", done=" + done +
           '}';
  }
}
