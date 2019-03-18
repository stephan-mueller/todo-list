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

import static de.openknowledge.projects.todolist.graphql.application.todo.TodoEvent.ChangeType.CREATED;
import static de.openknowledge.projects.todolist.graphql.application.todo.TodoEvent.ChangeType.DELETED;
import static de.openknowledge.projects.todolist.graphql.application.todo.TodoEvent.ChangeType.UPDATED;

import de.openknowledge.projects.todolist.graphql.domain.todo.Todo;

import java.time.LocalDateTime;

public class TodoEvent extends TodoFullDTO {

  public enum ChangeType {

    CREATED,

    DELETED,

    UPDATED;
  }

  public static TodoEvent newCreatedTodo(final Todo todo) {
    return new TodoEvent(todo, CREATED);
  }

  public static TodoEvent newDeletedTodo(final Todo todo) {
    return new TodoEvent(todo, DELETED);
  }

  public static TodoEvent newUpdatedTodo(final Todo todo) {
    return new TodoEvent(todo, UPDATED);
  }

  private ChangeType changeType;

  private LocalDateTime changeDate;

  private TodoEvent(final Todo todo, final ChangeType changeType) {
    super(todo);
    this.changeType = changeType;
    this.changeDate = LocalDateTime.now();
  }

  public ChangeType getChangeType() {
    return changeType;
  }

  public LocalDateTime getChangeDate() {
    return changeDate;
  }

  @Override
  protected Object[] values() {
    return new Object[]{getId(), getTitle(), getDueDate(), getDone(), changeType, changeDate};
  }

  @Override
  public String toString() {
    return "TodoEvent{" +
           "id=" + getId() +
           ", title='" + getTitle() + '\'' +
           ", dueDate=" + getDueDate() +
           ", done=" + getDone() +
           ", changeType=" + changeType +
           ", changeDate=" + changeDate +
           "} " + super.toString();
  }
}
