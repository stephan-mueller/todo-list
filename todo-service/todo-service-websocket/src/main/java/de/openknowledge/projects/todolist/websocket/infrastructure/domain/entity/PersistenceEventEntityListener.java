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
package de.openknowledge.projects.todolist.websocket.infrastructure.domain.entity;

import de.openknowledge.projects.todolist.websocket.infrastructure.qualifier.Created;
import de.openknowledge.projects.todolist.websocket.infrastructure.qualifier.Deleted;
import de.openknowledge.projects.todolist.websocket.infrastructure.qualifier.Updated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * Entity listener that fires a CDI event after persisting an entity.
 */
public class PersistenceEventEntityListener {

  private static final Logger LOG = LoggerFactory.getLogger(PersistenceEventEntityListener.class);

  @PostPersist
  public <E extends AbstractEntity> void postPersist(final E entity) {
    LOG.info("Fire created event for entity {} with id {}", entity.getClass().getSimpleName(), entity.getId());

    CDI.current()
        .select(new TypeLiteral<Event<E>>() {}, new AnnotationLiteral<Created>() {})
        .get()
        .fire(entity);
  }

  @PostRemove
  public <E extends AbstractEntity> void postRemove(final E entity) {
    LOG.info("Fire created event for entity {} with id {}", entity.getClass().getSimpleName(), entity.getId());

    CDI.current()
        .select(new TypeLiteral<Event<E>>() {}, new AnnotationLiteral<Deleted>() {})
        .get()
        .fire(entity);
  }

  @PostUpdate
  public <E extends AbstractEntity> void postUpdate(final E entity) {
    LOG.info("Fire updated event for entity {} with id {}", entity.getClass().getSimpleName(), entity.getId());

    CDI.current()
        .select(new TypeLiteral<Event<E>>() {}, new AnnotationLiteral<Updated>() {})
        .get()
        .fire(entity);
  }
}
