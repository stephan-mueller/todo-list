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

import de.openknowledge.projects.todolist.microprofile.infrastructure.domain.value.AbstractValueObject;
import de.openknowledge.projects.todolist.microprofile.infrastructure.rest.error.ApplicationError;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO that represents an error response.
 */
public class ErrorResponse implements Serializable {

  private String code;

  private String message;

  public ErrorResponse() {
    super();
  }

  public ErrorResponse(final ApplicationError error, final String message) {
    this();
    this.code = notNull(error, "errorCode must not be null").getCode();
    this.message = notNull(message, "message must not be null");
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse that = (ErrorResponse) o;
    return Objects.equals(code, that.code) &&
           Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message);
  }

  @Override
  public String toString() {
    return "ApplicationErrorDTO{"
           + "code='" + code + '\''
           + ", message='" + message + '\''
           + '}';
  }
}
