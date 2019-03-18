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
package de.openknowledge.projects.todolist.graphql.infrastructure.graphql;

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.graphql.infrastructure.validation.ValidationErrorDTO;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

/**
 * A GraphQL error that represents a validation error.
 */
public class GraphQLValidationError implements GraphQLError {

  private final ErrorType errorType = ErrorType.ValidationError;

  private final String message;

  private final List<SourceLocation> locations;

  private final List<Object> path;

  private final Map<String, Object> extensions = new LinkedHashMap<>();

  public GraphQLValidationError(final ConstraintViolationException e, final List<SourceLocation> locations, final List<Object> path) {
    notNull(e, "e must not be null");
    this.locations = notNull(locations, "locations must not be null");
    this.path = notNull(path, "path must not be null");
    this.message = extractMessage(e);
    this.extensions.put("errors", extractValidationErrors(e));
  }

  private String extractMessage(final ConstraintViolationException e) {
    return String.format("Exception while fetching data (%s) : %d constraint violation(s) occurred during method validation", path, e.getConstraintViolations().size());
  }

  private List<ValidationErrorDTO> extractValidationErrors(final ConstraintViolationException e) {
    return e.getConstraintViolations()
        .stream()
        .map(ValidationErrorDTO::new)
        .collect(Collectors.toList());
  }

  @Override
  public ErrorType getErrorType() {
    return errorType;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return Collections.unmodifiableList(locations);
  }

  @Override
  public List<Object> getPath() {
    return Collections.unmodifiableList(path);
  }

  @Override
  public Map<String, Object> getExtensions() {
    return Collections.unmodifiableMap(extensions);
  }
}
