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
package de.openknowledge.projects.todolist.rest.domain.todo;

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.rest.infrastructure.domain.builder.DefaultBuilder;
import de.openknowledge.projects.todolist.rest.infrastructure.domain.entity.AbstractEntity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An entity that represents a to-do.
 */
@Entity
@Table(name = "TAB_TODO")
public class Todo extends AbstractEntity<Long> {

  public static final String DUE_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
  @TableGenerator(name = "TABLE_GEN", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "TOD_SEQ", initialValue = 1000, allocationSize = 1)
  @Column(name = "TOD_ID", nullable = false)
  private Long id;

  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "TOD_TITLE", nullable = false, length = 80)
  private String title;

  @Size(max = 500)
  @Column(name = "TOD_DESCRIPTION", length = 500)
  private String description;

  @NotNull
  @Column(name = "TOD_DUEDATE", nullable = false)
  private LocalDateTime dueDate;

  @Column(name = "TOD_DONE", nullable = false)
  private boolean done;

  protected Todo() {
    super();
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

  public boolean isDone() {
    return done;
  }

  public void updateTodo(String title, String description, LocalDateTime dueDate, boolean done) {
    this.title = notNull(title,"title must not be null");
    this.description = description;
    this.dueDate = notNull(dueDate,"dueDate must not be null");
    this.done = done;
  }

  public static TodoBuilder newBuilder() {
    return new TodoBuilder();
  }

  /**
   * Builder for the entity {@link Todo}.
   */
  public static class TodoBuilder extends DefaultBuilder<Todo> {

    public TodoBuilder() {
      this.instance.done = false;
    }

    public TodoBuilder setDone(boolean done) {
      this.instance.done = done;
      return this;
    }

    public TodoBuilder withDescription(final String description) {
      this.instance.description = description;
      return this;
    }

    public TodoBuilder withDueDate(final LocalDateTime dueDate) {
      this.instance.dueDate = notNull(dueDate,"dueDate must not be null");
      return this;
    }

    public TodoBuilder withTitle(final String title) {
      this.instance.title = notNull(title,"title must not be null");
      return this;
    }
  }
}
