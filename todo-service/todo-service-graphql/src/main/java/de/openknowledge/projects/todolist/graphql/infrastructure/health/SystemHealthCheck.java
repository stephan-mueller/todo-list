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
package de.openknowledge.projects.todolist.graphql.infrastructure.health;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.enterprise.context.ApplicationScoped;

/**
 * Health check for the system.
 */
@Health
@ApplicationScoped
public class SystemHealthCheck implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();

    return HealthCheckResponse.named("system")
        .withData("name", mxBean.getName())
        .withData("arch", mxBean.getArch())
        .withData("version", mxBean.getVersion())
        .withData("processors", mxBean.getAvailableProcessors())
        .withData("loadAverage", String.valueOf(mxBean.getSystemLoadAverage()))
        .withData("loadAverage per processor", String.valueOf(getLoadAveragePerProcessors(mxBean)))
        .up()
        .build();
  }

  private double getLoadAveragePerProcessors(OperatingSystemMXBean mxBean) {
    return mxBean.getSystemLoadAverage() / mxBean.getAvailableProcessors();
  }
}
