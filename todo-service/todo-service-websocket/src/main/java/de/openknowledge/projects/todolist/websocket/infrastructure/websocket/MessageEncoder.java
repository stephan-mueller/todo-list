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
package de.openknowledge.projects.todolist.websocket.infrastructure.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.Encoder.Text;
import javax.websocket.EndpointConfig;

/**
 * Encoder for outgoing messages.
 */
public class MessageEncoder implements Text<Message> {

  private static final Logger LOG = LoggerFactory.getLogger(MessageEncoder.class);

  public void init(final EndpointConfig config) {
    LOG.trace("initialize ...");
  }

  public String encode(final Message message) {
    Jsonb jsonb = JsonbBuilder.create();
    return jsonb.toJson(message);
  }

  public void destroy() {
    LOG.trace("destroy ...");
  }
}
