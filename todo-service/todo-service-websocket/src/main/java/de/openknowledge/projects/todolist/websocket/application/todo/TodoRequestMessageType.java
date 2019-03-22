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
package de.openknowledge.projects.todolist.websocket.application.todo;

import de.openknowledge.projects.todolist.websocket.infrastructure.websocket.MessageType;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enum that represents the todo request message types.
 */
public enum TodoRequestMessageType implements MessageType {

  CREATE_TODO,

  DELETE_TODO,

  GET_TODO,

  GET_TODOS,

  UPDATE_TODO;

  private static final Map<String, TodoRequestMessageType> LOOKUP = EnumSet.allOf(TodoRequestMessageType.class)
      .stream()
      .collect(Collectors.toMap(TodoRequestMessageType::name, Function.identity()));

  public static boolean contains(final String name) {
    return LOOKUP.keySet().contains(name);
  }
}
