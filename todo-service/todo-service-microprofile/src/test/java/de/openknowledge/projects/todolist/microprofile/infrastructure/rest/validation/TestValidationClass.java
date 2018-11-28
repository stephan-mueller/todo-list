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
package de.openknowledge.projects.todolist.microprofile.infrastructure.rest.validation;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@CustomTestCheck
class TestValidationClass {

  @AssertFalse
  private Boolean assertFalse = true;

  @AssertTrue
  private Boolean assertTrue = false;

  @DecimalMax("0")
  private Integer decimalMax = 1;

  @DecimalMin("1")
  private Integer decimalMin = 0;

  @Digits(integer = 1, fraction = 1)
  private BigDecimal digits = new BigDecimal("11.11");

  @Future
  private Date future = new Date(0);

  @Max(0)
  private Integer max = 1;

  @Min(1)
  private Integer min = 0;

  @NotNull
  private String notNullValue = null;

  @Null
  private String nullValue = new String();

  @Past
  private Date past = new Date(System.currentTimeMillis() + 10000l);

  @Pattern(regexp = "\\d")
  private String pattern = "a";

  @Size(min = 1)
  private String size = "";
}
