/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.openknowledge.projects.todolist.rest.infrastructure.domain.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.openknowledge.projects.todolist.rest.infrastructure.domain.value.AbstractSimpleValueObject;
import de.openknowledge.projects.todolist.rest.infrastructure.domain.value.AbstractValueObject;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Collections;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

public class ObjectBuilderTest {

  @Test
  public void stringSubclass() {
    assertThat(ObjectBuilder.fromGenericType(StringSubclass.class, GenericSuperclass.class, 0).getType()).isEqualTo(String.class);
  }

  @Test
  public void integerSubclass() {
    assertThat(ObjectBuilder.fromGenericType(IntegerSubclass.class, GenericSuperclass.class, 0).getType()).isEqualTo(Integer.class);
  }

  @Test(expected = IllegalStateException.class)
  public void rawSuperclassFails() {
    ObjectBuilder.fromGenericType(RawSubclass.class, GenericSuperclass.class, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void unresolvableSuperclassFails() {
    ObjectBuilder.fromGenericType(OuterClass.UnspecificSubclass.class, GenericSuperclass.class, 0);
  }

  @Test
  public void multipleGenericParameters() {
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSubclass.class, MultiparameterSuperclass.class, 0).getType()).isEqualTo(String.class);
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSubclass.class, MultiparameterSuperclass.class, 1).getType()).isEqualTo(Integer.class);
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSwitchingSubclass.class, MultiparameterSuperclass.class, 0).getType()).isEqualTo(Integer.class);
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSwitchingSubclass.class, MultiparameterSuperclass.class, 1).getType()).isEqualTo(String.class);
  }

  @Test
  public void buildWithoutConstructor() {
    assertThat(ObjectBuilder.forType(ClassWithoutConstructor.class).build()).isInstanceOf(ClassWithoutConstructor.class);
  }

  @Test
  public void buildWithPrivateConstructor() {
    assertThat(ObjectBuilder.forType(ClassWithPrivateConstructor.class).build()).isInstanceOf(ClassWithPrivateConstructor.class);
  }

  @Test
  public void buildWithAbstractClass() {
    assertThatIllegalStateException()
        .isThrownBy(() -> ObjectBuilder.forType(GenericSuperclass.class).build())
        .withCause(new InstantiationException());
  }

  @Test
  public void buildWithExceptionThrowingConstructor() {
    assertThatIllegalStateException()
        .isThrownBy(() -> ObjectBuilder.forType(ClassWithExceptionThrowingConstructor.class).build())
        .withCause(new IOException());
  }

  @Test
  public void buildWithRuntimeExceptionThrowingConstructor() {
    assertThatIllegalArgumentException()
        .isThrownBy(() -> ObjectBuilder.forType(ClassWithRuntimeExceptionThrowingConstructor.class).build())
        .withNoCause();
  }

  @Test
  public void buildWithParameter() {
    assertThat(ObjectBuilder.forType(TestSecondSimpleValueObject.class).withParameter("second").build())
      .isEqualTo(new TestSecondSimpleValueObject("second"));
  }

  @Test
  public void buildWithParameterConversion() {
    TestMultiValueObject name = ObjectBuilder.forType(TestMultiValueObject.class).withParameter("first").andParameter("second").andParameter(true).build();
    assertThat(name).isEqualTo(new TestMultiValueObject(new TestFirstSimpleValueObject("first"), new TestSecondSimpleValueObject("second")));
  }

  @Test
  public void buildWithSpecificConstructor() {
    TestMultiValueObject name = ObjectBuilder.forType(TestMultiValueObject.class).withParameter("first").andParameter("second").build();
    assertThat(name).isEqualTo(new TestMultiValueObject(new TestFirstSimpleValueObject("first"), new TestSecondSimpleValueObject("second")));
  }

  @Test
  public void buildWithNullParameter() {
    TestMultiValueObject name = ObjectBuilder.forType(TestMultiValueObject.class).withParameter(null).andParameter("second").build();
    assertThat(name).isEqualTo(new TestMultiValueObject(null, new TestSecondSimpleValueObject("second")));
  }

  @Test(expected = IllegalStateException.class)
  public void buildWithInconvertibleConstructor() {
    TestMultiValueObject name = ObjectBuilder.forType(TestMultiValueObject.class).withParameter("first").andParameter("second").andParameter("true").andParameter("now").build();
    assertThat(name).isEqualTo(new TestMultiValueObject(new TestFirstSimpleValueObject("first"), new TestSecondSimpleValueObject("second")));
  }

  @Test(expected = IllegalStateException.class)
  public void buildWithNoMatchingConstructor() {
    TestMultiValueObject name = ObjectBuilder.forType(TestMultiValueObject.class).withParameter("first").andParameter("second").andParameter("now").andParameter("false").build();
    assertThat(name).isEqualTo(new TestMultiValueObject(new TestFirstSimpleValueObject("first"), new TestSecondSimpleValueObject("second")));
  }

  @Test(expected = IllegalStateException.class)
  public void buildWithAmbiguousConstructors() {
    ObjectBuilder.forType(ClassWithAmbiguousConstructors.class).withParameter("first").andParameter("second").build();
  }

  @Test
  public void buildWithValidator() throws ReflectiveOperationException {
    Validator validator = mock(Validator.class);
    ExecutableValidator executableValidator = mock(ExecutableValidator.class);
    when(validator.forExecutables()).thenReturn(executableValidator);

    TestSecondSimpleValueObject value = ObjectBuilder.forType(TestSecondSimpleValueObject.class).withParameter("second").validatedBy(validator).build();
    assertThat(value).isEqualTo(new TestSecondSimpleValueObject("second"));

    Constructor<TestSecondSimpleValueObject> constructor = TestSecondSimpleValueObject.class.getDeclaredConstructor(String.class);
    verify(executableValidator).validateConstructorParameters(ArgumentMatchers.eq(constructor), aryEq(new Object[] {"second" }));
  }

  @Test(expected = ConstraintViolationException.class)
  public void buildWithValidationError() throws ReflectiveOperationException {
    Validator validator = mock(Validator.class);
    ExecutableValidator executableValidator = mock(ExecutableValidator.class);
    when(validator.forExecutables()).thenReturn(executableValidator);
    Constructor<TestSecondSimpleValueObject> constructor = TestSecondSimpleValueObject.class.getDeclaredConstructor(String.class);
    when(executableValidator.validateConstructorParameters(ArgumentMatchers.eq(constructor), aryEq(new Object[] {"second" }))).thenReturn(Collections.singleton(mock(ConstraintViolation.class)));

    ObjectBuilder.forType(TestSecondSimpleValueObject.class).withParameter("second").validatedBy(validator).build();
  }

  public abstract static class GenericSuperclass<A> {
  }

  public static class GenericNumberSubclass<N extends Number> extends GenericSuperclass<N> {
  }

  public static class IntegerSubclass extends GenericNumberSubclass<Integer> {
  }

  public static class StringSubclass extends GenericSuperclass<String> {
  }

  public static class RawSubclass extends GenericSuperclass {
  }

  public static class OuterClass<X> {

    public class UnspecificSubclass extends GenericSuperclass<X> {
    }

  }

  public static class MultiparameterSuperclass<A, B> {
  }

  public static class MultiparameterInbetweenClass<A, B> extends MultiparameterSuperclass<B, A> {
  }

  public static class MultiparameterSubclass extends MultiparameterSuperclass<String, Integer> {
  }

  public static class MultiparameterSwitchingSubclass extends MultiparameterInbetweenClass<String, Integer> {
  }

  public static class ClassWithoutConstructor {
  }

  public static class ClassWithPrivateConstructor {

    protected ClassWithPrivateConstructor() {
    }
  }

  public static class ClassWithExceptionThrowingConstructor {

    public ClassWithExceptionThrowingConstructor() throws IOException {
      throw new IOException();
    }

  }

  public static class ClassWithRuntimeExceptionThrowingConstructor {

    public ClassWithRuntimeExceptionThrowingConstructor() {
      throw new IllegalArgumentException();
    }

  }

  public static class ClassWithAmbiguousConstructors {

    public ClassWithAmbiguousConstructors(String first, Object second) {
    }

    public ClassWithAmbiguousConstructors(Object first, String second) {
    }

  }

  private static class TestFirstSimpleValueObject extends AbstractSimpleValueObject<String> {

    public TestFirstSimpleValueObject(final String value) {
      super(value);
    }
  }

  private static class TestSecondSimpleValueObject extends AbstractSimpleValueObject<String> {

    public TestSecondSimpleValueObject(final String value) {
      super(value);
    }

  }

  private static class TestMultiValueObject extends AbstractValueObject {

    private TestFirstSimpleValueObject first;

    private TestSecondSimpleValueObject second;

    protected TestMultiValueObject() {
      // for JPA
    }

    public TestMultiValueObject(final TestFirstSimpleValueObject first, final TestSecondSimpleValueObject second) {
      this.first = first;
      this.second = second;
    }

    public TestMultiValueObject(final Object first, final TestSecondSimpleValueObject second) {
      this(first, second, true);
    }

    public TestMultiValueObject(final TestFirstSimpleValueObject first, final Object second) {
      this(first, new TestSecondSimpleValueObject(second.toString()), false);
    }

    public TestMultiValueObject(final Object first, final TestSecondSimpleValueObject second, final boolean convert) {
      this(convert ? new TestFirstSimpleValueObject(first.toString()) : (TestFirstSimpleValueObject)first, second);
    }
    public TestMultiValueObject(final Object first, final TestSecondSimpleValueObject second, final boolean convert, final LocalDate date) {
      this(convert ? new TestFirstSimpleValueObject(first.toString()) : (TestFirstSimpleValueObject)first, second);
    }

    @Override
    protected Object[] values() {
      return new Object[] { first, second };
    }
  }
}
