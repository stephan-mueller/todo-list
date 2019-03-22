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

import de.openknowledge.projects.todolist.websocket.domain.todo.TodoValidationErrorCodes;
import de.openknowledge.projects.todolist.websocket.infrastructure.domain.value.AbstractValueObject;

import javax.validation.constraints.NotNull;

public class TodoIdentifier extends AbstractValueObject {

  @NotNull(payload = TodoValidationErrorCodes.IdIsNull.class)
  private Long id;

  public TodoIdentifier() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  @Override
  protected Object[] values() {
    return new Object[]{id};
  }

  @Override
  public String toString() {
    return "TodoIdentifier{" +
           "id=" + id +
           "} " + super.toString();
  }
}
