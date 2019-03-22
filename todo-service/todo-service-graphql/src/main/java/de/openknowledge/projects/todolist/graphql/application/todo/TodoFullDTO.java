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

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.graphql.domain.todo.DueDateAdapter;
import de.openknowledge.projects.todolist.graphql.domain.todo.Todo;
import de.openknowledge.projects.todolist.graphql.infrastructure.domain.value.AbstractValueObject;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.leangen.graphql.annotations.GraphQLId;

/**
 * A DTO that represents a full {@link Todo}.
 */
public class TodoFullDTO extends AbstractValueObject {

  @GraphQLId
  private Long id;

  private String title;

  private String description;

  @XmlJavaTypeAdapter(DueDateAdapter.class)
  private LocalDateTime dueDate;

  private Boolean done;

  public TodoFullDTO() {
    super();
  }

  public TodoFullDTO(final Todo todo) {
    notNull(todo, "todo must not be null");
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.description = todo.getDescription();
    this.dueDate = todo.getDueDate();
    this.done = todo.getDone();
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public Boolean getDone() {
    return done;
  }

  @Override
  protected Object[] values() {
    return new Object[]{id, title, description, dueDate, done};
  }

  @Override
  public String toString() {
    return "TodoFullDTO{" +
           "id=" + id +
           ", title='" + title + '\'' +
           ", description='" + description + '\'' +
           ", dueDate=" + dueDate +
           ", done=" + done +
           "} " + super.toString();
  }
}
