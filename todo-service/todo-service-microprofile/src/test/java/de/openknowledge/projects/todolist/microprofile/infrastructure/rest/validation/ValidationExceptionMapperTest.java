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

import static org.assertj.core.api.Assertions.assertThat;

import de.openknowledge.projects.todolist.microprofile.infrastructure.domain.entity.AbstractEntity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

/**
 * Test class for the the exception mapper {@link ValidationExceptionMapper} which handles {@link
 * ConstraintViolationException}s.
 */
public class ValidationExceptionMapperTest {

  @BeforeClass
  public static void setUpBeforeClass() {
    Locale.setDefault(Locale.ENGLISH);
  }

  @AfterClass
  public static void tearDown() {
    Locale.setDefault(Locale.getDefault());
  }

  @Test
  public void toResponse() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    TestEntity entity = new TestEntity();

    Set<ConstraintViolation<TestEntity>> constraintViolations = validator.validate(entity);
    ConstraintViolationException exception = new ConstraintViolationException(constraintViolations);

    ValidationExceptionMapper exceptionMapper = new ValidationExceptionMapper();
    Response response = exceptionMapper.toResponse(exception);

    assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    List<ValidationErrorDTO> validationErrors = (List<ValidationErrorDTO>) response.getEntity();
    assertThat(validationErrors).hasSize(1);

    ValidationErrorDTO validationError = validationErrors.get(0);
    assertThat(validationError.getCode()).isEqualTo("BVAL_NOT-NULL");
    assertThat(validationError.getMessage()).isEqualTo("id may not be null");
  }

  private static class TestEntity extends AbstractEntity<Long> {

    @NotNull
    private Long id;

    @Override
    public Long getId() {
      return id;
    }
  }
}
