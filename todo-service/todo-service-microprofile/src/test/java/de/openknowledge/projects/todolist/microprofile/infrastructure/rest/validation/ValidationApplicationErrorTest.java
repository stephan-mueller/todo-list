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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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

public class ValidationApplicationErrorTest {

  @Test
  public void getByClazzShouldReturnCustomError() {
    assertThat(ValidationApplicationError.getByClazz(CustomTestCheck.class)).isEqualTo(ValidationApplicationError.CUSTOM);
  }

  @Test
  public void getByClazzShouldReturnError() {
    assertThat(ValidationApplicationError.getByClazz(AssertFalse.class)).isEqualTo(ValidationApplicationError.ASSERT_FALSE);
    assertThat(ValidationApplicationError.getByClazz(AssertTrue.class)).isEqualTo(ValidationApplicationError.ASSERT_TRUE);
    assertThat(ValidationApplicationError.getByClazz(DecimalMax.class)).isEqualTo(ValidationApplicationError.DECIMAL_MAX);
    assertThat(ValidationApplicationError.getByClazz(DecimalMin.class)).isEqualTo(ValidationApplicationError.DECIMAL_MIN);
    assertThat(ValidationApplicationError.getByClazz(Digits.class)).isEqualTo(ValidationApplicationError.DIGITS);
    assertThat(ValidationApplicationError.getByClazz(Future.class)).isEqualTo(ValidationApplicationError.FUTURE);
    assertThat(ValidationApplicationError.getByClazz(Max.class)).isEqualTo(ValidationApplicationError.MAX);
    assertThat(ValidationApplicationError.getByClazz(Min.class)).isEqualTo(ValidationApplicationError.MIN);
    assertThat(ValidationApplicationError.getByClazz(NotNull.class)).isEqualTo(ValidationApplicationError.NOT_NULL);
    assertThat(ValidationApplicationError.getByClazz(Null.class)).isEqualTo(ValidationApplicationError.NULL);
    assertThat(ValidationApplicationError.getByClazz(Past.class)).isEqualTo(ValidationApplicationError.PAST);
    assertThat(ValidationApplicationError.getByClazz(Pattern.class)).isEqualTo(ValidationApplicationError.PATTERN);
    assertThat(ValidationApplicationError.getByClazz(Size.class)).isEqualTo(ValidationApplicationError.SIZE);
  }
}

