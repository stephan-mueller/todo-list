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
package de.openknowledge.projects.todolist.microprofile.application;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.ApplicationPath;

import io.swagger.jaxrs.config.BeanConfig;

@WebServlet(loadOnStartup = 1)
public class SwaggerBootstrap extends HttpServlet {

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("1.0.0");
    beanConfig.setSchemes(new String[] { "http" });

    String contextPath = config.getServletContext().getContextPath();
    ApplicationPath applicationPath = JaxRsActivator.class.getAnnotation(ApplicationPath.class);
    beanConfig.setBasePath(contextPath + "/" + applicationPath.value());

    beanConfig.setResourcePackage(this.getClass().getPackage().getName());
    beanConfig.setScan(true);
    beanConfig.setPrettyPrint(true);
  }
}
