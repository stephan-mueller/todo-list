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

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.rest.domain.todo.Todo;
import de.openknowledge.projects.todolist.rest.infrastructure.domain.value.AbstractValueObject;
import de.openknowledge.projects.todolist.rest.domain.todo.DueDateAdapter;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A DTO that represents a {@link Todo} in a list.
 */
@Schema
public class TodoListDTO extends AbstractValueObject {

  @Schema(example = "1000")
  private Long id;

  @Schema(example = "clean fridge")
  private String title;

  @Schema(example = "2018-01-01T12:34:56.000Z")
  @XmlJavaTypeAdapter(DueDateAdapter.class)
  private LocalDateTime dueDate;

  @Schema(example = "false")
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

  public boolean isDone() {
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
