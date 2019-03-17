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
package de.openknowledge.projects.todolist.rest.domain.todo;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.core.util.EntityManagerProvider;

import de.openknowledge.projects.todolist.rest.infrastructure.domain.entity.AbstractEntity;
import de.openknowledge.projects.todolist.rest.infrastructure.domain.entity.EntityNotFoundException;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

/**
 * DbUnit test for the repository {@link TodoRepository}.
 */
public class TodoRepositoryIT {

  @Rule
  public EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("test-local");

  @Rule
  public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> entityManagerProvider.connection());

  private TodoRepository repository;

  @BeforeClass
  public static void setUpBeforeClass() {
    CDIProvider cdiProvider = Mockito.mock(CDIProvider.class);
    CDI.setCDIProvider(cdiProvider);

    CDI cdi = Mockito.mock(CDI.class);
    Mockito.doReturn(cdi).when(cdiProvider).getCDI();

    Instance instance = Mockito.mock(Instance.class);
    Mockito.doReturn(instance).when(cdi).select(Mockito.any(TypeLiteral.class), Mockito.any(AnnotationLiteral.class));

    Event event = Mockito.mock(Event.class);
    Mockito.doReturn(event).when(instance).get();
    Mockito.doNothing().when(event).fire(Mockito.any(AbstractEntity.class));
  }

  @Before
  public void setUp() {
    repository = new TodoRepository(entityManagerProvider.getEm());
  }

  @Test
  @DataSet(strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-create-expected.yml", ignoreCols = "TOD_ID")
  public void create() {
    Todo todo = Todo.newBuilder()
        .withTitle("clean fridge")
        .withDescription("It's a mess")
        .withDueDate(LocalDateTime.now().minusDays(1))
        .setDone(false)
        .build();

    repository.create(todo);
  }

  @Test
  @DataSet(value = "datasets/todos-delete.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-delete-expected.yml")
  public void delete() throws EntityNotFoundException {
    Todo foundTodo = repository.find(1L);
    repository.delete(foundTodo);
  }

  @Test
  @DataSet(value = "datasets/todos.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
  public void find() throws EntityNotFoundException {
    Todo todo = repository.find(1L);
    assertThat(todo).isNotNull();
  }

  @Test
  @DataSet(value = "datasets/todos.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true)
  public void findAll() {
    List<Todo> todos = repository.findAll();
    Assertions.assertThat(todos).hasSize(7);
  }

  @Test
  @DataSet(value = "datasets/todos-update.yml", strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = true, transactional = true)
  @ExpectedDataSet(value = "datasets/todos-update-expected.yml")
  public void update() throws EntityNotFoundException {
    Todo foundTodo = repository.find(1L);
    foundTodo.updateTodo(foundTodo.getTitle(), null, foundTodo.getDueDate(), true);
    repository.update(foundTodo);
  }
}
