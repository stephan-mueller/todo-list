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

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Exception to be thrown when a todo is not found.
 */
public class TodoNotFoundException extends Exception {

  private final Long identifier;

  /**
   * @param identifier the given identifier.
   * @throws NullPointerException
   */
  public TodoNotFoundException(final Long identifier) {
    super();
    this.identifier = notNull(identifier, "identifier must not be null");
  }

  @Override
  public String getMessage() {
    return String.format("Todo %s was not found", identifier);
  }

  public Long getIdentifier() {
    return identifier;
  }
}
