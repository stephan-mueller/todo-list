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
package de.openknowledge.projects.todolist.graphql.infrastructure.security;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import java.security.Principal;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;

/**
 * Produces the {@link KeycloakSecurityContext} for the application.
 */
@ApplicationScoped
public class KeycloakSecurityContextProducer {

  @Produces
  public KeycloakSecurityContext getSecurityContext(final HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>)principal;
    return keycloakPrincipal.getKeycloakSecurityContext();
  }
}
