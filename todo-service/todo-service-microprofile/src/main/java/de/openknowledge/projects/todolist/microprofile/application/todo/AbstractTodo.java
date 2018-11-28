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

import de.openknowledge.projects.todolist.microprofile.infrastructure.domain.value.AbstractValueObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

/**
 * Abstract to-do.
 */
public class AbstractTodo implements Serializable {

  @NotNull
  @Size(min = 1, max = 80)
  @ApiModelProperty(required = true, example = "")
  private String title;

  @Size(max = 500)
  @ApiModelProperty(required = true, example = "")
  private String description;

  @NotNull
  @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
  @ApiModelProperty(required = true, example = "")
  private LocalDateTime dueDate;

  @ApiModelProperty(required = true, example = "")
  private boolean done;

  public AbstractTodo() {
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
    AbstractTodo that = (AbstractTodo) o;
    return done == that.done &&
           Objects.equals(title, that.title) &&
           Objects.equals(description, that.description) &&
           Objects.equals(dueDate, that.dueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, dueDate, done);
  }
}
