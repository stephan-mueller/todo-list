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

import de.openknowledge.projects.todolist.graphql.infrastructure.error.ApplicationErrorDTO;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.servlet.GraphQLErrorHandler;

/**
 * Custom GraphQL error handler.
 */
public class CustomGraphQLErrorHandler implements GraphQLErrorHandler {

  @Override
  public List<GraphQLError> processErrors(final List<GraphQLError> errors) {
    List<GraphQLError> wrappedErrors = new ArrayList<>();
    for (GraphQLError error : errors) {
      if (error instanceof ExceptionWhileDataFetching) {
        Throwable throwable = ((ExceptionWhileDataFetching) error).getException();
        wrappedErrors.add(handleException(throwable, error.getLocations(), error.getPath()));
      }
    }
    return wrappedErrors;
  }

  private GraphQLError handleException(final Throwable throwable, final List<SourceLocation> locations, final List<Object> path) {
    if (throwable instanceof GraphQLError) {
      return (GraphQLError) throwable;
    }
    if (throwable instanceof ConstraintViolationException) {
      return new GraphQLValidationError((ConstraintViolationException) throwable, locations, path);
    } else {
      ApplicationErrorDTO applicationError = new ApplicationErrorDTO(() -> "UNKNOWN", "An unknown error occurred");
      return new GraphQLApplicationError(applicationError, locations, path);
    }
  }
}
