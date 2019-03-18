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
import de.openknowledge.projects.todolist.rest.infrastructure.sse.SseBroadcastStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

/**
 * Server Sent Event resource for {@link TodoEvent}s.
 */
@Path("todos/all/events")
public class TodoSseResource {

  private static final Logger LOG = LoggerFactory.getLogger(TodoSseResource.class);

  @Context
  private Sse sse;

  @Inject
  private SseBroadcastStore store;

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void subscribe(@Context final SseEventSink eventSink) {
    LOG.info("subscribe to event sink");
    eventSink.send(sse.newEvent("Subscription accepted. ID - " + UUID.randomUUID().toString()));
    store.register(sse, eventSink);
  }

  public void observeCreatedEvent(@Observes(notifyObserver = ALWAYS) @Created final Todo todo) {
    LOG.info("Observer todo created event");
    store.notifyChannel(sse, "Todo created", TodoEvent.newCreatedTodo(todo));
  }

  public void observeDeletedEvent(@Observes(notifyObserver = ALWAYS) @Deleted final Todo todo) {
    LOG.info("Observer todo deleted event");
    store.notifyChannel(sse, "Todo deleted", TodoEvent.newDeletedTodo(todo));
  }

  public void observeUpdatedEvent(@Observes(notifyObserver = ALWAYS) @Updated final Todo todo) {
    LOG.info("Observer todo updated event");
    store.notifyChannel(sse, "Todo updated", TodoEvent.newUpdatedTodo(todo));
  }
}
