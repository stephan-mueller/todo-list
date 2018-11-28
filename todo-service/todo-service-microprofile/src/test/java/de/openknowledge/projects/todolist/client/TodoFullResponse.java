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

import de.openknowledge.projects.todolist.microprofile.application.todo.TodoListDTO;
import de.openknowledge.projects.todolist.microprofile.domain.todo.Todo;

import java.util.Objects;

import javax.json.bind.annotation.JsonbNillable;

/**
 * A DTO that represents a full {@link Todo}.
 */
@JsonbNillable
public class TodoFullResponse extends TodoListDTO {

  private String description;

  public TodoFullResponse() {
    super();
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    TodoFullResponse that = (TodoFullResponse) o;
    return Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), description);
  }

  @Override
  public String toString() {
    return "TodoFullResponse{" +
           "id=" + getId() +
           ", title='" + getTitle() + '\'' +
           ", description='" + description + '\'' +
           ", dueDate=" + getDueDate() +
           ", done=" + isDone() +
           '}';
  }
}
