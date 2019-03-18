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
package de.openknowledge.projects.todolist.graphql.domain.user;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;

/**
 * An entity that represents an user;
 */
public class User implements Serializable {

  private String username;

  private String firstName;

  private String lastName;

  public User() {
  }

  public User(final String username, final String firstName, final String lastName) {
    this.username = notNull(username, "username must not be null");
    this.firstName = notNull(firstName, "firstName must not be null");
    this.lastName = notNull(lastName, "lastName must not be null");
  }

  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User)o;

    if (username != null ? !username.equals(user.username) : user.username != null) {
      return false;
    }
    if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) {
      return false;
    }
    return !(lastName != null ? !lastName.equals(user.lastName) : user.lastName != null);

  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "User{" +
      "username='" + username + '\'' +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      '}';
  }
}
