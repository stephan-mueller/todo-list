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

import org.jboss.resteasy.plugins.providers.atom.Content;
import org.jboss.resteasy.plugins.providers.atom.Entry;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.jboss.resteasy.plugins.providers.atom.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("events")
public class TodoFeedResource {

  private static final Logger LOG = LoggerFactory.getLogger(TodoFeedResource.class);

  @Context
  private UriInfo uriInfo;

  @Inject
  private TodoEventStore eventStore;

  @GET
  @Path("atom")
  @Produces({MediaType.APPLICATION_ATOM_XML})
  public Response getAtomFeed() {
    LOG.info("Get atom feed");

    Set<TodoEvent> events = eventStore.getEvents();

    Feed feed = new Feed();
    feed.setId(UriBuilder.fromPath("tag:openknowledge.de/todo-list-rest/events").build());
    feed.setTitle("Todo events");
    feed.setSubtitle("List of all todo events");
    feed.setUpdated(getLastEntryDate(events));

    Link link = new Link();
    link.setHref(uriInfo.getRequestUri());
    link.setRel("self");
    feed.getLinks().add(link);

    List<Entry> entries = events.stream()
        .map(i -> createFeedEntry(i))
        .collect(Collectors.toList());

    feed.getEntries().addAll(entries);

    return Response.status(Response.Status.OK).entity(feed).build();
  }

  private Date getLastEntryDate(Set<TodoEvent> events) {
    return Timestamp.valueOf(events.toArray(new TodoEvent[0])[events.size() - 1].getChangeDate());
  }

  private Entry createFeedEntry(final TodoEvent todoEvent) {
    URI uri = getURI(todoEvent);

    Entry entry = new Entry();
    entry.setId(uri);
    entry.setTitle(String.format("Todo %s %s", todoEvent.getId(), todoEvent.getChangeType().name().toLowerCase()));
    entry.setPublished(Timestamp.valueOf(todoEvent.getChangeDate()));
    entry.setUpdated(Timestamp.valueOf(todoEvent.getChangeDate()));

    Content content = new Content();
    content.setSrc(uri);
    content.setType(MediaType.APPLICATION_JSON_TYPE);
    entry.setContent(content);

    Link link = new Link();
    link.setHref(uri);
    link.setRel("get");
    entry.getLinks().add(link);

    return entry;
  }

  private URI getURI(final TodoEvent todoEvent) {
    return UriBuilder.fromUri(uriInfo.getBaseUri()).path(TodoResource.class, "getTodo").build(todoEvent.getId());
  }
}
