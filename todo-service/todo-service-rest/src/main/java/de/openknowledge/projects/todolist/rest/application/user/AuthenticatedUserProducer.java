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
package de.openknowledge.projects.todolist.rest.application.user;

import de.openknowledge.projects.todolist.rest.domain.user.User;
import de.openknowledge.projects.todolist.rest.infrastructure.security.Authenticated;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

/**
 * Produces an authenticated user for the application.
 */
@RequestScoped
public class AuthenticatedUserProducer {

  @Produces
  @RequestScoped
  @Authenticated
  public User getUser(final KeycloakSecurityContext context) {
    AccessToken token = context.getToken();
    return new User(token.getPreferredUsername(), token.getGivenName(), token.getFamilyName());
  }
}
