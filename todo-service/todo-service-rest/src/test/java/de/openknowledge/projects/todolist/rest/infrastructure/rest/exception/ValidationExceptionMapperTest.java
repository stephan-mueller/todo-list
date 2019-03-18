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
package de.openknowledge.projects.todolist.rest.infrastructure.rest.exception;

import static org.assertj.core.api.Assertions.assertThat;

import de.openknowledge.projects.todolist.rest.infrastructure.domain.entity.AbstractEntity;
import de.openknowledge.projects.todolist.rest.infrastructure.validation.ValidationErrorDTO;
import de.openknowledge.projects.todolist.rest.infrastructure.validation.ValidationErrorPayload;

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
 * Test class for the the exception mapper {@link ValidationExceptionMapper} which handles {@link ConstraintViolationException}s.
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
    assertThat(validationErrors).hasSize(2);

    ValidationErrorDTO validationError = validationErrors.get(0);
    assertThat(validationError.getCode()).isEqualTo("UNKNOWN");
    assertThat(validationError.getMessage()).isEqualTo("value1 may not be null");

    validationError = validationErrors.get(1);
    assertThat(validationError.getCode()).isEqualTo("VALUE_IS_NULL");
    assertThat(validationError.getMessage()).isEqualTo("value2 may not be null");
  }

  private static class TestEntity extends AbstractEntity<Long> {

    private Long id;

    @NotNull
    private String value1;

    @NotNull(payload = ValueIsNull.class)
    private String value2;

    @Override
    public Long getId() {
      return id;
    }

    public String getValue1() {
      return value1;
    }

    public String getValue2() {
      return value2;
    }
  }

  private static class ValueIsNull extends ValidationErrorPayload {

    public ValueIsNull() {
      super("VALUE_IS_NULL");
    }
  }
}
