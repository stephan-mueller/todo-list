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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/*
 * Test class for the DTO {@link ValidationErrorDTO}.
 */
@Ignore
public class ValidationErrorDTOTest {

  @Before
  public void setUp() {
    Locale.setDefault(Locale.ENGLISH);
  }

  @Test
  public void instantiationShouldSucceed() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    TestValidationClass entity = new TestValidationClass();

    List<ConstraintViolation<TestValidationClass>> constraintViolations = new LinkedList<>(validator.validate(entity));

    Collections.sort(constraintViolations,
                     Comparator.comparing(o -> o.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()));

    assertConstraintViolation(constraintViolations.get(0), "BVAL_ASSERT-FALSE", "assertFalse must be false");
    assertConstraintViolation(constraintViolations.get(1), "BVAL_ASSERT-TRUE", "assertTrue must be true");
    assertConstraintViolation(constraintViolations.get(2), "BVAL_CUSTOM", "custom error message");
    assertConstraintViolation(constraintViolations.get(3), "BVAL_DECIMAL-MAX", "decimalMax must be less than or equal to 0");
    assertConstraintViolation(constraintViolations.get(4), "BVAL_DECIMAL-MIN", "decimalMin must be greater than or equal to 1");
    assertConstraintViolation(constraintViolations.get(5), "BVAL_DIGITS",
                              "digits numeric value out of bounds (<1 digits>.<1 digits> expected)");
    assertConstraintViolation(constraintViolations.get(6), "BVAL_FUTURE", "future must be in the future");
    assertConstraintViolation(constraintViolations.get(7), "BVAL_MAX", "max must be less than or equal to 0");
    assertConstraintViolation(constraintViolations.get(8), "BVAL_MIN", "min must be greater than or equal to 1");
    assertConstraintViolation(constraintViolations.get(9), "BVAL_NOT-NULL", "notNullValue may not be null");
    assertConstraintViolation(constraintViolations.get(10), "BVAL_NULL", "nullValue must be null");
    assertConstraintViolation(constraintViolations.get(11), "BVAL_PAST", "past must be in the past");
    assertConstraintViolation(constraintViolations.get(12), "BVAL_PATTERN", "pattern must match \"\\d\"");
    assertConstraintViolation(constraintViolations.get(13), "BVAL_SIZE", "size size must be between 1 and 2147483647");
  }

  private void assertConstraintViolation(final ConstraintViolation<TestValidationClass> constraintViolation, final String expectedCode, final String expectedMessage) {
    ValidationErrorDTO validationError = new ValidationErrorDTO(constraintViolation);
    assertThat(validationError.getCode()).isEqualTo(expectedCode);
    assertThat(validationError.getMessage()).isEqualTo(expectedMessage);
  }
}
