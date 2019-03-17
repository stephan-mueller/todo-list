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
package de.openknowledge.projects.todolist.rest.infrastructure.domain.builder;

import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

/**
 * Test class for {@link ConstraintViolationFormat}.
 */
public class ConstraintViolationFormatTest {

  private Validator validator;

  @BeforeClass
  public static void setUpBeforeClass() {
    Locale.setDefault(ENGLISH);
  }

  @Before
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void format() {
    Set<ConstraintViolation<TestEntity>> constraintViolations = validator.validate(new TestEntity());
    assertThat(ConstraintViolationFormat.format(constraintViolations)).isEqualTo("TestEntity.value may not be null");
  }

  @Test(expected = NullPointerException.class)
  public void formatShouldFailForMissingValue() {
    ConstraintViolationFormat.format(null);
  }

  private static class TestEntity {

    @NotNull
    private Integer value;
  }
}
