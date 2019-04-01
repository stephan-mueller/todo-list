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

import de.openknowledge.projects.todolist.rest.application.JaxRsActivator;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Health check for the resource {@link TodoResource}.
 */
@Health
@ApplicationScoped
public class TodoResourceHealthCheck implements HealthCheck {

  private static final String HTTP_PORT = "thorntail.http.port";

  private static final String CONTEXT_ROOT = "thorntail.context.path";

  @Override
  public HealthCheckResponse call() {
    HealthCheckResponseBuilder builder = HealthCheckResponse.named(TodoResource.class.getSimpleName());

    Response response = ClientBuilder.newClient()
        .target(getResourceUri())
        .request()
        .options();

    boolean up = Response.Status.OK.equals(response.getStatusInfo().toEnum());

    if (up) {
      builder.withData("resource", "available").up();
    } else {
      builder.withData("resource", "not available").down();
    }

    return builder.build();
  }

  private String getResourceUri() {
    String uri = "http://{host}:{port}/{context-root}/{application-path}/{resource}";
    return UriBuilder.fromUri(uri)
        .resolveTemplate("host", "localhost")
        .resolveTemplate("port", getPort())
        .resolveTemplate("context-root", getContextRoot())
        .resolveTemplate("application-path", getApplicationPath())
        .resolveTemplate("resource", getResourcePath())
        .toTemplate();
  }

  private int getPort() {
    return Integer.parseInt(System.getProperty(HTTP_PORT));
  }

  private String getContextRoot() {
    return System.getProperty(CONTEXT_ROOT);
  }

  private String getApplicationPath() {
    return JaxRsActivator.class.getAnnotation(ApplicationPath.class).value();
  }

  private String getResourcePath() {
    return TodoResource.class.getAnnotation(Path.class).value();
  }
}
