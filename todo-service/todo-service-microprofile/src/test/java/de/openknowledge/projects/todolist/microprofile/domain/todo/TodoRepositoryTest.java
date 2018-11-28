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
package de.openknowledge.projects.todolist.microprofile.domain.todo;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;

/**
 * Test class for the repository {@link TodoRepository}.
 */
public class TodoRepositoryTest {

  private TodoRepository repository;

  @Before
  public void setUp() {
    EntityManager entityManager = Mockito.mock(EntityManager.class);
    repository = new TodoRepository(entityManager);
  }

  @Test
  public void createShouldFailForMissingValue() {
    assertThatNullPointerException()
        .isThrownBy(() -> repository.create(null))
        .withMessage("entity must not be null")
        .withNoCause();
  }

  @Test
  public void deleteShouldFailForMissingValue() {
    assertThatNullPointerException()
        .isThrownBy(() -> repository.delete(null))
        .withMessage("entity must not be null")
        .withNoCause();
  }

  @Test
  public void findShouldFailForMissingValue() {
    assertThatNullPointerException()
        .isThrownBy(() -> repository.find(null))
        .withMessage("identifier must not be null")
        .withNoCause();
  }

  @Test
  public void updateShouldFailForMissingValue() {
    assertThatNullPointerException()
        .isThrownBy(() -> repository.update(null))
        .withMessage("entity must not be null")
        .withNoCause();
  }
}
