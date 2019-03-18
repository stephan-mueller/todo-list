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

import de.openknowledge.projects.todolist.rest.domain.todo.TodoValidationErrorCodes;
import de.openknowledge.projects.todolist.rest.infrastructure.domain.value.AbstractValueObject;
import de.openknowledge.projects.todolist.rest.domain.todo.DueDateAdapter;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Abstract to-do.
 */
public abstract class AbstractTodo extends AbstractValueObject {

  @Schema(example = "clean fridge", required = true, minLength = 1, maxLength = 80)
  @NotNull(payload = TodoValidationErrorCodes.TitleIsNull.class)
  @Size(min = 1, max = 80, payload = TodoValidationErrorCodes.InvalidTitleSize.class)
  private String title;

  @Schema(example = "It's a mess", maxLength = 500)
  @Size(max = 500, payload = TodoValidationErrorCodes.DescriptionTooLong.class)
  private String description;

  @Schema(example = "2018-01-01T12:34:56.000Z", required = true, format = "date-time")
  @NotNull(payload = TodoValidationErrorCodes.DueDateIsNull.class)
  @XmlJavaTypeAdapter(DueDateAdapter.class)
  private LocalDateTime dueDate;

  @Schema(example = "false", required = true)
  @NotNull(payload = TodoValidationErrorCodes.DoneIsNull.class)
  private Boolean done;

  public AbstractTodo() {
    super();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(final LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public Boolean getDone() {
    return done;
  }

  public void setDone(final Boolean done) {
    this.done = done;
  }

  @Override
  protected Object[] values() {
    return new Object[]{title, description, dueDate, done};
  }
}
