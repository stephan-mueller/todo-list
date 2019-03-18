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
package de.openknowledge.projects.todolist.graphql.infrastructure.domain.entity;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Exception to be thrown when an entity is not found.
 */
public class EntityNotFoundException extends Exception {

  private final Long identifier;

  /**
   * @param identifier the given identifier.
   * @throws NullPointerException
   */
  public EntityNotFoundException(final Long identifier) {
    super();
    this.identifier = notNull(identifier, "identifier must not be null");
  }

  @Override
  public String getMessage() {
    return String.format("Entity %s was not found", identifier);
  }

  public Long getIdentifier() {
    return identifier;
  }
}
