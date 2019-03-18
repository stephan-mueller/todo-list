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
package de.openknowledge.projects.todolist.rest.application.todo;

import static javax.enterprise.event.Reception.ALWAYS;

import de.openknowledge.projects.todolist.rest.domain.todo.Todo;
import de.openknowledge.projects.todolist.rest.infrastructure.qualifier.Created;
import de.openknowledge.projects.todolist.rest.infrastructure.qualifier.Deleted;
import de.openknowledge.projects.todolist.rest.infrastructure.qualifier.Updated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * Events Store for {@link TodoEvent}s.
 */
@ApplicationScoped
public class TodoEventStore {

  private static final Logger LOG = LoggerFactory.getLogger(TodoEventStore.class);

  private Set<TodoEvent> events = new LinkedHashSet();

  public Set<TodoEvent> getEvents() {
    return Collections.unmodifiableSet(events);
  }

  public void observeCreatedEvent(@Observes(notifyObserver = ALWAYS) @Created final Todo todo) {
    LOG.info("Observer todo created event");
    events.add(TodoEvent.newCreatedTodo(todo));
  }

  public void observeDeletedEvent(@Observes(notifyObserver = ALWAYS) @Deleted final Todo todo) {
    LOG.info("Observer todo deleted event");
    events.add(TodoEvent.newDeletedTodo(todo));
  }

  public void observeUpdatedEvent(@Observes(notifyObserver = ALWAYS) @Updated final Todo todo) {
    LOG.info("Observer todo updated event");
    events.add(TodoEvent.newUpdatedTodo(todo));
  }
}
