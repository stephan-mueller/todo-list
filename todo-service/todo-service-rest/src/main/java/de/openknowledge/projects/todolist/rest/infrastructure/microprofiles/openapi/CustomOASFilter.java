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
package de.openknowledge.projects.todolist.rest.infrastructure.microprofiles.openapi;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Paths;
import org.eclipse.microprofile.openapi.models.media.Schema;

import java.util.Map;

/**
 * Custom OpenAPI Spec Filter
 */
public class CustomOASFilter implements OASFilter {

  private static final String PATH_RESTEASY_REGISTRY = "/api/resteasy/registry";

  private static final String SCHEMA_REGISTRY_DATA = "RegistryData";

  @Override
  public void filterOpenAPI(final OpenAPI openAPI) {
    Paths paths = openAPI.getPaths();
    if (paths.keySet().contains(PATH_RESTEASY_REGISTRY)) {
      paths.remove(PATH_RESTEASY_REGISTRY);
    }

    Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
    if (schemas.keySet().contains(SCHEMA_REGISTRY_DATA)) {
      schemas.keySet().remove(SCHEMA_REGISTRY_DATA);
    }
  }
}
