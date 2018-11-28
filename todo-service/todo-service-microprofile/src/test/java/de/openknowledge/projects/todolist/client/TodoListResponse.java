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

import de.openknowledge.projects.todolist.microprofile.domain.todo.Todo;
import de.openknowledge.projects.todolist.microprofile.infrastructure.domain.value.AbstractValueObject;

import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;

/**
 * A DTO that represents a {@link Todo} in a list.
 */
public class TodoListResponse extends AbstractValueObject {

  private Long id;

  private String title;

  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime dueDate;

  private boolean done;

  public TodoListResponse() {
    super();
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
  protected Object[] values() {
    return new Object[] {id, title, dueDate, done};
  }

  @Override
  public String toString() {
    return "TodoListResponse{" +
           "id=" + id +
           ", title='" + title + '\'' +
           ", dueDate=" + dueDate +
           ", done=" + done +
           '}';
  }
}
