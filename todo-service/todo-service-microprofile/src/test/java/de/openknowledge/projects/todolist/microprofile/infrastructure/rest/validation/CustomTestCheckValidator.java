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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom constraint validator to validate a test class.
 */
class CustomTestCheckValidator implements ConstraintValidator<CustomTestCheck, TestValidationClass> {

  @Override
  public void initialize(final CustomTestCheck constraintAnnotation) {
    // do nothing
  }

  @Override
  public boolean isValid(TestValidationClass value, ConstraintValidatorContext context) {
    return false;
  }
}
