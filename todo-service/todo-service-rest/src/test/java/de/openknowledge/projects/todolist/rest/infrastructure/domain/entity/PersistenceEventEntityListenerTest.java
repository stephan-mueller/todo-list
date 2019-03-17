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
package de.openknowledge.projects.todolist.rest.infrastructure.domain.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

/**
 * Test class for the entity listener {@link PersistenceEventEntityListener}.
 */
public class PersistenceEventEntityListenerTest {

  private PersistenceEventEntityListener entityListener;

  private CDI cdi;

  private Instance instance;

  private Event event;

  @Before
  public void setUp() {
    CDIProvider cdiProvider = Mockito.mock(CDIProvider.class);
    CDI.setCDIProvider(cdiProvider);

    cdi = Mockito.mock(CDI.class);
    Mockito.doReturn(cdi).when(cdiProvider).getCDI();

    instance = Mockito.mock(Instance.class);
    Mockito.doReturn(instance).when(cdi).select(Mockito.any(TypeLiteral.class), Mockito.any(AnnotationLiteral.class));

    event = Mockito.mock(Event.class);
    Mockito.doReturn(event).when(instance).get();
    Mockito.doNothing().when(event).fire(Mockito.any(AbstractEntity.class));

    entityListener = new PersistenceEventEntityListener();
  }

  @After
  public void tearDown() {
    Mockito.verify(cdi).select(Mockito.any(TypeLiteral.class), Mockito.any(AnnotationLiteral.class));
    Mockito.verify(event).fire(Mockito.any(AbstractEntity.class));
    Mockito.verify(instance).get();
    Mockito.verifyNoMoreInteractions(cdi, event, instance);
  }

  @Test
  public void postPersist() {
    entityListener.postPersist(new TestEntity());
  }

  @Test
  public void postRemove() {
    entityListener.postRemove(new TestEntity());
  }

  @Test
  public void postUpdate() {
    entityListener.postUpdate(new TestEntity());
  }

  private static class TestEntity extends AbstractEntity<Long> {

    private Long id;

    public Long getId() {
      return id;
    }
  }
}