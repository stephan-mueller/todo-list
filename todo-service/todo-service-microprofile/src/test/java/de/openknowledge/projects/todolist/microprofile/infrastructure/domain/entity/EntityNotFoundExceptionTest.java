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
package de.openknowledge.projects.todolist.microprofile.infrastructure.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.Test;

/**
 * Test class for the exception {@link EntityNotFoundException}.
 */
public class EntityNotFoundExceptionTest {

  @Test
  public void instantiationShouldFailForMissingIdentifier() {
    assertThatNullPointerException()
        .isThrownBy(() -> new EntityNotFoundException(null))
        .withMessage("identifier must not be null")
        .withNoCause();
  }

  @Test
  public void getMessage() {
    EntityNotFoundException exception = new EntityNotFoundException(1L);
    assertThat(exception.getMessage()).isEqualTo("Entity 1 was not found");
  }

  @Test
  public void getIdentifier() {
    EntityNotFoundException exception = new EntityNotFoundException(1L);
    assertThat(exception.getIdentifier()).isEqualTo(1L);
  }
}
