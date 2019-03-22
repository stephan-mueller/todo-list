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

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.websocket.domain.todo.Todo;
import de.openknowledge.projects.todolist.websocket.infrastructure.domain.value.AbstractValueObject;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;

/**
 * A DTO that represents a {@link Todo} in a list.
 */
public class TodoListDTO extends AbstractValueObject {

  private Long id;

  private String title;

  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime dueDate;

  private Boolean done;

  public TodoListDTO() {
    super();
  }

  public TodoListDTO(final Todo todo) {
    this();
    notNull(todo, "todo must not be null");
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.dueDate = todo.getDueDate();
    this.done = todo.getDone();
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

  public Boolean getDone() {
    return done;
  }

  @Override
  protected Object[] values() {
    return new Object[] {id, title, dueDate, done};
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
