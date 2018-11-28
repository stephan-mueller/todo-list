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
package de.openknowledge.projects.todolist.microprofile;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import org.junit.runner.RunWith;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "de.openknowledge.playground.api.rest.basic")
public class ArchitectureTest {

  @ArchTest
  public static final ArchRule applicationAccessRule = ArchRuleDefinition.classes()
      .that()
      .resideInAPackage("..application..")
      .should()
      .onlyBeAccessed()
      .byAnyPackage("..application..");

  @ArchTest
  public static final ArchRule domainAccessRule = ArchRuleDefinition.classes()
      .that()
      .resideInAPackage("..domain..")
      .should()
      .onlyBeAccessed()
      .byAnyPackage("..application..", "..domain..");

  @ArchTest
  public static final ArchRule infrastructureAccessRule = ArchRuleDefinition.classes()
      .that()
      .resideInAPackage("..infrastructure..")
      .should()
      .onlyBeAccessed()
      .byAnyPackage("..application..", "..domain..", "..infrastructure..");

  @ArchTest
  public static final ArchRule layeredArchitecture = Architectures.layeredArchitecture()
      .layer("Application").definedBy("..application..")
      .layer("Domain").definedBy("..domain..")
      .layer("Infrastructure").definedBy("..infrastructure..")
      .whereLayer("Application").mayNotBeAccessedByAnyLayer()
      .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application")
      .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Application", "Domain");

  @ArchTest
  public static final ArchRule layersShouldBeFreeOfCycles = SlicesRuleDefinition.slices()
      .matching("de.openknowledge.playground.api.rest.basic.(*)..")
      .should()
      .beFreeOfCycles();
}