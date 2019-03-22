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

import static org.apache.commons.lang3.Validate.notNull;

import de.openknowledge.projects.todolist.websocket.infrastructure.domain.value.AbstractValueObject;

/**
 * Generic websocket message.
 *
 * @param <E> data
 */
public class Message<E> extends AbstractValueObject {

  private MessageType type;

  private E data;

  public Message(final MessageType type) {
    super();
    this.type = notNull(type, "type must not be null");
  }

  public Message(final MessageType type, final E data) {
    this(type);
    this.data = notNull(data, "data must not be null");
  }

  public MessageType getType() {
    return type;
  }

  public E getData() {
    return data;
  }

  @Override
  protected Object[] values() {
    return new Object[]{data};
  }

  @Override
  public String toString() {
    return "Message{" +
           "type=" + type +
           ", data=" + data +
           "} " + super.toString();
  }
}
