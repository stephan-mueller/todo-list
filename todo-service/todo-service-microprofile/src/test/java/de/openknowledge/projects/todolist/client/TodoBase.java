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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.json.bind.annotation.JsonbDateFormat;

/**
 * Base Todo.
 */
public class TodoBase implements Serializable {

  private String title;

  private String description;

  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime dueDate;

  private boolean done;

  public TodoBase() {
    super();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public boolean isDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TodoBase that = (TodoBase) o;
    return done == that.done &&
           Objects.equals(title, that.title) &&
           Objects.equals(description, that.description) &&
           Objects.equals(dueDate, that.dueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, dueDate, done);
  }

  @Override
  public String toString() {
    return "TodoBase{" +
           "title='" + title + '\'' +
           ", description='" + description + '\'' +
           ", dueDate=" + dueDate +
           ", done=" + done +
           '}';
  }
}
