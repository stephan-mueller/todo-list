package de.openknowledge.projects.todolist.rest.infrastructure.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

/**
 * Store for Server Sent Event sinks.
 */
@ApplicationScoped
public class SseBroadcastStore {

  private static final Logger LOG = LoggerFactory.getLogger(SseEventSink.class);

  private static final int RETRY_INTERVAL = 30 * 1000;

  private SseBroadcaster channel;

  public void register(final Sse sse, final SseEventSink eventSink) {
    LOG.debug("register client");
    if (channel == null) {
      channel = sse.newBroadcaster();
    }

    channel.register(eventSink);
  }

  public <T> void notifyChannel(final Sse sse, final String name, final T data) {
    LOG.debug("notify channel");
    channel.broadcast(sse.newEventBuilder()
                          .mediaType(MediaType.APPLICATION_JSON_TYPE)
                          .id(UUID.randomUUID().toString())
                          .name(name)
                          .data(data)
                          .reconnectDelay(RETRY_INTERVAL)
                          .build());
  }
}
