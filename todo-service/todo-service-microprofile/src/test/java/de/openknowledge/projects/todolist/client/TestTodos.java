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
package de.openknowledge.projects.todolist.client;

import java.time.LocalDateTime;

/**
 * Test data builder for the DTO {@link TodoBase}.
 */
public class TestTodos {

  public static TodoBase newTodo() {
    TodoBase todo = new TodoBase();
    todo.setTitle("clean fridge");
    todo.setDescription("It's a mess");
    todo.setDueDate(LocalDateTime.of(2018,01,01,12,34, 56));
    todo.setDone(false);

    return todo;
  }

  public static TodoBase invalidTodo() {
    TodoBase todo = new TodoBase();
    todo.setDone(false);

    return todo;
  }

  public static TodoBase modifiedTodo() {
    TodoBase todo = new TodoBase();
    todo.setTitle("clean fridge");
    todo.setDescription("");
    todo.setDueDate(LocalDateTime.of(2018,01,01,12,34, 56));
    todo.setDone(true);

    return todo;
  }
}
