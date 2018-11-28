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
package de.openknowledge.projects.todolist.microprofile.infrastructure.rest.validation;

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.microprofile.infrastructure.rest.error.ApplicationError;
import de.openknowledge.projects.todolist.microprofile.infrastructure.rest.error.ApplicationErrorDTO;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;

/**
 * A DTO that represents a validation error.
 */
public class ValidationErrorDTO extends ApplicationErrorDTO {

  public ValidationErrorDTO(final ConstraintViolation constraintViolation) {
    super(extractApplicationError(notNull(constraintViolation, "must not be null")),
          extractMessage(notNull(constraintViolation, "must not be null")));
  }

  private static ApplicationError extractApplicationError(final ConstraintViolation constraintViolation) {
    Annotation annotation = constraintViolation.getConstraintDescriptor().getAnnotation();
    return ValidationApplicationError.getByClazz(annotation.annotationType());
  }

  private static String extractMessage(final ConstraintViolation constraintViolation) {
    String propertyPath = getPropertyPath(constraintViolation);

    StringBuilder sb = new StringBuilder();
    if (StringUtils.isNotBlank(propertyPath) && constraintViolation.getMessageTemplate().startsWith("{javax.validation.constraints")) {
      sb.append(propertyPath).append(' ');
    }

    sb.append(constraintViolation.getMessage());
    return sb.toString();
  }

  private static String getPropertyPath(final ConstraintViolation constraintViolation) {
    StringBuilder sb = new StringBuilder();
    for (Path.Node node : constraintViolation.getPropertyPath()) {
      if (ElementKind.PROPERTY.equals(node.getKind())) {
        if (sb.length() > 0) {
          sb.append('.');
        }
        sb.append(node.getName());
      }
    }

    return sb.toString();
  }
}
